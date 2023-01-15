package imagemodel.model;

import java.util.ArrayList;

/**
 * A class that manages multiple layers of images that contains a current layer. If the current
 * layer is 0, that means the current layer has not been set; otherwise the current number is the
 * number of layer that is the current layer.
 */
public class MultiImageModel implements ImageModel {

  private ArrayList<Image> layers;
  private ArrayList<Integer> currentLog;
  private int current;

  /**
   * The default constructor that load in an arraylist of layers.
   */
  public MultiImageModel() {
    this.layers = new ArrayList<Image>();
    currentLog = new ArrayList<Integer>();
    current = 0;
  }

  @Override
  public void setCurrent(Image image) {
    this.layers.set(current - 1, image);
  }

  @Override
  public void changeCurrent(int currentIdx) {
    this.current = currentIdx;
    currentLog.add(currentIdx);
  }


  @Override
  public Image getCurrentImage() throws IllegalArgumentException {
    if (this.current == 0) {
      throw new IllegalArgumentException("current does not exist");
    }
    return this.layers.get(current - 1);
  }

  @Override
  public ArrayList<Image> getAllLayers() {
    return this.layers;
  }

  @Override
  public int getCurrent() {
    if (current == 0) {
      throw new IllegalArgumentException("there is no current yet");
    }
    return current;
  }

  @Override
  public void makeInvisible(int imageIdx) {
    this.getCurrentImage().setTransparency();
    if (this.currentLog.size() > 2) {
      this.current = this.currentLog.get(this.currentLog.size() - 2);
      this.currentLog.add(this.current);
    } else {
      this.currentLog.add(0);
      this.current = 0;
    }
  }

  @Override
  public int getLastCurrent() {
    if (this.currentLog.size() < 2) {
      throw new IllegalArgumentException("this last current does not exist");
    } else {
      return this.currentLog.get(this.currentLog.size() - 2);
    }
  }

  @Override
  public Image getLastCurrentImage() {
    if (this.currentLog.size() < 2) {
      throw new IllegalArgumentException("this last current does not exist");
    } else {
      return this.layers.get(this.currentLog.get(this.currentLog.size() - 2) - 1);
    }
  }
}