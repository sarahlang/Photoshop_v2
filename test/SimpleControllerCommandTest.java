import imagecontroller.adaptors.CommandAdaptor;
import imagecontroller.controllers.ImageController;
import imagecontroller.controllers.SimpleImageController;
import userimagecommand.Load;
import imagemodel.filters.colortransformation.MonoChrome;
import imagemodel.filters.colortransformation.Sepia;
import imagemodel.filters.filtering.Blur;
import imagemodel.filters.filtering.Sharp;
import imagemodel.model.Image;
import imagemodel.model.MultiImageModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * tests for ControllerCommand.
 */
public class SimpleControllerCommandTest {

  MultiImageModel model;
  String initiated = "program initiated";
  CommandAdaptor messageAdaptor;

  @Before
  public void init() {
    model = new MultiImageModel();
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
    return "\r\n" + "Originally layer count: "
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
    return "\r\n" + "Current Layer that is worked on: Layer No." + String.valueOf(imgIdx);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerConstructorReadable() {
    StringBuilder out = new StringBuilder();
    ImageController imageController = new SimpleImageController(model, null, out);
  }

  @Test
  public void testIfAbleToInitiated() {
    Reader in = new StringReader("oijfoijfrijfroisoigeoij");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToReadScript() throws FileNotFoundException {
    Readable in = new FileReader("example_script_file_1.txt");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    //test if the script actually ran
    Assert.assertEquals(model.getAllLayers().size(), 5);
  }

  @Test
  public void testifNothingComeAfterCreate() {
    Reader in = new StringReader("create brgbrtb ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + "\r\n"
            + "Input again" + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToCreateLayers() {
    Reader in = new StringReader("create 3");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3) + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToCreateOneThenCreateAgain() {
    Reader in = new StringReader("create 3 create 2 layer 4");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3)
            + layersCreatedString(3, 2) + "\r\n", out.toString());
    Assert.assertEquals(5, model.getAllLayers().size());
  }

  @Test
  public void testHavingRandomCharInbetween() {
    Reader in = new StringReader("create 3 sriughberkgverp create 4");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3)
            + layersCreatedString(3, 4) + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToCreateZeroLayer() {
    Reader in = new StringReader("create 0");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 0) + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToSetCurrent() {
    Reader in = new StringReader("create 3 current 2");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3) + "\r\n"
            + "Current Layer that is worked on: Layer No.2" + "\r\n", out.toString());
  }

  @Test
  public void testifNothingComeAfterCurrent() {
    Reader in = new StringReader("create 1 current ert ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + "\r\n"
            + "Input again" + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleToSwitchCurrent() {
    Reader in = new StringReader("create 3 current 2 current 3");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3)
            + currentlayer(2)
            + currentlayer(3) + "\r\n", out.toString());
    Assert.assertEquals(3, model.getAllLayers().size());

  }

  @Test
  public void testNoValidCurrentYet() {
    Reader in = new StringReader("create 3 current 0");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3) + "\r\n"
            + "0 is out of bound." + "\r\n", out.toString());
    Assert.assertEquals(3, model.getAllLayers().size());
  }

  @Test
  public void testIfAbleTosayCurrentOutOfBount() {
    Reader in = new StringReader("create 3 current 0");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3) + "\r\n"
            + "0 is out of bound." + "\r\n", out.toString());
  }

  @Test
  public void testIfAbleTosayCurrentOverTheBound() {
    Reader in = new StringReader("create 3 current 4");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3) + "\r\n"
            + "4 is out of bound." + "\r\n", out.toString());
  }

  @Test
  public void ifAbleToLoad() {
    Reader in = new StringReader("create 3 current 1 "
            + "load res/bear.png "
            + "current 2 "
            + "load res/crocodile.jpg "
            + "current 3 "
            + "load res/bear.ppm ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 3)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + currentlayer(2)
            + "\r\n" + "res/crocodile.jpg successfully loaded"
            + currentlayer(3)
            + "\r\n" + "res/bear.ppm successfully loaded" + "\r\n", out.toString());
    for (int i = 0; i < 3; i++) {
      //all the image shouldn't be empty now
      Assert.assertTrue(model.getAllLayers().get(i).getWidth() != 0);
    }
  }

  @Test
  public void ifAbleToAlarmReplaced() {
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "load res/crocodile.jpg ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n" + "res/crocodile.jpg successfully loaded" + "\r\n", out.toString());
  }

  @Test
  public void testifNothingComeAfterLoad() {
    Reader in = new StringReader("create 1 current 1 "
            + "load u");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "java.lang.IllegalArgumentException: File Not Found caught in creating."
            + "\r\n"
            + "file is not found, please re-input file name." + "\r\n", out.toString());
  }

  @Test
  public void testifAbleToApplyFilterExpectedlyBlur() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter blur ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "blur filter successfully set" + "\r\n", out.toString());
    Image blurredOnce = new Blur().applyTo(bearImage);
    Assert.assertTrue(blurredOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlySharp() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter sharp ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "sharp filter successfully set" + "\r\n", out.toString());
    Image sharpedOnce = new Sharp().applyTo(bearImage);
    Assert.assertTrue(sharpedOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlySepia() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter sepia ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "sepia filter successfully set" + "\r\n", out.toString());
    Image sepiaOnce = new Sepia().applyTo(bearImage);
    Assert.assertTrue(sepiaOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterExpectedlyGrey() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter monochrome ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "monochrome filter successfully set" + "\r\n", out.toString());
    Image monochromeOnce = new MonoChrome().applyTo(bearImage);
    Assert.assertTrue(monochromeOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifAbleToApplyFilterTwice() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter monochrome "
            + "filter blur ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "monochrome filter successfully set"
            + "\r\n"
            + "blur filter successfully set" + "\r\n", out.toString());
    Image twoFilted = new Blur().applyTo(new MonoChrome().applyTo(bearImage));
    Assert.assertTrue(twoFilted.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testifNothingComeAfterFilter() {
    Image bearImage = new Load().importImage("res/bear.png");
    Reader in = new StringReader("create 1 current 1 "
            + "load res/bear.png "
            + "filter ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n", out.toString());
    Image monochromeOnce = new MonoChrome().applyTo(bearImage);
    Assert.assertTrue(monochromeOnce.sameImage(model.getCurrentImage()));
  }

  @Test
  public void testFilterOnNonLoadedImage() {
    Reader in = new StringReader("create 1 current 1 "
            + "filter blur");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "there is no image to work on yet" + "\r\n", out.toString());
  }


  @Test
  public void testFilterOnNoImage() {
    Reader in = new StringReader("create 1 current 1 "
            + "filter blur ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 1)
            + currentlayer(1)
            + "\r\n"
            + "there is no image to work on yet" + "\r\n", out.toString());
  }

  @Test
  public void testIfableToMakeLayerInvisibleCountCorrect() {
    Reader in = new StringReader("create 5 current 1 "
            + "load res/bear.png "
            + "invisible " + "current 3 " + "invisible");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 5)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "layer 1 just set invisible" + "\r\n"
            + "the current layer is now the last current: 0" + "\r\n"
            + "Current Layer that is worked on: Layer No.3" + "\r\n"
            + "layer 3 just set invisible" + "\r\n", out.toString());
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
    Reader in = new StringReader("create 5 current 1 "
            + "load res/bear.png "
            + "invisible " + "visible");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 5)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n"
            + "layer 1 just set invisible" + "\r\n"
            + "the current layer is now the last current: 0" + "\r\n", out.toString());
  }

  @Test
  public void testApplyFilterAfterIsTransparant() {
    Reader in = new StringReader("create 5 current 1 "
            + "invisible ");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 5)
            + currentlayer(1)
            + "\r\n"
            + "layer 1 just set invisible" + "\r\n", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).isTransparent());
  }

  @Test
  public void testloadImageOnTransparantLayer() {
    Reader in = new StringReader("create 5 current 1 "
            + "invisible "
            + "load res/bear.png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 5)
            + currentlayer(1)
            + "\r\n"
            + "layer 1 just set invisible"
            + "\r\n"
            + "current does not exist" + "\r\n", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).isTransparent());
  }

  @Test
  public void testIfCanSaveWithoutHavingCreate() {
    Reader in = new StringReader("save png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + "\r\n"
            + "current does not exist to be saved" + "\r\n", out.toString());
  }

  @Test
  public void testIfCanSaveWithoutCurrent() {
    Reader in = new StringReader(" Create 2 "
            + "save png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + "\r\n"
            + "current does not exist to be saved" + "\r\n", out.toString());
  }

  @Test
  public void testIfCanSaveWithoutHavingLoad() {
    Reader in = new StringReader("Create 2 Current 1 save png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "can not save when has not bean loaded" + "\r\n", out.toString());
  }

  @Test
  public void testIfCanSaveWithTransparant() {
    Reader in = new StringReader("Create 2 Current 1 load res/bear.png invisible save png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.png successfully loaded"
            + "\r\n" + "layer 1 just set invisible"
            + "\r\n" + "the current layer is now the last current: 0"
            + "\r\n" + "current does not exist to be saved" + "\r\n", out.toString());
  }

  @Test
  public void testIfSaveCanReturnSameImageppm() {
    Reader in = new StringReader("Create 2 Current 1 load res/bear.ppm save ppm"
            + " current 2 load res/bear.png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.ppm successfully loaded"
            + "\r\n" + "Writing out to file: Image1.ppm"
            + "\r\n" + "Current Layer that is worked on: Layer No.2"
            + "\r\n" + "res/bear.png successfully loaded" + "\r\n", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model.getAllLayers().get(1)));
  }

  @Test
  public void testIfSaveCanReturnSameImagepng() {
    Reader in = new StringReader("Create 2 Current 1 load res/bear.ppm save png" +
            " current 2 load res/bear.png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.ppm successfully loaded"
            + "\r\n" + "Writing out to file: Image1.png"
            + "\r\n" + "Current Layer that is worked on: Layer No.2"
            + "\r\n" + "res/bear.png successfully loaded" + "\r\n", out.toString());
    Assert.assertTrue(model.getAllLayers().get(0).sameImage(model.getAllLayers().get(1)));
  }

  @Test
  public void testIfSaveCanReturnSameImagejpg() {
    Reader in = new StringReader("Create 2 Current 1 load res/bear.ppm save jpg" +
            " current 2 load res/bear.png");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.ppm successfully loaded"
            + "\r\n" + "Writing out to file: Image1.jpg"
            + "\r\n" + "Current Layer that is worked on: Layer No.2"
            + "\r\n" + "res/bear.png successfully loaded" + "\r\n", out.toString());
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
    Reader in = new StringReader("Create 2 Current 1 load res/bear.ppm"
            + " current 2 load res/Crocodile.jpg saveAll");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    //if out put string is as expected
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.ppm successfully loaded"
            + currentlayer(2)
            + "\r\n"
            + "res/Crocodile.jpg successfully loaded"
            + "\r\nres/" + timeStamp + "_imgfolder Directory created successfully"
            + "\r\n" + "Writing out to file: " + timeStamp + ".txt"
            + "\r\n" + "Writing out to file: res/" + timeStamp + "_imgfolder/image1.png"
            + "\r\n" + "Writing out to file: res/" + timeStamp
            + "_imgfolder/image2.png" + "\r\n", out.toString());

    //if able to retrive those images
    Reader in2 = new StringReader("Create 2 Current 1 load res/"
            + timeStamp + "_imgfolder/image0.png" +
            " current 2 load res/" + timeStamp + "_imgfolder/image1.png");
    StringBuilder out2 = new StringBuilder();
    MultiImageModel model2 = new MultiImageModel();
    ImageController a_controller_2 = new SimpleImageController(model2, in2, out2);
    a_controller_2.runProgram();//if out put string is as expected
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
    Reader in = new StringReader("saveAll");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    //if out put string is as expected
    Assert.assertEquals(initiated
            + "\r\nres/" + timeStamp + "_imgfolder Directory created successfully"
            + "\r\n" + "Writing out to file: " + timeStamp + ".txt"
            + "\r\n" + "There is no image saved into the folder" + "\r\n", out.toString());
  }

  @Test
  public void saveAllbeforecurrent() {
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
    Reader in = new StringReader("create 2 saveAll");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    //if out put string is as expected
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + "\r\nres/" + timeStamp + "_imgfolder Directory created successfully"
            + "\r\n" + "Writing out to file: " + timeStamp + ".txt"
            + "\r\n" + "There is no image saved into the folder" + "\r\n", out.toString());
  }

  @Test
  public void saveAllonlyOneImage() {
    Reader in = new StringReader("create 2 Current 1 load res/bear.ppm saveAll");
    StringBuilder out = new StringBuilder();
    ImageController a_controller = new SimpleImageController(model, in, out);
    a_controller.runProgram();
    String timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date());
    //if out put string is as expected
    Assert.assertEquals(initiated
            + layersCreatedString(0, 2)
            + currentlayer(1)
            + "\r\n"
            + "res/bear.ppm successfully loaded"
            + "\r\nres/" + timeStamp + "_imgfolder Directory created successfully"
            + "\r\n" + "Writing out to file: " + timeStamp + ".txt"
            + "\r\n" + "Writing out to file: res/" + timeStamp
            + "_imgfolder/image1.png" + "\r\n", out.toString());
  }
}
