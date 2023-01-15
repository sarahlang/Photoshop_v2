package imageview.textual;

import java.io.IOException;
import java.util.Objects;

/**
 * This is the view class that display image processing program in text.
 */
public class ImageTextView implements SimpleImageView {
  private final Appendable out;

  /**
   * the default constructor for image text view.
   *
   * @param out the appendable use to render message
   */
  public ImageTextView(Appendable out) {
    this.out = out;
  }

  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  @Override
  public void renderMessage(String message) throws IOException {
    Objects.requireNonNull(message);
    this.out.append(message + "\r\n");
  }

}
