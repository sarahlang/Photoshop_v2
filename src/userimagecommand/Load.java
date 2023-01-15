package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Loads an image onto the current layer.
 */
public class Load implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  /**
   * Loading an image to be shown in the interactive view.
   *
   * @param messageAdaptor a command adaptor.
   * @param model          an image model.
   */
  public Load(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  /**
   * A convenience constructor for loading an image from the res/ folder.
   */
  public Load() {
    this.messageAdaptor = null;
    this.model = null;
  }


  @Override
  public void apply() throws IOException {
    String filename = this.messageAdaptor.getNextCommand();
    if (filename.equals("Input again")) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    }
    try {
      model.getCurrent();
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.appendCommand("there is no workable layer yet");
      return;
    }
    try {
      importImage(filename);
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.appendCommand(e + " caught in creating.");
      this.messageAdaptor.appendCommand("file is not found, please re-input file name.");
      return;
    }
    if (model.getCurrent() == 0) {
      this.messageAdaptor.appendCommand("Can not load image on transparant layer");
      return;
    }
    model.setCurrent(importImage(filename));
    this.messageAdaptor.appendCommand(filename + " successfully loaded");
    this.messageAdaptor.reFreshImage(importImage(filename).toBufferImage());
  }

  /**
   * Import image file from png, ppm, jpg as an Image object.
   *
   * @param filename the filename where the image file is located
   * @return an image object
   */
  public static Image importImage(String filename) {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File Not Found");
    }
    int width;
    int height;
    int[][][] rgb;
    if (filename.endsWith("ppm")) {
      StringBuilder builder = new StringBuilder();
      //read the file line by line, and populate a string. This will throw away any comment lines
      while (sc.hasNextLine()) {
        String s = sc.nextLine();
        if (s.charAt(0) != '#') {
          builder.append(s + System.lineSeparator());
        }
      }
      //now set up the scanner to read from the string we just built
      sc = new Scanner(builder.toString());
      String token;

      token = sc.next();
      width = sc.nextInt();
      height = sc.nextInt();
      int maxVal = sc.nextInt();
      rgb = new int[width][height][3];

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int r = sc.nextInt();
          int g = sc.nextInt();
          int b = sc.nextInt();
          rgb[x][y] = new int[]{r, g, b};
        }
      }
    } else if (filename.endsWith("png") || filename.endsWith("jpg")) {
      try {
        Path source = Paths.get(filename);
        BufferedImage img = ImageIO.read(source.toFile());
        width = img.getWidth();
        height = img.getHeight();
        rgb = new int[width][height][3];
        for (int y = 0; y < img.getHeight(); y++) {
          for (int x = 0; x < img.getWidth(); x++) {
            //Retrieving contents of a pixel
            int pixel = img.getRGB(x, y);
            int red = (pixel >> 16) & 0xff;
            int green = (pixel >> 8) & 0xff;
            int blue = pixel & 0xff;
            rgb[x][y][0] = red;
            rgb[x][y][1] = green;
            rgb[x][y][2] = blue;
          }
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("File read unsuccessful");
      }
    } else {
      throw new IllegalArgumentException("File format incorrect");
    }
    return new Image(width, height, rgb, false);
  }
}