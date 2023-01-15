package imagemodel.filters.filtering;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Filter to blur the image.
 */
public class Blur extends AbstractFiltering {

  /**
   * Create a new Image that is blurred.
   */
  public Blur() {
    this.kernel = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(1 / 16f, 1 / 8f, 1 / 16f)),
            new ArrayList<>(Arrays.asList(1 / 8f, 1 / 4f, 1 / 8f)),
            new ArrayList<>(Arrays.asList(1 / 16f, 1 / 8f, 1 / 16f))
    ));
  }

//  /**
//   * Main class to read in file and blur it three times and output.
//   *
//   * @param args from the system input
//   * @throws IllegalArgumentException when the input of the file are not read in correctly
//   */
//
//  public static void main(String[] args) throws IllegalArgumentException {
//
//    //png file read in
//    Image slothImage = new Load().importImage("res/Sloth.png");
//    Image blurredOnce = new Blur().applyTo(slothImage);
//    new Save().saveToFile("res/slothBlurredOnce", "png", blurredOnce);
//
//    //ppm file read in
//    Image bearImage = new Load().importImage("res/bear.ppm");
//    new Save().saveToFile("res/bearOutPpm", "ppm", bearImage);
//    Image bearBlurredOnce = new Blur().applyTo(bearImage);
//    new Save().saveToFile("res/bearBlurredOnce", "ppm", bearBlurredOnce);
//  }
}