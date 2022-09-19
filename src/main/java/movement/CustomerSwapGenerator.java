package movement;

import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class CustomerSwapGenerator {
  public static List<SwapMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int customers = problem.getCustomers();
    List<SwapMovement> movements = new LinkedList<SwapMovement>();
    for (int first_customer = 0; first_customer < customers - 1; first_customer++) {
      for (int second_customer = first_customer + 1; second_customer < customers; second_customer++) {
        int first_door = solution.getCustomerDoor(first_customer);
        int second_door = solution.getCustomerDoor(second_customer);
        if (first_door != second_door) {
          int first_capacity_left = solution.getOutCapacity(first_door);
          int second_capacity_left = solution.getOutCapacity(second_door);
          int first_pallets = problem.getCustomerPallets(first_customer);
          int second_pallets = problem.getCustomerPallets(second_customer);
          if (first_capacity_left - second_pallets >= 0 && second_capacity_left - first_pallets >= 0) {
            double delta = Delta.CustomerRelocation(first_customer, second_door, solution)
                + Delta.CustomerRelocation(second_customer, first_door, solution);
            movements.add(new SwapMovement(first_customer, second_customer, delta));
          }
        }
      }
    }

    return movements;
  }
}
