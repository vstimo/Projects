package mafia.serverThings;

import mafia.InputThings.LoopedTillRightInput;
import mafia.commandThings.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * the server of the game
 */
public class Server {
    private static int playersCount;
    private static LoopedTillRightInput loopedTillRightInput;
    private static ArrayList<ServerSidePlayerDetails> playersDetails;
    private static ServerSocket serverSocket;
    private static ServerMafiaGameLogic serverMafiaGame;

    public static void main(String[] args) {
        playersDetails = new ArrayList<>();
        loopedTillRightInput = new LoopedTillRightInput();

        System.out.println("! WELCOME TO MAFIA GAME !");

        initPlayersCount();

        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            System.err.println("! the server could not get online !");
            e.printStackTrace();
            System.exit(0);
        }

        while (playersDetails.size() < playersCount){
            Socket socket = null;
            ObjectOutputStream objectOutputStream = null;
            ObjectInputStream objectInputStream = null;
            Socket connection = null;
            try {
                makeAndAddServerSidePlayerDetails(connection = serverSocket.accept());
                System.out.println("new player joined the game : " + connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        serverMafiaGame = new ServerMafiaGameLogic(playersDetails);

        serverMafiaGame.gameBegins();
    }

    /**
     * takes the number of the player from the person who starts the server
     */
    private static void initPlayersCount(){
        System.out.println("please Enter the number of the players : ");
        playersCount = loopedTillRightInput.rangedIntInput(3 , 16);
        System.out.println("building game for " + playersCount + " players .");
        System.out.println("waiting for players to join ...");
    }

    public static void doTheCommand(Command command){
        serverMafiaGame.doTheCommand(command);
    }

    /**
     * takes a socket from the server and makes a ServerSidePlayerDetails for the player
     * @param socket is the connection socket to the player
     * @see ServerSidePlayerDetails
     */
    public static void makeAndAddServerSidePlayerDetails(Socket socket){
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try{
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                outputStream.flush();

                objectOutputStream = new ObjectOutputStream(outputStream);
                objectInputStream = new ObjectInputStream(inputStream);

                objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerSidePlayerDetails serverSidePlayerDetails = new ServerSidePlayerDetails(socket ,objectOutputStream , objectInputStream);
        playersDetails.add(serverSidePlayerDetails);
    }

    /**
     * removes the player from the game (player is no longer connected to the server)
     */
    public static void removeOfflinePlayerNotifyOthers(ServerSidePlayerDetails removingPlayer){
        serverMafiaGame.removeOfflinePlayerNotifyOthers(removingPlayer);
    }
}
