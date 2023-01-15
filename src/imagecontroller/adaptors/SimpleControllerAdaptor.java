package imagecontroller.adaptors;

import imageview.textual.ImageTextView;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An adaptor for the simple controller to talk to the view.
 */
public class SimpleControllerAdaptor implements CommandAdaptor {
  private ImageTextView view;
  private String nextCommand;

  /**
   * Constructor for the SimpleController that takes in a scanner and a view.
   *
   * @param view a simple image view
   */
  public SimpleControllerAdaptor(ImageTextView view) {
    this.view = view;
  }

  @Override
  public void takeNextcommand(String nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public String getNextCommand() {
    return this.nextCommand;
  }

  @Override
  public void appendCommand(String command) {
    try {
      this.view.renderMessage(command);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void reFreshImage(BufferedImage img) {
    return;
  }

  @Override
  public void cleanBoard() {
    return;
  }

}
