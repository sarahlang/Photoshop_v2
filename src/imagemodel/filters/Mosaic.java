package imagemodel.filters;

import java.util.ArrayList;
import java.util.Random;
import imagemodel.model.Image;

/**
 * A filter to make a mosaic version of the given image.
 */
public class Mosaic extends AbstractImageFilter {

  private final int seedVal;

  /**
   * Constructor for the Mosaicing class that accept a seed val for the random object.
   *
   * @param seedVal a set of points in the image that the nearest pixels map to.
   */
  public Mosaic(int seedVal) {
    this.seedVal = seedVal;
  }

  @Override
  public Image applyTo(Image image) {
    Random rand1 = new Random();
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    ArrayList<Posn> randSeeds = new ArrayList<Posn>();
    ArrayList<int[]> seedRGBs = new ArrayList<int[]>();
    for (int a = 0; a < seedVal; a++) {
      int dimensionXpicked = rand1.nextInt(imageWidth);
      int dimensionYpicked = rand1.nextInt(imageHeight);
      //get seedVal number of random pixels
      randSeeds.add(new Posn(dimensionXpicked, dimensionYpicked));
      //get all the random pixels' rgbs
      seedRGBs.add(image.getRGBVal(dimensionXpicked, dimensionYpicked));
    }

    int[][][] imageRgb = new int[imageWidth][imageHeight][3];
    for (int i = 0; i < imageWidth; i++) {
      for (int j = 0; j < imageHeight; j++) {
        double minDist = imageHeight + imageWidth;
        int index = -1;
        for (int a = 0; a < seedVal - 1; a++) {
          int thisSeedX = randSeeds.get(a).getX();
          int thisSeedY = randSeeds.get(a).getY();
          //helper
          double distanceToSeed1 = Math
                  .sqrt(Math.pow(thisSeedY - j, 2) + (thisSeedX - i) * (thisSeedX - i));
          if (distanceToSeed1 < minDist) {
            minDist = distanceToSeed1;
            index = a;
          }
        }
        imageRgb[i][j] = seedRGBs.get(index);
      }
    }
    return new Image(imageWidth, imageHeight, imageRgb, false);
  }

  /**
   * A position that has an x and y value.
   */
  private class Posn {

    private int x;
    int y;

    /**
     * The Constructor for the Posn class.
     *
     * @param x the horizontal value of the seed.
     * @param y the vertical value of the seed.
     */
    Posn(int x, int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Getter for the x value of the seed.
     *
     * @return the x value of the seed
     */
    int getX() {
      return this.x;
    }

    /**
     * Getter for the y value of the seed.
     *
     * @return the y value of the seed
     */
    int getY() {
      return this.y;
    }
  }

}