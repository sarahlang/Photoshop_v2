package imagemodel.model;

import userimagecommand.Save;

import java.awt.image.BufferedImage;

/**
 * An image can be of PPM format, PNG format, or JPEG format. Each of the three has a image width,
 * image height, and an image rgb value.
 */
public class Image {


  private final int width;
  private final int height;
  //a 3d array that store the red, green,blue values in each grid by the width and height
  private final int[][][] rgb;
  private boolean transparency;


  /**
   * constructor for Image that takes in width, height, and color of the image instead of its file
   * name.
   *
   * @param width  width of the image
   * @param height height of the image
   * @param rgb    red, green, and blue color values of the image
   */
  public Image(int width, int height, int[][][] rgb, boolean trans) {
    this.width = width;
    this.height = height;
    this.rgb = rgb;
    this.transparency = trans;
  }


  /**
   * A convenience constructor to create an image.
   */
  public Image() {
    this.width = 0;
    this.height = 0;
    this.rgb = new int[0][0][0];
    this.transparency = false;
  }


  /**
   * Getter for the width of the image.
   *
   * @return integer value of width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Getter for the height of the image.
   *
   * @return integer value of height
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Getter for the RGB Value with the specific indication of the column and the row.
   *
   * @param width  integer value of width
   * @param height integer value of height
   * @return return an array of three containing the RGB value
   */
  public int[] getRGBVal(int width, int height) {
    return this.rgb[width][height];
  }

  /**
   * Toggles the transparency of this image.
   */
  public void setTransparency() {
    this.transparency = !this.transparency;
  }

  /**
   * Checks if this image is transparent.
   *
   * @return true if the image is transparent, false otherwise.
   */
  public boolean isTransparent() {
    return this.transparency;
  }

  /**
   * Checks if this image is the same as the given.
   *
   * @param that the given image to compare against
   * @return true if this image is the same as the given image
   */
  public boolean sameImage(Image that) {
    int width = this.width;
    int height = this.height;
    if (width == that.getWidth() && height == that.getHeight()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (this.getRGBVal(x, y).equals(that.getRGBVal(x, y))) {
            return false;
          }
        }
      }
    }
    return true;
  }


  /**
   * Creates one tile for the checkerboard.
   *
   * @param result     rgb values of the image
   * @param h          height of the image
   * @param w          width of the image
   * @param color      color of the tile
   * @param sizeOfTile size of the tile
   */
  private void createOneTile(int[][][] result, int h, int w, int[] color, int sizeOfTile) {
    for (int a = h; a < h + sizeOfTile; a++) {
      for (int b = w; b < w + sizeOfTile; b++) {
        result[a][b][0] = color[0];
        result[a][b][1] = color[1];
        result[a][b][2] = color[2];
      }
    }
  }

  /**
   * Converting an image object to a bufferimage.
   * @return buffedimage that is able to save and display on swing directly
   */
  public BufferedImage toBufferImage() {
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int red = this.getRGBVal(x, y)[0];
        int green = this.getRGBVal(x, y)[1];
        int blue = this.getRGBVal(x, y)[2];
        int pixel = 0xFF000000 + (red << 16) + (green << 8) + blue;
        img.setRGB(x, y, pixel);
      }
    }
    return img;
  }

  /**
   * check if all the inputs of createCheckerBoard are valid.
   *
   * @param sizeOfTile size of each tile
   * @param numOfTiles number of tiles in the checkerboard
   * @param rgb        red, blue, and green values of the colors in checkerboard
   * @throws IllegalArgumentException if any parameter is not a valid input
   */
  private void checkExceptions(int sizeOfTile, int numOfTiles, int[][] rgb) {
    if (numOfTiles <= 1 || Math.sqrt(numOfTiles) % 1 != 0) {
      throw new IllegalArgumentException("invalid num of tile input.");
    }
    if (sizeOfTile <= 0) {
      throw new IllegalArgumentException("invalid size of tile input.");
    }
    if (rgb.length != 2 || rgb[0].length != 3 || rgb[1].length != 3) {
      throw new IllegalArgumentException("red, blue, and green values input is not valid.");
    }
    for (int[] ints : rgb) {
      for (int anInt : ints) {
        if (anInt < 0 || anInt > 255) {
          throw new IllegalArgumentException("Color does not exist.");
        }
      }
    }
  }

  /**
   * Create a checkerboard image to be scanned into a ppm file. Since one of the parameters is the
   * number of tiles, we assumed that the checkerboard is a square so that the squares can be
   * distributed evenly.
   *
   * @return a checkerboard image that has alternating colors
   */
  public Image createCheckerBoard(int sizeOfTile, int numOfTiles, int[][] rgb) {
    this.checkExceptions(sizeOfTile, numOfTiles, rgb);
    int length = (int) (sizeOfTile * Math.sqrt(numOfTiles));
    int[][][] result = new int[length][length][3];
    for (int h = 0; h < length; h = h + sizeOfTile) { // height
      int i = h / sizeOfTile;
      for (int w = 0; w < length; w = w + sizeOfTile) { // width
        int j = w / sizeOfTile;
        if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
          this.createOneTile(result, h, w, rgb[0], sizeOfTile);
        } else {
          this.createOneTile(result, h, w, rgb[1], sizeOfTile);
        }
      }
    }
    return new Image(length, length, result, false);
  }

  /**
   * Main class to read in file and output a checkerBoard.
   *
   * @param args from the system input
   * @throws IllegalArgumentException when the input of the file are not read in correctly
   */
  public static void main(String[] args) {
    Image checkerBoard = new Image()
            .createCheckerBoard(2, 36, new int[][]{new int[]{255, 0, 0}, new int[]{0, 0, 255}});
    Save.saveToFile("res/checkerBoard", "ppm", checkerBoard);
  }

}