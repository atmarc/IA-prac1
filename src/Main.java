import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import prac1.Prac1GoalTest;
import prac1.Prac1HeuristicFunction;
import prac1.Prac1State;
import prac1.Prac1SuccessorFunction;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {
        int nserv = 100; // Number of servers
        int nrep = 10; // Number of repetitons of files
        int nUsers = 100;
        int requestsPerUser = 10;
        int seed = 12345678;

        Servers servers = new Servers(nserv, nrep, seed);
        Requests requests = new Requests(nUsers, requestsPerUser, seed);

        Prac1State initialState = new Prac1State(requests, servers, nserv, seed);


        Problem problem = new Problem(initialState, new Prac1SuccessorFunction(), new Prac1GoalTest(),
                new Prac1HeuristicFunction());

        Search search = new HillClimbingSearch();

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
