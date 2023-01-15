import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import userimagecommand.Load;
import imagemodel.filters.ImageFilterModel;
import imagemodel.filters.filtering.Blur;
import imagemodel.filters.filtering.Sharp;
import imagemodel.model.Image;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Abstract Filtering.
 */
public class FilterTest {

  ImageFilterModel blurFilter;
  ImageFilterModel sharpFilter;
  Image bearImage;
  Image blurred;
  Image sharped;
  Image blurredTwice;
  Image sharpedTwice;

  /**
   * Class to initialize the test cases.
   */
  @Before
  public void init() {
    bearImage = Load.importImage("res/bear.ppm");
    blurFilter = new Blur();
    blurred = blurFilter.applyTo(bearImage);
    blurredTwice = blurFilter.applyTo(blurred);
    sharpFilter = new Sharp();
    sharped = sharpFilter.applyTo(bearImage);
    sharpedTwice = sharpFilter.applyTo(sharped);
  }

  /**
   * Helper test to select 100 random pixels to see if the image can react correctly with the given
   * kernel and after ward image.
   *
   * @param kernelToTest the kernel that work with sepecific filter
   * @param original     the original image
   * @param after        the image after processed
   */
  public void aHundredRandomPixelKernelTest(List<List<Float>> kernelToTest,
          Image original, Image after) {
    Assert.assertEquals(after.getHeight(), bearImage.getHeight());
    Assert.assertEquals(after.getWidth(), bearImage.getWidth());
    int kernelDimension = kernelToTest.size();
    int offset = (int) Math.floor((double) kernelDimension / 2);
    int imageWidth = original.getWidth();
    int imageHeight = original.getHeight();
    Random rn = new Random();
    Random rn2 = new Random();
    //pick 100 random coordinates to check their pixels
    for (int a = 0; a < 100; a++) {
      double[] expectedVal = new double[3];
      int dimensionXpicked = rn.nextInt(imageWidth);
      int dimensionYpicked = rn2.nextInt(imageHeight);
      for (int i = 0; i < kernelDimension; i++) {
        for (int j = 0; j < kernelToTest.get(i).size(); j++) {
          int pixelX = dimensionXpicked - offset + i;
          int pixelY = dimensionYpicked - offset + j;
          if (!(pixelX < 0 || pixelX > imageWidth - 1 || pixelY < 0 || pixelY > imageHeight - 1)) {
            int[] rgb = original.getRGBVal(pixelX, pixelY);
            for (int x = 0; x < 3; x++) {
              double valToAdd = (double) rgb[x] * kernelToTest.get(i).get(j);
              expectedVal[x] += valToAdd;
            }
          }
        }
      }
      int[] newRGBVal = new int[3];
      for (int u = 0; u < 3; u++) {
        newRGBVal[u] = Math.round(Math.min(Math.max((int) (expectedVal[u]), 0), 255));
      }
      System.out.println(
              "x: " + dimensionXpicked + " y: " + dimensionYpicked
                      + " RGB Value: [" + expectedVal[0]
                      + ", " + expectedVal[1] + ", " + expectedVal[2] + "] ");
      Assert.assertArrayEquals(after.getRGBVal(dimensionXpicked, dimensionYpicked), newRGBVal);
    }
  }

  /**
   * Random select pixels to see if it fits requirement for both blur and sharp filter.
   */
  @Test
  //testing if any random pixel selected can adhere to the rule
  public void testRandomSelectedPixel() {
    Assert.assertTrue(blurFilter instanceof ImageFilterModel);
    Assert.assertTrue(sharpFilter instanceof ImageFilterModel);
    aHundredRandomPixelKernelTest(blurFilter.getKernel(), bearImage, blurred);
    aHundredRandomPixelKernelTest(sharpFilter.getKernel(), bearImage, sharped);
    //testing for blurring image twice can be still work as expected
    aHundredRandomPixelKernelTest(blurFilter.getKernel(), blurred, blurredTwice);
    aHundredRandomPixelKernelTest(sharpFilter.getKernel(), sharped, sharpedTwice);
  }

  /**
   * helper method to produce a result rgb value after processed with a given RGB list and kernel
   * list.
   *
   * @param listOfRGB       list of rgb value to process
   * @param listOfKernelVal list of kernel value to process
   * @return an array of rgb value
   */
  public static int[] manualAddRGBVal(ArrayList<int[]> listOfRGB,
          ArrayList<Float> listOfKernelVal) {
    float[] returnVal = new float[]{0, 0, 0};
    for (int u = 0; u < listOfRGB.size(); u++) {
      for (int i = 0; i < 3; i++) {
        returnVal[i] += listOfRGB.get(u)[i] * listOfKernelVal.get(u);
      }
    }
    int[] intreturn = new int[3];
    for (int i = 0; i < 3; i++) {
      intreturn[i] = Math.round(Math.min(Math.max((int) (returnVal[i]), 0), 255));
    }
    return intreturn;
  }

  /**
   * Manually into the surrounding RGB and kernel for the corner and edge and compare with results
   * from blur and sharp filter.
   */
  @Test
  //test the pixels on the edge of the picture are correctly adhere to the rule
  public void testEdgesPixel() {
    init();
    int[] sharped00 = manualAddRGBVal(new ArrayList<int[]>(Arrays.asList(bearImage.getRGBVal(0, 0),
            bearImage.getRGBVal(1, 0),
            bearImage.getRGBVal(2, 0),
            bearImage.getRGBVal(0, 1),
            bearImage.getRGBVal(1, 1),
            bearImage.getRGBVal(2, 1),
            bearImage.getRGBVal(0, 2),
            bearImage.getRGBVal(1, 2),
            bearImage.getRGBVal(2, 2))),
            new ArrayList<Float>(Arrays.asList(1f, 1 / 4f, -1 / 8f, 1 / 4f, 1 / 4f,
                    -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f)));
    Assert.assertArrayEquals(sharped00, sharped.getRGBVal(0, 0));
    int[] sharped02 = manualAddRGBVal(new ArrayList<int[]>(Arrays.asList(bearImage.getRGBVal(0, 0),
            bearImage.getRGBVal(1, 0),
            bearImage.getRGBVal(2, 0),
            bearImage.getRGBVal(3, 0),
            bearImage.getRGBVal(4, 0),
            bearImage.getRGBVal(0, 1),
            bearImage.getRGBVal(1, 1),
            bearImage.getRGBVal(2, 1),
            bearImage.getRGBVal(3, 1),
            bearImage.getRGBVal(4, 1),
            bearImage.getRGBVal(0, 2),
            bearImage.getRGBVal(1, 2),
            bearImage.getRGBVal(2, 2),
            bearImage.getRGBVal(3, 2),
            bearImage.getRGBVal(4, 2))),
            new ArrayList<Float>(Arrays.asList(-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f,
                    -1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f,
                    -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f)));
    Assert.assertArrayEquals(sharped02, sharped.getRGBVal(2, 0));
    int[] blur00 = manualAddRGBVal(new ArrayList<int[]>(Arrays.asList(bearImage.getRGBVal(0, 0),
            bearImage.getRGBVal(1, 0),
            bearImage.getRGBVal(0, 1),
            bearImage.getRGBVal(1, 1))),
            new ArrayList<Float>(Arrays.asList(1 / 4f, 1 / 8f,
                    1 / 8f, 1 / 16f)));
    Assert.assertArrayEquals(blur00, blurred.getRGBVal(0, 0));
    int[] blur01 = manualAddRGBVal(new ArrayList<int[]>(Arrays.asList(bearImage.getRGBVal(0, 0),
            bearImage.getRGBVal(1, 0),
            bearImage.getRGBVal(2, 0),
            bearImage.getRGBVal(0, 1),
            bearImage.getRGBVal(1, 1),
            bearImage.getRGBVal(2, 1))),
            new ArrayList<Float>(Arrays.asList(1 / 8f, 1 / 4f, 1 / 8f,
                    1 / 16f, 1 / 8f, 1 / 16f)));
    Assert.assertArrayEquals(blur01, blurred.getRGBVal(1, 0));
  }

  /**
   * test if there's any value in sharp or blur image out of bound (checks the clamp method.
   */
  @Test
  public void catchOverBound() {
    for (int y = 0; y < bearImage.getHeight(); y++) {
      for (int x = 0; x < bearImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertTrue(sharped.getRGBVal(x, y)[i] >= 0 && sharped.getRGBVal(x, y)[i] <= 255);
          Assert.assertTrue(blurred.getRGBVal(x, y)[i] >= 0 && blurred.getRGBVal(x, y)[i] <= 255);
        }
      }
    }
  }

  @Test
  public void testGetKernel() {
    List<List<Float>> blurKernel = blurFilter.getKernel();
    ArrayList<ArrayList<Float>> blurKernelToTest = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(1 / 16f, 1 / 8f, 1 / 16f)),
            new ArrayList<>(Arrays.asList(1 / 8f, 1 / 4f, 1 / 8f)),
            new ArrayList<>(Arrays.asList(1 / 16f, 1 / 8f, 1 / 16f))
    ));
    for (int i = 0; i < blurKernel.size(); i++) {
      for (int u = 0; u < blurKernel.get(i).size(); u++) {
        Assert.assertEquals(blurKernelToTest.get(i).get(u), blurKernel.get(i).get(u));
      }
    }
    List<List<Float>> sharpKernel = sharpFilter.getKernel();
    ArrayList<ArrayList<Float>> sharpKernelToTest = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f))
    ));
    for (int i = 0; i < sharpKernel.size(); i++) {
      for (int u = 0; u < sharpKernel.get(i).size(); u++) {
        Assert.assertEquals(sharpKernel.get(i).get(u), sharpKernelToTest.get(i).get(u));
      }
    }

  }

}
