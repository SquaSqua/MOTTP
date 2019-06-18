package ewasko.metaheuristic;

/**
 * interface for different kinds of metaheuristics able to run form runner
 */
public interface IMetaheuristics {
    Object[] run(String... chartName);
}
