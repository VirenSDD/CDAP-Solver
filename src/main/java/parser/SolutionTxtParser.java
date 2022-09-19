package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.FileUtils;

public class SolutionTxtParser {
  public static Solution parseTXTFile(String path) throws Exception {
    List<String[]> file_contents = processFile(path);

    String instance_path = FileUtils.removeExtension(file_contents.get(0)[1]);
    instance_path = "instances_json/" + instance_path.trim() + ".json";

    OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(instance_path);

    Solution solution = new Solution(problem);
    double objective_function_value = Double.parseDouble(file_contents.get(1)[1]);
    solution.setObjectiveFunctionValue(objective_function_value);
    for (int i = 0; i < problem.getInDoors(); i++) {
      solution.setInCapacity(i, problem.getInDoorCapacity(i));
    }

    for (int i = 0; i < problem.getOutDoors(); i++) {
      solution.setOutCapacity(i, problem.getOutDoorCapacity(i));
    }

    setSupplierDoor(file_contents, solution);

    setCustomerDoor(file_contents, solution);

    return solution;
  }

  public static double parseSecondsInTXTFile(String path) throws Exception {
    List<String[]> file_contents = processFile(path);

    String instance_path = FileUtils.removeExtension(file_contents.get(0)[1]);
    instance_path = "instances_json/" + instance_path.trim() + ".json";

    return Double.parseDouble(file_contents.get(2)[1].trim());
  }

  private static void setSupplierDoor(List<String[]> file_contents, Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    String[] suppliers = file_contents.get(3)[1].trim().split("\s+");
    String[] in_doors = file_contents.get(4)[1].trim().split("\s+");
    for (int i = 0; i < in_doors.length; i++) {
      int supplier = Integer.valueOf(suppliers[i]) - 1;
      int in_door = Integer.valueOf(in_doors[i]) - 1;
      int pallets = problem.getSupplierPallets(supplier);
      solution.setSupplierDoor(supplier, in_door);
      solution.setInCapacity(in_door, solution.getInCapacity(in_door) - pallets);
    }
  }

  private static void setCustomerDoor(List<String[]> file_contents, Solution solution) {
    OptimizationProblem problem = solution.getProblem();
    String[] customers = file_contents.get(5)[1].trim().split("\s+");
    String[] out_doors = file_contents.get(6)[1].trim().split("\s+");
    for (int i = 0; i < out_doors.length; i++) {
      int customer = Integer.valueOf(customers[i]) - 1;
      int out_door = Integer.valueOf(out_doors[i]) - 1;
      int pallets = problem.getCustomerPallets(customer);
      solution.setCustomerDoor(customer, out_door);
      solution.setOutCapacity(out_door, solution.getOutCapacity(out_door) - pallets);
    }
  }

  private static List<String[]> processFile(String path) throws FileNotFoundException, IOException {
    BufferedReader bf = new BufferedReader(new FileReader(path));
    List<String[]> file_contents = new ArrayList<String[]>();
    String line = bf.readLine();

    while (line != null) {
      if (line.length() > 0) {
        String[] parsed_line = line.split("[:]");
        file_contents.add(parsed_line);
      }
      line = bf.readLine();
    }

    bf.close();
    return file_contents;
  }
}
