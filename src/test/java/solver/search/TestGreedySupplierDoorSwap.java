package solver.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import solver.ConstructiveMinCapacity;

public class TestGreedySupplierDoorSwap {
  @Test
  public void SetA_10x4S10() throws Exception {
    String path = Path.of("", "src/test/java/resources/SetA_10x4S10.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    Solution initial_solution = new ConstructiveMinCapacity(problem).run();
    Solution solution = new GreedySupplierDoorSwap(initial_solution).run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 6673);
  }
}
