import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import prac1.*;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int nserv = 50;             // Number of servers
        int nrep = 5;               // Number of repetitons of files
        int nUsers = 200;           // Number of users
        int requestsPerUser = 5;    // Number of requests per user
        int seed = 78411;           // Random seed



        // Per executar l'algoritme Simulated Annealing, descomentar les linies 26 i 31 i comentar les linies 27 i 32.

        Servers servers = new Servers(nserv, nrep, seed);
        Requests requests = new Requests(nUsers, requestsPerUser, seed);
        Prac1State initialState = new Prac1State(requests, servers, nserv);
        //SuccessorFunction successor = new Prac1SuccessorFunctionSA();
        SuccessorFunction successor = new Prac1SuccessorFunctionHC();

        Problem problem = new Problem(initialState, successor, new Prac1GoalTest(), new Prac1HeuristicFunction());

        //runSimulatedAnealing(problem);
        runHillClimbing(problem);
    }

    private static double runHillClimbing(Problem problem) throws Exception {
        Search search = new HillClimbingSearch();
        double before = System.currentTimeMillis();
        SearchAgent searchAgent = new SearchAgent(problem, search);
        double after = System.currentTimeMillis();

        //printActions(searchAgent.getActions());
        //printInstrumentation(searchAgent.getInstrumentation());
        Prac1State goal = (Prac1State) search.getGoalState();
        System.out.println("Temps màxim: " + goal.getMaxTime() + " Temps total: " + goal.getTotalTime() +
                " Temps d'execució: " + (int)(after - before) + " ms");
        return after - before;
    }

    private static double runSimulatedAnealing(Problem problem) throws Exception {
        SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(100000, 100, 5, 0.001);
        double before = System.currentTimeMillis();
        SearchAgent searchAgent = new SearchAgent(problem, search);
        double after = System.currentTimeMillis();
        //printActions(searchAgent.getActions());
        //printInstrumentation(searchAgent.getInstrumentation());
        Prac1State goal = (Prac1State) search.getGoalState();
        System.out.println("Temps màxim: " + goal.getMaxTime() + " Temps total: " + goal.getTotalTime() +
                " Temps d'execució: " + (int)(after - before) + " ms");

        return after - before;
    }

    private static void printActions(List actions) {
        for(int i = 0; i < actions.size(); ++i) {
            String action = (String)actions.get(i);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

}
