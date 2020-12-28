package github.hmasum18.carshowroombackend.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import github.hmasum18.carshowroombackend.model.Car;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * this class holds the client socket and it's stream to read and write data
 * in short is has all client socket info to communicate
 */
public class ClientHolder {
    public static final String TAG = "ClientHolder->";
    private final Socket socket;
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    private final String clientName;

    public ClientHolder(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientName = socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }


    public void getRequest() throws IOException {
        System.out.println(TAG + " waiting for client message");
        //read the meta and data from the client
        String meta = bufferedReader.readLine();
        String data = bufferedReader.readLine();
        if (meta != null && data != null) {
            System.out.println(TAG + "getRequest() from <= " + clientName + ": meta = " + meta);
            System.out.println(TAG+"getRequest() from <= "+clientName+" dataSize = "+data.length());

            RequestManger requestManger = new RequestManger(this,meta,data);
            requestManger.manageRequest();
        } else {
            System.out.println(TAG+"getRequest() from <= "+clientName+" request in not valid");
        }
    }



    public void sendResponse(ResponseBuilder responseBuilder) {
        System.out.println(TAG+ " sending response to => "+clientName);
        if(responseBuilder.getMeta()!=null){
            System.out.println(TAG+" sending request to => "+clientName+": meta = "+responseBuilder.getMeta());
            //write meta to client by the stream
            printWriter.println(responseBuilder.getMeta());
            if(responseBuilder.getData()!=null)
                System.out.println(TAG+" sending request to => "+clientName+": dataSize = "+responseBuilder.getData().length());
            //write data to client by stream
            printWriter.println(responseBuilder.getData());

            System.out.println(TAG+"message sent");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getClientName() {
        return clientName;
    }

    public void closeConnection(){
        try {
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
