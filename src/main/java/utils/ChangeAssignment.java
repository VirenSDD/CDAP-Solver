package utils;

import model.Solution;

public class ChangeAssignment {
  public static Solution supplier(Solution solution, int supplier, int new_door, double delta) {
    int old_door = solution.getSupplierDoor(supplier);
    int pallets = solution.getProblem().getSupplierPallets(supplier);
    Solution new_solution = new Solution(solution);
    new_solution.setSupplierDoor(supplier, new_door);
    new_solution.setInCapacity(old_door, new_solution.getInCapacity(old_door) + pallets);
    new_solution.setInCapacity(new_door, new_solution.getInCapacity(new_door) - pallets);
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);
    return new_solution;
  }

  public static Solution customer(Solution solution, int customer, int new_door, double delta) {
    int old_door = solution.getCustomerDoor(customer);
    int pallets = solution.getProblem().getCustomerPallets(customer);
    Solution new_solution = new Solution(solution);
    new_solution.setCustomerDoor(customer, new_door);
    new_solution.setOutCapacity(old_door, new_solution.getOutCapacity(old_door) + pallets);
    new_solution.setOutCapacity(new_door, new_solution.getOutCapacity(new_door) - pallets);
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);
    return new_solution;
  }

  public static Solution supplierSwap(Solution solution, int first_supplier, int second_supplier, double delta) {
    int first_door = solution.getSupplierDoor(first_supplier);
    int first_pallets = solution.getProblem().getSupplierPallets(first_supplier);
    int second_pallets = solution.getProblem().getSupplierPallets(second_supplier);
    int second_door = solution.getSupplierDoor(second_supplier);
    Solution new_solution = new Solution(solution);

    new_solution.setSupplierDoor(first_supplier, second_door);
    new_solution.setSupplierDoor(second_supplier, first_door);
    new_solution.setInCapacity(first_door, solution.getInCapacity(first_door) - first_pallets + second_pallets);
    new_solution.setInCapacity(second_door, solution.getInCapacity(second_door) - second_pallets + first_pallets);
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);

    return new_solution;
  }

  public static Solution customerSwap(Solution solution, int first_customer, int second_customer, double delta) {
    int first_door = solution.getCustomerDoor(first_customer);
    int first_pallets = solution.getProblem().getCustomerPallets(first_customer);
    int second_pallets = solution.getProblem().getCustomerPallets(second_customer);
    int second_door = solution.getCustomerDoor(second_customer);
    Solution new_solution = new Solution(solution);

    new_solution.setCustomerDoor(first_customer, second_door);
    new_solution.setCustomerDoor(second_customer, first_door);
    new_solution.setOutCapacity(first_door, solution.getOutCapacity(first_door) - first_pallets + second_pallets);
    new_solution.setOutCapacity(second_door, solution.getOutCapacity(second_door) - second_pallets + first_pallets);
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);

    return new_solution;
  }

  public static Solution supplierDoor(Solution solution, int first_door, int second_door, double delta) {
    Solution new_solution = new Solution(solution);
    int suppliers = solution.getProblem().getSuppliers();
    for (int supplier = 0; supplier < suppliers; supplier++) {
      if (solution.getSupplierDoor(supplier) == first_door) {
        new_solution = supplier(new_solution, supplier, second_door, 0);
      }
      if (solution.getSupplierDoor(supplier) == second_door) {
        new_solution = supplier(new_solution, supplier, first_door, 0);
      }
    }
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);

    return new_solution;
  }

  public static Solution customerDoor(Solution solution, int first_door, int second_door, double delta) {
    Solution new_solution = new Solution(solution);
    int customers = solution.getProblem().getCustomers();
    for (int customer = 0; customer < customers; customer++) {
      if (solution.getCustomerDoor(customer) == first_door) {
        new_solution = customer(new_solution, customer, second_door, 0);
      }
      if (solution.getCustomerDoor(customer) == second_door) {
        new_solution = customer(new_solution, customer, first_door, 0);
      }
    }
    new_solution.setObjectiveFunctionValue(solution.getObjectiveFunctionValue() + delta);

    return new_solution;
  }
}
