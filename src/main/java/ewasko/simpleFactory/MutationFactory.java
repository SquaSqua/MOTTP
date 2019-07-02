package ewasko.simpleFactory;

import ewasko.mutation.Mutation;
import ewasko.mutation.MutationDisplacement;
import ewasko.mutation.MutationInversion;
import ewasko.mutation.MutationSwap;

public class MutationFactory {

    public Mutation createMutation(String type) {
        Mutation mutation;
        if (type.equals("MutationDisplacement") || type.equals("1")) {
            mutation = new MutationDisplacement();

        }
        else if(type.equals("MutationInversion") || type.equals("2")){
            mutation = new MutationInversion();
        }
        else {
            mutation = new MutationSwap();
        }
        return mutation;
    }
}