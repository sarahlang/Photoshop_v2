package imagemodel.filters.filtering;

import imagemodel.filters.AbstractImageFilter;
import imagemodel.model.Image;
import java.util.List;

/**
 * A basic operation in many image processing algorithms that filters an image. Two types of
 * filtering are blurring and sharpening.
 */
public abstract class AbstractFiltering extends AbstractImageFilter {

  @Override
  public Image applyTo(Image image) {
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    int[][][] newRGB = new int[imageWidth][imageHeight][3];
    int offset = (int) Math.floor((double) this.kernel.size() / 2);
    for (int y = 0; y < imageHeight; y++) {
      for (int x = 0; x < imageWidth; x++) {
        double[] transformedRGB = new double[3];
        for (int i = 0; i < this.kernel.size(); i++) {
          for (int j = 0; j < this.kernel.get(i).size(); j++) {
            int imageX = x - offset + i;
            int imageY = y - offset + j;
            if (!(imageX < 0 || imageX > imageWidth - 1 || imageY < 0
                    || imageY > imageHeight - 1)) {
              int[] rgb = image.getRGBVal(imageX, imageY);
              for (int u = 0; u < 3; u++) {
                double valToAdd = (double) rgb[u] * this.kernel.get(i).get(j);
                transformedRGB[u] += valToAdd;
              }
            }
          }
        }
        int[] newRGBVal = new int[3];
        for (int u = 0; u < 3; u++) {
          // The value is truncated to 0 and 255 if it goes beyond
          newRGBVal[u] = Math.round(Math.min(Math.max((int) (transformedRGB[u]), 0), 255));
        }
        newRGB[x][y] = newRGBVal;
      }
    }
    return new Image(image.getWidth(), image.getHeight(), newRGB, false);
  }

  @Override
  public List<List<Float>> getKernel() {
    return this.kernel;
  }
}