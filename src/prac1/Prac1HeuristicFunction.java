package prac1;

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;

public class Prac1HeuristicFunction implements HeuristicFunction {
    final int MIN_TRANSMISSION_TIME = 100;
    final int MAX_TRANSMISSION_TIME = 5000;
    final int TIME_LAPSE = MAX_TRANSMISSION_TIME - MIN_TRANSMISSION_TIME;

    public double getHeuristicValue(Object o) {

        // Fer 3 heuristiques i combinar-les:
        //  - Temps màxim de transmissió
        //  - Variància total dels temps de transmissió
        //  - Suma total dels temps de transmissió

        Prac1State current = (Prac1State) o;

        double h1 = getH1(current);
        double h2 = getH2(current);
        double h3 = getH3(current);

        double heuristicaSuprema = (h1 + h2 + h3);
        //System.out.println(h1 + " " + h2 + " " + h3);
        return heuristicaSuprema;
    }

    public double getH1(Prac1State obj) {
        return obj.getMaxTime();
    }

    public double getH2(Prac1State obj) {

        double tempsTotal = obj.getTotalTime();

        double mitjana = tempsTotal / obj.getNserv();
        double sumaDesviacions = 0;

        HashMap<Integer, Integer> selects = obj.getServerTransmissionTimes();

        for(HashMap.Entry<Integer, Integer> entry : selects.entrySet()) {
            double desviacio = (mitjana - entry.getValue()) * (mitjana - entry.getValue());
            sumaDesviacions += desviacio;
        }

        double variancia = sumaDesviacions / obj.getNserv();
        double desviacioTipica = Math.sqrt(variancia);
        double coeficientVariacio = desviacioTipica / mitjana;
        //return coeficientVariacio;
        return variancia/6;
    }

    public double getH3(Prac1State obj) {
        int nReqs = obj.getNreq();
        double tempsTotal = obj.getTotalTime();
        double h3 = ((tempsTotal - MIN_TRANSMISSION_TIME * nReqs)/(TIME_LAPSE * nReqs));
        //return h3;
        return tempsTotal/obj.getNserv();
    }
}
