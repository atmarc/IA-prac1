import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import prac1.Prac1GoalTest;
import prac1.Prac1HeuristicFunction;
import prac1.Prac1Estate;
import prac1.Prac1SuccessorFunction;

public class Main {

    public static void main(String[] args) throws Exception {
        Servers serv = new Servers(100, 100, 1);
        Requests req = new Requests(100, 100, 1);

        Prac1Estate prac1Estate = new Prac1Estate(req, serv);

        Problem problem = new Problem(prac1Estate, new Prac1SuccessorFunction(), new Prac1GoalTest(),
                new Prac1HeuristicFunction());

        System.out.println("Starting program");
    }

}
