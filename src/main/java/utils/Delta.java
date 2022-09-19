package utils;

import model.OptimizationProblem;
import model.Solution;

public class Delta {
  public static double SupplierRelocation(int supplier, int new_door, Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int[] supplier_customers = problem.getSupplierCustomers(supplier);
    int prev_door = solution.getSupplierDoor(supplier);
    double delta = 0;
    for (int customer : supplier_customers) {
      int pallets = problem.getDelivery(supplier, customer);
      int customer_door = solution.getCustomerDoor(customer);
      int prev_distance = problem.getDistance(prev_door, customer_door);
      int new_distance = problem.getDistance(new_door, customer_door);
      delta += (pallets * (new_distance - prev_distance));
    }
    return delta;
  }

  public static double CustomerRelocation(int customer, int new_door, Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int[] customer_suppliers = problem.getCustomerSuppliers(customer);
    int prev_door = solution.getCustomerDoor(customer);
    double delta = 0;
    for (int supplier : customer_suppliers) {
      int pallets = problem.getDelivery(supplier, customer);
      int supplier_door = solution.getSupplierDoor(supplier);
      int prev_distance = problem.getDistance(supplier_door, prev_door);
      int new_distance = problem.getDistance(supplier_door, new_door);
      delta += (pallets * (new_distance - prev_distance));
    }
    return delta;
  }
}
