import imagecontroller.controllers.ComplexImageController;
import imagecontroller.controllers.ImageController;
import imagemodel.filters.colortransformation.MonoChrome;
import imagemodel.filters.colortransformation.Sepia;
import imagemodel.filters.filtering.Blur;
import imagemodel.filters.filtering.Sharp;
import imagemodel.model.Image;
import imagemodel.model.MultiImageModel;
import imageview.swing.ImageViewSwing;
import imageview.swing.InteractiveImageView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import userimagecommand.Load;

import java.io.Reader;
import java.io.StringReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * a mock test to test the funcationality of the complex and the gui wokring together.
 */
public class ComplexControlTest {

  private MultiImageModel model;
  private StringBuilder out;
  private InteractiveImageView frame;
  private ComplexImageController controller;

  @Before
  public void init() {
    model = new MultiImageModel();
    controller = new ComplexImageController(model);
    out = new StringBuilder();
    frame = new ImageViewMock(controller, out);
    controller.setView(frame);
  }

  /**
   * Convinient method to product a string reporting the layers.
   *
   * @param before  how many layers are there before
   * @param created how many layers created
   * @return the string created
   */
  public static String layersCreatedString(int before, int created) {
    int total = before + created;
    return "Originally layer count: "
        + before
        + "\r\n"
        + created
        + " layers created"
        + "\r\n"
        + "total size now: " + total;
  }

  /**
   * Convinient method to product a string reporting the current layer.
   *
   * @param imgIdx the current layer is current at
   * @return the produced string
   */
  public String currentlayer(int imgIdx) {
    return "Current Layer that is worked on: Layer No." + String.valueOf(imgIdx);
  }

  @Test(expected = NullPointerException.class)
  public void testControllerConstructorReadable() {
    ImageController imageController = new ComplexImageController(null);
  }

  @Test
  public void testIfAbleToInitiated() {
    String[] command = new String[]{"create", "oijfoijfrijfroisoigeoij"};
    controller.workOnImages(command);
    Assert.assertEquals("Input again", out.toString());
  }

  @Test
  public void testIfAbleToReadScript() throws FileNotFoundException {
    controller.readScript("example_scrip_file_2.txt");
    //test if the script actually ran
    Assert.assertEquals(model.getAllLayers().size(), 3);
  }

  @Test
  public void testIfAbleToCreateLayers() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    Assert.assertEquals(layersCreatedString(0, 3), out.toString());
  }

  @Test
  public void testIfAbleToCreateOneThenCreateAgain() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "create 2";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "layer 4";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    Assert.assertEquals(layersCreatedString(0, 3)
        + layersCreatedString(3, 2), out.toString());
    Assert.assertEquals(5, model.getAllLayers().size());
  }

  @Test
  public void testIfAbleToCreateZeroLayer() {
    String str = "create 0";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    Assert.assertEquals(layersCreatedString(0, 0), out.toString());
  }

  @Test
  public void testIfAbleToSetCurrent() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 2";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    Assert.assertEquals(layersCreatedString(0, 3)
        + "Current Layer that is worked on: Layer No.2", out.toString());
  }

  @Test
  public void testIfAbleToSwitchCurrent() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 2";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "current 3";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    Assert.assertEquals(layersCreatedString(0, 3)
        + currentlayer(2)
        + currentlayer(3), out.toString());
    Assert.assertEquals(3, model.getAllLayers().size());
  }

  @Test
  public void testNoValidCurrentYet() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 0";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    Assert.assertEquals(layersCreatedString(0, 3)
        + "0 is out of bound.", out.toString());
    Assert.assertEquals(3, model.getAllLayers().size());
  }

  @Test
  public void testIfAbleTosayCurrentOverTheBound() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 4";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    Assert.assertEquals(layersCreatedString(0, 3)
        + "4 is out of bound.", out.toString());
  }

  @Test
  public void ifAbleToLoad() {
    String str = "create 3";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "current 2";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "load res/crocodile.jpg";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "current 3";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    String str7 = "load res/bear.ppm";
    String[] words7 = str7.split(" ");
    controller.workOnImages(words7);
    Assert.assertEquals(layersCreatedString(0, 3)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + currentlayer(2)
        + "res/crocodile.jpg successfully loaded"
        + currentlayer(3)
        + "res/bear.ppm successfully loaded", out.toString());
    for (int i = 0; i < 3; i++) {
      //all the image shouldn't be empty now
      Assert.assertTrue(model.getAllLayers().get(i).getWidth() != 0);
    }
  }

  @Test
  public void testifAbleToApplyFilterExpectedlyBlur() {
    Image bearImage = new Load().importImage("res/bear.png");
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "filter blur";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "blur filter successfully set", out.toString());
    Image blurredOnce = new Blur().applyTo(bearImage);
    Assert.assertTrue(blurredOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlySharp() {
    Image bearImage = new Load().importImage("res/bear.png");
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "filter sharp";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "sharp filter successfully set", out.toString());
    Image sharpedOnce = new Sharp().applyTo(bearImage);
    Assert.assertTrue(sharpedOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlySepia() {
    Image bearImage = new Load().importImage("res/bear.png");
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "filter sepia";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "sepia filter successfully set", out.toString());
    Image sepiaOnce = new Sepia().applyTo(bearImage);
    Assert.assertTrue(sepiaOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlyGrey() {
    Image bearImage = new Load().importImage("res/bear.png");
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "filter monochrome";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "monochrome filter successfully set", out.toString());
    Image monochromeOnce = new MonoChrome().applyTo(bearImage);
    Assert.assertTrue(monochromeOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterTwice() {
    Image bearImage = new Load().importImage("res/bear.png");
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "filter monochrome";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "filter blur";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "monochrome filter successfully set"
        + "blur filter successfully set", out.toString());
    Image twoFilted = new Blur().applyTo(new MonoChrome().applyTo(bearImage));
    Assert.assertTrue(twoFilted.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testFilterOnNonLoadedImage() {
    String str = "create 1";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "filter blur";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    Assert.assertEquals(layersCreatedString(0, 1)
        + currentlayer(1)
        + "there is no image to work on yet", out.toString());
  }


  @Test
  public void testIfableToMakeLayerInvisibleCountCorrect() {
    String str = "create 5";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "invisible";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "current 3";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "invisible";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 5)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "layer 1 just set invisible"
        + "the current layer is now the last current: 0"
        + "Current Layer that is worked on: Layer No.3"
        + "layer 3 just set invisible", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).isTransparent());
    int count = 0;
    for (Image i : model.getAllLayers()) {
      if (i.isTransparent()) {
        count += 1;
      }
    }
    Assert.assertEquals(2, count);
  }

  @Test
  public void testIfableToRevertInvisibility() {
    String str = "create 5";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "current 2";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "load res/sloth.png";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "invisible";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 5)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + currentlayer(2)
        + "res/sloth.png successfully loaded"
        + "layer 2 just set invisible"
        + "the current layer is now the last current: 1", out.toString());
  }

  @Test
  public void testApplyFilterAfterIsTransparant() {
    String str = "create 5";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "invisible";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    Assert.assertEquals(layersCreatedString(0, 5)
        + currentlayer(1)
        + "layer 1 just set invisible", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).isTransparent());
  }

  @Test
  public void testloadImageOnTransparantLayer() {
    String str = "create 5";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "invisible";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "load res/bear.png";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    Assert.assertEquals(layersCreatedString(0, 5)
        + currentlayer(1)
        + "layer 1 just set invisible"
        + "there is no workable layer yet", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).isTransparent());
  }

  @Test
  public void testIfCanSaveWithoutHavingCreate() {
    String str = "save png";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    Assert.assertEquals("current does not exist to be saved", out.toString());
  }

  @Test
  public void testIfCanSaveWithoutCurrent() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "save png";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    Assert.assertEquals(layersCreatedString(0, 2)
        + "current does not exist to be saved", out.toString());
  }

  @Test
  public void testIfCanSaveWithoutHavingLoad() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str1 = "Current 1";
    String[] words1 = str1.split(" ");
    controller.workOnImages(words1);
    String str2 = "save png";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "can not save when has not bean loaded", out.toString());
  }

  @Test
  public void testIfCanSaveWithTransparant() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "invisible";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "save png";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "layer 1 just set invisible"
        + "the current layer is now the last current: 0"
        + "current does not exist to be saved", out.toString());
  }

  @Test
  public void testIfSaveCanReturnSameImageppm() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "save ppm";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "current 2";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "load res/Image1.ppm";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "Writing out to file: res/Image1.ppm"
        + "Current Layer that is worked on: Layer No.2"
        + "res/Image1.ppm successfully loaded", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model.getAllLayers().get(1)));
  }

  @Test
  public void testIfSaveCanReturnSameImagepng() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "save png";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "current 2";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "load res/Image1.png";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "Writing out to file: res/Image1.png"
        + "Current Layer that is worked on: Layer No.2"
        + "res/Image1.png successfully loaded", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model.getAllLayers().get(1)));
  }

  @Test
  public void testIfSaveCanReturnSameImagejpg() {
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.png";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "save jpg";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "current 2";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "load res/Image1.jpg";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "res/bear.png successfully loaded"
        + "Writing out to file: res/Image1.jpg"
        + "Current Layer that is worked on: Layer No.2"
        + "res/Image1.jpg successfully loaded", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model.getAllLayers().get(1)));
  }

  @Test
  public void testIfSaveAll() {
    String timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date());
    String path = "res/" + timeStamp + "_imgfolder";
    //Creating a File object
    File file = new File(path);
    int filecount = 1;
    while (file.exists()) {
      timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date())
          + "_" + String.valueOf(filecount);
      path = "res/" + timeStamp + "_imgfolder";
      file = new File(path);
      filecount += 1;
    }
    String str = "create 2";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    String str2 = "current 1";
    String[] words2 = str2.split(" ");
    controller.workOnImages(words2);
    String str3 = "load res/bear.ppm";
    String[] words3 = str3.split(" ");
    controller.workOnImages(words3);
    String str4 = "current 2";
    String[] words4 = str4.split(" ");
    controller.workOnImages(words4);
    String str5 = "load res/Crocodile.jpg";
    String[] words5 = str5.split(" ");
    controller.workOnImages(words5);
    String str6 = "saveAll";
    String[] words6 = str6.split(" ");
    controller.workOnImages(words6);
    Assert.assertEquals(layersCreatedString(0, 2)
        + currentlayer(1)
        + "res/bear.ppm successfully loaded"
        + currentlayer(2)
        + "res/Crocodile.jpg successfully loaded"
        + "res/" + timeStamp + "_imgfolder Directory created successfully"
        + "Writing out to file: " + timeStamp + ".txt"
        + "Writing out to file: res/" + timeStamp + "_imgfolder/image1.png"
        + "Writing out to file: res/" + timeStamp
        + "_imgfolder/image2.png", out.toString());

    //if able to retrive those images
    StringBuilder out2 = new StringBuilder();
    MultiImageModel model2 = new MultiImageModel();
    ComplexImageController a_controller_2 = new ComplexImageController(model2);
    out2 = new StringBuilder();
    frame = new ImageViewMock(a_controller_2, out2);
    a_controller_2.setView(frame);
    String str7 = "create 2";
    String[] word7 = str7.split(" ");
    a_controller_2.workOnImages(word7);
    String str8 = "current 1";
    String[] words8 = str8.split(" ");
    a_controller_2.workOnImages(words8);
    String str9 = "load res/" + timeStamp + "_imgfolder/image0.png";
    String[] words9 = str9.split(" ");
    a_controller_2.workOnImages(words9);
    String str11 = "current 2";
    String[] words11 = str11.split(" ");
    a_controller_2.workOnImages(words11);
    String str12 = "load res/" + timeStamp + "_imgfolder/image1.png";
    String[] words12 = str12.split(" ");
    a_controller_2.workOnImages(words12);
    Reader in2 = new StringReader("Create 2 Current 1 load res/"
        + timeStamp + "_imgfolder/image0.png"
        + " current 2 load res/" + timeStamp + "_imgfolder/image1.png");
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model2.getAllLayers().get(0)));
    Assert.assertTrue(model.getAllLayers().get(1).sameImage(model2.getAllLayers().get(1)));
  }

  @Test
  public void saveAllbeforecreate() {
    String timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date());
    String path = "res/" + timeStamp + "_imgfolder";
    //Creating a File object
    File file = new File(path);
    int filecount = 1;
    while (file.exists()) {
      timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date())
          + "_" + String.valueOf(filecount);
      path = "res/" + timeStamp + "_imgfolder";
      file = new File(path);
      filecount += 1;
    }
    String str = "saveAll";
    String[] words = str.split(" ");
    controller.workOnImages(words);
    //if out put string is as expected
    Assert.assertEquals("res/" + timeStamp + "_imgfolder Directory created successfully"
        + "Writing out to file: " + timeStamp + ".txt"
        + "There is no image saved into the folder", out.toString());
  }


  private class ImageViewMock extends ImageViewSwing {

    private ComplexImageController feature;
    private Appendable out;

    private ImageViewMock(ComplexImageController feature, Appendable out) {
      super(feature);
      this.feature = feature;
      this.out = out;
    }

    @Override
    public void popTextBox(String output) {
      try {
        this.out.append(output);
      } catch (IOException e) {
        System.out.println(e.toString());
      }
    }
  }
}
