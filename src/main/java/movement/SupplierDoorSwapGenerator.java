package movement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class SupplierDoorSwapGenerator {
  public static List<DoorSwapMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int in_doors = problem.getInDoors();
    List<DoorSwapMovement> movements = new LinkedList<DoorSwapMovement>();
    ArrayList<Integer>[] suppliers_in_door = new ArrayList[in_doors];
    for (int supplier = 0; supplier < problem.getSuppliers(); supplier++) {
      if (suppliers_in_door[solution.getSupplierDoor(supplier)] == null) {
        suppliers_in_door[solution.getSupplierDoor(supplier)] = new ArrayList<>();
      }
      suppliers_in_door[solution.getSupplierDoor(supplier)].add(supplier);
    }
    for (int first_door = 0; first_door < in_doors - 1; first_door++) {
      ArrayList<Integer> first_suppliers = suppliers_in_door[first_door];
      if (first_suppliers == null) {
        continue;
      }

      for (int second_door = first_door + 1; second_door < in_doors; second_door++) {
        int first_capacity_left = solution.getInCapacity(first_door);
        int second_capacity_left = solution.getInCapacity(second_door);
        double delta = 0;
        if (problem.getInDoorCapacity(first_door) - second_capacity_left >= 0
            && problem.getInDoorCapacity(second_door) - first_capacity_left >= 0) {
          ArrayList<Integer> second_suppliers = suppliers_in_door[second_door];
          if (second_suppliers == null) {
            continue;
          }
          for (int i = 0; i < first_suppliers.size(); i++) {
            delta += Delta.SupplierRelocation(first_suppliers.get(i), second_door, solution);
          }

          for (int i = 0; i < second_suppliers.size(); i++) {
            delta += Delta.SupplierRelocation(second_suppliers.get(i), first_door, solution);
          }
        }

        movements.add(new DoorSwapMovement(first_door, second_door, delta));
      }
    }
    return movements;
  }
}
