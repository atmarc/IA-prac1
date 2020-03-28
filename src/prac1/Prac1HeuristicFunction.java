package prac1;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;

public class Prac1HeuristicFunction implements HeuristicFunction {

    public double getHeuristicValue(Object o) {

        // Fer 3 heuristiques i combinar-les:
        //  - Temps màxim de transmissió
        //  - Variància total dels temps de transmissió
        //  - Suma total dels temps dev transmissió

        Prac1State current = (Prac1State) o;

/*
        int [] assignations = current.getReqAssignations();
        ArrayList<Integer> users = current.getUserID();
        double [] transmissionTimes = new double[assignations.length];
        int nReqs = current.getNreq();

        double maxTime = -1;
        double tempsTotal = 0;
        for (int i = 0; i < nReqs; ++i) {
            double time = current.getServers().tranmissionTime(assignations[i], users.get(i));
            transmissionTimes[i] = time;
            tempsTotal += time;
            if (maxTime < time) {
                maxTime = time;
            }
        }

        double mitjana = tempsTotal / nReqs;
        double sumaDesviacions = 0;

        for (int i = 0; i < nReqs; ++i) {
            double desviacio = (mitjana - transmissionTimes[i]) * (mitjana - transmissionTimes[i]);
            sumaDesviacions += desviacio;
        }

        double variancia = sumaDesviacions / nReqs;
        double desviacioTipica = Math.sqrt(variancia);
        double coeficientVariacio = desviacioTipica / mitjana;
*/
        final int MIN_TRANSMISSION_TIME = 100;
        final int MAX_TRANSMISSION_TIME = 5000;
        //final int TIME_LAPSE = MAX_TRANSMISSION_TIME - MIN_TRANSMISSION_TIME;
        final double maxH = 10;

        double maxValue = current.getMaxTime();

        double h1 = (maxValue / (MAX_TRANSMISSION_TIME * current.getNserv())) * maxH;

        //double h1 = (maxTime - MIN_TRANSMISSION_TIME)/(TIME_LAPSE) * maxH;
        //double h2 = coeficientVariacio * maxH;
        //double h3 = (tempsTotal - MIN_TRANSMISSION_TIME * nReqs)/(TIME_LAPSE * nReqs) * maxH;

        //double heuristicaSuprema = (h1 + h2 + h3) / 3;

        return maxValue;
    }
}
