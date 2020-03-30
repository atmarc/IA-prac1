package prac1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class Prac1SuccessorFunctionSA implements SuccessorFunction {

    public List getSuccessors(Object o) {
        Prac1State father = (Prac1State) o;
        ArrayList<Successor> successors = new ArrayList<>();
        Prac1HeuristicFunction hf = new Prac1HeuristicFunction();

        int operadorRandom = new Random().nextInt(10);

        if (operadorRandom > 5) {
            int reqI = new Random().nextInt(father.getNreq());
            int file = father.getFileID().get(reqI);
            Set<Integer> serversWithFile = father.getFileLocations().get(file);
            Iterator <Integer> it = serversWithFile.iterator();

            Prac1State child = new Prac1State(father);

            int oldServerID = father.getReqAssignations()[reqI];
            int newServerID = oldServerID;
            int randNum = new Random().nextInt(serversWithFile.size());
            for (int i = 0; i < randNum; ++i) newServerID = it.next();
            child.moveAssignation(reqI, newServerID);
            successors.add(new Successor("Change server " + oldServerID + " assignation's to server " +
                    newServerID + " Heuristic: "+ hf.getHeuristicValue(child), child));
        }
        else {
            int file1, file2, reqI, reqI2;
            int secure = 0;
            do {
                secure++;
                reqI = new Random().nextInt(father.getNreq());
                do {
                    reqI2 = new Random().nextInt(father.getNreq());
                } while (reqI2 == reqI);

                file1 = father.getFileID().get(reqI);
                file2 = father.getFileID().get(reqI2);
            }
            while (file1 != file2 && secure < father.getNreq() * 2);

            if (secure < father.getNreq() * 2) {
                Prac1State child2 = new Prac1State(father);
                try {
                    child2.swapAssignation(reqI, reqI2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                successors.add(new Successor("Change Assignations " + reqI + " <--> " + reqI2 + " " +
                        hf.getHeuristicValue(child2), child2));
            }
        }
        return successors;
    }

}
