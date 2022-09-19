package solver.search;

import java.util.Comparator;
import java.util.List;

import model.Solution;
import movement.CustomerSwapGenerator;
import movement.SwapMovement;
import solver.Solver;
import utils.ChangeAssignment;

public class GreedyCustomerSwap implements Solver {
  private Solution solution_;

  public GreedyCustomerSwap(Solution solution) {
    solution_ = solution;
  }

  public Solution run() {
    boolean solution_improved = false;
    Solution best_solution = solution_;
    do {
      solution_improved = false;
      List<SwapMovement> movements = CustomerSwapGenerator.generateMovements(best_solution);
      movements.sort(Comparator.comparing(SwapMovement::getDelta));
      if (movements.size() > 0) {
        SwapMovement best_candidate = movements.get(0);
        if (best_candidate.getDelta() < 0) {
          best_solution = ChangeAssignment.customerSwap(best_solution, best_candidate.getFirstTruck(),
              best_candidate.getSecondTruck(), best_candidate.getDelta());
          solution_improved = true;
        }
      }
    } while (solution_improved);
    return best_solution;
  }
}
