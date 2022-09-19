package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.OptimizationProblem;

public class Sort {
  // Returns a list of the customers in the problem sorted by the total pallets
  // they receive
  public static List<Integer> customersByPalletSumDesc(OptimizationProblem problem) {
    int customers = problem.getCustomers();
    List<Integer> L = new ArrayList<>();
    for (int i = 0; i < customers; i++) {
      L.add(i);
    }
    Collections.sort(L, (Comparator.comparing(s -> problem.getCustomerPallets((int) s))).reversed());
    return L;
  }

  // Returns a list of the suppliers in the problem sorted by the total pallets
  // they send
  public static List<Integer> suppliersByPalletSumDesc(OptimizationProblem problem) {
    int suppliers = problem.getSuppliers();
    List<Integer> M = new ArrayList<>();
    for (int i = 0; i < suppliers; i++) {
      M.add(i);
    }
    Collections.sort(M, (Comparator.comparing(s -> problem.getSupplierPallets((int) s))).reversed());
    return M;
  }
}
