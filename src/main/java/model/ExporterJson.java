package model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class ExporterJson {
  public static String exportSolutionToJSON(Solution solution) {
    Map data = new HashMap<>();
    OptimizationProblem problem = solution.getProblem();
    int suppliers = problem.getSuppliers();
    int customers = problem.getCustomers();
    int in_doors = problem.getInDoors();
    int out_doors = problem.getOutDoors();

    int[] in_capacities = new int[in_doors];
    int[] in_capacities_left = new int[in_doors];
    for (int i = 0; i < in_capacities.length; i++) {
      in_capacities[i] = problem.getInDoorCapacity(i);
      in_capacities_left[i] = solution.getInCapacity(i);
    }

    int[] out_capacities = new int[out_doors];
    int[] out_capacities_left = new int[out_doors];
    for (int i = 0; i < out_capacities.length; i++) {
      out_capacities[i] = problem.getOutDoorCapacity(i);
      out_capacities_left[i] = solution.getOutCapacity(i);
    }

    int[][] distances = new int[in_doors][out_doors];
    for (int i = 0; i < distances.length; i++) {
      for (int j = 0; j < distances[i].length; j++) {
        distances[i][j] = problem.getDistance(i, j);
      }
    }

    int[] suppliers_assigned = new int[suppliers];
    int[] supplier_pallets = new int[suppliers];
    int[][] supplier_customers = new int[suppliers][];
    for (int i = 0; i < suppliers; i++) {
      suppliers_assigned[i] = solution.getSupplierDoor(i);
      supplier_pallets[i] = problem.getSupplierPallets(i);
      supplier_customers[i] = problem.getSupplierCustomers(i);
    }

    int[] customers_assigned = new int[customers];
    int[] customer_pallets = new int[customers];
    for (int i = 0; i < customers; i++) {
      customers_assigned[i] = solution.getCustomerDoor(i);
      customer_pallets[i] = problem.getCustomerPallets(i);
    }

    Map<Integer, Integer>[] deliveries = new HashMap[suppliers];
    for (int i = 0; i < suppliers; i++) {
      deliveries[i] = new HashMap<>();
      for (int j = 0; j < supplier_customers[i].length; j++) {
        int current_customer = supplier_customers[i][j];
        deliveries[i].put(current_customer, problem.getDelivery(i, current_customer));
      }
    }

    double objective_function_value = solution.getObjectiveFunctionValue();

    data.put("suppliers", suppliers);
    data.put("customers", customers);
    data.put("in_doors", in_doors);
    data.put("out_doors", out_doors);

    data.put("in_capacities", toJSONArray(in_capacities));
    data.put("out_capacities", toJSONArray(out_capacities));
    data.put("in_capacities_left", toJSONArray(in_capacities_left));
    data.put("out_capacities_left", toJSONArray(out_capacities_left));
    JSONArray json_distances = distancesToJSONArray(distances);
    data.put("distances", json_distances);

    data.put("deliveries", deliveriesToJSONArray(deliveries));
    data.put("supplier_pallets", toJSONArray(supplier_pallets));
    data.put("customer_pallets", toJSONArray(customer_pallets));

    data.put("suppliers_assigned", toJSONArray(suppliers_assigned));
    data.put("customers_assigned", toJSONArray(customers_assigned));
    data.put("objective_function_value", objective_function_value);

    return JSONValue.toJSONString(data);
  }

  private static JSONArray deliveriesToJSONArray(Map<Integer, Integer>[] deliveries) {
    JSONArray json_deliveries = new JSONArray();
    for (int i = 0; i < deliveries.length; i++) {
      json_deliveries.add(deliveries[i]);
    }
    return json_deliveries;
  }

  private static JSONArray distancesToJSONArray(int[][] distances) {
    JSONArray json_distances = new JSONArray();
    for (int i = 0; i < distances.length; i++) {
      json_distances.add(toJSONArray(distances[i]));
    }
    return json_distances;
  }

  private static JSONArray toJSONArray(int[] in_capacities) {
    JSONArray json_array = new JSONArray();
    for (int i = 0; i < in_capacities.length; i++) {
      json_array.add(in_capacities[i]);
    }
    return json_array;
  }
}
