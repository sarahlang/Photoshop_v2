package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.filters.Downsizing;
import imagemodel.filters.Mosaic;
import imagemodel.filters.colortransformation.MonoChrome;
import imagemodel.filters.colortransformation.Sepia;
import imagemodel.filters.filtering.Blur;
import imagemodel.filters.filtering.Sharp;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;

import java.io.IOException;

/**
 * Apply filter onto the image on the current layer.
 */
public class Filter implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  /**
   * Filtering an image to be shown in the interactive view.
   *
   * @param messageAdaptor a command adaptor.
   * @param model          an image model.
   */
  public Filter(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  @Override
  public void apply() throws IOException {
    Image currentImage;
    try {
      currentImage = model.getCurrentImage();
    } catch (IllegalArgumentException e) {
      this.messageAdaptor.appendCommand("there is no workable layer yet");
      return;
    }
    String filterToApply = this.messageAdaptor.getNextCommand();
    if (filterToApply.equals("Input again")) {
      this.messageAdaptor.appendCommand("Input again");
      return;
    }
    if (currentImage.isTransparent()) {
      this.messageAdaptor.appendCommand("this layer is transparent, cannot work on this layer");
      return;
    }
    if (currentImage.getWidth() == 0) {
      this.messageAdaptor.appendCommand("there is no image to work on yet");
      return;
    }
    Image image;
    switch (filterToApply) {
      case "blur":
        image = new Blur().applyTo(currentImage);
        model.setCurrent(image);
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      case "sharp":
        image = new Sharp().applyTo(currentImage);
        model.setCurrent(image);
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      case "monochrome":
        image = new MonoChrome().applyTo(currentImage);
        model.setCurrent(image);
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      case "sepia":
        image = new Sepia().applyTo(currentImage);
        model.setCurrent(image);
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      case "mosaic":
        image = new Mosaic(Integer.parseInt(
            this.messageAdaptor.getNextCommand())).applyTo(currentImage);
        model.setCurrent(image);
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      case "downsize":
        int inputHeight = Integer.parseInt(this.messageAdaptor.getNextCommand());
        int inputWidth = Integer.parseInt(this.messageAdaptor.getNextCommand());
        try {
          image = new Downsizing(inputWidth, inputHeight).applyTo(currentImage);
          model.setCurrent(image);
        } catch (IllegalArgumentException ex) {
          this.messageAdaptor.appendCommand("Can not downsize to width"
              + " and height bigger than current \r\n" + "current height: "
              + model.getCurrentImage().getHeight()
              + "current width: " + model.getCurrentImage().getWidth());
        }
        this.messageAdaptor.appendCommand(filterToApply + " filter successfully set");
        break;
      default:
        return;
    }
    this.messageAdaptor.reFreshImage(model.getCurrentImage().toBufferImage());
  }
}