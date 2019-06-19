package ewasko.simpleFactory;

import ewasko.crossingOver.CX;
import ewasko.crossingOver.CrossingOver;
import ewasko.crossingOver.OX;
import ewasko.crossingOver.PMX;

public class CrossingOverFactory {

    public CrossingOver createCrossingOver(String type) {
        CrossingOver crossingOver;
        if (type.equals("CX")) {
            crossingOver = new CX();

        }
        else if(type.equals("PMX")){
            crossingOver = new PMX();
        }
        else {
            crossingOver = new OX();
        }
        return crossingOver;
    }
}