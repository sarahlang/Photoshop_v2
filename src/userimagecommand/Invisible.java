package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;
import java.io.IOException;

/**
 * A command to set a layer invisible.
 */
public class Invisible implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  /**
   * Constructor for the Invisible class.
   *
   * @param messageAdaptor a message adaptor to talk to view.
   * @param model           a multi image model.
   */
  public Invisible(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  @Override
  public void apply() throws IOException {
    int currentLayerToSet;
    int lastCurrent;
    //try to see if there is any current image
    try {
      currentLayerToSet = model.getCurrent();
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.appendCommand("there is no workable layer yett");
      return;
    }
    lastCurrent = 0;
    //try to see if there's any last current image so it can
    //display on the complex controller
    try {
      lastCurrent = model.getLastCurrent();
    } catch (IllegalArgumentException eLastCurrent) {
      this.messageAdaptor.cleanBoard();
    }
    this.messageAdaptor.appendCommand("layer " + currentLayerToSet + " just set invisible");
    try {
      //try to see if there is any last current image
      Image lastCurrentImg = model.getLastCurrentImage();
      this.messageAdaptor.reFreshImage(lastCurrentImg.toBufferImage());
      this.messageAdaptor
          .appendCommand("the current layer is now the last current: " + lastCurrent);
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.cleanBoard();
    }
    //make the current one invisible
    model.makeInvisible(currentLayerToSet);
  }
}