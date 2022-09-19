package solver.feasible;

import java.util.HashSet;

import model.OptimizationProblem;
import model.Solution;
import solver.ConstructiveMaxCapacityRandom;
import utils.Constants;

public class MaxCapacityFeasibleCalculator {

  int feasible_solutions_;
  int unique_feasible_solutions_;
  private static int MAX_ITERATIONS = Constants.ITERATIONS_FEASIBLE_CALCULATOR;

  public MaxCapacityFeasibleCalculator(OptimizationProblem problem) throws Exception {
    ConstructiveMaxCapacityRandom constructiveMaxCapacityRandom = new ConstructiveMaxCapacityRandom(problem,
        problem.getInDoors());

    HashSet<Solution> hash_set = new HashSet<>();

    for (int i = 0; i < MAX_ITERATIONS; i++) {
      Solution solution = constructiveMaxCapacityRandom.run();
      if (solution.isFeasible()) {
        hash_set.add(solution);
        ++feasible_solutions_;
      }
    }
    unique_feasible_solutions_ = hash_set.size();
  }

  public int getFeasibleSolutionsNumber() {
    return feasible_solutions_;
  }

  public int getUniqueFeasibleSolutionsNumber() {
    return unique_feasible_solutions_;
  }
}
