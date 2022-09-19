import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import model.ExporterJson;
import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import solver.ConstructiveMaxCapacity;
import solver.grasp.GraspMachineLearning;
import solver.grasp.GraspVND_DRS;
import solver.grasp.GraspVND_RSD;
import solver.search.GreedySupplierRelocation;

public class Main {
  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        throw new Exception("A filename must be provided.");
      }

      OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(args[0]);
      Solution initial_solution = new ConstructiveMaxCapacity(problem).run();
      Solution supplier_relocation = new GreedySupplierRelocation(initial_solution).run();
      System.out.println(initial_solution);
      System.out.println(supplier_relocation);
      Solution grasp_learning_solution = new GraspMachineLearning(problem,
          1000000).run();
      Solution grasp_vnd_drs = new GraspVND_DRS(problem, 100000).run();
      Solution grasp_vnd_rsd = new GraspVND_RSD(problem, 100000).run();
      System.out.println("Problem:\n" + problem);
      System.out.println("\n\nGRASP VND Learning Solution:\n" +
          grasp_learning_solution);
      System.out.println("\n\nGRASP VND DRS:\n" + grasp_vnd_drs);
      System.out.println("\n\nGRASP VND RSD:\n" + grasp_vnd_rsd);
      writeToFile("ConstructiveMaxCapacitySolution.json", initial_solution);
      writeToFile("MachineLearningSolution.json", grasp_learning_solution);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void writeToFile(String filename, Solution sol) throws IOException {
    File file = new File(filename);

    if (!file.exists()) {
      file.createNewFile();
    }

    FileWriter fw = new FileWriter(file);

    BufferedWriter writer = new BufferedWriter(fw);
    writer.write(ExporterJson.exportSolutionToJSON(sol));
    writer.close();
    System.out.println("File written successfully");
  }
}
