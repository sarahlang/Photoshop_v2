package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Saves the current layer of image in res/ folder.
 */
public class Save implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  /**
   * Saving an image to be shown in the interactive view.
   *
   * @param messageAdaptor a command adaptor.
   * @param model          an image model.
   */
  public Save(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  /**
   * A convenience constructor for saving an image from the res/ folder.
   */
  public Save() {
    this.messageAdaptor = null;
  }

  @Override
  public void apply() throws IOException {
    Image currentImage;
    try {
      currentImage = model.getCurrentImage();
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.appendCommand("current does not exist to be saved");
      return;
    }
    if (currentImage.getWidth() == 0) {
      this.messageAdaptor.appendCommand("can not save when has not bean loaded");
      return;
    }
    String formatToSave = this.messageAdaptor.getNextCommand();
    if (formatToSave.equals("Input again")) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    }
    Save.saveToFile("res/Image" + String.valueOf(model.getCurrent()), formatToSave, currentImage);
    this.messageAdaptor.appendCommand("Writing out to file: " + "res/Image"
        + String.valueOf(model.getCurrent()) + "." + formatToSave);
  }

  /**
   * Saves the given image to res/ folder with the given format and name.
   *
   * @param filename name of the saved image
   * @param format   format of the image, either ppm, png, or jpeg
   * @param image    image to be saved
   */
  public static void saveToFile(String filename, String format, Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    try {
      if (format.equals("ppm")) {
        PrintWriter outfile = new PrintWriter(filename + ".ppm");
        outfile.println("P3");   // Ascii PPM file
        outfile.println("# Image created by program");
        outfile.println(width + " " + height);
        outfile.println(255);

        for (int r = 0; r < height; r++) {
          for (int c = 0; c < width; c++) {
            outfile.println(image.getRGBVal(c, r)[0]);
            outfile.println(image.getRGBVal(c, r)[1]);
            outfile.println(image.getRGBVal(c, r)[2]);
          }
        }
        outfile.close();
      } else if (format.equals("png") || format.equals("jpeg") || format.equals("jpg")) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            int red = image.getRGBVal(x, y)[0];
            int green = image.getRGBVal(x, y)[1];
            int blue = image.getRGBVal(x, y)[2];
            int pixel = 0xFF000000 + (red << 16) + (green << 8) + blue;
            img.setRGB(x, y, pixel);
          }
        }
        File outputFile = new File(filename + "." + format);
        boolean value = false;
        try {
          value = ImageIO.write(img, format, outputFile);
        } catch (IOException e) {
          System.out.println("image caught in creating");
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("file could not be found");
    }
  }
}