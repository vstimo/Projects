package mafia.serverThings;

import mafia.MafiaGameException.RepetitiousUserNameException;
import mafia.Runnables.ServerSideRunnables.*;
import mafia.chatThings.Message;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.night.PlayerAction;
import mafia.night.PlayersActionTypes;
import mafia.night.NightEvents;
import mafia.roleThings.RoleNames;
import mafia.votingThings.Vote;
import mafia.votingThings.VotingBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * the class for mafia game logic
 */
public class ServerMafiaGameLogic implements Iterable {
    private ArrayList<ServerSidePlayerDetails> alivePlayers;
    private ArrayList<ServerSidePlayerDetails> spectators;
    private ArrayList<ServerSidePlayerDetails> offlineDeadOnes;
    private VotingBox votingBox;
    private NightEvents nightEvents;

    public ServerMafiaGameLogic(ArrayList<ServerSidePlayerDetails> alivePlayers){
        this.spectators = new ArrayList<>();
        this.offlineDeadOnes = new ArrayList<>();
        this.alivePlayers = alivePlayers;
        this.votingBox = new VotingBox();
        this.nightEvents = new NightEvents();
    }

    @Override
    public Iterator<ServerSidePlayerDetails> iterator() {
        return alivePlayers.iterator();
    }



    /**
     * the beginning of the game
     */
    public void gameBegins(){
        initUserNames();
        waitForAllToGetReady();
        givePlayersTheirRoles();

        while (!isGameOver()){
            night();
            if(isGameOver()){
                break;
            }
            day();
            voting();
        }
        gameEnding();
    }

    /**
     * the end of the game
     */
    private void gameEnding(){
        ArrayList<ServerSidePlayerDetails> winnersArrayList = null;
        ArrayList<ServerSidePlayerDetails> losersArrayList = null;
        String endOfTheGame ="! END OF THE GAME !\nthe winner team : ";
        if(didTownWinTheGame()){
            endOfTheGame += " villagers\n";
            winnersArrayList = getGoodGuysNoMatterDeadOrAlive();
            losersArrayList = getBadGuysNoMatterDeadOrAlive();

        }
        else {
            endOfTheGame += " mafias\n";
            winnersArrayList = getBadGuysNoMatterDeadOrAlive();
            losersArrayList = getGoodGuysNoMatterDeadOrAlive();
        }

        endOfTheGame += "WINNERS : \n";

        for(ServerSidePlayerDetails player : winnersArrayList){
            endOfTheGame += player.getUserName() + " was : " + RoleNames.getRoleAsString(player.getRoleName()) + "\n";
        }

        endOfTheGame += "LOSERS : \n";

        for(ServerSidePlayerDetails player : losersArrayList){
            endOfTheGame += player.getUserName() + " was : " + RoleNames.getRoleAsString(player.getRoleName()) + "\n";
        }
        System.out.println(endOfTheGame);
        sendCommandToAliveAndSpectatorPlayers(new Command(CommandTypes.endOfTheGame , endOfTheGame));
    }

    /**
     * initials the players' usernames
     */
    private void initUserNames(){
        Command determineYourUserName = new Command(CommandTypes.determineYourUserName , null);

        sendCommandToAlivePlayers(determineYourUserName);

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSidePlayerDetails player = null;

        Iterator<ServerSidePlayerDetails> playerIterator = alivePlayers.iterator();

        while (playerIterator.hasNext()){
            player = playerIterator.next();
            executorService.execute(new Thread(new RunnableUserNameTaker(this ,player)));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1 , TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * gives the players their roles
     */
    private void givePlayersTheirRoles() {
        shuffleThePlayers();

        Iterator<ServerSidePlayerDetails> playerIterator = iterator();
        int counter = 1;
        while (playerIterator.hasNext()) {
            ServerSidePlayerDetails player = playerIterator.next();
            try {
                assignRole(player, counter);
            } catch (IOException e) {
                removeOfflinePlayerNotifyOthers(player);
                continue;
            }
            counter++;
        }

        shuffleThePlayers();
    }

    private void assignRole(ServerSidePlayerDetails player, int counter) throws IOException {
        RoleNames role;
        switch (counter) {
            case 1:
                role = RoleNames.killer;
                break;
            case 2:
                role = RoleNames.townDoctor;
                break;
            case 3:
                role = RoleNames.detective;
                break;
            case 4:
                role = RoleNames.therapist;
                break;
            case 5:
                role = RoleNames.mayor;
                break;
            default:
                role = RoleNames.normalCitizen;
                break;
        }

        player.sendTheRoleToThePlayer(role);
        player.setRoleName(role);
        System.out.println(player.getUserName() + " role is : " + RoleNames.getRoleAsString(role));
    }

    /**
     * shuffles the players in their list ( to give them their roles randomly )
     */
    private void shuffleThePlayers(){
        ArrayList<ServerSidePlayerDetails> shuffledPlayers = new ArrayList<>();
        Random random = new Random();
        int randomNumber = 0;
        while (alivePlayers.size() > 0){
            randomNumber = random.nextInt(alivePlayers.size());
            shuffledPlayers.add(alivePlayers.get(randomNumber));
            alivePlayers.remove(randomNumber);
        }
        alivePlayers = shuffledPlayers;

    }

    public void doTheCommand(Command command){
        syncDoTheCommand(command);
    }

    /**
     * gets a command and do what it says synchronized
     */
    private synchronized void syncDoTheCommand(Command command){
        if(command.getType() == CommandTypes.messageToOthers){
            sendMessageToAll((Message) command.getCommandNeededThings());
        }

        else if(command.getType() == CommandTypes.iVote){
            Vote vote = (Vote) command.getCommandNeededThings();
            votingBox.saveVoting(getPlayerByName(vote.getSuspectName()));
            notifyOthersThisVote(vote);
        }

        else if(command.getType() == CommandTypes.iDoMyAction){
            PlayerAction playerAction =(PlayerAction) command.getCommandNeededThings();
            if(playerAction.getPlayerActionType() == PlayersActionTypes.killerVictim){
                if(playerAction.getNameOfThePlayerActionHappensTo() == null){
                    System.out.println("killer kills nobody tonight .");
                }
                else{
                    System.out.println("killer kills " + playerAction.getNameOfThePlayerActionHappensTo() + " tonight .");
                }
                synchronized (nightEvents){
                    nightEvents.mafiaTakesVictim(getPlayerByName(playerAction.getNameOfThePlayerActionHappensTo()));
                }
            }
            else if(playerAction.getPlayerActionType() == PlayersActionTypes.townDoctorSave){
                if(playerAction.getNameOfThePlayerActionHappensTo() == null){
                    System.out.println("town doctor saves nobody tonight .");
                }
                else{
                    System.out.println("town doctor saves player : " + playerAction.getNameOfThePlayerActionHappensTo() + " tonight .");
                    synchronized (nightEvents){
                        nightEvents.townDoctorSave(getPlayerByName(playerAction.getNameOfThePlayerActionHappensTo()));
                    }
                }
            }
            else if(playerAction.getPlayerActionType() == PlayersActionTypes.detect) {
                if(playerAction.getNameOfThePlayerActionHappensTo() == null){
                    System.out.println("the detective wants to detect nobody .");
                }
                else {
                    System.out.println("the detective wants to detect player : " + playerAction.getNameOfThePlayerActionHappensTo());
                    synchronized (nightEvents){
                        nightEvents.setWhoDetectiveWantsToDetect(getPlayerByName(playerAction.getNameOfThePlayerActionHappensTo()));
                    }
                }

            }
            else if(playerAction.getPlayerActionType() == PlayersActionTypes.mute){
                if(playerAction.getNameOfThePlayerActionHappensTo() == null){
                    System.out.println("the therapist mutes nobody .");
                }
                else {
                    System.out.println("therapist mutes player : " +playerAction.getNameOfThePlayerActionHappensTo());
                    synchronized (nightEvents){
                        nightEvents.mute(getPlayerByName(playerAction.getNameOfThePlayerActionHappensTo()));
                    }
                }
            }
        }
    }

    /**
     * a person who votes , this method notifies the other players this vote
     */
    private void notifyOthersThisVote(Vote vote){
        String  voteDescription = null;
        if(vote.getSuspectName() == null){
            voteDescription = "voting : " + vote.getVoterName() + " voted nobody" ;
        }
        else {
            voteDescription = "voting : " + vote.getVoterName() + " voted player : " + vote.getSuspectName();
        }

        Command someOneVoted = new Command(CommandTypes.serverToClientString , voteDescription);
        sendCommandToAliveAndSpectatorPlayers(someOneVoted);
    }

    public synchronized void setUserName(ServerSidePlayerDetails playerDetails , String userName) throws RepetitiousUserNameException {
        if(isUserNameRepetitious(userName))
            throw new RepetitiousUserNameException("this userName is already taken");
        else playerDetails.setUserName(userName);
    }

    private  boolean isUserNameRepetitious(String userName){
        Iterator<ServerSidePlayerDetails> serverSidePlayerDetailsIterator = alivePlayers.iterator();
        ServerSidePlayerDetails serverSidePlayerDetails = null;
        while (serverSidePlayerDetailsIterator.hasNext()){
            serverSidePlayerDetails = serverSidePlayerDetailsIterator.next();
            if(userName.equals(serverSidePlayerDetails.getUserName())){
                return true;
            }
        }
        return false;
    }

    private ServerSidePlayerDetails findSpecificAliveRolePlayer(RoleNames roleName){
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();

        while (iterator.hasNext()){
            ServerSidePlayerDetails player = iterator.next();

            if(player.getRoleName() == roleName) return player;
        }
        return null;
    }

    /**
     * removes the player that is no longer online ( connected to the server )
     * and notifies other players that he is not online any more
     * if by the player's absence , the game is over , the game ends
     * @param removingPlayer is the player is removed
     */
    public synchronized void removeOfflinePlayerNotifyOthers(ServerSidePlayerDetails removingPlayer){
        System.err.println("! player " + removingPlayer.getUserName() + " has disconnected !") ;

        if(alivePlayers.contains(removingPlayer)){
            offlineDeadOnes.add(removingPlayer);
            alivePlayers.remove(removingPlayer);
        }

        notifyOthersThePlayerGotOffline(removingPlayer);

        if(isGameOver()) gameEnding();
    }

    /**
     * notifies the other players ( alive ones and spectators ), the input player is offline
     * @param offlinePlayer is the player that is offline
     */
    private void notifyOthersThePlayerGotOffline(ServerSidePlayerDetails offlinePlayer){
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();

        while (iterator.hasNext()){
            ServerSidePlayerDetails messageReceiver = iterator.next();
            try {
                messageReceiver.sendCommandToPlayer(new Command(CommandTypes.serverToClientString ,
                        " the player : " + offlinePlayer.getUserName()  + " left the game ! "));
            } catch (IOException e) {
                iterator.remove();
                removeOfflinePlayerNotifyOthers(messageReceiver);
            }
        }

        iterator = spectators.iterator();

        while (iterator.hasNext()){
            ServerSidePlayerDetails messageReceiver = iterator.next();
            try {
                messageReceiver.sendCommandToPlayer(new Command(CommandTypes.serverToClientString ,
                        " the player : " + offlinePlayer.getUserName()  + " left the game ! "));
            } catch (IOException e) {
                iterator.remove();
                removeSpectatorToOfflineDeadOnes(messageReceiver);
            }
        }

    }

    private boolean isGameOver(){
        if(getAliveBadGuysNumber() >= getAliveGoodGuysNumber()) return true;

        else if(getAliveBadGuysNumber() == 0)
            return true;
        return false;
    }

    private boolean didTownWinTheGame(){
        if(getAliveBadGuysNumber() == 0){
            return true;
        }
        return false;
    }

    private int getAliveBadGuysNumber(){
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        RoleNames playerRoleName = null;
        int badGuysNumber = 0;
        while (playerDetailsIterator.hasNext()){
            playerRoleName = playerDetailsIterator.next().getRoleName();

            if(playerRoleName == RoleNames.killer ){
                badGuysNumber ++;
            }
        }
        return badGuysNumber;
    }

    private int getAliveGoodGuysNumber(){
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        RoleNames playerRoleName = null;
        int goodGuysNumber = 0;
        while (playerDetailsIterator.hasNext()){
            playerRoleName = playerDetailsIterator.next().getRoleName();

            if(playerRoleName != RoleNames.killer){

                goodGuysNumber ++;
            }
        }
        return goodGuysNumber;
    }

    private void waitForAllToGetReady(){
        ExecutorService executorService = Executors.newCachedThreadPool();

        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();

        while(playerDetailsIterator.hasNext()){
            executorService.execute(new Thread(new RunnableWaitToGetReady(playerDetailsIterator.next())));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1 , TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * the night of the game ( mafia kills , doctors save and ...)
     */
    private void night(){
        System.out.println("! NIGHT !");
        if(nightEvents.getMutedOne() != null){
            nightEvents.getMutedOne().setMuted(false);
        }

        nightEvents.resetNightEvents();

        informAllPlayersItsNightDoYourAction();

        runTheNightHandlers();

        revealTheDetectedPlayerForDetective();

        killThoseWhoDiedTonight();

        if(nightEvents.getMutedOne() != null){
            muteTheMutedOne(nightEvents.getMutedOne());
        }
    }

    /**
     * the day ( players chat )
     */
    private void day(){
        System.out.println("! DAY !");
        informAllWhoAreAlive();
        tellEveryOneItsChattingTime();

        ExecutorService executorService = Executors.newCachedThreadPool();
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        while (playerDetailsIterator.hasNext()){
            player = playerDetailsIterator.next();
            if(!player.isMuted()){
                executorService.execute(new RunnableServerSideMessageReceiver(player));
            }
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tellEveryOneChatIsOver();
    }

    /**
     * informs all players , it is chatting time
     */
    private void tellEveryOneItsChattingTime(){
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        Command chattingTimeCommand = new Command(CommandTypes.chatRoomStarted,  null);
        while (playerDetailsIterator.hasNext()){
            player = playerDetailsIterator.next();
            try {
                player.sendCommandToPlayer(chattingTimeCommand);
            } catch (IOException e) {
                playerDetailsIterator.remove();
                removeOfflinePlayerNotifyOthers(player);
            }
        }

        Iterator<ServerSidePlayerDetails> spectatorsIterator = spectators.iterator();
        ServerSidePlayerDetails spectator = null;
        while (spectatorsIterator.hasNext()){
            spectator = spectatorsIterator.next();
            try {
                spectator.sendCommandToPlayer(chattingTimeCommand);
            } catch (IOException e) {
                spectatorsIterator.remove();
                removeSpectatorToOfflineDeadOnes(spectator);
            }

        }
    }

    /**
     * tells all players that the chat is over
     */
    private void tellEveryOneChatIsOver(){
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        Command chattingTimeCommand = new Command(CommandTypes.chatRoomIsClosed,  null);

        while (playerDetailsIterator.hasNext()){
            player = playerDetailsIterator.next();
            try {
                player.sendCommandToPlayer(chattingTimeCommand);
            } catch (IOException e) {
                removeOfflinePlayerNotifyOthers(player);
            }
        }

        Iterator<ServerSidePlayerDetails> spectatorsIterator = spectators.iterator();
        ServerSidePlayerDetails spectator = null;
        while (spectatorsIterator.hasNext()){
            spectator = spectatorsIterator.next();
            try {
                spectator.sendCommandToPlayer(chattingTimeCommand);
            } catch (IOException e) {
                spectatorsIterator.remove();
                removeSpectatorToOfflineDeadOnes(spectator);
            }
        }
    }

    /**
     * players vote to kill somebody
     */
    private void voting(){
        System.out.println("! VOTING !");
        Command votingResult;
        Command finalResult;
        informAllPlayersItsVotingTime();

        ExecutorService executorService = Executors.newCachedThreadPool();
        Iterator<ServerSidePlayerDetails> onlinePlayersIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        while (onlinePlayersIterator.hasNext()){
            player = onlinePlayersIterator.next();
            executorService.execute(new Thread(new RunnableVoteHandler(player)));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(3 , TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(votingBox.getTheLynchingPlayer() == null){
            votingResult = new Command(CommandTypes.votingResult , " nobody is lynched today .");
            sendCommandToAliveAndSpectatorPlayers(votingResult);
        }

        else {
            ServerSidePlayerDetails mostVotedPlayer = votingBox.getTheLynchingPlayer();
            votingResult = new Command(CommandTypes.votingResult,mostVotedPlayer.getUserName() + " is about to lynch today .");

            sendCommandToAliveAndSpectatorPlayers(votingResult);

            if(mayorSaysToLynchOrNot()){
                System.out.println("the lynch happens .");
                finalResult = new Command(CommandTypes.serverToClientString ,
                        "the player " + mostVotedPlayer.getUserName() + " is lynched today");
                sendCommandToAliveAndSpectatorPlayers(finalResult);

                try {
                    mostVotedPlayer.sendCommandToPlayer(new Command(CommandTypes.youAreDead , " you are lynched "));
                    removePlayerFromAlivePlayersToSpectators(mostVotedPlayer);
                } catch (IOException e) {
                    removeOfflinePlayerNotifyOthers(mostVotedPlayer);
                }
            }

            else {
                System.out.println("the mayor canceled the lynch .");
                finalResult = new Command(CommandTypes.serverToClientString , "the mayor canceled the lynch . ");
                sendCommandToAliveAndSpectatorPlayers(finalResult);
            }
        }
        votingBox.resetTheBox();
    }

    /**
     * informs all players ( alive or spectator ) to vote
     */
    private void informAllPlayersItsVotingTime(){
        Command votingTimeInform = new Command(CommandTypes.vote , getAlivePlayersUserNames());
        sendCommandToAliveAndSpectatorPlayers(votingTimeInform);
    }

    /**
     * asks the mayor to lynch the most voted player or not
     * @return true if mayor says lynch or if there is no alive mayor , else false
     */
    private boolean mayorSaysToLynchOrNot(){
        ServerSidePlayerDetails mayor = findSpecificAliveRolePlayer(RoleNames.mayor);
        Command mayorRespond = null;

        if(mayor == null){
            return true; //lynch
        }
        else {
            try {
                mayor.sendCommandToPlayer(new Command(CommandTypes.doYourAction , null));
            } catch (IOException e) {
                removeOfflinePlayerNotifyOthers(mayor);
                return true;
            }

            try {
                mayorRespond = mayor.receivePlayerRespond();
            } catch (IOException e) {
                removeOfflinePlayerNotifyOthers(mayor);
                return true;
            }

            if(mayorRespond.getType() == CommandTypes.mayorSaysLynch){
                return true;//lynch
            }
            else if(mayorRespond.getType() == CommandTypes.mayorSaysDontLynch){
                return false;//don't lynch
            }
        }
        return false;
    }

    /**
     * @return an arrayList of the names of the alive players' usersNames
     */
    private ArrayList<String> getAlivePlayersUserNames(){
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        ArrayList<String> playersNames = new ArrayList<>();

        while (iterator.hasNext()){
            player = iterator.next();
            playersNames.add(player.getUserName());
        }

        return playersNames;
    }

    /**
     * @return an arrayList of the names of the players that are town members
     */
    private ArrayList<String> getAliveGoodGuysNames(){
        ArrayList<String> aliveGoodGuysNames = new ArrayList<>();
        Iterator<ServerSidePlayerDetails> playerIterator = alivePlayers.iterator();
        ServerSidePlayerDetails playerDetails = null;
        synchronized (alivePlayers){
            while (playerIterator.hasNext()){
                playerDetails = playerIterator.next();
                if(! RoleNames.isEvil(playerDetails.getRoleName())){
                    aliveGoodGuysNames.add(playerDetails.getUserName());
                }
            }
        }
        return aliveGoodGuysNames;
    }

    /**
     * sends a message to all players (alive or spectator
     * @param message is the message is sent to all players
     */
    public void sendMessageToAll(Message message){
        // sending to alive players :
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        Command command = new Command(CommandTypes.newMessage , message);
        while (iterator.hasNext()){
            player = iterator.next();

            try {
                player.sendCommandToPlayer(command);
            } catch (IOException e) {
                iterator.remove();
                removeOfflinePlayerNotifyOthers(player);
            }

        }

        //sending message to the spectators ( dead online players ) :
        Iterator<ServerSidePlayerDetails> spectatorsIterator = spectators.iterator();
        while (spectatorsIterator.hasNext()){
            player = spectatorsIterator.next();

            try {
                player.sendCommandToPlayer(command);
            } catch (IOException e) {
                spectatorsIterator.remove();
                removeOfflinePlayerNotifyOthers(player);
            }
        }
    }

    /**
     * finds a player that has the input username
     * @param userName is the username of the player
     * @return the player by this username , if there is none , null
     */
    private ServerSidePlayerDetails getPlayerByName(String userName){
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        while (iterator.hasNext()){
            player = iterator.next();
            if(player.getUserName().equals(userName)){
                return player;
            }
        }

        iterator = spectators.iterator();
        while (iterator.hasNext()){
            player = iterator.next();
            if(player.getUserName().equals(userName)){
                return player;
            }
        }
        iterator = offlineDeadOnes.iterator();
        while (iterator.hasNext()){
            player = iterator.next();
            if (player.getUserName().equals(userName)){
                return player;
            }
        }
        return null;
    }

    private synchronized void sendCommandToAlivePlayers(Command command){
        Iterator<ServerSidePlayerDetails> playerIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        while (playerIterator.hasNext()){

            player = playerIterator.next();

            try {
                player.sendCommandToPlayer(command);
            } catch (IOException e) {
                playerIterator.remove();
                removeOfflinePlayerNotifyOthers(player);
            }

        }
    }

    /**
     * sends a command to all online players ( alive or spectator )
     * @param command is the command to send to online players
     */
    private void sendCommandToAliveAndSpectatorPlayers(Command command){
        sendCommandToAlivePlayers(command);
        sendCommandToSpectators(command);
    }

    private void sendCommandToSpectators(Command command){
        Iterator<ServerSidePlayerDetails> playerIterator = spectators.iterator();
        ServerSidePlayerDetails player = null;
        while (playerIterator.hasNext()){

            player = playerIterator.next();
            try {
                player.sendCommandToPlayer(command);
            } catch (IOException e) {
                playerIterator.remove();
                removeOfflinePlayerNotifyOthers(player);
            }
        }
    }

    private void removePlayerFromAlivePlayersToSpectators(ServerSidePlayerDetails player){
        synchronized (alivePlayers){
            alivePlayers.remove(player);
        }
        synchronized (spectators){
            spectators.add(player);
        }
    }

    /**
     * informs all players ( except mayor ) it is night , and do your action
     */
    private void informAllPlayersItsNightDoYourAction(){
        sendCommandToAliveAndSpectatorPlayers(new Command(CommandTypes.itIsNight , null));

        Command informWithAllOnlinePlayersNames = new Command(CommandTypes.doYourAction , getAlivePlayersUserNames());
        Command informWithGoodGuyPlayersNames = new Command(CommandTypes.doYourAction , getAliveGoodGuysNames());

        sendCommandToEveryAliveGoodGuysExceptMayor(informWithAllOnlinePlayersNames);
        sendCommandToEveryAliveBadGuy(informWithGoodGuyPlayersNames);
    }

    private void sendCommandToEveryAliveGoodGuysExceptMayor(Command command){
        sendCommandToSpectators(command);
        Iterator<ServerSidePlayerDetails> alivePlayersIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        while (alivePlayersIterator.hasNext()){
            player = alivePlayersIterator.next();

            if(player.getRoleName() != RoleNames.mayor && !RoleNames.isEvil(player.getRoleName())){
                try {
                    player.sendCommandToPlayer(command);
                } catch (IOException e) {
                    alivePlayersIterator.remove();
                    removePlayerFromAlivePlayersToSpectators(player);
                }
            }
        }
    }

    private void sendCommandToEveryAliveBadGuy(Command command){
        Iterator<ServerSidePlayerDetails> alivePlayersIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        while (alivePlayersIterator.hasNext()){
            player = alivePlayersIterator.next();

            if(RoleNames.isEvil(player.getRoleName())){
                try {
                    player.sendCommandToPlayer(command);
                } catch (IOException e) {
                    alivePlayersIterator.remove();
                    removeOfflinePlayerNotifyOthers(player);
                }
            }
        }
    }

    /**
     * runs the RunnableNightPlayerHandler for those players that have actions to do at night
     */
    private void runTheNightHandlers(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        Iterator<ServerSidePlayerDetails> iterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        while (iterator.hasNext()){
            player = iterator.next();
            if(player.getRoleName() != RoleNames.normalCitizen && player.getRoleName() != RoleNames.mayor){
                executorService.execute(new RunnableNightPlayerHandler(player));
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1 , TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * kills those players that mafia and professional shot at and are not saved
     */
    private void killThoseWhoDiedTonight(){
        ServerSidePlayerDetails playerWhoDiesTonight = nightEvents.getWhoDiesTonight();

        if(playerWhoDiesTonight != null) {
            sendCommandToAliveAndSpectatorPlayers(new Command(CommandTypes.serverToClientString,
                    "ATTENTION : ! player " + playerWhoDiesTonight.getUserName() + " died last night !"));
            if (!RoleNames.isEvil(playerWhoDiesTonight.getRoleName())) {
                try {
                    playerWhoDiesTonight.sendCommandToPlayer(new Command(CommandTypes.youAreDead , "mafia killed you"));
                    removePlayerFromAlivePlayersToSpectators(playerWhoDiesTonight);
                } catch (IOException e) {
                    removeOfflinePlayerNotifyOthers(playerWhoDiesTonight);
                }
            }
        }
    }

    private void revealTheDetectedPlayerForDetective(){
        ServerSidePlayerDetails detective = findSpecificAliveRolePlayer(RoleNames.detective);

        if(detective != null){
            ServerSidePlayerDetails detectedPlayer = nightEvents.getWhoDetectiveWantsToDetect();
            Command detectionResult = null;
            if(detectedPlayer != null){
                if(RoleNames.isEvil(detectedPlayer.getRoleName())){
                    detectionResult = new Command(CommandTypes.serverToClientString ,
                            "player " + detectedPlayer.getUserName() + " is evil (bad guy)!");
                }

                else {
                    detectionResult = new Command(CommandTypes.serverToClientString ,
                            "player " + detectedPlayer.getUserName() + " is not evil (he is a good guy)!");
                }

                try {
                    detective.sendCommandToPlayer(detectionResult);
                } catch (IOException e) {
                    removeOfflinePlayerNotifyOthers(detectedPlayer);
                }
            }
        }
    }

    /**
     * @return an arrayList of ServerSidePlayerDetails of good guys ( no matter dead or alive )
     */
    private ArrayList<ServerSidePlayerDetails> getGoodGuysNoMatterDeadOrAlive(){
        ArrayList<ServerSidePlayerDetails> goodGuysArrayList = new ArrayList<>();
        Iterator<ServerSidePlayerDetails> playerIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;
        while (playerIterator.hasNext()){
            player = playerIterator.next();

            if(player.getRoleName() != null && ! RoleNames.isEvil(player.getRoleName())){
                goodGuysArrayList.add(player);
            }
        }

        playerIterator = spectators.iterator();

        while (playerIterator.hasNext()){
            player = playerIterator.next();
            if(player.getRoleName() != null && ! RoleNames.isEvil(player.getRoleName())){
                goodGuysArrayList.add(player);
            }
        }

        playerIterator = offlineDeadOnes.iterator();

        while (playerIterator.hasNext()){
            player = playerIterator.next();
            if(player.getRoleName() != null && !RoleNames.isEvil(player.getRoleName())){
                goodGuysArrayList.add(player);
            }
        }
        return goodGuysArrayList;
    }

    /**
     * @return an arrayList of ServerSidePlayerDetails of bad guys ( no matter dead or alive )
     */
    private ArrayList<ServerSidePlayerDetails> getBadGuysNoMatterDeadOrAlive(){
        ArrayList<ServerSidePlayerDetails> badGuysArrayList = new ArrayList<>();
        Iterator<ServerSidePlayerDetails> playerIterator = alivePlayers.iterator();
        ServerSidePlayerDetails player = null;

        while (playerIterator.hasNext()){
            player = playerIterator.next();

            if(player.getRoleName() != null && RoleNames.isEvil(player.getRoleName())){
                badGuysArrayList.add(player);
            }

        }

        playerIterator = spectators.iterator();

        while (playerIterator.hasNext()){
            player = playerIterator.next();
            if(player.getRoleName() != null && RoleNames.isEvil(player.getRoleName())){
                badGuysArrayList.add(player);
            }
        }

        playerIterator = offlineDeadOnes.iterator();

        while (playerIterator.hasNext()){
            player = playerIterator.next();
            if(player.getRoleName() != null && RoleNames.isEvil(player.getRoleName())){
                badGuysArrayList.add(player);
            }
        }
        return badGuysArrayList;
    }

    /**
     * mutes a player ( the therapist choice )
     * @param mutedPlayer the player who gets muted for tomorrow
     */
    private void muteTheMutedOne(ServerSidePlayerDetails mutedPlayer){
        sendCommandToAliveAndSpectatorPlayers(new Command(CommandTypes.serverToClientString ,
                "ATTENTION : ! PLAYER : " + mutedPlayer.getUserName() + " IS MUTED TODAY !"));
        mutedPlayer.setMuted(true);
        Command muteCommand = new Command(CommandTypes.youAreMutedForTomorrow , null);
        try {
            mutedPlayer.sendCommandToPlayer(muteCommand);
        } catch (IOException e) {
            removeOfflinePlayerNotifyOthers(mutedPlayer);
        }
    }

    private void removeSpectatorToOfflineDeadOnes(ServerSidePlayerDetails player){
        System.out.println("player : " + player.getUserName() + " is no longer spectating the game .");
        spectators.remove(player);
        offlineDeadOnes.add(player);
    }

    /**
     * @return a string which contains the names of the alive players
     */
    private String getAlivePlayersNamesAsString(){
        Iterator<ServerSidePlayerDetails> playerDetailsIterator = alivePlayers.iterator();
        StringBuilder alivePlayersNamesAsString = new StringBuilder();
        int counter = 1;
        while (playerDetailsIterator.hasNext()){
            alivePlayersNamesAsString.append(counter+ "- " + playerDetailsIterator.next().getUserName() + "\n");
            counter++;
        }
        return alivePlayersNamesAsString.toString();
    }

    /**
     * sends a string , contains the names of alive players in the game
     * to all alive and spectators
     */
    private void informAllWhoAreAlive(){
        String alivePlayersNamesString = "ALIVE PLAYERS :\n" + getAlivePlayersNamesAsString();
        Command alivePlayersInformCommand = new Command(CommandTypes.serverToClientString , alivePlayersNamesString);
        sendCommandToAliveAndSpectatorPlayers(alivePlayersInformCommand);
    }
}