package solver.grasp;

import java.util.HashSet;

import model.OptimizationProblem;
import model.Solution;
import solver.ConstructiveMinCapacityRandom;
import solver.Solver;
import solver.search.GreedyCustomerDoorSwap;
import solver.search.GreedyCustomerRelocation;
import solver.search.GreedyCustomerSwap;
import solver.search.GreedySupplierDoorSwap;
import solver.search.GreedySupplierRelocation;
import solver.search.GreedySupplierSwap;
import utils.Constants;

public class GraspVND_RSDMinCapacity implements Solver {
  OptimizationProblem problem_;
  int iterations_;
  HashSet<Solution> solutions_explored_;

  public GraspVND_RSDMinCapacity(OptimizationProblem problem, int iterations) {
    problem_ = problem;
    iterations_ = iterations;
    solutions_explored_ = new HashSet<>();
  }

  public Solution run() throws Exception {
    int max_iterations_without_improvement = iterations_ / 100;
    ConstructiveMinCapacityRandom constructiveMinCapacityRandom = new ConstructiveMinCapacityRandom(problem_,
        problem_.getInDoors());
    Solution best_solution = initialSolution(constructiveMinCapacityRandom);
    int iterations_without_improvement = 0;
    for (int i = 0; i < iterations_; i++) {
      Solution current_solution = constructivePhase(constructiveMinCapacityRandom);

      if (!solutions_explored_.contains(current_solution) && current_solution.isFeasible()) {
        solutions_explored_.add(current_solution);
        Solution vnd_solution = vnd(current_solution);
        if (vnd_solution.getObjectiveFunctionValue() < best_solution.getObjectiveFunctionValue()) {
          best_solution = vnd_solution;
          iterations_without_improvement = 0;
        } else {
          ++iterations_without_improvement;
        }
      }
      if (iterations_without_improvement > max_iterations_without_improvement) {
        break;
      }
    }
    return best_solution;
  }

  private Solution initialSolution(ConstructiveMinCapacityRandom constructiveMinCapacityRandom) throws Exception {
    Solution solution = constructiveMinCapacityRandom.run();
    while (!solution.isFeasible()) {
      solution = constructiveMinCapacityRandom.run();
    }
    return solution;
  }

  private Solution vnd(Solution current_solution) throws Error {
    int neighborhood = 0;
    while (neighborhood < Constants.NEIGHBORHOODS) {
      Solution local_search_solution = saturated_local_search(current_solution, neighborhood);

      if (local_search_solution.getObjectiveFunctionValue() < current_solution.getObjectiveFunctionValue()) {
        current_solution = local_search_solution;
        neighborhood = 0;
      } else {
        ++neighborhood;
      }
    }
    return current_solution;
  }

  private Solution saturated_local_search(Solution current_solution, int neighborhood) throws Error {
    // Relocation - Truck Swap - Door Swap
    switch (neighborhood) {
      case 0:
        return new GreedySupplierRelocation(current_solution).run();
      case 1:
        return new GreedyCustomerRelocation(current_solution).run();
      case 2:
        return new GreedySupplierSwap(current_solution).run();
      case 3:
        return new GreedyCustomerSwap(current_solution).run();
      case 4:
        return new GreedySupplierDoorSwap(current_solution).run();
      case 5:
        return new GreedyCustomerDoorSwap(current_solution).run();
      default:
        throw new Error("Unknown Neighborhood");
    }
  }

  private Solution constructivePhase(ConstructiveMinCapacityRandom constructiveMinCapacityRandom) throws Exception {
    Solution current_solution = constructiveMinCapacityRandom.run();
    int feasible_iteration = 0;
    while (!current_solution.isFeasible() && feasible_iteration < Constants.ITERATIONS_CONSTRUCTIVE_PHASE_GRASP) {
      current_solution = constructiveMinCapacityRandom.run();
      ++feasible_iteration;
    }
    return current_solution;
  }
}
