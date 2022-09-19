package solver;

import org.junit.jupiter.api.Test;

import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

public class TestConstructiveMaxCapacityRandom {

  @Test
  public void smallSymmetricExample25Slackness25Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x25.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMaxCapacityRandom cmax_random = new ConstructiveMaxCapacityRandom(problem, 1);
    Solution solution = cmax_random.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 80);
  }

  @Test
  public void smallSymmetricExample25Slackness75Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x75.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMaxCapacityRandom cmax_random = new ConstructiveMaxCapacityRandom(problem, 1);
    Solution solution = cmax_random.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 327);
  }

  @Test
  public void notFeasibleSolution() throws Exception {
    String path = Path.of("", "src/test/java/resources/SimpleExample.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMaxCapacityRandom cmax_random = new ConstructiveMaxCapacityRandom(problem, 1);
    Solution solution = cmax_random.run();

    assertEquals(solution.getObjectiveFunctionValue(), Constants.NOT_FEASIBLE);
  }

}
