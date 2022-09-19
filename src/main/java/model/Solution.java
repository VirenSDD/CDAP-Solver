package model;

import java.util.Arrays;

import utils.Constants;

public class Solution {
  private OptimizationProblem problem_;
  private int[] suppliers_assigned_;
  private int[] customers_assigned_;
  private int[] in_capacities_;
  private int[] out_capacities_;
  private double objective_function_value_;

  public Solution(OptimizationProblem problem) throws Exception {
    problem_ = problem;
    suppliers_assigned_ = new int[problem.getSuppliers()];
    customers_assigned_ = new int[problem.getCustomers()];
    in_capacities_ = new int[problem.getInDoors()];
    out_capacities_ = new int[problem.getOutDoors()];
    objective_function_value_ = Constants.NOT_EVALUATED;
  }

  public Solution(Solution solution) {
    this.problem_ = solution.problem_;
    this.suppliers_assigned_ = solution.suppliers_assigned_.clone();
    this.customers_assigned_ = solution.customers_assigned_.clone();
    this.in_capacities_ = solution.in_capacities_.clone();
    this.out_capacities_ = solution.out_capacities_.clone();
    this.objective_function_value_ = solution.objective_function_value_;
  }

  public void setSupplierDoor(int supplier, int door) {
    suppliers_assigned_[supplier] = door;
  }

  public void setCustomerDoor(int customer, int door) {
    customers_assigned_[customer] = door;
  }

  public void setInCapacity(int door, int capacity) {
    in_capacities_[door] = capacity;
  }

  public void setOutCapacity(int door, int capacity) {
    out_capacities_[door] = capacity;
  }

  public OptimizationProblem getProblem() {
    return problem_;
  }

  public int getSupplierDoor(int supplier) {
    return suppliers_assigned_[supplier];
  }

  public int getCustomerDoor(int customer) {
    return customers_assigned_[customer];
  }

  public int getInCapacity(int door) {
    return in_capacities_[door];
  }

  public int getOutCapacity(int door) {
    return out_capacities_[door];
  }

  public void setObjectiveFunctionValue(double objective_function_value) {
    objective_function_value_ = objective_function_value;
  }

  public double getObjectiveFunctionValue() {
    return objective_function_value_;
  }

  public void evaluate() {
    objective_function_value_ = Evaluator.evaluate(this);
  }

  public boolean isFeasible() {
    return objective_function_value_ != Constants.NOT_FEASIBLE;
  }

  public String toString() {
    int[] suppliers_normalized = new int[suppliers_assigned_.length];
    for (int i = 0; i < suppliers_normalized.length; i++) {
      suppliers_normalized[i] = suppliers_assigned_[i] + 1;
    }
    int[] customers_normalized = new int[customers_assigned_.length];
    for (int i = 0; i < customers_normalized.length; i++) {
      customers_normalized[i] = customers_assigned_[i] + 1;
    }
    return "Suppliers: " + Arrays.toString(suppliers_normalized) +
        "\nCustomers: " + Arrays.toString(customers_normalized) +
        "\nTotal cost: " + objective_function_value_ +
        "\nCapacities Left Suppliers: " + Arrays.toString(in_capacities_) +
        "\nCapacities Left Customers: " + Arrays.toString(out_capacities_);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    Solution other_solution = (Solution) obj;

    if (other_solution.objective_function_value_ != objective_function_value_) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Double.valueOf(objective_function_value_).hashCode();
  }
}
