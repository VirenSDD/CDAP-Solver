package movement;

public class SwapMovement extends Movement {
  private int first_truck_;
  private int second_truck_;

  SwapMovement(int first_truck, int second_truck, double delta) {
    setDelta(delta);
    first_truck_ = first_truck;
    second_truck_ = second_truck;
  }

  public int getFirstTruck() {
    return first_truck_;
  }

  public int getSecondTruck() {
    return second_truck_;
  }

  public String toString() {
    return "T" + first_truck_ + " <-> T" + second_truck_ + " = " + getDelta();
  }
}
