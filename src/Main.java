import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import prac1.Prac1GoalTest;
import prac1.Prac1HeuristicFunction;
import prac1.Prac1State;
import prac1.Prac1SuccessorFunction;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int nserv = 50; // Number of servers
        int nrep = 5; // Number of repetitons of files
        int nUsers = 200;
        int requestsPerUser = 5;
        int seed = 1234;
        Random random = new Random();
        ArrayList<Integer> Ks = new ArrayList<>(List.of(1, 5, 25, 50, 125, 250, 500));
        ArrayList<Double> lmbds = new ArrayList<>(List.of(100D, 10D, 1D, 0.1D, 0.01D, 0.001D, 0.0001D));

        /*

        int nIter = 1;
        int nRep = 1;

        for (int j = 0; j < nIter; j++) {
            for (int i = 0; i < nIter; i++) {
                double sum = 0;
                for (int x = 1; x <= nRep; x++) {
                    int current_seed = seed * x;
                    Servers servers = new Servers(nserv, nrep, current_seed);
                    Requests requests = new Requests(nUsers, requestsPerUser, current_seed);
                    Prac1State initialState = new Prac1State(requests, servers, nserv, current_seed);
                    Problem problem = new Problem(initialState, new Prac1SuccessorFunction(), new Prac1GoalTest(),
                            new Prac1HeuristicFunction());

                    //sum += runSimulatedAnealing(problem, 8000, 2000, Ks.get(i), lmbds.get(j));
                    sum += runSimulatedAnealing(problem, 2000, 500, 125, 0.001D);
                    //runHillClimbing(problem);
                }
                //System.out.println(sum/nIter + " " + Ks.get(i) + " " + lmbds.get(j));
            }
        }

         */

        /* Experiment 4
        for (;nserv < 1000; nserv += 50) {
            double totalExecutionTime = 0;
            for (int i = 0; i < 10; ++i) {
                seed = random.nextInt(900000);
                Servers servers = new Servers(nserv, nrep, seed);
                Requests requests = new Requests(nUsers, requestsPerUser, seed);
                Prac1State initialState = new Prac1State(requests, servers, nserv, seed);
                Problem problem = new Problem(initialState, new Prac1SuccessorFunction(), new Prac1GoalTest(),
                        new Prac1HeuristicFunction());
                totalExecutionTime += runHillClimbing(problem);
            }
            System.out.println(Double.toString(totalExecutionTime / 10.0));
        }
         */



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
        //System.out.println(after - before);
        //System.out.println("Total time: " + goal.getTotalTime() + " ms");
        return after - before;
    }

    private static double runSimulatedAnealing(Problem problem, int steps, int siter, int k, double lamb) throws Exception {
        SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(steps, siter, k, lamb);
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
        System.out.println(goal.getMaxTime() + " " + steps + " " + siter + " " + k + " " + lamb);
        //System.out.println("Total time: " + goal.getTotalTime() + " ms");
        return goal.getMaxTime();
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
