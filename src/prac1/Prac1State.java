package prac1;

import IA.DistFS.Requests;
import IA.DistFS.Servers;
import java.util.*;

public class Prac1State {

    private Requests requests;
    private Servers servers;

    // Requests
    final private ArrayList<Integer> UserID;
    final private ArrayList<Integer> FileID;

    // Servers
    final private HashMap<Integer, Set<Integer>> FileLocations;

    // Assignation
    private int [] reqAssignations; // la posició i conté el serverID que respon a la request i
    private int maxTransmissionTimeServer;
    private int nserv;

    private int firstMaxTime;

    private HashMap<Integer, Integer>  serverTransmissionTimes;


    // Constructora solució inicial
    public Prac1State(Requests req, Servers serv, int nserv) {
        this.UserID = new ArrayList<>();
        this.FileID = new ArrayList<>();
        this.FileLocations = new HashMap<Integer, Set<Integer>>();
        this.reqAssignations = new int [req.size()];
        this.serverTransmissionTimes = new HashMap<>();
        this.nserv = nserv;
        this.requests = req;
        this.servers = serv;

        for (int i = 0; i < req.size(); ++i) {
            int [] aux = req.getRequest(i);
            int user = aux[0];
            int file = aux[1];

            UserID.add(user);
            FileID.add(file);
        }

        for (int i = 0; i < req.size(); ++i) {
            Set<Integer> aux = serv.fileLocations(FileID.get(i));
            FileLocations.put(FileID.get(i), aux);
        }

        // Solució inicial
        for (int i = 0; i < req.size(); ++i) {
            assignReqToMinTransmissionTime(i);
            //assignReqToFirstServer(i);
        }

        calcMaxTransmissionTimeServer();
        this.firstMaxTime = getMaxTime();

    }

    // Constructora de copia
    public Prac1State (Prac1State estatAnterior) {
        this.UserID = estatAnterior.getUserID();
        this.FileID = estatAnterior.getFileID();
        this.FileLocations = estatAnterior.getFileLocations();
        this.nserv = estatAnterior.getNserv();
        this.reqAssignations = estatAnterior.getReqAssignations().clone();
        this.serverTransmissionTimes = estatAnterior.copyTransmissionTimes();
        this.maxTransmissionTimeServer = estatAnterior.getMaxTransmissionTimeServer();
        this.servers = estatAnterior.getServers();
        this.firstMaxTime = estatAnterior.getFirstMaxTime();
    }

    public ArrayList<Integer> getUserID() {
        return UserID;
    }

    public ArrayList<Integer> getFileID() {
        return FileID;
    }

    public HashMap<Integer, Set<Integer>> getFileLocations() {
        return FileLocations;
    }

    private HashMap <Integer,Integer> copyTransmissionTimes() {
        HashMap <Integer, Integer> hashCopy = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : serverTransmissionTimes.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            hashCopy.put(key, value);
        }
        return  hashCopy;
    }

    public int[] getReqAssignations() {
        return reqAssignations;
    }

    public int getNserv() {
        return nserv;
    }

    public int getNreq() {
        return reqAssignations.length;
    }

    public Servers getServers() {
        return this.servers;
    }

    public int getServerTransmissionTime(int id) {
        if (serverTransmissionTimes.containsKey(id)) {
            return serverTransmissionTimes.get(id);
        }
        return 0;
    }

    private void calcMaxTransmissionTimeServer () {
        int maxValue = 0;
        int maxServer = -1;
        for(Map.Entry<Integer, Integer> entry : serverTransmissionTimes.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if (maxValue < value) {
                maxValue = value;
                maxServer = key;
            }
        }
        this.maxTransmissionTimeServer = maxServer;
    }

    public int getMaxTransmissionTimeServer() {
        this.calcMaxTransmissionTimeServer();
        return maxTransmissionTimeServer;
    }

    public int getMaxTime() {
        int maxValue = 0;
        for(Map.Entry<Integer, Integer> entry : serverTransmissionTimes.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if (maxValue < value) maxValue = value;
        }
        return maxValue;
    }

    public int getFirstMaxTime() {
        return firstMaxTime;
    }

    public double getTotalTime() {
        double totalTime = 0;
        for(Map.Entry<Integer, Integer> entry : serverTransmissionTimes.entrySet()) {
            totalTime += entry.getValue();
        }
        return totalTime;
    }

    public HashMap<Integer, Integer> getServerTransmissionTimes() {
        return serverTransmissionTimes;
    }

    public void addTime(int serverID, int time) {
        if (serverTransmissionTimes.containsKey(serverID)) {
            serverTransmissionTimes.put(serverID, time + serverTransmissionTimes.get(serverID));
        }
        else {
            serverTransmissionTimes.put(serverID, time);
        }
    }

    // Initial state, to the first server
    private void assignReqToFirstServer(int i) {
        int current_server = FileLocations.get(FileID.get(i)).iterator().next();
        reqAssignations[i] = current_server;
        int time = servers.tranmissionTime(current_server, UserID.get(i));
        addTime(current_server, time);
    }

    // Initial state, every request goes to the closest server
    private void assignReqToMinTransmissionTime(int i) {
        int file = FileID.get(i);
        Iterator <Integer> it = FileLocations.get(file).iterator();

        int minServer = -1;
        int minValue = Integer.MAX_VALUE;
        for (int x = 0; x < FileLocations.get(file).size(); ++x) {
            int currentServer = it.next();
            int transTime = servers.tranmissionTime(currentServer, UserID.get(i));
            if (transTime < minValue) {
                minValue = transTime;
                minServer = currentServer;
            }
        }
        reqAssignations[i] = minServer;
        addTime(minServer, servers.tranmissionTime(minServer, UserID.get(i)));
    }

    // Operador moure
    public void moveAssignation(int reqI, int serverID) {
        int oldServer = reqAssignations[reqI];
        addTime(serverID, servers.tranmissionTime(serverID, UserID.get(reqI)));
        addTime(oldServer, -servers.tranmissionTime(oldServer, UserID.get(reqI)));
        reqAssignations[reqI] = serverID;
    }

    // Operador permutar
    public void swapAssignation(int i, int j) throws Exception {

        if (!FileID.get(i).equals(FileID.get(j))) {
            throw new Exception("Requests of different files");
        }

        int server1 = reqAssignations[i];
        int server2 = reqAssignations[j];

        reqAssignations[i] = server2;
        reqAssignations[j] = server1;

        addTime(server1, -this.servers.tranmissionTime(server1, UserID.get(i)));
        addTime(server1, this.servers.tranmissionTime(server1, UserID.get(j)));

        addTime(server2, -this.servers.tranmissionTime(server2, UserID.get(j)));
        addTime(server2, this.servers.tranmissionTime(server2, UserID.get(i)));
    }

}
