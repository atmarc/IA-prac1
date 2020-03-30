package prac1;

import aima.search.framework.HeuristicFunction;
import java.util.HashMap;

public class Prac1HeuristicFunction implements HeuristicFunction {
    final int MIN_TRANSMISSION_TIME = 100;
    final int MAX_TRANSMISSION_TIME = 5000;
    final int TIME_LAPSE = MAX_TRANSMISSION_TIME - MIN_TRANSMISSION_TIME;

    public double getHeuristicValue(Object o) {

        // Fem 3 heuristiques i les combinem:
        //  - Temps màxim de transmissió
        //  - Variància total dels temps de transmissió
        //  - Suma total dels temps de transmissió

        Prac1State current = (Prac1State) o;
        return getH1(current) + getH2(current) + getH3(current);
    }

    private double getH1(Prac1State obj) {
        return obj.getMaxTime();
    }

    private double getH2(Prac1State obj) {
        double tempsTotal = obj.getTotalTime();
        double mitjana = tempsTotal / obj.getNserv();
        double sumaDesviacions = 0;

        HashMap<Integer, Integer> selects = obj.getServerTransmissionTimes();
        for(HashMap.Entry<Integer, Integer> entry : selects.entrySet()) {
            double desviacio = (mitjana - entry.getValue()) * (mitjana - entry.getValue());
            sumaDesviacions += desviacio;
        }

        double variancia = sumaDesviacions / obj.getNserv();
        //double desviacioTipica = Math.sqrt(variancia);
        //double coeficientVariacio = desviacioTipica / mitjana;
        return variancia/6;
    }

    private double getH3(Prac1State obj) {
        return obj.getTotalTime()/obj.getNserv();
    }
}
