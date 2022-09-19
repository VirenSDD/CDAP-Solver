package parser;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.OptimizationProblem;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptimizationProblemJsonParser {

  public static OptimizationProblem parseJSONFile(String path) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, ?> json_data = mapper.readValue(new File(path), Map.class);

    // Number of customers
    int customers = (int) json_data.get("customers");

    // Number of suppliers
    List<Map<String, ?>> suppliers_array = (ArrayList<Map<String, ?>>) json_data.get("suppliers");
    int suppliers = suppliers_array.size();

    // Deliveries from supplier to client
    Map[] deliveries = generateDeliveries(suppliers_array);

    Map<String, List<Map>> crossdocking_center = (Map<String, List<Map>>) json_data.get("crossdocking_center");

    // Inbound Doors Capacities
    int[] in_door_capacity = processDoorCapacities(crossdocking_center.get("in_doors"));
    int in_doors = in_door_capacity.length;

    // Outbound Doors Capacities
    int[] out_door_capacity = processDoorCapacities(crossdocking_center.get("out_doors"));
    int out_doors = out_door_capacity.length;

    List<Map> door_distances = crossdocking_center.get("door_distances");
    int[][] distances = processDistances(in_door_capacity.length, out_door_capacity.length, door_distances);

    return new OptimizationProblem(suppliers, customers, in_doors, out_doors, in_door_capacity,
        out_door_capacity, distances, deliveries);
  }

  private static int[][] processDistances(int in_doors, int out_doors,
      List<Map> door_distances) {
    int[][] result = new int[in_doors][out_doors];

    for (Map distance : door_distances) {
      result[(int) distance.get("in_door")][(int) distance.get("out_door")] = (int) distance.get("distance");
    }

    return result;
  }

  private static int[] processDoorCapacities(List<Map> doors_capacities) {
    int[] result_list = new int[doors_capacities.size()];
    for (int i = 0; i < doors_capacities.size(); i++) {
      result_list[i] = (int) doors_capacities.get(i).get("capacity");
    }
    return result_list;
  }

  private static Map[] generateDeliveries(List<Map<String, ?>> suppliers_array) {
    Map[] deliveries = new Map[suppliers_array.size()];
    for (Map<String, ?> supplier : suppliers_array) {
      Map client_delivery = new HashMap<>();
      List<Map> deliveries_json = (List<Map>) supplier.get("deliveries");
      for (Map customer : deliveries_json) {
        client_delivery.put((int) customer.get("id"), (int) customer.get("pallets"));
      }
      deliveries[(int) supplier.get("id")] = client_delivery;
    }
    return deliveries;
  }
}
