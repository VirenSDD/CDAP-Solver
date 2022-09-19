package solver;

import org.junit.jupiter.api.Test;

import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

public class TestConstructiveMinCapacity {

  @Test
  public void smallSymmetricExample25Slackness25Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x25.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMinCapacity cmin = new ConstructiveMinCapacity(problem);
    Solution solution = cmin.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 80);
  }

  @Test
  public void smallSymmetricExample25Slackness75Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x75.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMinCapacity cmin = new ConstructiveMinCapacity(problem);
    Solution solution = cmin.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 319);
  }

  @Test
  public void notFeasibleSolution() throws Exception {
    String path = Path.of("", "src/test/java/resources/SimpleExample.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveMinCapacity cmin = new ConstructiveMinCapacity(problem);
    Solution solution = cmin.run();

    assertEquals(solution.getObjectiveFunctionValue(), Constants.NOT_FEASIBLE);
  }

}
