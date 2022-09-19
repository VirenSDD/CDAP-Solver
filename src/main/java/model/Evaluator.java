package model;

import utils.Constants;

public interface Evaluator {

  public static double evaluate(Solution solution) {
    if (solution.getObjectiveFunctionValue() == Constants.NOT_EVALUATED) {
      OptimizationProblem problem = solution.getProblem();
      double result = 0;
      int suppliers_number = problem.getSuppliers();
      for (int i = 0; i < suppliers_number; i++) {
        int[] customers = problem.getSupplierCustomers(i);
        for (int j : customers) {
          int pallets = problem.getDelivery(i, j);
          int doors_distance = problem.getDistance(solution.getSupplierDoor(i), solution.getCustomerDoor(j));
          result += pallets * doors_distance;
        }
      }
      return result;
    } else {
      return Constants.NOT_FEASIBLE;
    }
  }
}
