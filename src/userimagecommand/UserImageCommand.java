package userimagecommand;

import java.io.IOException;

/**
 * Handles the different commands that the user might give. This includes creating layers, setting
 * current layer, loading current layer image, filtering current layer image, making a layer
 * invisible, saving current layer image, and saving all layers of images.
 */
public interface UserImageCommand {

  /**
   * Applies a certain operation onto a multi image model.
   *
   */
  public void apply() throws IOException;
}
