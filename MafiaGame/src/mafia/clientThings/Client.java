package mafia.clientThings;

import mafia.gui.Lobby;

import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
/** a class for the client side of the mafia game
 */
public class Client {
    private static Socket socket;
    private static int port = 1234;
    private static String host = "localhost"; //aici va trebuie modificat ulterior!!!!!!!!!!!!!!!!!!!!
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    /**
     * the main method for starting the game
     */
    public static void main(String[] args) {
        Lobby lobby = new Lobby();
        lobby.setStatusPlayerLabel(" waiting to get connected..", Color.YELLOW);

        waitTillGetConnected();
        initStreams();

        lobby.setStatusPlayerLabel("                 connected", Color.GREEN);
        lobby.setInstructionLabel("         waiting for all players to join", Color.BLACK);

        ClientMafiaGameLogic clientMafiaGameLogic = new ClientMafiaGameLogic(socket , objectInputStream , objectOutputStream, lobby);
        clientMafiaGameLogic.play();
    }

    /**
     * if the server is not still turned on, the client waits for the server and every 0.2 second,
     * checks the server is on or not, if it is on, connects to it
     */
    public static void waitTillGetConnected(){
        while (socket == null){
            try {
                Thread.sleep(200);
                socket = new Socket(host , port);
            } catch (ConnectException e){
                // the socket is trying to get connected
            }
            catch(UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * inits the streams of the connection
     */
    public static void initStreams(){
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            outputStream.flush();

            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);

            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}