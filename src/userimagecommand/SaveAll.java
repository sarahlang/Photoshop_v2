package userimagecommand;

import imagecontroller.adaptors.CommandAdaptor;
import imagemodel.model.Image;
import imagemodel.model.ImageModel;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * Saves all layers of images.
 */
public class SaveAll implements UserImageCommand {

  private CommandAdaptor messageAdaptor;
  private ImageModel model;

  /**
   * Saving all images.
   *
   * @param messageAdaptor a command adaptor.
   * @param model          an image model.
   */
  public SaveAll(CommandAdaptor messageAdaptor, ImageModel model) {
    this.messageAdaptor = messageAdaptor;
    this.model = model;
  }

  /**
   * A convenience constructor used to save all the layers of images.
   */
  public SaveAll() {
    this.messageAdaptor = null;
  }

  @Override
  public void apply() throws IOException {
    try {
      String timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date());
      String path = "res/" + timeStamp + "_imgfolder";
      //Creating a File object
      File file = new File(path);
      int filecount = 1;
      while (file.exists()) {
        timeStamp = new SimpleDateFormat("MMddHHmm").format(new java.util.Date()) + "_" + String
            .valueOf(filecount);
        path = "res/" + timeStamp + "_imgfolder";
        file = new File(path);
        filecount += 1;
      }
      //Creating the directory
      boolean bool = file.mkdir();
      if (bool) {
        this.messageAdaptor.appendCommand(path + " Directory created successfully");
      } else {
        this.messageAdaptor.appendCommand("Sorry couldn’t create specified directory");
      }
      PrintWriter outfile = new PrintWriter(path + "/" + timeStamp + "_layer.txt");
      this.messageAdaptor.appendCommand("Writing out to file: " + timeStamp + ".txt");
      int count = 0;
      for (Image img : model.getAllLayers()) {
        if (!img.isTransparent() && img.getWidth() > 0) {
          Save.saveToFile(path + "/image" + String.valueOf(count), "png", img);
          outfile.println(
              "Image Layer. " + count + ":" + "image" + String.valueOf(count + 1) + ".png");
          count++;
          this.messageAdaptor.appendCommand("Writing out to file: " + path
              + "/image" + String.valueOf(count) + ".png");
        }
      }
      if (count == 0) {
        this.messageAdaptor.appendCommand("There is no image saved into the folder");
      }
      outfile.close();
    } catch (Exception e) {
      this.messageAdaptor.appendCommand(e + " caught in creating.");
      e.printStackTrace();
    }
  }
}