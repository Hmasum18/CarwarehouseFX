package github.hmasum18.carshowroombackend.server;

/**
 * This is thread is always up for a client request
 */
public class ClientListener extends Thread {
    public static final String TAG = "ClientListener->";
    private final ClientHolder clientHolder;
    public ClientListener(ClientHolder clientHolder){
        this.clientHolder = clientHolder;
        System.out.println(TAG+" started successfully.");
        //this.start();
    }

    @Override
    public void run() {
        //this function is always up to receive request from a client
        try{
            while(true) {
                clientHolder.getRequest();
            }
        }catch (Exception e){
            System.out.println(TAG+clientHolder.getClientName()+" has left");
            clientHolder.closeConnection();
            ClientManager.getInstance().removeClient(clientHolder.getClientName());
        }
    }
}
