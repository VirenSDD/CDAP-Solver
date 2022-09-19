import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.ExporterJson;
import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import solver.ConstructiveMaxCapacity;

public class MainSolutionExporter {
  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        throw new Exception("A filename containing the problem must be provided.");
      } else if (args.length == 1) {
        throw new Exception("The output filename must be provided");
      }
      OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(args[0]);
      System.out.println("Problem parsed successfully");

      ConstructiveMaxCapacity m1 = new ConstructiveMaxCapacity(problem);
      Solution sol = m1.run();

      writeToFile(args[1], sol);
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
