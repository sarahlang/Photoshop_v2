package imageview.swing;

import java.awt.image.BufferedImage;

/**
 * An interactive image view to create a graphical user interface.
 */
public interface InteractiveImageView {

  /**
   * Creates a message dialogue.
   *
   * @param output the message to output.
   */
  void popTextBox(String output);

  /**
   * Refreshes the image displayed to the processed image.
   *
   * @param image the processed image.
   */
  void reFreshImage(BufferedImage image);

  /**
   * Cleans up the board, removing all images.
   */
  void cleanBoard();
}
