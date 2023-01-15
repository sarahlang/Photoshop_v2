package imagemodel.filters.colortransformation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A greyscale image is composed only of shades of grey (if the red, green and blue values are the
 * same, it is a shade of grey).
 */
public class MonoChrome extends AbstractColorTransformation {

  /**
   * Constructor for MonoChrome with a specific kernel calculation.
   */
  public MonoChrome() {
    this.kernel = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f)),
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f)),
            new ArrayList<>(Arrays.asList(0.2126f, 0.7152f, 0.0722f))
    ));
  }
//
//  /**
//   * Main class to read in file and make it monochrome.
//   *
//   * @param args from the system input
//   * @throws IllegalArgumentException when the input of the file are not read in correctly
//   */
//
//  public static void main(String[] args) throws IllegalArgumentException {
//
//    //png file read in
//    Image slothImage = new Load().importImage("res/Sloth.png");
//    Image monochromeSloth = new MonoChrome().applyTo(slothImage);
//    new Save().saveToFile("res/monochromeSloth", "png", monochromeSloth);
//
//    //ppm file read in
//    Image bearImage = new Load().importImage("res/bear.ppm");
//    Image monochromeBear = new MonoChrome().applyTo(bearImage);
//    new Save().saveToFile("res/monochromeBear", "ppm", monochromeBear);
//  }


}
