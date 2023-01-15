package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;

import java.io.IOException;

/**
 * Create however many layers the user need for this program, once it is created, it cannot be
 * reverted. For example, "create layer 4" creates all 4 layers.
 */
public class Create implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  public Create(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  /**
   * Applying the command instruction onto the layers within the model.
   */
  @Override
  public void apply() throws IOException {
    String layerNum;
    String nextCommand = this.messageAdaptor.getNextCommand();
    if (nextCommand.equals("Input again")) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    } else {
      layerNum = nextCommand;
    }
    try {
      Integer.parseInt(layerNum);
    } catch (NumberFormatException e) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    }
    int originalSize = model.getAllLayers().size();
    for (int i = 0; i < Integer.parseInt(layerNum); i++) {
      model.getAllLayers().add(new Image());
    }
    this.messageAdaptor.appendCommand("Originally layer count: "
            + originalSize
            + "\r\n"
            + layerNum
            + " layers created"
            + "\r\n"
            + "total size now: " + model.getAllLayers().size());
  }


}