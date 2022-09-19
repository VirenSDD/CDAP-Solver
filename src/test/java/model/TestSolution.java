package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import parser.OptimizationProblemJsonParser;
import solver.ConstructiveMaxCapacity;
import solver.ConstructiveMinCapacity;

public class TestSolution {
  static private OptimizationProblem problem_SetA_8x4S15;

  @BeforeAll
  static public void init() throws Exception {
    problem_SetA_8x4S15 = OptimizationProblemJsonParser
        .parseJSONFile(Path.of("", "src/test/java/resources/SetA_8x4S15.json").toString());
  }

  @Test
  public void testEmptySolutionsAreEqual() throws Exception {
    Solution sol1 = new Solution(problem_SetA_8x4S15);
    Solution sol2 = new Solution(problem_SetA_8x4S15);
    assertEquals(sol1, sol2);
  }

  @Test
  public void testSolutionsAreNotEqual() throws Exception {
    Solution sol1 = new ConstructiveMaxCapacity(problem_SetA_8x4S15).run();
    Solution sol2 = new ConstructiveMinCapacity(problem_SetA_8x4S15).run();
    assertNotEquals(sol1, sol2);
  }

  @Test
  public void testSolutionsAreEqual() throws Exception {
    Solution sol1 = new Solution(problem_SetA_8x4S15);
    Solution sol2 = new Solution(problem_SetA_8x4S15);
    assertEquals(sol1, sol2);
  }

}