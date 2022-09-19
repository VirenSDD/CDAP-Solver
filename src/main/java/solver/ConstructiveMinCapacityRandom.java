package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.OptimizationProblem;
import model.Solution;
import utils.SetNotFeasible;
import utils.Sort;

public class ConstructiveMinCapacityRandom implements Solver {
  private OptimizationProblem problem_;
  private Solution solution_;
  private int door_list_number_;
  private static int MAX_DOOR_NUMBER = 5;

  public ConstructiveMinCapacityRandom(OptimizationProblem problem, int door_list_number) {
    problem_ = problem;
    door_list_number_ = Math.min(door_list_number, MAX_DOOR_NUMBER);
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
  // minimum capacity inbound door
  void assignSuppliers(List<Integer> suppliers_sorted) throws Exception {
    int in_doors = problem_.getInDoors();
    int[] in_capacities = new int[in_doors];
    for (int i = 0; i < in_doors; i++) {
      in_capacities[i] = problem_.getInDoorCapacity(i);
    }

    while (!suppliers_sorted.isEmpty()) {
      int highest_pallets_supplier = (int) suppliers_sorted.get(0);
      int pallets = problem_.getSupplierPallets(highest_pallets_supplier);
      int min_capacity_in_door = getKMinElement(in_capacities, pallets);
      solution_.setSupplierDoor(highest_pallets_supplier, min_capacity_in_door);
      // Update outbound door capacity
      in_capacities[min_capacity_in_door] -= pallets;

      if (in_capacities[min_capacity_in_door] < 0) {
        setNotFeasible();
      }

      suppliers_sorted.remove(0);
    }

    for (int i = 0; i < in_capacities.length; i++) {
      solution_.setInCapacity(i, in_capacities[i]);
    }
  }

  // Assigns the customer with the highest amount of pallets to the current
  // minimum capacity outbound door
  private void assignCustomers(List<Integer> customers_sorted) throws Exception {
    int out_doors = problem_.getOutDoors();
    int[] out_capacities = new int[out_doors];
    for (int i = 0; i < out_doors; i++) {
      out_capacities[i] = problem_.getOutDoorCapacity(i);
    }

    while (!customers_sorted.isEmpty()) {
      int highest_pallets_customer = (int) customers_sorted.get(0);
      int pallets = problem_.getCustomerPallets(highest_pallets_customer);
      int min_capacity_out_door = getKMinElement(out_capacities, pallets);
      solution_.setCustomerDoor(highest_pallets_customer, min_capacity_out_door);
      // Update outbound door capacity
      out_capacities[min_capacity_out_door] -= pallets;

      if (out_capacities[min_capacity_out_door] < 0) {
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

  // Given an array returns a random element from the k highest
  private int getKMinElement(int[] array, int pallets) throws Exception {
    List<Integer> indices = new ArrayList<Integer>();
    int min = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] < array[min]) {
        min = i;
      }
      if (array[i] >= pallets) {
        indices.add(i);
      }
    }

    if (indices.isEmpty()) {
      return min;
    }

    Collections.sort(indices, (Comparator.comparing(s -> array[(int) s])));

    Random random = new Random();

    int randomIndex = random.nextInt(Math.min(door_list_number_, indices.size()));

    return indices.get(randomIndex);
  }
}
