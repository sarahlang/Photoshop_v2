package imageview.textual;

import java.io.IOException;

/**
 * The view interface to render the result of the image processing program.
 */
public interface SimpleImageView {

  /**
   * Render a specific message to the provided data destination.
   * @param message the message to be transmitted
   */
  void renderMessage(String message) throws IOException;

}
