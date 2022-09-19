package movement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class CustomerDoorSwapGenerator {
  public static List<DoorSwapMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int out_doors = problem.getOutDoors();
    List<DoorSwapMovement> movements = new LinkedList<DoorSwapMovement>();
    ArrayList<Integer>[] customers_in_door = new ArrayList[out_doors];
    for (int customer = 0; customer < problem.getCustomers(); customer++) {
      if (customers_in_door[solution.getCustomerDoor(customer)] == null) {
        customers_in_door[solution.getCustomerDoor(customer)] = new ArrayList<>();
      }
      customers_in_door[solution.getCustomerDoor(customer)].add(customer);
    }
    for (int first_door = 0; first_door < out_doors - 1; first_door++) {
      ArrayList<Integer> first_customers = customers_in_door[first_door];
      if (first_customers == null) {
        continue;
      }
      for (int second_door = first_door + 1; second_door < out_doors; second_door++) {
        int first_capacity_left = solution.getOutCapacity(first_door);
        int second_capacity_left = solution.getOutCapacity(second_door);
        double delta = 0;
        if (problem.getOutDoorCapacity(first_door) - second_capacity_left >= 0
            && problem.getOutDoorCapacity(second_door) - first_capacity_left >= 0) {
          ArrayList<Integer> second_customers = customers_in_door[second_door];
          if (second_customers == null) {
            continue;
          }
          for (int i = 0; i < first_customers.size(); i++) {
            delta += Delta.CustomerRelocation(first_customers.get(i), second_door, solution);
          }

          for (int i = 0; i < second_customers.size(); i++) {
            delta += Delta.CustomerRelocation(second_customers.get(i), first_door, solution);
          }
        }

        movements.add(new DoorSwapMovement(first_door, second_door, delta));
      }
    }
    return movements;
  }
}
