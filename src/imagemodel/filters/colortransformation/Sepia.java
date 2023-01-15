package imagemodel.filters.colortransformation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Converts a normal color image into a sepia-toned image.
 */
public class Sepia extends AbstractColorTransformation {

  /**
   * Constructor for Sepia with a specific kernel calculation.
   */
  public Sepia() {
    this.kernel = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(0.393f, 0.769f, 0.189f)),
            new ArrayList<>(Arrays.asList(0.349f, 0.686f, 0.168f)),
            new ArrayList<>(Arrays.asList(0.272f, 0.534f, 0.131f))
    ));
  }

//  /**
//   * Main class to read in file and make it sepia.
//   *
//   * @param args from the system input
//   * @throws IllegalArgumentException when the input of the file are not read in correctly
//   */
//
//  public static void main(String[] args) {
//    //png file read in
//    Image slothImage = new Load().importImage("res/Sloth.png");
//    Image sepiaSloth = new Sepia().applyTo(slothImage);
//    new Save().saveToFile("res/sepiaSloth", "png", sepiaSloth);
//
//    //ppm file read in
//    Image bearImage = new Load().importImage("res/bear.ppm");
//    Image sepiaBear = new Sepia().applyTo(bearImage);
//    new Save().saveToFile("res/sepiaBear", "ppm", sepiaBear);
//
//  }

}