package prac1;

import IA.DistFS.Requests;
import IA.DistFS.Servers;

import java.util.ArrayList;
import java.util.Set;

public class Prac1State {

    private Requests requests;
    private Servers servers;

    // Requests
    private int [] UserID;
    private int [] FileID;

    // Servers
    private ArrayList<Set<Integer>> FileLocations;

    // Assignation
    private ArrayList<Integer> assignations; // la posició i conté el serverID que respon a la request i

    public Prac1State(Requests req, Servers serv) {
        for (int i = 0; i < req.size(); ++i) {
            list.add(req.getRequest(i));
        }
        this.requests = req;
        this.servers = serv;
    }


}
