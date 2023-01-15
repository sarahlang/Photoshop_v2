package imagecontroller.adaptors;

import imageview.swing.InteractiveImageView;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * An adaptor for complex controller to talk to view.
 */
public class ComplexControllerAdaptor implements CommandAdaptor {

  private InteractiveImageView view;
  private LinkedList<String> commands;

  /**
   * Constructor for the ComplexControllerAdaptor.
   *
   * @param view the interactive view that complex controller talks to.
   */
  public ComplexControllerAdaptor(InteractiveImageView view) {
    this.view = view;
    this.commands = new LinkedList<String>();
  }

  @Override
  public void takeNextcommand(String nextCommand) {
    this.commands.addFirst(nextCommand);
  }

  @Override
  public String getNextCommand() {
    return this.commands.pop();
  }

  @Override
  public void appendCommand(String command) {
    this.view.popTextBox(command);
  }

  @Override
  public void reFreshImage(BufferedImage image) {
    this.view.reFreshImage(image);
  }

  @Override
  public void cleanBoard() {
    this.view.cleanBoard();
  }
}