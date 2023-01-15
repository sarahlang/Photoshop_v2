package imagemodel.filters;

import userimagecommand.Save;
import imagemodel.model.Image;

/**
 * Creating a checkerboard where every tile's color is different from the one next to it.
 */
public class CreateCheckerBoard {

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
   * check if all the inputs of createCheckerBoard are valid.
   *
   * @param sizeOfTile size of each tile
   * @param numOfTiles number of tiles in the checkerboard
   * @param rgb        red, blue, and green values of the colors in checkerboard
   * @throws IllegalArgumentException if any parameter is not a valid input
   */
  public void checkExceptions(int sizeOfTile, int numOfTiles, int[][] rgb) {
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
    Image checkerBoard = new CreateCheckerBoard()
            .createCheckerBoard(2, 36, new int[][]{new int[]{255, 0, 0}, new int[]{0, 0, 255}});
    new Save().saveToFile("res/checkerBoard", "ppm", checkerBoard);
  }
}
