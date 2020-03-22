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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {
        int nserv = 50; // Number of servers
        int nrep = 5; // Number of repetitons of files
        int nUsers = 200;
        int requestsPerUser = 5;
        int seed = 1234;

        Servers servers = new Servers(nserv, nrep, seed);
        Requests requests = new Requests(nUsers, requestsPerUser, seed);

        Prac1State initialState = new Prac1State(requests, servers, nserv, seed);


        Problem problem = new Problem(initialState, new Prac1SuccessorFunction(), new Prac1GoalTest(),
                new Prac1HeuristicFunction());

        Search search = new HillClimbingSearch();
        //SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(2000, 100, 5, 0.001D);

        SearchAgent searchAgent = new SearchAgent(problem, search);
        System.out.println("Starting program!");

        printActions(searchAgent.getActions());
        printInstrumentation(searchAgent.getInstrumentation());

        Prac1State goal = (Prac1State) search.getGoalState();
        String s = "";
        int [] goalAssig = goal.getReqAssignations();
        for (int i = 0; i < goalAssig.length; ++i) {
            s += goalAssig[i] + " ";
        }
        System.out.println(s);
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();
        System.out.println("Punctuation: " + hf.getHeuristicValue(goal));
        System.out.println("Max time: " + goal.getMaxTime() + " ms");
        System.out.println("Total time: " + goal.getTotalTime() + " ms");
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
