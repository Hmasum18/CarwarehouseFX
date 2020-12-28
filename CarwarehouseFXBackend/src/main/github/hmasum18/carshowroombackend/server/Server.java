
package github.hmasum18.carshowroombackend.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String TAG = "Server->";

    public static void main(String[] args) {
       // System.out.println(Server.class.getPackageName());
        try {
            ServerSocket serverSocket = new ServerSocket(3333);
            System.out.println(TAG + "Server started at:" +serverSocket.getInetAddress()+":"+3333);
            while (true){
                System.out.println();
                System.out.println(TAG+" Waiting for client......");
                Socket clientSocket = serverSocket.accept();
                System.out.println(TAG + "Constructor:client is connected");
                System.out.println(TAG + "Client hostAddress:" + clientSocket.getInetAddress().getHostAddress());
                System.out.println(TAG + "Client Port:" + clientSocket.getPort()+"("+clientSocket.getLocalPort()+")");
                System.out.println(TAG + " serving client");
                serverClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serverClient(Socket clientSocket) throws IOException {
        //create a client holder to communicate with client
        ClientHolder clientHolder = new ClientHolder(clientSocket);
        String clientName = clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort();

        //name the client to communicate when there is multiple client
        ClientManager.getInstance().addClient(clientName,clientHolder);

        //start a read thread to get request from client
        new ClientListener(clientHolder).start();
    }
}
