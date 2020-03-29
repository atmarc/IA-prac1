import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import prac1.*;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int nserv = 50; // Number of servers
        int nrep = 5; // Number of repetitons of files
        int nUsers = 200;
        int requestsPerUser = 5;
        int seed = 1299364;

        ArrayList<Integer> Ks = new ArrayList<>(List.of(1, 5, 25, 125));
        ArrayList<Double> lmbds = new ArrayList<>(List.of(0.001D, 0.0005D, 0.0001D, 0.00005D, 0.00001D));

        int nRep = 5;
        int nIter = 1;

        for (int j = 0; j < nIter; j++) {
            for (int i = 0; i < nIter; i++) {
                double sum = 0;
                for (int x = 1; x <= nRep; x++) {
                    int current_seed = seed * x;
                    Servers servers = new Servers(nserv, nrep, current_seed);
                    Requests requests = new Requests(nUsers, requestsPerUser, current_seed);
                    Prac1State initialState = new Prac1State(requests, servers, nserv, current_seed);
                    Problem problem = new Problem(initialState, new Prac1SuccessorFunctionSA(), new Prac1GoalTest(),
                            new Prac1HeuristicFunction());

                    sum += runSimulatedAnealing(problem, 100000, 100, i, lmbds.get(j));
                    //sum += runSimulatedAnealing(problem, 2000, 500, 125, 0.001D);
                    //runHillClimbing(problem);
                }
                System.out.println(sum/nRep + " " + i + " " + lmbds.get(j));
            }
        }

    }

    private static double runHillClimbing(Problem problem) throws Exception {
        Search search = new HillClimbingSearch();
        double before = System.currentTimeMillis();
        SearchAgent searchAgent = new SearchAgent(problem, search);
        double after = System.currentTimeMillis();

        //printActions(searchAgent.getActions());
        //printInstrumentation(searchAgent.getInstrumentation());

        Prac1State goal = (Prac1State) search.getGoalState();
        //System.out.println("Execution time: " + (after - before) + " ms");
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();
        //System.out.println("Punctuation: " + hf.getHeuristicValue(goal));
        //System.out.println("Max time: " + goal.getMaxTime() + " ms");
        //System.out.println(goal.getMaxTime());
        System.out.println(after - before);
        //System.out.println(after - before);
        //System.out.println("Total time: " + goal.getTotalTime() + " ms");
        return after - before;
    }

    private static double runSimulatedAnealing(Problem problem, int steps, int siter, int k, double lamb) throws Exception {
        SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(steps, siter, k, lamb);
        double before = System.currentTimeMillis();
        SearchAgent searchAgent = new SearchAgent(problem, search);
        double after = System.currentTimeMillis();
        Prac1State goal = (Prac1State) search.getGoalState();
        //printActions(searchAgent.getActions());
        //printInstrumentation(searchAgent.getInstrumentation());
        //System.out.println("Execution time: " + (after - before) + " ms");
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();
        //System.out.println("Punctuation: " + hf.getHeuristicValue(goal));
        //System.out.println("Max time: " + goal.getMaxTime() + " ms");
        int maxTime = goal.getMaxTime();
        //System.out.println(maxTime + " " + steps + " " + siter + " " + k + " " + lamb);
        //System.out.println("Total time: " + goal.getTotalTime() + " ms");
        return maxTime;
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
