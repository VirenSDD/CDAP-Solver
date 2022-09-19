package movement;

import java.util.LinkedList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.Delta;

public class SupplierSwapGenerator {
  public static List<SwapMovement> generateMovements(Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    int suppliers = problem.getSuppliers();
    List<SwapMovement> movements = new LinkedList<SwapMovement>();
    for (int first_supplier = 0; first_supplier < suppliers - 1; first_supplier++) {
      for (int second_supplier = first_supplier + 1; second_supplier < suppliers; second_supplier++) {
        int first_door = solution.getSupplierDoor(first_supplier);
        int second_door = solution.getSupplierDoor(second_supplier);
        int first_capacity_left = solution.getInCapacity(first_door);
        int second_capacity_left = solution.getInCapacity(second_door);
        int first_pallets = problem.getSupplierPallets(first_supplier);
        int second_pallets = problem.getSupplierPallets(second_supplier);
        if (first_capacity_left - second_pallets >= 0 && second_capacity_left - first_pallets >= 0) {
          double delta = Delta.SupplierRelocation(first_supplier, second_door, solution)
              + Delta.SupplierRelocation(second_supplier, first_door, solution);
          movements.add(new SwapMovement(first_supplier, second_supplier, delta));
        }
      }
    }
    return movements;
  }
}
