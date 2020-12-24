package github.hmasum18.carshowroombackend.server;

import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.ClientInfo;
import github.hmasum18.carshowroombackend.model.Meta;
import github.hmasum18.carshowroombackend.model.UserInfo;

import java.util.*;

/**
 * A singleton which helps to achieve live update
 */
public class ClientManager {
    public static final String TAG = "ClientManager->";
    public static ClientManager instance;

    private ClientManager() {

    }

    public static ClientManager getInstance() {
        if (instance == null)
            instance = new ClientManager();
        return instance;
    }

    private HashMap<String, ClientHolder> clientHolderHashMap = new HashMap<>();
    private HashMap<String, ClientInfo> clientInfoHashMap = new HashMap<>();

    public void addClient(String name, ClientHolder clientHolder) {
        clientHolderHashMap.put(name, clientHolder);
    }

    public void addClientInfo(String name,ClientInfo clientInfo){
        clientInfoHashMap.put(name,clientInfo);
    }

    public void removeClient(String name) {
        try {
            clientHolderHashMap.remove(name);
            clientInfoHashMap.remove(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + " removeClient(): client " + name + " was already removed.");
        }
    }

    public void notifyAllOtherClient(ResponseBuilder responseBuilder, String clientName) {
        System.out.println(TAG + "notifyAllOtherClient(): notifying all clients ");
        for (Map.Entry<String, ClientHolder> mp : clientHolderHashMap.entrySet()) {
            if (!clientName.equals(mp.getKey())) {
                System.out.println(TAG + " sending update to => " + mp.getKey());
                mp.getValue().sendResponse(responseBuilder);
                System.out.println(TAG + " update sent to => " + mp.getKey() + " successfully");
            } else {
                System.out.println(TAG + " no need to sent update to self => " + clientName);
            }
        }
    }

    public List<ClientInfo> getAllAuthenticatedClientInfo(){
        List<ClientInfo> allClientInfo = (List<ClientInfo>) clientInfoHashMap.values();
        for (ClientInfo info: allClientInfo) {
            System.out.println(TAG+info);
        }
        return allClientInfo;
    }

    public List<String> getAllConnectedClient(){
        return new ArrayList<>(clientHolderHashMap.keySet());
    }

    public UserInfo getUserInfo(String ipPort){
        return clientInfoHashMap.get(ipPort).getUserInfo();
    }
}
