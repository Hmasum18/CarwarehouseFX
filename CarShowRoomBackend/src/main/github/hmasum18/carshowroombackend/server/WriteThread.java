package github.hmasum18.carshowroombackend.server;

public class WriteThread extends Thread{
    private ClientHolder clientHolder;
    public WriteThread(ClientHolder clientManager){
        this.clientHolder = clientManager;
        this.start();
    }

    @Override
    public void run() {
        while(true){

        }
    }
}
