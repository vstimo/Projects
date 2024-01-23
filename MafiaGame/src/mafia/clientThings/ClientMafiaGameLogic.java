package mafia.clientThings;

import mafia.gui.InGameGUI;
import mafia.InputThings.InputProducer;
import mafia.InputThings.LoopedTillRightInput;
import mafia.gui.Lobby;
import mafia.Runnables.clientSideRunnables.RunnableActionDoer;
import mafia.Runnables.clientSideRunnables.RunnableClientMessageSender;
import mafia.Runnables.clientSideRunnables.RunnableClientVote;
import mafia.Runnables.clientSideRunnables.RunnableInputTaker;
import mafia.chatThings.Message;
import mafia.commandThings.Command;
import mafia.commandThings.CommandTypes;
import mafia.roleThings.Actionable;
import mafia.roleThings.Role;
import mafia.roleThings.RoleNames;
import mafia.roleThings.badGuys.Killer;
import mafia.roleThings.goodGuys.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMafiaGameLogic {
    private  String userName;
    private  Role role;
    private  boolean isMuted;
    private  boolean isAlive;

    private  LoopedTillRightInput loopedTillRightInput;
    private  ObjectOutputStream objectOutputStream;
    private  ObjectInputStream objectInputStream;
    private  Socket socket;
    private RunnableInputTaker runnableInputTaker;
    private InputProducer inputProducer;
    public Lobby lobby;
    public InGameGUI gameGUI;
    private boolean isLobbyOn=true;
    private ArrayList<String> alivePlayers = null;

    public ClientMafiaGameLogic(Socket socket, ObjectInputStream objectInputStream , ObjectOutputStream objectOutputStream, Lobby lobby){
        this.isAlive = true;
        this.isMuted = false;
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.loopedTillRightInput = new LoopedTillRightInput(lobby, gameGUI);

        this.inputProducer = new InputProducer(loopedTillRightInput);
        this.runnableInputTaker = new RunnableInputTaker(inputProducer);
        this.lobby = lobby;
    }

    /**
     * start of the game
     */
    public void play(){
        lobby.addButtonClickListener(inputProducer);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(runnableInputTaker);
        executorService.shutdown();

        while (true){
            try {
                doTheCommand((Command) objectInputStream.readObject());
            } catch (IOException | InterruptedException e) {
                disConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * takes a command and do it
     * @param command is the command from server
     */
    public void doTheCommand(Command command) throws InterruptedException {
        if (command.getType() == CommandTypes.determineYourUserName){
            synchronized (inputProducer){
                inputProducer.clearInputs();
            }
            determineUserName();
        } //----------------------bun--------------------------
        else if(command.getType() == CommandTypes.yourUserNameIsSet ){
            confirmPlayerUsername(command);
        } //----------------------bun--------------------------
        else if(command.getType() == CommandTypes.takeYourRole){
            takeYourRole(command);
        } //----------------------bun--------------------------
        else if(command.getType() == CommandTypes.chatRoomStarted){
            synchronized (inputProducer){
                inputProducer.clearInputs();
            }
            chat();
        } //----------------------bun--------------------------
        else if(command.getType() == CommandTypes.vote){
            synchronized (inputProducer){
                inputProducer.clearInputs();
            }
            vote(command);
        }
        else if(command.getType() == CommandTypes.doYourAction){
            synchronized (inputProducer){
                inputProducer.clearInputs();
            }
            doYourAction(command);
        } //----------------------bun--------------------------
        else if(command.getType() == CommandTypes.serverToClientString){
            printServerToClientString(command);
        }
        else if(command.getType() == CommandTypes.waitingForClientToGetReady){
            synchronized (inputProducer){
                inputProducer.clearInputs();
            }
            if(isLobbyOn) getReadyL();
            //else getReadyG
        } //----------------------aproape--------------------------
        else if(command.getType() == CommandTypes.youAreDead){
            die(command);
        }
        else if(command.getType() == CommandTypes.endOfTheGame){
            endOfTheGame(command);
        }
        else if(command.getType() == CommandTypes.youAreMutedForTomorrow){
            isMuted = true;
        }
    }

    /**
     * player chats
     */
    public void chat() throws InterruptedException {
        ExecutorService executorService = null;
        Future<?> future = null;
        gameGUI.setDay();
        gameGUI.setCountDownTimer(180);
        gameGUI.setGameTimeLabel("- DISCUSSION TIME -");
        if (alivePlayers != null) gameGUI.setPlayersPosition(alivePlayers);
        gameGUI.setDay();
        if(isAlive){
            if(isMuted){
                gameGUI.chatArea.append("SERVER: you are muted but you can listen what people say.\n");
            }
            else {
                gameGUI.chatArea.append("- -  GOOD MORNING, SUCH A GOOD DAY TO CHAT\nSERVER: if you want to skip discussion part press skip discussion button\n");
                executorService = Executors.newCachedThreadPool();
                future = executorService.submit(new RunnableClientMessageSender(userName , objectOutputStream , inputProducer, gameGUI));
            }
        }
        else {
            gameGUI.chatArea.append("SERVER: good morning , the alive people are chatting.\n");
        }

        Command command = null;
        do {
            command =  receiveServerCommand();

            if(command.getType() == CommandTypes.newMessage)
                printMessage((Message) command.getCommandNeededThings());
            else if(command.getType() != CommandTypes.chatRoomIsClosed) // just in case :
                doTheCommand(command);
        }while (command.getType() != CommandTypes.chatRoomIsClosed);
        gameGUI.chatArea.append("SERVER: CHAT ROOM HAS CLOSED!\n");

        if(isAlive && !isMuted){
            executorService.shutdown();
            try {
                future.cancel(true);
                executorService.awaitTermination(1  , TimeUnit.MILLISECONDS );

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(isMuted){
            gameGUI.chatArea.append("SERVER: you are not muted anymore.\n");
            isMuted = false;
        }
    }

    /**
     * player votes
     */
    public void vote(Command command){
        ExecutorService executorService = null;
        Future<?> future = null;
        Command receivingCommand = null;

        gameGUI.setCountDownTimer(60);
        gameGUI.setGameTimeLabel("- VOTING TIME -");
        if (alivePlayers != null) gameGUI.setPlayersPosition(alivePlayers);
        //if (!isAlive) alivePlayers.remove(userName); //????
        gameGUI.setDay();


        if(isAlive){
            gameGUI.chatArea.append("SERVER: vote somebody to lynch today.\n");
            ArrayList<String> playersNames =(ArrayList<String>) command.getCommandNeededThings();
            playersNames.remove(userName);
            executorService = Executors.newCachedThreadPool();
            future = executorService.submit(new Thread(new RunnableClientVote(userName , playersNames , objectOutputStream , inputProducer)));
            executorService.shutdown();
        }
        else {
            gameGUI.chatArea.append("SERVER: waiting for alive players to vote.\n");
        }

        do {
            receivingCommand = receiveServerCommand();

            if(receivingCommand.getType() == CommandTypes.serverToClientString)
                gameGUI.chatArea.append("SERVER: " + receivingCommand.getCommandNeededThings() + " \n");

            else if(receivingCommand.getType() == CommandTypes.votingResult) {
                gameGUI.chatArea.append("SERVER: voting time is over!\n");

                if(isAlive){
                    try {
                        future.cancel(true);
                        executorService.awaitTermination(0 , TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                gameGUI.chatArea.append("SERVER: " + receivingCommand.getCommandNeededThings() + " \n"); //printing the voting result
            }
        }while (receivingCommand.getType() != CommandTypes.votingResult);
    }

    /**
     * player does his action
     * @param command is the command contains the needed things for the player action
     */
    public void doYourAction(Command command){
        ExecutorService executorService = Executors.newCachedThreadPool();
        gameGUI.setNight();
        gameGUI.setGameTimeLabel("! NIGHT !");
        gameGUI.setCountDownTimer(180);

        if (this.role.getRoleString().toUpperCase().equals("NORMALCITIZEN") || !isAlive)
            gameGUI.setEveryoneSleeps();

        if(isAlive){
            if (role instanceof Actionable){
                executorService.execute(new RunnableActionDoer((Actionable) role , command));
                executorService.shutdown();
            }
            else {
                gameGUI.chatArea.append("SERVER: waiting for other players\n");
            }
        }
        else {
            gameGUI.chatArea.append("SERVER: it is night, waiting for players to do their actions .!\n");
        }
    }

    /**
     * player takes his RoleName and creates his role by the roleFactory
     */
    public void takeYourRole(Command command){
        isLobbyOn = false;
        lobby.dispose();
        gameGUI = new InGameGUI();
        gameGUI.setNight();
        gameGUI.setGameTimeLabel("! NIGHT !");
        this.role =  generateRole((RoleNames) command.getCommandNeededThings() , objectOutputStream , objectInputStream);
        if (this.role.getRoleString().toUpperCase().equals("MAYOR") || this.role.getRoleString().toUpperCase().equals("NORMALCITIZEN"))
            gameGUI.setEveryoneSleeps();
        printTheClientsRole();
    }

    /**
     * prints the role of the player
     */
    public void printTheClientsRole(){
        String roleDescription = "";
        String roleString = role.getRoleString().toUpperCase();

        switch (roleString) {
            case "KILLER":
                roleDescription = "Your goal is to eliminate the other players at night.";
                break;
            case "MAYOR":
                roleDescription = "At the end of the day you decide if the voted person is lynched or not.";
                break;
            case "TOWN DOCTOR":
                roleDescription = "Heal and protect other players from harm during the night.";
                break;
            case "DETECTIVE":
                roleDescription = "Investigate and identify potential threats to the town every night.";
                break;
            case "NORMAL CITIZEN":
                roleDescription = "Work with others to identify and eliminate threats.";
                break;
            case "THERAPIST":
                roleDescription = "You can mute someone who you consider is going crazy.";
                break;
            default:
                roleDescription = "Unknown role. Something went wrong.";
        }
        gameGUI.setPopWindow("Your role is " + roleString + ". " + roleDescription, "ROLE", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * player write his username and if it is not repetitious, it will be set, else, player enters another
     */
    public void determineUserName(){
        String testingUserName = null;
        Command serverRespond = null;

        lobby.setStatusPlayerLabel("ENTER YOUR USERNAME", Color.GRAY);
        lobby.setInstructionLabel(" ", Color.BLACK);
        while (true){
            try {
                inputProducer.clearInputs();
                while (true){
                    if(inputProducer.hasNext()){
                        testingUserName = inputProducer.consumeInput().trim();
                        break;
                    }
                    else {
                        Thread.sleep(150);
                    }
                }

                objectOutputStream.writeObject(new Command(CommandTypes.setMyUserName , testingUserName ));
                serverRespond = receiveServerCommand();

                if(serverRespond.getType() == CommandTypes.yourUserNameIsSet){
                    doTheCommand(serverRespond);
                    break;
                }

                else if(serverRespond.getType() == CommandTypes.repetitiousUserName){
                    lobby.setStatusPlayerLabel("USERNAME ALREADY TAKEN", Color.RED);
                    lobby.setInstructionLabel("         choose another username!", Color.BLACK);
                }

                else {
                    doTheCommand(serverRespond);
                }

            } catch (IOException e) {
                disConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * player exits the game , by closing the client program
     */
    public void playerExitsTheGame(){
        gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            disConnection();
        }finally {
            System.exit(0);
        }
    }

    /**
     * the entered username of the player is set ( it was not repetitious )
     */
    public void confirmPlayerUsername(Command command){
        this.userName = (String) command.getCommandNeededThings();
        lobby.setStatusPlayerLabel("    YOUR USERNAME: " + this.userName, Color.BLACK);
        lobby.setInstructionLabel(" " , Color.WHITE);
        lobby.submitButton.setEnabled(false);
    }

    /**
     * a role factory that makes a role by input roleName and streams
     */
    public Role generateRole(RoleNames roleName , ObjectOutputStream objectOutputStream , ObjectInputStream objectInputStream){
        if(roleName == RoleNames.killer){
            return new Killer(objectInputStream , objectOutputStream , userName , inputProducer, gameGUI);
        }
        else if(roleName == RoleNames.detective){
            return new Detective(objectInputStream , objectOutputStream, userName , inputProducer, gameGUI);
        }
        else if(roleName == RoleNames.mayor){
            return new Mayor(objectInputStream , objectOutputStream , userName , inputProducer, gameGUI);
        }
        else if(roleName == RoleNames.normalCitizen){
            return new NormalCitizen(objectInputStream , objectOutputStream);
        }
        else if(roleName == RoleNames.therapist){
            return new Therapist(objectInputStream , objectOutputStream , userName , inputProducer, gameGUI);
        }
        else if(roleName == RoleNames.townDoctor){
            return new TownDoctor(objectInputStream , objectOutputStream , userName , inputProducer, gameGUI);
        }
        else{
            return null;
        }
    }

    /**
     * prints the string in a command from server
     * @param command is the command contains the string
     */
    public void printServerToClientString(Command command) {
        gameGUI.chatArea.append(command.getCommandNeededThings() + " \n");

        if (command.getType() == CommandTypes.serverToClientString) {
            String commandText = (String) command.getCommandNeededThings();
            if (commandText.startsWith("ALIVE PLAYERS")) {
                Pattern pattern = Pattern.compile("\\d+-\\s*(\\w+)");
                Matcher matcher = pattern.matcher(commandText);

                ArrayList<String> playerNames = new ArrayList<>();

                while (matcher.find()) playerNames.add(matcher.group(1));

                alivePlayers = playerNames;
            }
        }
    }

    /**
     * player informs the server that he is ready to start the game
     */
    private void getReadyL(){
        lobby.setStatusPlayerLabel("PRESS READY BUTTON", Color.BLACK);
        String input = "";
        do {
            while (!inputProducer.hasNext()){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            input = inputProducer.consumeInput();
        }while (!input.equals("ready"));

        try {
            objectOutputStream.writeObject(new Command(CommandTypes.imReady , null));
        } catch (IOException e) {
            disConnection();
        }
        lobby.setStatusPlayerLabel("YOU ARE READY, GOOD LUCK!", Color.GREEN);
        lobby.setInstructionLabel("  waiting for other players to get ready", Color.BLACK);
        lobby.readyButton.setEnabled(false);
    }

    /**
     * player receives command from server
     * @return the received command from server
     */
    private Command receiveServerCommand(){
        try {
            return (Command) objectInputStream.readObject();
        } catch (IOException e) {
            disConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printMessage(Message message){
        gameGUI.chatArea.append(message.getSenderName() + ": " + message.getMessageText() + " \n");
    }

    /**
     * player dies (the program still runs) and the reason of death will be printed
     */
    private void die(Command command){
        isAlive = false;
        gameGUI.setPopWindow("YOU HAVE BEEN KILLED!", "DEATH", JOptionPane.INFORMATION_MESSAGE);
        gameGUI.chatArea.append("SERVER: ! YOU    ARE    DEAD !\n");
        gameGUI.chatArea.append("REASON OF DEATH: " + command.getCommandNeededThings() +" \n");
    }

    private void endOfTheGame(Command command) throws InterruptedException {
        gameGUI.setPopWindow((String) command.getCommandNeededThings(), "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("ajunge aici?");
        Thread.sleep(10000);
    }

    private void disConnection(){
        gameGUI.setPopWindow("SERVER: you're disconnecting...", "connection lost", JOptionPane.ERROR_MESSAGE);
        playerExitsTheGame();
    }
}
