package imagemodel.filters.colortransformation;

import imagemodel.filters.AbstractImageFilter;
import imagemodel.model.Image;

/**
 * A color transformation modifies the color of a pixel based on its own color.
 */
public abstract class AbstractColorTransformation extends AbstractImageFilter {

  @Override
  public Image applyTo(Image image) {
    int[][][] newrgb = new int[image.getWidth()][image.getHeight()][3];
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        int[] rgb = image.getRGBVal(x, y);
        // get the three different colors.
        // set the rgb values according to the calculation.
        newrgb[x][y][0] =
                (int) (rgb[0] * kernel.get(0).get(0) + rgb[1]
                        * kernel.get(0).get(1) + rgb[2] * kernel.get(0)
                        .get(2));
        newrgb[x][y][1] = (int)
                (rgb[0] * kernel.get(1).get(0) + rgb[1]
                        * kernel.get(1).get(1) + rgb[2] * kernel.get(1)
                        .get(2));
        newrgb[x][y][2] = (int)
                (rgb[0] * kernel.get(2).get(0) + rgb[1]
                        * kernel.get(2).get(1) + rgb[2] * kernel.get(2)
                        .get(2));

        this.clamp(newrgb[x][y]);
      }
    }
    return new Image(image.getWidth(), image.getHeight(), newrgb, false);
  }

}
