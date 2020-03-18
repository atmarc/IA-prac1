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

        for (int i = 0; i < serv.size(); ++i) {
            Set<Integer> aux = serv.fileLocations(i);
            FileLocations.add(aux);
        }

        Random rand = new Random(seed);
        for (int i = 0; i < req.size(); ++i) {
            int [] aux = req.getRequest(i);
            int user = aux[0];
            int file = aux[1];

            UserID.add(user);
            FileID.add(file);

            reqAssignations[i] = FileLocations.get(file).iterator().next();
        }

        this.requests = req;
        this.servers = serv;
    }

    public Prac1State (ArrayList<Integer> userID, ArrayList<Integer> fileID, ArrayList<Set<Integer>> fileLocations,
                       int [] reqAssignations) {
        UserID = userID;
        FileID = fileID;
        FileLocations = fileLocations;
        this.reqAssignations = reqAssignations.clone();
    }

}
