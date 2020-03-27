package prac1;

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Prac1SuccessorFunction implements SuccessorFunction {

    public List getSuccessors(Object o) {
        Prac1State father = (Prac1State) o;
        ArrayList<Successor> successors = new ArrayList<>();
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();

        for (int i = 0; i < father.getNreq(); ++i) {
            Prac1State child = new Prac1State(father);

            //child.swapAssignations(random.nextInt(father.getNreq()));
            //child.moveAssignation(random.nextInt(father.getNreq()));
            //child.changeAssignation(i);
            child.changeReqToMin(i);
            //child.swapRandom(i);

            successors.add(new Successor("Change Assignation " + i + " " + hf.getHeuristicValue(child), child));
        }

        return successors;
    }

}
