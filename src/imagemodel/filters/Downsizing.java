package imagemodel.filters;

import imagemodel.model.Image;

/**
 * A filter to downsize the original image according to the height and width of user input.
 */
public class Downsizing extends AbstractImageFilter {

  private final int height;
  private final int width;

  /**
   * The constructor for downsizing that takes in the height and width of the image.
   * @param height height of the image
   * @param width width of the image
   */
  public Downsizing(int height, int width) {
    this.height = height;
    this.width = width;
  }

  @Override
  public Image applyTo(Image image) {
    int width = this.getDownsizedWidth();
    int height = this.getDownsizedHeight();
    if (image.getHeight() <= this.height || image.getWidth() <= this.width) {
      throw new IllegalArgumentException("height and width bigger than usual");
    }
    int[][][] newRgb = new int[width][height][3];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        float x = (float) (i * image.getWidth() * 1.0 / width);
        float y = (float) (j * image.getHeight() * 1.0 / height);
        for (int c = 0; c < 3; c++) {
          int[] rgbA = image.getRGBVal((int) Math.floor(x), (int) Math.floor(y));
          int[] rgbB = image.getRGBVal((int) Math.ceil(x), (int) Math.floor(y));
          int[] rgbC = image.getRGBVal((int) Math.floor(x), (int) Math.ceil(y));
          int[] rgbD = image.getRGBVal((int) Math.ceil(x), (int) Math.ceil(y));

          // if x is an integer
          if (x % 1 == 0) {
            if (y % 1 != 0) { // and if y is not an integer
              float m = rgbB[c];
              float n = rgbD[c];
              newRgb[i][j][c] = (int) (n * (y - Math.floor(y)) + m * (Math.ceil(y) - y));
            } else { // and if y is an integer
              newRgb[i][j][0] = image.getRGBVal((int) x, (int) y)[0];
              newRgb[i][j][1] = image.getRGBVal((int) x, (int) y)[1];
              newRgb[i][j][2] = image.getRGBVal((int) x, (int) y)[2];
            }
          } else { // if x is not an integer
            if (y % 1 == 0) { //if y is an integer
              float m = (float) (rgbB[c] * (x - Math.floor(x)) + rgbA[c] * (Math.ceil(x) - x));
              newRgb[i][j][c] = (int) m;
            } else { //if y is not an integer
              float m = (float) (rgbB[c] * (x - Math.floor(x)) + rgbA[c] * (Math.ceil(x) - x));
              float n = (float) (rgbD[c] * (x - Math.floor(x)) + rgbC[c] * (Math.ceil(x) - x));
              newRgb[i][j][c] = (int) (n * (y - Math.floor(y)) + m * (Math.ceil(y) - y));
            }
          }
        }
        this.clamp(newRgb[i][j]);
      }
    }
    return new Image(width, height, newRgb, false);
  }

  /**
   * Getter for the height of this downsized image.
   *
   * @return the height of the downsized image
   */
  private int getDownsizedHeight() {
    return this.height;
  }

  /**
   * Getter for the width of this downsized image.
   *
   * @return the width of the downsized image
   */
  private int getDownsizedWidth() {
    return this.width;
  }

}