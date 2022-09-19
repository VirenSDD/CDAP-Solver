package solver;

import model.Solution;

public interface Solver {
  public abstract Solution run() throws Exception;
}
