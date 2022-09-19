package movement;

public class DoorSwapMovement extends Movement {
  private int first_door_;
  private int second_door_;

  DoorSwapMovement(int first_door, int second_door, double delta) {
    setDelta(delta);
    first_door_ = first_door;
    second_door_ = second_door;
  }

  public int getFirstDoor() {
    return first_door_;
  }

  public int getSecondDoor() {
    return second_door_;
  }

  public String toString() {
    return "D" + first_door_ + " <-> D" + second_door_ + " = " + getDelta();
  }
}
