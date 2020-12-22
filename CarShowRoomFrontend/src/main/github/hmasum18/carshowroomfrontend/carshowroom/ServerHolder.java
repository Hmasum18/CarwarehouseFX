package github.hmasum18.carshowroomfrontend.carshowroom;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.view.controller.CarListViewController;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHolder {
    public static final String TAG = "ServerHolder->";
    private final Socket socket;
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    private final String serverName;

    public ServerHolder(String serverIp, int serverPort) throws IOException {
        this.socket = new Socket(serverIp,serverPort);
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()) );
        this.printWriter = new PrintWriter(socket.getOutputStream(),true);
        this.serverName = socket.getInetAddress().getHostName()+":"+this.socket.getPort();
    }

    public void sendRequest(RequestBuilder requestBuilder){
        System.out.println(TAG+" sending request to => "+serverName+": meta = "+requestBuilder.getMeta());
        printWriter.println(requestBuilder.getMeta());
        if(requestBuilder.getData()!=null)
            System.out.println(TAG+" sending request to => "+serverName+": dataSize = "+requestBuilder.getData().length());
        printWriter.println(requestBuilder.getData());
        System.out.println(TAG+"message sent");
    }

    public void receiveResponse() throws IOException {
        System.out.println(TAG + "receiveResponse() = Waiting for server receive.");
        String meta = bufferedReader.readLine();
        String data = bufferedReader.readLine();
        if (meta !=  null&& data != null) {
            System.out.println(TAG + "receiveResponse() from <= " + serverName + " meta = " + meta);
            System.out.println(TAG + "receiveResponse() from <= " + serverName + " dataSize = " + data.length());
            ResponseManager responseManager = new ResponseManager(meta,data);
            responseManager.manageResponse();
        } else {
            System.out.println("response in not valid");
            throw  new NullPointerException(TAG+" Response from server is null");
        }
    }

    public void closeConnection() throws IOException {
        printWriter.close();
        bufferedReader.close();
        socket.close();
    }

    public Socket getSocketOfServer() {
        return socket;
    }
}
