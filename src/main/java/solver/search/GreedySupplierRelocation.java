package solver.search;

import java.util.Comparator;
import java.util.List;

import model.Solution;
import movement.RelocationMovement;
import movement.SupplierRelocationGenerator;
import solver.Solver;
import utils.ChangeAssignment;

public class GreedySupplierRelocation implements Solver {
  Solution solution_;

  public GreedySupplierRelocation(Solution solution) {
    solution_ = solution;
  }

  public Solution run() {
    boolean solution_improved = false;
    Solution best_solution = solution_;
    do {
      solution_improved = false;
      List<RelocationMovement> movements = SupplierRelocationGenerator.generateMovements(best_solution);
      movements.sort(Comparator.comparing(RelocationMovement::getDelta));
      if (movements.size() > 0) {
        RelocationMovement best_candidate = movements.get(0);
        if (best_candidate.getDelta() < 0) {
          best_solution = ChangeAssignment.supplier(best_solution, best_candidate.getTruck(),
              best_candidate.getNewDoor(), best_candidate.getDelta());
          solution_improved = true;
        }
      }
    } while (solution_improved);

    return best_solution;
  }
}
