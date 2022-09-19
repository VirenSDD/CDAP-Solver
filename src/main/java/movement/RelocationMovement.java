package movement;

public class RelocationMovement extends Movement {
  private int truck_;
  private int new_door_;

  RelocationMovement(int truck, int new_door, double delta) {
    setDelta(delta);
    truck_ = truck;
    new_door_ = new_door;
  }

  public int getTruck() {
    return truck_;
  }

  public int getNewDoor() {
    return new_door_;
  }

  public String toString() {
    return "T" + String.valueOf(truck_) + " -> D" + String.valueOf(new_door_) + " = " + getDelta();
  }
}
