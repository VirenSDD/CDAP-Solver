package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.OptimizationProblem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

public class TestOptimizationProblemJsonParser {

  static private OptimizationProblem problem_;

  @BeforeAll
  static public void init() throws Exception {
    problem_ = OptimizationProblemJsonParser
        .parseJSONFile(Path.of("", "src/test/java/resources/SimpleExample.json").toString());
  }

  @Test
  public void testSuppliersNumber() throws Exception {
    assertEquals(problem_.getSuppliers(), 2);
  }

  @Test
  public void testCustomersNumber() throws Exception {
    assertEquals(problem_.getCustomers(), 4);
  }

  @Test
  public void testInboundDoorsNumber() throws Exception {
    assertEquals(problem_.getInDoors(), 3);
  }

  @Test
  public void testOutboundDoorsNumber() throws Exception {
    assertEquals(problem_.getOutDoors(), 5);
  }

  @Test
  public void testInboundDoorsCapacities() throws Exception {
    for (int i = 0; i < 3; i++) {
      assertEquals(problem_.getInDoorCapacity(i), 26);
    }
  }

  @Test
  public void testOutboundDoorsCapacities() throws Exception {
    for (int i = 0; i < 5; i++) {
      assertEquals(problem_.getOutDoorCapacity(i), 26);
    }
  }

  @Test
  public void testDoorsDistances() throws Exception {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++) {
        assertEquals(problem_.getDistance(i, j), 8 + Math.abs(i - j));
      }
    }
  }

  @Test
  public void testDeliveries() throws Exception {
    assertEquals(problem_.getDelivery(0, 0), 18);
    assertEquals(problem_.getDelivery(0, 2), 20);
    assertEquals(problem_.getDelivery(1, 1), 14);
    assertEquals(problem_.getDelivery(1, 3), 19);
  }
}
