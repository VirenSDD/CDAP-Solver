package solver;

import org.junit.jupiter.api.Test;

import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

public class TestConstructiveSuppliersDependent {

  @Test
  public void smallSymmetricExample25Slackness25Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x25.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveSuppliersDependent csd = new ConstructiveSuppliersDependent(problem);
    Solution solution = csd.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 80);
  }

  @Test
  public void smallSymmetricExample25Slackness75Density() throws Exception {
    String path = Path.of("", "src/test/java/resources/4x2x25x75.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveSuppliersDependent csd = new ConstructiveSuppliersDependent(problem);
    Solution solution = csd.run();

    double solutionObjectiveValue = solution.getObjectiveFunctionValue();

    assertEquals(solutionObjectiveValue, 322);
  }

  @Test
  public void notFeasibleSolution() throws Exception {
    String path = Path.of("", "src/test/java/resources/SimpleExample.json").toString();
    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(path);
    ConstructiveSuppliersDependent csd = new ConstructiveSuppliersDependent(problem);

    Throwable exception = assertThrows(Exception.class, () -> {
      csd.run();
    });

    assertEquals(exception.getMessage(), "Solution not feasible using this algorithm.");
  }
}