package imagecontroller.controllers;

import imagecontroller.adaptors.CommandAdaptor;
import imagecontroller.adaptors.SimpleControllerAdaptor;
import imageview.textual.ImageTextView;
import imagemodel.model.ImageModel;
import userimagecommand.Create;
import userimagecommand.Current;
import userimagecommand.Load;
import userimagecommand.Save;
import userimagecommand.SaveAll;
import userimagecommand.Invisible;
import userimagecommand.Filter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * A simple version of the image controller that handles commands like importing, exporting,
 * filtering, layering images.
 */
public class SimpleImageController extends AbstractController {

  private ImageModel model;
  private ImageTextView view;
  private Scanner scan;
  private CommandAdaptor adaptor;

  /**
   * Constructor for the controller that passes commands from the client to the model.
   *
   * @param model the MultiImageModel that will be operated on
   * @param rd    the commands that will be read in from the users
   * @throws IllegalArgumentException if the readable is null
   */
  public SimpleImageController(ImageModel model, Readable rd,
      Appendable ap) throws IllegalArgumentException {
    if (rd == null) {
      throw new IllegalArgumentException("Readable cannot be null");
    }
    this.model = model;
    this.view = new ImageTextView(ap);
    scan = new Scanner(rd);
    this.adaptor = new SimpleControllerAdaptor(this.view);
  }

  /**
   * Take in a script file and construct the commands.
   *
   * @param fileName the script to be worked on
   */
  @Override
  public void readScript(String fileName) {
    try {
      Readable in = new FileReader(fileName);
      this.scan = new Scanner(in);
    } catch (FileNotFoundException e) {
      adaptor.appendCommand("File Not Found");
    }
  }

  @Override
  public void runProgram() {
    try {
      view.renderMessage("program initiated");
      while (scan.hasNext()) {
        String[] command = new String[2];
        String first = scan.next().toLowerCase();
        command[0] = first;
        String nextCommand;
        if (super.isAcceptableCommand(first)) {
          if (!(first.equals("#")
              || first.equals("visible")
              || first.equals("invisible")
              || first.equals("saveall"))) {
            if (scan.hasNext()) {
              nextCommand = scan.next();
              adaptor.takeNextcommand(nextCommand);
              command[1] = nextCommand;
              this.workOnImages(command);
            }
          } else {
            this.workOnImages(command);
          }
        }
      }
    } catch (Exception e) {
      System.out.println(e + " caught in creating.");
      e.printStackTrace();
    }
  }

  @Override
  public void workOnImages(String[] command) {
    String first = command[0];
    try {
      if (isAcceptableCommand(first)) {
        switch (first) {
          case "create":
            new Create(adaptor, model).apply();
            break;
          case "current":
            new Current(adaptor, model).apply();
            break;
          case "#":
            scan.nextLine();
            break;
          case "load":
            new Load(adaptor, model).apply();
            break;
          case "save":
            new Save(adaptor, model).apply();
            break;
          case "filter":
            new Filter(adaptor, model).apply();
            break;
          case "invisible":
            new Invisible(adaptor, model).apply();
            break;
          case "saveall":
            new SaveAll(adaptor, model).apply();
            break;
          default:
            break;
        }
      }
    } catch (Exception e) {
      System.out.println(e + " caught in creating.");
      e.printStackTrace();
    }
  }


}