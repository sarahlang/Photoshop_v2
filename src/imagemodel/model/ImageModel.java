package imagemodel.model;

import java.util.ArrayList;

/**
 * An image model that manages image object.
 */
public interface ImageModel {

  /**
   * Sets the given image as the current layer of image.
   *
   * @param image the image to be set as current
   */
  void setCurrent(Image image);

  /**
   * Changes the index of current layer into the given image index.
   *
   * @param currentIdx index of the current layer to be changed into
   */
  void changeCurrent(int currentIdx);

  /**
   * Gets the current image among the layers of images.
   *
   * @throws IllegalArgumentException if the current layer does not exist.
   */
  Image getCurrentImage() throws IllegalArgumentException;

  /**
   * Get all the layers within the model.
   *
   * @return a list of all the images in the layers
   */
  ArrayList<Image> getAllLayers();

  /**
   * Gets the image index of the current layer.
   *
   * @return an integer that represents the image index of the current layer
   */
  int getCurrent();

  /**
   * Make the target image invisible.
   *
   * @param imageIdx the image index correspond to that image.
   */
  void makeInvisible(int imageIdx);

  /**
   * Get the index of the previous image in the layers.
   *
   * @return the index of the previous image in the layers.
   */
  int getLastCurrent();

  /**
   * Get the previous image in the layers.
   *
   * @return the previous image in the layers.
   */
  Image getLastCurrentImage();
}