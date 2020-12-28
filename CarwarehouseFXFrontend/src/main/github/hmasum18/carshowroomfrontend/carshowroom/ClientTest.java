package github.hmasum18.carshowroomfrontend.carshowroom;

import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        System.out.println("hello world");
        try {
            ServerHolder serverHolder = new ServerHolder("127.0.0.1",3333);
            //start a new listener(a thread)
            ServerListener serverListener = new ServerListener(serverHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
