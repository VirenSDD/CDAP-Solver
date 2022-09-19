import model.OptimizationProblem;
import model.Solution;
import parser.OptimizationProblemJsonParser;
import solver.ConstructiveMaxCapacity;
import solver.search.GreedyCustomerDoorSwap;
import solver.search.GreedyCustomerRelocation;
import solver.search.GreedyCustomerSwap;
import solver.search.GreedySupplierDoorSwap;
import solver.search.GreedySupplierRelocation;
import solver.search.GreedySupplierSwap;

public class MainMovements {
  public static void main(String[] args) {
    try {
      if (args.length < 1) {
        throw new Exception("A filename must be provided.");
      }
      OptimizationProblem problem = OptimizationProblemJsonParser.parseJSONFile(args[0]);
      System.out.println("Problem:\n" + problem);

      ConstructiveMaxCapacity m1 = new ConstructiveMaxCapacity(problem);

      Solution solution = m1.run();
      Solution greedy_supplier_relocation = new GreedySupplierRelocation(solution).run();
      Solution greedy_customer_relocation = new GreedyCustomerRelocation(solution).run();
      Solution greedy_supplier_swap = new GreedySupplierSwap(solution).run();
      Solution greedy_customer_swap = new GreedyCustomerSwap(solution).run();
      Solution greedy_supplier_door_swap = new GreedySupplierDoorSwap(solution).run();
      Solution greedy_customer_door_swap = new GreedyCustomerDoorSwap(solution).run();
      System.out.println("\nInitial Solution:\n" + solution);
      System.out.println("\nSupplier Relocation Solution:\n" + greedy_supplier_relocation);
      System.out.println("\nCustomer Relocation Solution:\n" + greedy_customer_relocation);
      System.out.println("\nSupplier Swap Solution:\n" + greedy_supplier_swap);
      System.out.println("\nCustomer Swap Solution:\n" + greedy_customer_swap);
      System.out.println("\nSuppliers Door Swap Solution:\n" + greedy_supplier_door_swap);
      System.out.println("\nCustomers Door Swap Solution:\n" + greedy_customer_door_swap);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
