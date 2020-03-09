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

    private int nserv;


    public Prac1State(Requests req, Servers serv, int nserv, int seed) {
        UserID = new ArrayList<>();
        FileID = new ArrayList<>();
        FileLocations = new ArrayList<>();
        reqAssignations = new int [req.size()];
        this.nserv = nserv;

        Random rand = new Random(seed);
        for (int i = 0; i < req.size(); ++i) {
            int [] aux = req.getRequest(i);
            UserID.add(aux[0]);
            FileID.add(aux[1]);
            reqAssignations[i] = rand.nextInt(nserv);
        }

        for (int i = 0; i < serv.size(); ++i) {
            Set<Integer> aux = serv.fileLocations(i);
            FileLocations.add(aux);
        }

        this.requests = req;
        this.servers = serv;
    }

    public Prac1State (Prac1State estatAnterior) {
        this.UserID = estatAnterior.getUserID();
        this.FileID = estatAnterior.getFileID();
        this.FileLocations = estatAnterior.getFileLocations();
        this.nserv = estatAnterior.getNserv();
        this.reqAssignations = estatAnterior.getReqAssignations().clone();
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
}
