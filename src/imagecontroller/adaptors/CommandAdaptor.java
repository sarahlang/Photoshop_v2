package imagecontroller.adaptors;

import java.awt.image.BufferedImage;

/**
 * An adaptor that communicates either simple or complex controller to text or interactive view.
 */
public interface CommandAdaptor {

  /**
   * Takes the next command from the controller.
   *
   * @param input the next command from the controller
   */
  void takeNextcommand(String input);

  /**
   * Getter for the next command from the controller.
   *
   * @return the next command from the controller
   */
  String getNextCommand();

  /**
   * get the message from command to transmit back to the view.
   *
   * @param command the command to output
   */
  void appendCommand(String command);

  /**
   * Refreshes the image so that the image is in its original look.
   *
   * @param image the image to refresh
   */
  void reFreshImage(BufferedImage image);

  /**
   * Cleans the board by removing all images, repainting, and revalidating.
   */
  void cleanBoard();
}
