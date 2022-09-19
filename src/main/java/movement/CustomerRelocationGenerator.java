package movement;

import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class CustomerRelocationGenerator {
  public static List<RelocationMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int customers = problem.getCustomers();
    int out_doors = problem.getOutDoors();
    List<RelocationMovement> movements = new LinkedList<RelocationMovement>();
    for (int customer = 0; customer < customers; customer++) {
      for (int door = 0; door < out_doors; door++) {
        int assigned_door = solution.getCustomerDoor(customer);
        int pallets = problem.getCustomerPallets(customer);
        int capacity_left = solution.getOutCapacity(door);
        if (assigned_door != door && pallets <= capacity_left) {
          double delta = Delta.CustomerRelocation(customer, door, solution);
          movements.add(new RelocationMovement(customer, door, delta));
        }
      }
    }
    return movements;
  }
}
