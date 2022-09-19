package solver.search;

import java.util.Comparator;
import java.util.List;

import model.Solution;
import movement.CustomerDoorSwapGenerator;
import movement.DoorSwapMovement;
import solver.Solver;
import utils.ChangeAssignment;

public class GreedyCustomerDoorSwap implements Solver {
  Solution solution_;

  public GreedyCustomerDoorSwap(Solution solution) {
    solution_ = solution;
  }

  public Solution run() {
    boolean solution_improved = false;
    Solution best_solution = solution_;
    do {
      solution_improved = false;
      List<DoorSwapMovement> movements = CustomerDoorSwapGenerator.generateMovements(best_solution);
      movements.sort(Comparator.comparing(DoorSwapMovement::getDelta));
      if (movements.size() > 0) {
        DoorSwapMovement best_candidate = movements.get(0);
        if (best_candidate.getDelta() < 0) {
          best_solution = ChangeAssignment.customerDoor(best_solution, best_candidate.getFirstDoor(),
              best_candidate.getSecondDoor(), best_candidate.getDelta());
          solution_improved = true;
        }
      }
    } while (solution_improved);

    return best_solution;
  }
}
