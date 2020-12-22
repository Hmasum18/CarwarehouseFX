package github.hmasum18.carshowroombackend.server;

import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.Meta;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton which helps to achieve live update
 */
public class ClientManager {
    public static final String TAG = "ClientManager->";
    public static ClientManager instance;
    private ClientManager(){

    }
    public static ClientManager getInstance() {
        if(instance == null)
            instance = new ClientManager();
        return instance;
    }

    private HashMap<String,ClientHolder> clientHolderHashMap = new HashMap<>();

    public void addClient(String name, ClientHolder clientHolder){
        clientHolderHashMap.put(name,clientHolder);
    }
    public void removeClient(String name){
        clientHolderHashMap.remove(name);
    }

    public void notifyAllOtherClient(ResponseBuilder responseBuilder,String clientName){
        System.out.println(TAG+"notifyAllOtherClient(): notifying all clients ");
        for (Map.Entry<String, ClientHolder> mp: clientHolderHashMap.entrySet()){
            if(!clientName.equals(mp.getKey())){
                System.out.println(TAG+" sending update to => "+mp.getKey());
                mp.getValue().sendResponse(responseBuilder);
                System.out.println(TAG+" update sent to => "+mp.getKey() +" successfully");
            }else{
                System.out.println(TAG+" no need to sent update to self => "+clientName);
            }
        }
    }
}
