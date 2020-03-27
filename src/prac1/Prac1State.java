package prac1;

import IA.DistFS.Requests;
import IA.DistFS.Servers;

import java.util.*;
import java.util.function.BiConsumer;

public class Prac1State {

    private Requests requests;
    private Servers servers;

    // Requests
    private ArrayList<Integer> UserID;
    private ArrayList<Integer> FileID;

    // Servers
    private ArrayList<Set<Integer>> FileLocations;

    // Assignation
    private int [] reqAssignations; // la posició i conté el serverID que respon a la request i
    private int maxTransmissionTimeServer;
    private int nserv;
    private HashMap<Integer, Integer>  serverTransmissionTimes;



    public Prac1State(Requests req, Servers serv, int nserv, int seed) {
        this.UserID = new ArrayList<>();
        this.FileID = new ArrayList<>();
        this.FileLocations = new ArrayList<>();
        this.reqAssignations = new int [req.size()];
        this.serverTransmissionTimes = new HashMap<>();
        this.nserv = nserv;
        this.requests = req;
        this.servers = serv;

        for (int i = 0; i < serv.size(); ++i) {
            Set<Integer> aux = serv.fileLocations(i);
            FileLocations.add(aux);
        }

        for (int i = 0; i < req.size(); ++i) {
            int [] aux = req.getRequest(i);
            int user = aux[0];
            int file = aux[1];

            UserID.add(user);
            FileID.add(file);



            /*
            int current_server = FileLocations.get(file).iterator().next();
            reqAssignations[i] = current_server;
            int time = servers.tranmissionTime(current_server, user);
            addTime(current_server, time);
            */

            assignReqToMinTransmissionTime(i);

        }
        calcMaxTransmissionTimeServer();

        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();

        System.out.println("First node heuristic: " + hf.getHeuristicValue(this));
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
    }

    public ArrayList<Integer> getUserID() {
        return UserID;
    }

    public ArrayList<Integer> getFileID() {
        return FileID;
    }

    public ArrayList<Set<Integer>> getFileLocations() {
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

    public double getTotalTime() {
        double totalTime = 0;
        for(Map.Entry<Integer, Integer> entry : serverTransmissionTimes.entrySet()) {
            totalTime += entry.getValue();
        }
        return totalTime;
    }

    public void addTime(int serverID, int time) {
        if (serverTransmissionTimes.containsKey(serverID)) {
            serverTransmissionTimes.put(serverID, time + serverTransmissionTimes.get(serverID));
        }
        else {
            serverTransmissionTimes.put(serverID, time);
        }
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

    // Operador 1
    public void changeAssignation (int i) {
        int file = FileID.get(i);
        int previous_server = reqAssignations[i];
        int previousTime = this.servers.tranmissionTime(previous_server, UserID.get(i));
        addTime(previous_server, -previousTime);
        Iterator<Integer> it = FileLocations.get(file).iterator();
        Set <Integer> serversWithTheFile = FileLocations.get(file);
        Random rand = new Random();

        for (int x = 0; x < rand.nextInt(serversWithTheFile.size()); ++x)
            it.next();

        reqAssignations[i] = it.next();
        int newServer = reqAssignations[i];
        int newTime = this.servers.tranmissionTime(newServer, UserID.get(i));
        addTime(newServer, newTime);

        // Check max transmission time server
        calcMaxTransmissionTimeServer();
    }

    // Operador 2
    public void changeReqToMin(int i) {
        int oldServer = reqAssignations[i];
        int file = FileID.get(i);
        Iterator <Integer> it = FileLocations.get(file).iterator();

        int minServer = -1;
        int minValue = Integer.MAX_VALUE;
        for (int x = 0; x < FileLocations.get(file).size(); ++x) {
            int currentServer = it.next();
            if (currentServer != oldServer) {
                int transTime = getServerTransmissionTime(currentServer);
                int addedTime = servers.tranmissionTime(currentServer, UserID.get(i));
                if (transTime + addedTime < minValue) {
                    minValue = transTime + addedTime;
                    minServer = currentServer;
                }
            }
        }

        reqAssignations[i] = minServer;
        addTime(oldServer, -servers.tranmissionTime(oldServer, UserID.get(i)));
        addTime(minServer, servers.tranmissionTime(minServer, UserID.get(i)));
    }

    // Operador 3
    public void swapAssignations(int i) {
        int oldServer = reqAssignations[i];
        int file = FileID.get(i);
        Iterator <Integer> it = FileLocations.get(file).iterator();

        for (int x = 0; x < FileLocations.get(file).size(); ++x) {
            int currentServer = it.next();
            if (currentServer != oldServer) {
                boolean trobat = false;
                int index = -1;
                for (int j = 0; j < reqAssignations.length; ++j) {
                    if (reqAssignations[j] == currentServer) {
                        index = j;
                        trobat = true;
                        break;
                    }
                }
                if (trobat) {
                    reqAssignations[i] = currentServer;
                    reqAssignations[index] = oldServer;
                    addTime(currentServer, -servers.tranmissionTime(currentServer, UserID.get(index)));
                    addTime(currentServer, servers.tranmissionTime(currentServer, UserID.get(i)));

                    addTime(oldServer, -servers.tranmissionTime(oldServer, UserID.get(i)));
                    addTime(oldServer, servers.tranmissionTime(oldServer, UserID.get(index)));

                    break;
                }
            }
        }


    }

    // Operador 4
    public void moveAssignation(int reqI, int serverID) {
        int oldServer = reqAssignations[reqI];
        addTime(serverID, servers.tranmissionTime(serverID, UserID.get(reqI)));
        addTime(oldServer, -servers.tranmissionTime(oldServer, UserID.get(reqI)));
        reqAssignations[reqI] = serverID;
    }

    // Operador 5
    public void changeMaxRandom() {
        calcMaxTransmissionTimeServer();
        int maxServer = this.maxTransmissionTimeServer;
        ArrayList<Integer> reqMaxServer = new ArrayList<>();
        int minTimeReq = -1;
        int minTime = Integer.MAX_VALUE;

        for (int i = 0; i < this.getNreq(); ++i) {
            if (reqAssignations[i] == maxServer) {
                reqMaxServer.add(i);
                int currentTime = servers.tranmissionTime(maxServer, UserID.get(i));
                if (currentTime < minTime) {
                    minTime = currentTime;
                    minTimeReq = i;
                }
            }
        }

        //changeReqToMin(maxTimeReq);
        changeAssignation(minTimeReq);
    }

    public void swapRandom(int i) {
        int file = FileID.get(i);
        Iterator<Integer> it = FileLocations.get(file).iterator();
        Set <Integer> serversWithTheFile = FileLocations.get(file);
        Random rand = new Random();

        for (int x = 0; x < rand.nextInt(serversWithTheFile.size()); ++x)
            it.next();

        int newServer = reqAssignations[i];
        int newTransmisionTime = this.servers.tranmissionTime(newServer, UserID.get(i));
        int previousServer = reqAssignations[i];
        int previousTransmisionTime = this.servers.tranmissionTime(previousServer, UserID.get(i));

        int maxTimeBefore = Math.max(this.serverTransmissionTimes.get(previousServer),
                this.serverTransmissionTimes.get(newServer));
        int maxTimeAfter = Math.max(this.serverTransmissionTimes.get(previousServer) - previousTransmisionTime,
                this.serverTransmissionTimes.get(newServer)) + newTransmisionTime;

        if (maxTimeBefore > maxTimeAfter) {
            this.reqAssignations[i] = newServer;
            addTime(newServer, newTransmisionTime);
            addTime(previousServer, -previousTransmisionTime);
        }

        // Check max transmission time server
        calcMaxTransmissionTimeServer();

    }

    public void swapAssignation(int i, int j) throws Exception {

        if (!FileID.get(i).equals(FileID.get(j))) {
            throw new Exception("Requests of different files");
        }

        int server1 = reqAssignations[i];
        int server2 = reqAssignations[j];

        reqAssignations[i] = server2;
        reqAssignations[j] = server1;

        addTime(server1, -this.servers.tranmissionTime(server1, FileID.get(i)));
        addTime(server1, this.servers.tranmissionTime(server1, FileID.get(j)));

        addTime(server1, -this.servers.tranmissionTime(server2, FileID.get(j)));
        addTime(server1, this.servers.tranmissionTime(server2, FileID.get(i)));
    }
}
