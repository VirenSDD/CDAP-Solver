package movement;

public abstract class Movement {
  private double delta_;

  protected void setDelta(double delta_) {
    this.delta_ = delta_;
  }

  public double getDelta() {
    return delta_;
  }
}