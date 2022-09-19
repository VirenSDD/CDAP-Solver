package model;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class OptimizationProblem {
  private int suppliers_;
  private int customers_;
  private int in_doors_;
  private int out_doors_;
  private int[] in_door_capacity_;
  private int[] out_door_capacity_;
  private int[][] distances_;
  private Map<Integer, Integer>[] deliveries_;
  private int[] suppliers_pallets_;
  private int[] customers_pallets_;
  private int[][] supplier_customers_;
  private int[][] customer_suppliers_;

  public OptimizationProblem(int suppliers, int customers, int in_doors,
      int out_doors, int[] in_door_capacity,
      int[] out_door_capacity, int[][] distances,
      Map<Integer, Integer>[] deliveries) {
    suppliers_ = suppliers;
    customers_ = customers;
    in_doors_ = in_doors;
    out_doors_ = out_doors;
    in_door_capacity_ = in_door_capacity;
    out_door_capacity_ = out_door_capacity;
    distances_ = distances;
    deliveries_ = deliveries;
    setSuppliersPallets();
    setCustomersPallets();
    setSupplierCustomers();
    setCustomerSuppliers();
  }

  public int getSuppliers() {
    return suppliers_;
  }

  public int getCustomers() {
    return customers_;
  }

  public int getInDoors() {
    return in_doors_;
  }

  public int getOutDoors() {
    return out_doors_;
  }

  public int getInDoorCapacity(int pos) {
    return in_door_capacity_[pos];
  }

  public int getOutDoorCapacity(int pos) {
    return out_door_capacity_[pos];
  }

  public int getDistance(int in_door, int out_door) {
    return distances_[in_door][out_door];
  }

  private void setSupplierCustomers() {
    supplier_customers_ = new int[suppliers_][];
    for (int i = 0; i < deliveries_.length; i++) {
      int customer_size = deliveries_[i].size();
      int[] customers = new int[customer_size];
      int iteration = 0;
      for (Object j : deliveries_[i].keySet()) {
        customers[iteration++] = (int) j;
      }
      supplier_customers_[i] = customers;
    }
  }

  public int[] getSupplierCustomers(int supplier) {
    return supplier_customers_[supplier].clone();
  }

  private void setCustomerSuppliers() {
    ArrayList<Integer>[] customers = new ArrayList[customers_];
    for (int i = 0; i < customers.length; i++) {
      customers[i] = new ArrayList<>();
    }
    customer_suppliers_ = new int[customers_][];
    for (int i = 0; i < deliveries_.length; i++) {
      for (Object j : deliveries_[i].keySet()) {
        customers[(int) j].add((int) i);
      }
    }
    for (int i = 0; i < customers_; i++) {
      int suppliers = customers[i].size();
      customer_suppliers_[i] = new int[suppliers];
      for (int j = 0; j < suppliers; j++) {
        customer_suppliers_[i][j] = (int) customers[i].get(j);
      }
    }
  }

  public int[] getCustomerSuppliers(int customer) {
    return customer_suppliers_[customer].clone();
  }

  public int getDelivery(int supplier, int client) {
    return (int) deliveries_[supplier].get(client);
  }

  public int getSupplierPallets(int supplier) {
    return suppliers_pallets_[supplier];
  }

  private void setSuppliersPallets() {
    suppliers_pallets_ = new int[suppliers_];
    for (int i = 0; i < deliveries_.length; i++) {
      for (Object pallets : deliveries_[i].values()) {
        suppliers_pallets_[i] += (int) pallets;
      }
    }
  }

  public int getCustomerPallets(int s) {
    return customers_pallets_[s];
  }

  private void setCustomersPallets() {
    customers_pallets_ = new int[customers_];
    for (int i = 0; i < deliveries_.length; i++) {
      Map<Integer, Integer> map_entry = deliveries_[i];
      map_entry.forEach((key, value) -> customers_pallets_[(int) key] += (int) value);
    }
  }

  public String toString() {
    return "Suppliers: " + suppliers_ + "\nCustomers: " + customers_ +
        "\nInbound Doors: " + in_doors_ + "\nOutbound Doors: " + out_doors_ +
        "\nInbound Doors Capacities: " + Arrays.toString(in_door_capacity_) +
        "\nOutbound Doors Capacities: " + Arrays.toString(out_door_capacity_) +
        "\nDoors Distances: " + Arrays.deepToString(distances_) +
        "\nDeliveries: " + Arrays.toString(deliveries_) +
        "\nSuppliers Pallets Sum: " + Arrays.toString(suppliers_pallets_) +
        "\nCustomers Pallets Sum: " + Arrays.toString(customers_pallets_);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    OptimizationProblem other_problem = (OptimizationProblem) obj;

    if (other_problem.suppliers_ != suppliers_
        || other_problem.customers_ != customers_
        || other_problem.in_doors_ != in_doors_
        || other_problem.out_doors_ != out_doors_
        || !Arrays.equals(other_problem.in_door_capacity_, in_door_capacity_)
        || !Arrays.equals(other_problem.out_door_capacity_, out_door_capacity_)
        || !Arrays.equals(other_problem.suppliers_pallets_, suppliers_pallets_)
        || !Arrays.equals(other_problem.customers_pallets_, customers_pallets_)
        || !Arrays.equals(other_problem.deliveries_, deliveries_)
        || !Arrays.deepEquals(other_problem.distances_, distances_)
        || !Arrays.deepEquals(other_problem.supplier_customers_, supplier_customers_)
        || !Arrays.deepEquals(other_problem.customer_suppliers_, customer_suppliers_)) {
      return false;
    }

    return true;
  }
}
