package solver;

import java.util.List;

import model.OptimizationProblem;
import model.Solution;
import utils.SetNotFeasible;
import utils.Sort;

public class ConstructiveMaxCapacity implements Solver {
  private OptimizationProblem problem_;
  private Solution solution_;

  public ConstructiveMaxCapacity(OptimizationProblem problem) {
    problem_ = problem;
  }

  public Solution run() throws Exception {
    solution_ = new Solution(problem_);

    // Customers L in descending order of rl
    List<Integer> customers_sorted = Sort.customersByPalletSumDesc(problem_);
    // Assigment of customers
    assignCustomers(customers_sorted);
    // Suppliers M in descending order of rm
    List<Integer> suppliers_sorted = Sort.suppliersByPalletSumDesc(problem_);
    // Assignment of suppliers
    assignSuppliers(suppliers_sorted);

    solution_.evaluate();

    return solution_;
  }

  // Assigns the supplier with the highest amount of pallets to the current
  // maximum capacity inbound door
  void assignSuppliers(List<Integer> suppliers_sorted) throws Exception {
    int in_doors = problem_.getInDoors();
    int[] in_capacities = new int[in_doors];
    for (int i = 0; i < in_doors; i++) {
      in_capacities[i] = problem_.getInDoorCapacity(i);
    }

    while (!suppliers_sorted.isEmpty()) {
      int max_capacity_in_door = getMaxElement(in_capacities);
      int highest_pallets_supplier = (int) suppliers_sorted.get(0);
      solution_.setSupplierDoor(highest_pallets_supplier, max_capacity_in_door);
      // Update outbound door capacity
      in_capacities[max_capacity_in_door] -= problem_.getSupplierPallets(highest_pallets_supplier);
      if (in_capacities[max_capacity_in_door] < 0) {
        setNotFeasible();
      }
      suppliers_sorted.remove(0);
    }

    for (int i = 0; i < in_capacities.length; i++) {
      solution_.setInCapacity(i, in_capacities[i]);
    }
  }

  // Assigns the customer with the highest amount of pallets to the current
  // maximum capacity outbound door
  private void assignCustomers(List<Integer> customers_sorted) throws Exception {
    int out_doors = problem_.getOutDoors();
    int[] out_capacities = new int[out_doors];
    for (int i = 0; i < out_doors; i++) {
      out_capacities[i] = problem_.getOutDoorCapacity(i);
    }

    while (!customers_sorted.isEmpty()) {
      int max_capacity_out_door = getMaxElement(out_capacities);
      int highest_pallets_customer = (int) customers_sorted.get(0);
      solution_.setCustomerDoor(highest_pallets_customer, max_capacity_out_door);
      // Update outbound door capacity
      out_capacities[max_capacity_out_door] -= problem_.getCustomerPallets(highest_pallets_customer);
      if (out_capacities[max_capacity_out_door] < 0) {
        setNotFeasible();
      }
      customers_sorted.remove(0);
    }

    for (int i = 0; i < out_capacities.length; i++) {
      solution_.setOutCapacity(i, out_capacities[i]);
    }
  }

  private void setNotFeasible() throws Exception {
    SetNotFeasible.solution(solution_);
  }

  // Given an array returns its maximum value element
  private int getMaxElement(int[] array) {
    int max = 0;
    for (int i = 1; i < array.length; i++) {
      if (array[i] > array[max]) {
        max = i;
      }
    }
    return max;
  }
}
