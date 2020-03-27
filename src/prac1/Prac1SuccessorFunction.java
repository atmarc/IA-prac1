package prac1;

import aima.search.framework.HeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class Prac1SuccessorFunction implements SuccessorFunction {

    public List getSuccessors(Object o) {
        Prac1State father = (Prac1State) o;
        ArrayList<Successor> successors = new ArrayList<>();
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();
        for (int i = 0; i < father.getNreq(); ++i) {
            Set<Integer> serversWithFile = father.getFileLocations().get(i);
            Iterator <Integer> it = serversWithFile.iterator();
            for (int j = 0; j < serversWithFile.size(); ++j) {
                Prac1State child = new Prac1State(father);
                int oldServerID = father.getReqAssignations()[i];
                int newServerID = it.next();
                child.moveAssignation(i, newServerID);
                successors.add(new Successor("Change assignation's server " + oldServerID + " to server " +
                        newServerID + " Heuristic: "+ hf.getHeuristicValue(child), child));
            }




            int fileOfReq = father.getFileID().get(i);
            for (int j = 0; j < father.getNreq(); ++j) {
                int otherFile = father.getFileID().get(j);
                if (fileOfReq == otherFile && i != j) {
                    Prac1State child = new Prac1State(father);
                    try {
                        child.swapAssignation(i, j);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Change Assignations " + i + " <--> " + j + " " +
                    //        hf.getHeuristicValue(child));
                    successors.add(new Successor("Change Assignations " + i + " <--> " + j + " " +
                            hf.getHeuristicValue(child), child));
                }
            }



        }
        return successors;
    }

}
