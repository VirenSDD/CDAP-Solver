package utils;

import model.Solution;

public class SetNotFeasible {
  public static void solution(Solution solution) {
    solution.setObjectiveFunctionValue(Constants.NOT_FEASIBLE);
  }
}
