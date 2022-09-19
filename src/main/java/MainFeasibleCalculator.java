import java.io.File;
import java.io.FileWriter;

import model.OptimizationProblem;
import parser.OptimizationProblemJsonParser;
import solver.feasible.MaxCapacityFeasibleCalculator;
import solver.feasible.MinCapacityFeasibleCalculator;
import utils.Constants;
import utils.FileUtils;

public class MainFeasibleCalculator {
  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        throw new Exception("A filename must be provided.");
      }

      if (args.length < 2) {
        throw new Exception("An output filename must be provided.");
      }

      if (args.length < 3) {
        throw new Exception("The algorithm must be provided.\n\t0 -> MaxCapacityRandom\n\t1 -> MinCapacityRandom");
      }

      String input_filename = args[0];
      OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(input_filename);
      File output_file = new File(args[1]);
      FileWriter fw = new FileWriter(output_file, true);

      // 0 -> MaxCapacityRandom
      // 0 -> MinCapacityRandom
      switch (Integer.parseInt(args[2])) {
        case 0:
          MaxCapacityFeasibleCalculator max_cfc = new MaxCapacityFeasibleCalculator(problem);
          int feasible_solutions_max = max_cfc.getFeasibleSolutionsNumber();
          float percentage_max = 100 * (float) feasible_solutions_max
              / (float) Constants.ITERATIONS_FEASIBLE_CALCULATOR;
          fw.write("\n" + FileUtils.removeExtension(input_filename) + ",Max," + feasible_solutions_max + ","
              + percentage_max + "," + max_cfc.getUniqueFeasibleSolutionsNumber());
          break;

        case 1:
          MinCapacityFeasibleCalculator min_cfc = new MinCapacityFeasibleCalculator(problem);
          int feasible_solutions_min = min_cfc.getFeasibleSolutionsNumber();
          float percentage_min = 100 * (float) feasible_solutions_min
              / (float) Constants.ITERATIONS_FEASIBLE_CALCULATOR;
          fw.write("\n" + FileUtils.removeExtension(input_filename) + ",Min," + feasible_solutions_min + ","
              + percentage_min + "," + min_cfc.getUniqueFeasibleSolutionsNumber());
          break;

        default:
          fw.close();
          break;
      }
      fw.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
