package movement;

import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class SupplierRelocationGenerator {

  public static List<RelocationMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int suppliers = problem.getSuppliers();
    int in_doors = problem.getInDoors();
    List<RelocationMovement> movements = new LinkedList<RelocationMovement>();
    for (int supplier = 0; supplier < suppliers; supplier++) {
      for (int door = 0; door < in_doors; door++) {
        int assigned_door = solution.getSupplierDoor(supplier);
        int pallets = problem.getSupplierPallets(supplier);
        int capacity_left = solution.getInCapacity(door);
        if (assigned_door != door && pallets <= capacity_left) {
          double delta = Delta.SupplierRelocation(supplier, door, solution);
          movements.add(new RelocationMovement(supplier, door, delta));
        }
      }
    }
    return movements;
  }
}