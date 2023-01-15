import imagemodel.filters.ImageFilterModel;
import java.util.List;
import userimagecommand.Load;
import imagemodel.filters.AbstractImageFilter;
import imagemodel.filters.colortransformation.MonoChrome;
import imagemodel.filters.colortransformation.Sepia;
import imagemodel.model.Image;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Tests for ColorTransformation class.
 */
public class ColorTransformationTest {

  private Image slothImage;
  private Image bearImage;

  private AbstractImageFilter monochromeFilter;
  private AbstractImageFilter sepiaFilter;

  private Image monochromeSloth;
  private Image monochromeSlothTwice;
  private Image sepiaSloth;
  private Image sepiaSlothTwice;

  private Image monochromeBear;
  private Image monochromeBearTwice;
  private Image sepiaBear;
  private Image sepiaBearTwice;

  @Before
  public void init() {
    slothImage = new Load().importImage("res/Sloth.png");
    bearImage = new Load().importImage("res/bear.ppm");

    monochromeFilter = new MonoChrome();
    sepiaFilter = new Sepia();

    monochromeSloth = monochromeFilter.applyTo(slothImage);
    monochromeSlothTwice = monochromeFilter.applyTo(monochromeSloth);
    sepiaSloth = sepiaFilter.applyTo(slothImage);
    sepiaSlothTwice = sepiaFilter.applyTo(sepiaSloth);

    monochromeBear = monochromeFilter.applyTo(bearImage);
    monochromeBearTwice = monochromeFilter.applyTo(monochromeBear);
    sepiaBear = sepiaFilter.applyTo(bearImage);
    sepiaBearTwice = sepiaFilter.applyTo(sepiaBear);
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

    int imageWidth = original.getWidth();
    int imageHeight = original.getHeight();
    Assert.assertEquals(after.getHeight(), imageHeight);
    Assert.assertEquals(after.getWidth(), imageWidth);

    Random rn = new Random();
    Random rn2 = new Random();

    //pick 100 random coordinates to check their pixels
    for (int a = 0; a < 100; a++) {
      int dimensionXpicked = rn.nextInt(imageWidth);
      int dimensionYpicked = rn2.nextInt(imageHeight);
      int[] newRGBVal = new int[3];
      int[] rgb = original.getRGBVal(dimensionXpicked, dimensionYpicked);
      // get the three different colors.
      int red = rgb[0];
      int green = rgb[1];
      int blue = rgb[2];
      // set the RGB values according to the calculation.
      newRGBVal[0] = (int) (red * kernelToTest.get(0).get(0) + green * kernelToTest.get(0).get(1)
              + blue * kernelToTest.get(0).get(2));
      newRGBVal[1] = (int) (red * kernelToTest.get(1).get(0) + green * kernelToTest.get(1).get(1)
              + blue * kernelToTest
              .get(1).get(2));
      newRGBVal[2] = (int) (red * kernelToTest.get(2).get(0) + green * kernelToTest.get(2).get(1)
              + blue * kernelToTest.get(2).get(2));
      // clamp
      for (int u = 0; u < 3; u++) {
        newRGBVal[u] = Math.min(Math.max(newRGBVal[u], 0), 255);
      }
      Assert.assertArrayEquals(after.getRGBVal(dimensionXpicked, dimensionYpicked),
              newRGBVal);
    }
  }

  @Test
  //testing if any random pixel selected can adhere to the rule
  public void testRandomSelectedPixel() {
    Assert.assertTrue(sepiaFilter instanceof ImageFilterModel);
    Assert.assertTrue(monochromeFilter instanceof ImageFilterModel);
    aHundredRandomPixelKernelTest(monochromeFilter.getKernel(), slothImage, monochromeSloth);
    aHundredRandomPixelKernelTest(sepiaFilter.getKernel(), slothImage, sepiaSloth);
    //testing for monochrome image twice can be still work as expected
    aHundredRandomPixelKernelTest(monochromeFilter.getKernel(), monochromeSloth,
            monochromeSlothTwice);
    //testing for sepia image twice can be still work as expected
    aHundredRandomPixelKernelTest(sepiaFilter.getKernel(), sepiaSloth, sepiaSlothTwice);

    aHundredRandomPixelKernelTest(monochromeFilter.getKernel(), bearImage, monochromeBear);
    aHundredRandomPixelKernelTest(sepiaFilter.getKernel(), bearImage, sepiaBear);
    //testing for monochrome image twice can be still work as expected
    aHundredRandomPixelKernelTest(monochromeFilter.getKernel(), monochromeBear,
            monochromeBearTwice);
    //testing for sepia image twice can be still work as expected
    aHundredRandomPixelKernelTest(sepiaFilter.getKernel(), sepiaBear, sepiaBearTwice);

  }

  //test if there's any value in monochrome or sepia image out of bound
  @Test
  public void catchOverBound() {
    for (int y = 0; y < slothImage.getHeight(); y++) {
      for (int x = 0; x < slothImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertTrue(
                  monochromeSloth.getRGBVal(x, y)[i] >= 0
                          && monochromeSloth.getRGBVal(x, y)[i] <= 255);
          Assert.assertTrue(
                  sepiaSloth.getRGBVal(x, y)[i] >= 0
                          && sepiaSloth.getRGBVal(x, y)[i] <= 255);
        }
      }
    }

    for (int y = 0; y < bearImage.getHeight(); y++) {
      for (int x = 0; x < bearImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertTrue(
                  monochromeBear.getRGBVal(x, y)[i] >= 0
                          && monochromeBear.getRGBVal(x, y)[i] <= 255);
          Assert
                  .assertTrue(sepiaBear.getRGBVal(x, y)[i] >= 0
                          && sepiaBear.getRGBVal(x, y)[i] <= 255);
        }
      }
    }
  }

  @Test
  public void testGetKernel() {
    List<List<Float>> monochromeKernel = monochromeFilter.getKernel();
    ArrayList<ArrayList<Float>> monochromeKernelToTest = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f)),
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f)),
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f))
    ));
    for (int i = 0; i < monochromeKernel.size(); i++) {
      for (int u = 0; u < monochromeKernel.get(i).size(); u++) {
        Assert.assertEquals(monochromeKernelToTest.get(i).get(u), monochromeKernel.get(i).get(u));
      }
    }
    List<List<Float>> sepiaKernel = sepiaFilter.getKernel();
    ArrayList<ArrayList<Float>> sepiaKernelToTest = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(0.393f, 0.769f, 0.189f)),
            new ArrayList<>(Arrays.asList(0.349f, 0.686f, 0.168f)),
            new ArrayList<>(Arrays.asList(0.272f, 0.534f, 0.131f))
    ));
    for (int i = 0; i < sepiaKernel.size(); i++) {
      for (int u = 0; u < sepiaKernel.get(i).size(); u++) {
        Assert.assertEquals(sepiaKernel.get(i).get(u), sepiaKernelToTest.get(i).get(u));
      }
    }
  }
}