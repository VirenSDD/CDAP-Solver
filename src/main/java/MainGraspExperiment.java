import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import parser.SolutionTxtParser;
import solver.Solver;
import solver.grasp.GraspMachineLearning;
import solver.grasp.GraspMachineLearningMinCapacity;
import solver.grasp.GraspVND_DRS;
import solver.grasp.GraspVND_DRSMinCapacity;
import solver.grasp.GraspVND_RSD;
import solver.grasp.GraspVND_RSDMinCapacity;
import utils.Constants;
import utils.FileUtils;

public class MainGraspExperiment {
  private static final DecimalFormat df = new DecimalFormat("0.000");

  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        throw new Exception("A filename with the problem must be provided.");
      }

      if (args.length < 2) {
        throw new Exception("The output file must be provided.");
      }

      if (args.length < 3) {
        throw new Exception("The algorithm must be provided.");
      }

      if (args.length < 4) {
        throw new Exception("The number of iterations for the GRASP must be provided.");
      }

      String input_filename = args[0];
      String solution_filename = FileUtils.removeExtension(input_filename) + "-Solution";
      OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(input_filename);

      File output_file = new File(args[1]);
      FileWriter fw = new FileWriter(output_file, true);

      String algorithm_name = "";
      Solver solver;
      String vnd_order = "";
      int iterations = Integer.parseInt(args[3]);

      // 0 -> Door Swap - Relocation - Swap
      // 1 -> Relocation - Swap - Door Swap
      switch (Integer.parseInt(args[2])) {
        case 0:
          algorithm_name = "GRASP_VND";
          vnd_order = "DoorSwap-Reloc-Swap";
          solver = new GraspVND_DRS(problem, iterations);

          break;

        case 1:
          algorithm_name = "GRASP_VND";
          vnd_order = "Reloc-Swap-DoorSwap";
          solver = new GraspVND_RSD(problem, iterations);

          break;

        case 2:
          algorithm_name = "GRASP_VND_Random_Learning";
          vnd_order = "";
          solver = new GraspMachineLearningMinCapacity(problem, iterations);

          break;

        case 3:
          algorithm_name = "GRASP_VND_Constructive_Min_Capacity";
          vnd_order = "DoorSwap-Reloc-Swap";
          solver = new GraspVND_DRSMinCapacity(problem, iterations);

          break;

        case 4:
          algorithm_name = "GRASP_VND_Constructive_Min_Capacity";
          vnd_order = "Reloc-Swap-DoorSwap";
          solver = new GraspVND_RSDMinCapacity(problem, iterations);

          break;

        case 5:
          algorithm_name = "GRASP_VND_Random_Learning_Constructive_Min_Capacity";
          vnd_order = "";
          solver = new GraspMachineLearning(problem, iterations);

          break;

        default:
          fw.close();
          throw new Error("You must provide a valid algorithm");
      }

      fw.write(computeFields(input_filename, solution_filename, algorithm_name, solver, vnd_order, iterations));

      fw.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static String computeFields(String input_filename, String solution_filename, String algorithm_name,
      Solver algorithm, String order, int iterations) throws Exception {
    long start = System.nanoTime();
    Solution sol = algorithm.run();
    long end = System.nanoTime();
    double execution_time_in_seconds = (end - start) / Constants.ONE_SECOND_IN_NANOSECONDS;
    String filename_without_extension = FileUtils.removeExtension(input_filename);

    double optimal_solution_value = getOptimalSolutionValue(filename_without_extension);
    double optimal_solution_time = getOptimalSolutionTime(filename_without_extension);
    String value_deviation = "";
    String time_deviation = "";

    if (optimal_solution_value != -1) {
      value_deviation = df
          .format(100 * (sol.getObjectiveFunctionValue() - optimal_solution_value) / optimal_solution_value);
      time_deviation = df
          .format(100 * (execution_time_in_seconds - optimal_solution_time) / optimal_solution_time);
    }

    String result_output = "\n" + filename_without_extension + "," + algorithm_name + "," + order + "," + iterations
        + "," + sol.getObjectiveFunctionValue() + "," + optimal_solution_value + "," + value_deviation + ","
        + df.format(execution_time_in_seconds) + "," + optimal_solution_time + "," + time_deviation + ","
        + solution_filename;
    return result_output;
  }

  private static double getOptimalSolutionValue(String filename_without_extension) throws Exception {
    String optimal_solution_filename = filename_without_extension.replace("SetA", "Sol");
    File optimal_solution_file = new File("solutions_txt/" + optimal_solution_filename + ".txt");
    if (optimal_solution_file.exists()) {
      Solution optimal_solution = SolutionTxtParser.parseTXTFile(optimal_solution_file.getPath());
      return optimal_solution.getObjectiveFunctionValue();
    }
    return -1;
  }

  private static double getOptimalSolutionTime(String filename_without_extension) throws Exception {
    String optimal_solution_filename = filename_without_extension.replace("SetA", "Sol");
    File optimal_solution_file = new File("solutions_txt/" + optimal_solution_filename + ".txt");
    if (optimal_solution_file.exists()) {
      return SolutionTxtParser.parseSecondsInTXTFile(optimal_solution_file.getPath());
    }
    return -1;
  }
}
