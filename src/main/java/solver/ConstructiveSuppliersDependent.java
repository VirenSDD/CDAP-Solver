package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.OptimizationProblem;
import model.Solution;
import utils.Sort;

public class ConstructiveSuppliersDependent implements Solver {
  private OptimizationProblem problem_;
  private Solution solution_;

  public ConstructiveSuppliersDependent(OptimizationProblem problem) {
    problem_ = problem;
  }

  public Solution run() throws Exception {
    solution_ = new Solution(problem_);
    // Customers L in descending order of rl
    List<Integer> customers_sorted = Sort.customersByPalletSumDesc(problem_);
    // Assigment of customers
    assignCustomers(new ArrayList<>(customers_sorted));
    // Assignment of suppliers
    assignSuppliers(new ArrayList<>(customers_sorted));

    solution_.evaluate();

    return solution_;
  }

  private void assignSuppliers(List<Integer> customers_sorted) throws Exception {
    int suppliers = problem_.getSuppliers();
    int in_doors = problem_.getInDoors();
    int[] in_capacities = new int[in_doors];
    for (int i = 0; i < in_doors; i++) {
      in_capacities[i] = problem_.getInDoorCapacity(i);
    }
    Set<Integer> supplier_set = new HashSet<>();
    for (int i = 0; i < suppliers; i++) {
      supplier_set.add(i);
    }

    int[] customer_pallets_sum = new int[problem_.getCustomers()];
    for (int i = 0; i < customer_pallets_sum.length; i++) {
      customer_pallets_sum[i] = problem_.getCustomerPallets(i);
    }

    while (!customers_sorted.isEmpty()) {
      // Customer l in first position of L1 (customers_sorted)
      int highest_pallets_customer = (int) customers_sorted.get(0);
      // Suppliers of l in B (supplier set)
      List<Integer> L2 = suppliersNotAssigned(supplier_set, highest_pallets_customer);

      // Decreasing order of fkl values
      sortSuppliersByDeliveriesToCustomer(L2, highest_pallets_customer);

      // Inbound Door closest to j (highest_pallets_customer)
      int in_door = getClosestInDoor(in_capacities, highest_pallets_customer, L2);

      assignSuppliersToInDoor(customers_sorted, in_capacities, supplier_set, customer_pallets_sum, L2, in_door);
      sortCustomersByPalletsLeft(customers_sorted, customer_pallets_sum);
    }

    for (int i = 0; i < in_capacities.length; i++) {
      solution_.setInCapacity(i, in_capacities[i]);
    }
  }

  private void sortCustomersByPalletsLeft(List<Integer> customers_sorted, int[] customer_pallets_sum) {
    Collections.sort(customers_sorted, (Comparator.comparing(s -> customer_pallets_sum[(int) s])).reversed());
  }

  private void assignSuppliersToInDoor(List<Integer> customers, int[] in_capacities, Set<Integer> suppliers,
      int[] customer_pallets_sum,
      List<Integer> not_assigned_suppliers, int in_door) {
    boolean available_capacity = true;

    while (available_capacity && !not_assigned_suppliers.isEmpty()) {
      int current_supplier = (int) not_assigned_suppliers.get(0);
      if (problem_.getSupplierPallets(current_supplier) > in_capacities[in_door]) {
        available_capacity = false;
      } else {
        solution_.setSupplierDoor(current_supplier, in_door);
        int[] current_supplier_customers = problem_.getSupplierCustomers(current_supplier);
        for (int customer : current_supplier_customers) {
          int pallets = problem_.getDelivery(current_supplier, customer);
          in_capacities[in_door] -= pallets; // Update inbound door capacity
          customer_pallets_sum[customer] -= pallets; // Update customer pallets
          if (customer_pallets_sum[customer] == 0) {
            customers.remove((Object) customer);
          }
        }
        not_assigned_suppliers.remove(0);
        suppliers.remove(current_supplier);
      }
    }
  }

  private int getClosestInDoor(int[] in_capacities, int highest_pallets_customer, List<Integer> L2) throws Exception {
    int pallets_first_supplier = problem_.getSupplierPallets((int) L2.get(0));
    int highest_pallets_customer_door = solution_.getCustomerDoor(highest_pallets_customer);
    int in_door = searchClosestInDoor(highest_pallets_customer_door, pallets_first_supplier, in_capacities);
    if (in_door == -1) {
      throwNotFeasibleError();
    }
    return in_door;
  }

  private void sortSuppliersByDeliveriesToCustomer(List<Integer> suppliers, int customer) {
    Collections.sort(suppliers, (Comparator.comparing(s -> problem_.getDelivery((int) s, customer))).reversed());
  }

  // Returns a List<Integer> of suppliers that send deliveries to the customer and
  // are in
  // the supplier set
  private List<Integer> suppliersNotAssigned(Set<Integer> supplier_set, int customer) {
    int[] customer_suppliers = problem_.getCustomerSuppliers(customer);

    List<Integer> L2 = new ArrayList<>();
    for (int i = 0; i < customer_suppliers.length; i++) {
      if (supplier_set.contains(customer_suppliers[i])) {
        L2.add(customer_suppliers[i]);
      }
    }
    return L2;
  }

  // Returns the closest available inbound door (least distance with out_door and
  // enough capacity for the pallets)
  private int searchClosestInDoor(int out_door, int pallets, int[] capacities) {
    boolean end = false;
    int left = out_door;
    int right = out_door + 1;
    while (!end) {
      if (left >= 0 && capacities[left] > pallets) {
        return left;
      }
      if (right < problem_.getInDoors() && capacities[right] > pallets) {
        return right;
      }
      --left;
      ++right;
      if (left < 0 && right >= problem_.getInDoors()) {
        end = true;
      }
    }
    return -1;
  }

  // Assigns the customer with the highest amount of pallets to the current
  // maximum capacity inbound door
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
        throwNotFeasibleError();
      }
      customers_sorted.remove(0);
    }

    for (int i = 0; i < out_capacities.length; i++) {
      solution_.setOutCapacity(i, out_capacities[i]);
    }
  }

  private void throwNotFeasibleError() throws Exception {
    throw new Exception("Solution not feasible using this algorithm.");
  }

  // Given an array returns its maximum value element
  private int getMaxElement(int[] array) {
    int max = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] > array[max]) {
        max = i;
      }
    }
    return max;
  }
}
