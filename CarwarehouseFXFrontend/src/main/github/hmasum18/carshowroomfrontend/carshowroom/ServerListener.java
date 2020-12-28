package github.hmasum18.carshowroomfrontend.carshowroom;

public class ServerListener extends Thread{
    public static final String TAG = "ServerListener->";
    private final ServerHolder serverHolder;
    private boolean stop = false;
    public ServerListener(ServerHolder serverHolder){
        stop = false;
        this.serverHolder = serverHolder;
        //this.start();
    }

    public void stopServerListener(){
        this.stop =true;
    }

    public boolean isServerListenerAlive(){
        return !stop;
    }

    @Override
    public void run() {
        //Scanner scanner = new Scanner(System.in);
        establishConnectionWithServer();
        //this loop is always open to receive any kind of response from the server
        System.out.println(TAG+" server listener started.");
        try{
            while (!stop){
                serverHolder.receiveResponse();
                System.out.println(TAG+ " response received.");
            }
            System.out.println(TAG+" server listener stopped");
        }catch (Exception e){
            System.out.println(TAG+" run() = server is offline");
            e.printStackTrace();
        }
    }

    /**
     * establish the initial connection with the server
     * to receive any kind of response from the server
     */
    private void establishConnectionWithServer(){
        long currentTimeStamp = System.currentTimeMillis();
        Meta connectionMeta = new Meta(Meta.RequestType.CONNECT, Meta.ContentType.NONE,currentTimeStamp);
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.setMeta(connectionMeta);
        serverHolder.sendRequest(requestBuilder);
    }
}
