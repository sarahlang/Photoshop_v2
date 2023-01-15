import userimagecommand.Load;
import userimagecommand.Save;
import imagemodel.model.Image;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for Image class, especially importing of the file.
 */
public class ImageTest {

  Image slothImage = Load.importImage("res/Sloth.png");
  Image bearImage = Load.importImage("res/bear.ppm");
  Image square = new Image(2, 2,
      new int[][][]{
          new int[][]{
              new int[]{255, 0, 0},
              new int[]{255, 0, 0}},
          new int[][]{
              new int[]{0, 0, 255},
              new int[]{0, 255, 0}}}, false);

  @Test
  public void testGetWidthAndHeight() {
    Assert.assertEquals(2, square.getHeight());
    Assert.assertEquals(2, square.getWidth());
  }

  @Test
  public void testGetRGBVal() {
    Assert.assertEquals(0, square.getRGBVal(1, 0)[0]);
    Assert.assertEquals(0, square.getRGBVal(1, 0)[1]);
    Assert.assertEquals(255, square.getRGBVal(1, 0)[2]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testreadScriptWrongFormat() {
    new Load().importImage("res/Sloth.jpeg");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testreadScriptNonExistedFile() {
    new Load().importImage("res/abc.ppm");
  }

  /**
   * Test if going through constructor and save to file the image still looked the same.
   */
  @Test
  public void sameFormatSameImage() {
    Save.saveToFile("res/newSloth", "png", slothImage);
    Image newSlothImage = Load.importImage("res/newSloth.png");

    Assert.assertEquals(slothImage.getWidth(), newSlothImage.getWidth());
    Assert.assertEquals(slothImage.getHeight(), newSlothImage.getHeight());
    for (int y = 0; y < slothImage.getHeight(); y++) {
      for (int x = 0; x < slothImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertEquals(slothImage.getRGBVal(x, y)[i], newSlothImage.getRGBVal(x, y)[i]);
        }
      }
    }
    //testing if png is still the same image after save to file

    Save.saveToFile("res/newBear", "ppm", bearImage);
    Image newBearImage = Load.importImage("res/newBear.ppm");

    Assert.assertEquals(bearImage.getWidth(), newBearImage.getWidth());
    Assert.assertEquals(bearImage.getHeight(), newBearImage.getHeight());
    //now for png image
    for (int y = 0; y < newBearImage.getHeight(); y++) {
      for (int x = 0; x < newBearImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertEquals(bearImage.getRGBVal(x, y)[i], newBearImage.getRGBVal(x, y)[i]);
        }
      }
    }
  }

  /**
   * Test if going through constructor and save to file the image still looked the same.
   */
  @Test
  public void diffFormatSameImage() {
    Save.saveToFile("res/newSloth", "png", slothImage);
    Image newSlothImage = Load.importImage("res/newSloth.png");
    Assert.assertEquals(slothImage.getWidth(), newSlothImage.getWidth());
    Assert.assertEquals(slothImage.getHeight(), newSlothImage.getHeight());
    for (int y = 0; y < slothImage.getHeight(); y++) {
      for (int x = 0; x < slothImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertEquals(slothImage.getRGBVal(x, y)[i], newSlothImage.getRGBVal(x, y)[i]);
        }
      }
    }
    //testing if png and ppm after save to file is still the same image
    Save.saveToFile("res/newBear", "png", bearImage);
    Image newBearImage = Load.importImage("res/newBear.png");
    Assert.assertEquals(bearImage.getWidth(), newBearImage.getWidth());
    Assert.assertEquals(bearImage.getHeight(), newBearImage.getHeight());
    //now for png image
    for (int y = 0; y < newBearImage.getHeight(); y++) {
      for (int x = 0; x < newBearImage.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertEquals(bearImage.getRGBVal(x, y)[i], newBearImage.getRGBVal(x, y)[i]);
        }
      }
    }
  }
}

