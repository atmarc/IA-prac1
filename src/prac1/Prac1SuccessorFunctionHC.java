package prac1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class Prac1SuccessorFunctionHC implements SuccessorFunction {

    public List getSuccessors(Object o) {
        Prac1State father = (Prac1State) o;
        ArrayList<Successor> successors = new ArrayList<>();
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();
        for (int i = 0; i < father.getNreq(); ++i) {

            // Operador moure
            // ---------------------------------------------------------------------------------------------------
            int file = father.getFileID().get(i);
            Set<Integer> serversWithFile = father.getFileLocations().get(file);
            Iterator <Integer> it = serversWithFile.iterator();

            for (int j = 0; j < serversWithFile.size(); ++j) {
                Prac1State child = new Prac1State(father);
                int oldServerID = father.getReqAssignations()[i];
                int newServerID = it.next();
                child.moveAssignation(i, newServerID);
                successors.add(new Successor("Change server " + oldServerID + " assignation's to server " +
                        newServerID + " Heuristic: "+ hf.getHeuristicValue(child), child));
            }
            // ---------------------------------------------------------------------------------------------------

            // Operador permutar
            // ---------------------------------------------------------------------------------------------------
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
                    successors.add(new Successor("Change Assignations " + i + " <--> " + j + " " +
                            hf.getHeuristicValue(child), child));
                }
            }
            // ---------------------------------------------------------------------------------------------------
        }
        return successors;
    }

}
