package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.ImageModel;

import java.io.IOException;

/**
 * Sets a layer as the current layer to be worked on.
 */
public class Current implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  public Current(CommandAdaptor messageAdaptorr,ImageModel model) {
    this.messageAdaptor = messageAdaptorr;
    this.model = model;
  }

  @Override
  public void apply() throws IOException {
    String layerNum = this.messageAdaptor.getNextCommand();
    if (!isNumeric(layerNum)) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    }
    int currentLayerToSet = Integer.parseInt(layerNum);
    if (currentLayerToSet <= model.getAllLayers().size() && currentLayerToSet != 0) {
      model.changeCurrent(currentLayerToSet);
      this.messageAdaptor.appendCommand("Current Layer that "
              + "is worked on: Layer No." + currentLayerToSet);
    } else {
      this.messageAdaptor.appendCommand(currentLayerToSet + " is out of bound.");
    }
  }

  /**
   * Checks if the string is a numeric value.
   *
   * @param str the string to be checked
   * @return true if the string is numeric, false if not.
   */
  public static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}