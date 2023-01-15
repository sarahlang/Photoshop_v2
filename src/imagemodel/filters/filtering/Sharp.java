package imagemodel.filters.filtering;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A sharpening filter that accentuates edges (the boundaries between regions of high contrast),
 * thereby giving the image a "sharper" look.
 */
public class Sharp extends AbstractFiltering {

  /**
   * Constructor for Sharp with a specific kernel calculation.
   */
  public Sharp() {
    this.kernel = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, 1 / 4f, 1 / 4f, 1 / 4f, -1 / 8f)),
            new ArrayList<>(Arrays.asList(-1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f, -1 / 8f))
    ));
  }
//
//  /**
//   * Main class to read in file and sharp it three times and output.
//   *
//   * @param args from the system input
//   * @throws IllegalArgumentException when the input of the file are not read in correctly
//   */
//
//  public static void main(String[] args) throws IllegalArgumentException {
//    //png file read in
//    Image slothImage = new Load().importImage("res/Sloth.png");
//    Image sharpedOnce = new Sharp().applyTo(slothImage);
//    new Save().saveToFile("res/slothSharpedOnce", "png", sharpedOnce);
//
//    //ppm file read in
//    Image bearImage = new Load().importImage("res/bear.ppm");
//    Image bearSharpedOnce = new Sharp().applyTo(bearImage);
//    new Save().saveToFile("res/bearSharpedOnce", "ppm", bearSharpedOnce);
//  }

}