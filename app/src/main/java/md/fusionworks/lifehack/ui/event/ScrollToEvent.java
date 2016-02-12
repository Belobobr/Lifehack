package md.fusionworks.lifehack.ui.event;

/**
 * Created by ungvas on 2/12/16.
 */
public class ScrollToEvent {

  private int x;
  private int y;

  public ScrollToEvent(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
