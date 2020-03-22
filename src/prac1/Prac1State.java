package prac1;

import IA.DistFS.Requests;
import IA.DistFS.Servers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

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
    private int [] serverTransmissionTimes;
    private int maxTransmissionTimeServer;
    private int nserv;



    public Prac1State(Requests req, Servers serv, int nserv, int seed) {
        UserID = new ArrayList<>();
        FileID = new ArrayList<>();
        FileLocations = new ArrayList<>();
        reqAssignations = new int [req.size()];
        serverTransmissionTimes =  new int [serv.size()];
        this.nserv = nserv;
        this.requests = req;
        this.servers = serv;

        for (int i = 0; i < serv.size(); ++i) {
            Set<Integer> aux = serv.fileLocations(i);
            FileLocations.add(aux);
            serverTransmissionTimes[i] = 0;
        }

        for (int i = 0; i < req.size(); ++i) {
            int [] aux = req.getRequest(i);
            int user = aux[0];
            int file = aux[1];

            UserID.add(user);
            FileID.add(file);

            int current_server = FileLocations.get(file).iterator().next();
            reqAssignations[i] = current_server;
            serverTransmissionTimes[i] += servers.tranmissionTime(current_server, user);
        }
        calcMaxTransmissionTimeServer();

        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();

        System.out.println("First node heuristic: " + hf.getHeuristicValue(this));
    }

    public Prac1State (Prac1State estatAnterior) {
        this.UserID = estatAnterior.getUserID();
        this.FileID = estatAnterior.getFileID();
        this.FileLocations = estatAnterior.getFileLocations();
        this.nserv = estatAnterior.getNserv();
        this.reqAssignations = estatAnterior.getReqAssignations().clone();
        this.serverTransmissionTimes = estatAnterior.getTransmissionTimes().clone();
        this.servers = estatAnterior.getServers();
    }

    public void changeAssignation (int i) {
        int file = FileID.get(i);
        int previous_server = reqAssignations[i];
        serverTransmissionTimes[previous_server] -= this.servers.tranmissionTime(previous_server, UserID.get(i));
        Iterator<Integer> it = FileLocations.get(file).iterator();
        Set <Integer> serversWithTheFile = FileLocations.get(file);
        Random rand = new Random();

        for (int x = 0; x < rand.nextInt(serversWithTheFile.size()); ++x)
            it.next();

        reqAssignations[i] = it.next();
        int new_server = reqAssignations[i];
        serverTransmissionTimes[new_server] += this.servers.tranmissionTime(new_server, UserID.get(i));

        // Check max transmission time server
        calcMaxTransmissionTimeServer();
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

    public int getServerTransmissionTime(int i) {
        return serverTransmissionTimes[i];
    }

    public int[] getTransmissionTimes() {
        return serverTransmissionTimes;
    }

    private void calcMaxTransmissionTimeServer () {
        int maxTransimssionTime = 0;
        for (int i = 0; i < this.nserv; ++i) {
            if (getServerTransmissionTime(i) > maxTransimssionTime) {
                maxTransimssionTime = getServerTransmissionTime(i);
                this.maxTransmissionTimeServer = i;
            }
        }
    }

    public int getMaxTransmissionTimeServer() {
        return maxTransmissionTimeServer;
    }

    public int getMaxTime() {
        return serverTransmissionTimes[maxTransmissionTimeServer];
    }

    public double getTotalTime() {
        double totalTime = 0;
        for (int i = 0; i < serverTransmissionTimes.length; ++i) {
            totalTime += serverTransmissionTimes[i];
        }
        return totalTime;
    }
}
