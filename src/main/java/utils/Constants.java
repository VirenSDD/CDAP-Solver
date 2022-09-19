package utils;

public class Constants {
  public static final double NOT_EVALUATED = -1;
  public static final double NOT_FEASIBLE = -2;
  public static final int ITERATIONS_FEASIBLE_CALCULATOR = 10000;
  public static final int ITERATIONS_CONSTRUCTIVE_PHASE_GRASP = 100000;
  public static final int NEIGHBORHOODS = 6;
  public static final double ONE_SECOND_IN_NANOSECONDS = 1000000000;
  public static final double IMPROVEMENT_WEIGHT = 0.7;
  public static final double NEIGHBORHOODS_WEIGHT = 1 - IMPROVEMENT_WEIGHT;
  public static final double INITIAL_WEIGHT = NEIGHBORHOODS_WEIGHT / NEIGHBORHOODS;

}
