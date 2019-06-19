package ewasko.simpleFactory;

import ewasko.generator.ParetoFrontsGenerator;
import ewasko.mutation.Mutation;
import ewasko.mutation.MutationDisplacement;
import ewasko.mutation.MutationInversion;
import ewasko.mutation.MutationSwap;

public class MutationFactory {

    public Mutation createMutation(String type) {
        Mutation mutation;
        if (type.equals("MutationDisplacement")) {
            mutation = new MutationDisplacement();

        }
        else if(type.equals("MutationInversion")){
            mutation = new MutationInversion();
        }
        else {
            mutation = new MutationSwap();
        }
        return mutation;
    }
}