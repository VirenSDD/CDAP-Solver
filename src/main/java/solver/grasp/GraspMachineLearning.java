package solver.grasp;

import java.util.HashSet;

import model.OptimizationProblem;
import model.Solution;
import solver.ConstructiveMaxCapacityRandom;
import solver.Solver;
import solver.search.GreedyCustomerDoorSwap;
import solver.search.GreedyCustomerRelocation;
import solver.search.GreedyCustomerSwap;
import solver.search.GreedySupplierDoorSwap;
import solver.search.GreedySupplierRelocation;
import solver.search.GreedySupplierSwap;
import utils.Constants;

public class GraspMachineLearning implements Solver {
  OptimizationProblem problem_;
  int iterations_;
  HashSet<Solution> solutions_explored_;
  double[] weights_;
  int[] neighborhood_improvements_;
  int total_improvements_;

  public GraspMachineLearning(OptimizationProblem problem, int iterations) {
    problem_ = problem;
    iterations_ = iterations;
    solutions_explored_ = new HashSet<>();
    weights_ = new double[Constants.NEIGHBORHOODS];
    neighborhood_improvements_ = new int[Constants.NEIGHBORHOODS];
    total_improvements_ = 0;
  }

  public Solution run() throws Exception {
    int max_iterations_without_improvement = iterations_ / 100;
    ConstructiveMaxCapacityRandom constructiveMaxCapacityRandom = new ConstructiveMaxCapacityRandom(problem_,
        problem_.getInDoors());
    Solution best_solution = initialSolution(constructiveMaxCapacityRandom);
    int iterations_without_improvement = 0;
    for (int i = 0; i < iterations_; i++) {
      Solution current_solution = constructivePhase(constructiveMaxCapacityRandom);

      if (!solutions_explored_.contains(current_solution) && current_solution.isFeasible()) {
        solutions_explored_.add(current_solution);
        Solution vnd_solution = vndMachineLearning(current_solution);
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

  private Solution initialSolution(ConstructiveMaxCapacityRandom constructiveMaxCapacityRandom) throws Exception {
    Solution solution = constructiveMaxCapacityRandom.run();
    while (!solution.isFeasible()) {
      solution = constructiveMaxCapacityRandom.run();
    }
    return solution;
  }

  private Solution constructivePhase(ConstructiveMaxCapacityRandom constructiveMaxCapacityRandom) throws Exception {
    Solution current_solution = constructiveMaxCapacityRandom.run();
    int feasible_iteration = 0;
    while (!current_solution.isFeasible() && feasible_iteration < Constants.ITERATIONS_CONSTRUCTIVE_PHASE_GRASP) {
      current_solution = constructiveMaxCapacityRandom.run();
      ++feasible_iteration;
    }
    return current_solution;
  }

  private Solution vndMachineLearning(Solution current_solution) throws Error {
    boolean[] active_neighborhoods = new boolean[Constants.NEIGHBORHOODS];
    for (int i = 0; i < Constants.NEIGHBORHOODS; i++) {
      active_neighborhoods[i] = true;
    }

    int neighborhoods_visited = 0;

    while (neighborhoods_visited < Constants.NEIGHBORHOODS) {
      updateWeights(active_neighborhoods);
      int neighborhood = getRandomWeightedNeighborhood();
      ++neighborhoods_visited;

      Solution local_search_solution = local_search(current_solution, neighborhood);
      active_neighborhoods[neighborhood] = false;
      if (local_search_solution.getObjectiveFunctionValue() < current_solution.getObjectiveFunctionValue()) {
        current_solution = local_search_solution;
        ++neighborhood_improvements_[neighborhood];
        ++total_improvements_;
        neighborhoods_visited = 0;
        for (int i = 0; i < Constants.NEIGHBORHOODS; i++) {
          if (i != neighborhood) {
            active_neighborhoods[i] = true;
          }
        }
      } else {
        ++neighborhoods_visited;
      }

    }
    return current_solution;
  }

  private void updateWeights(boolean[] active_neighborhoods) {
    if (total_improvements_ == 0) {
      for (int i = 0; i < Constants.NEIGHBORHOODS; i++) {
        weights_[i] = Constants.INITIAL_WEIGHT;
      }
    } else {
      for (int i = 0; i < Constants.NEIGHBORHOODS; i++) {
        double active = active_neighborhoods[i] ? 1.0 : 0.0;
        weights_[i] = active * getNewWeight(i);
      }
    }
  }

  private double getNewWeight(int neighborhood) {
    return ((Constants.IMPROVEMENT_WEIGHT * (neighborhood_improvements_[neighborhood] / (double) total_improvements_))
        + Constants.INITIAL_WEIGHT);
  }

  // This function returns the next active neighborhood after current_neighborhood

  // private int getNextActiveNeighborhood(boolean[] active_neighborhoods, int
  // current_neighborhood) {
  // for (int i = 1; i < Constants.NEIGHBORHOODS - 1; i++) {
  // int valid_neighborhood = (current_neighborhood + i) %
  // Constants.NEIGHBORHOODS;
  // if (active_neighborhoods[valid_neighborhood]) {
  // return valid_neighborhood;
  // }
  // }
  // return -1;
  // }

  private int getRandomWeightedNeighborhood() {
    double totalWeight = 0.0;
    for (double weight : weights_) {
      totalWeight += weight;
    }
    int idx = 0;
    for (double r = Math.random() * totalWeight; idx < Constants.NEIGHBORHOODS; idx++) {
      r -= weights_[idx];
      if (r <= 0.0)
        break;
    }
    int random_neighborhood = idx;
    return random_neighborhood;
  }

  private Solution local_search(Solution current_solution, int neighborhood) throws Error {
    // Door Swap - Relocation - Truck Swap
    switch (neighborhood) {
      case 0:
        return new GreedySupplierDoorSwap(current_solution).run();
      case 1:
        return new GreedyCustomerDoorSwap(current_solution).run();
      case 2:
        return new GreedySupplierRelocation(current_solution).run();
      case 3:
        return new GreedyCustomerRelocation(current_solution).run();
      case 4:
        return new GreedySupplierSwap(current_solution).run();
      case 5:
        return new GreedyCustomerSwap(current_solution).run();
      default:
        throw new Error("Unknown Neighborhood");
    }
  }
}
