package imagecontroller.controllers;

import imagecontroller.adaptors.CommandAdaptor;
import imagecontroller.adaptors.ComplexControllerAdaptor;
import imagemodel.model.ImageModel;
import imageview.swing.InteractiveImageView;
import userimagecommand.Create;
import userimagecommand.Current;
import userimagecommand.Load;
import userimagecommand.Save;
import userimagecommand.SaveAll;
import userimagecommand.Invisible;
import userimagecommand.Filter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * A complex image controller that communicates with the model and the interactive view to parse
 * commands for a graphical user interface.
 */
public class ComplexImageController extends AbstractController {

  private final ImageModel model;
  private CommandAdaptor adaptor;

  /**
   * Constructor for the controller that passes commands from the client to the model.
   *
   * @param model the MultiImageModel that will be operated on
   * @throws IllegalArgumentException if the readable is null
   */
  public ComplexImageController(ImageModel model) throws IllegalArgumentException {
    this.model = Objects.requireNonNull(model);
  }

  /**
   * A method to load in a script of commands.
   *
   * @param fileName the script to be worked on
   */
  public void readScript(String fileName) {
    Readable in = null;
    try {
      in = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      adaptor.appendCommand("File Not Found");
      return;
    }
    Scanner scan = new Scanner(in);
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
            command[1] = nextCommand;
            this.workOnImages(command);
          }
        } else if (first.equals("#")) {
          scan.nextLine();
        } else {
          this.workOnImages(command);
        }
      }
    }
  }


  /**
   * Sets the view to interactive view and the adaptor to complex controller adaptor.
   *
   * @param view the give interactive view.
   */
  public void setView(InteractiveImageView view) {
    Objects.requireNonNull(view);
    this.adaptor = new ComplexControllerAdaptor(view);
  }

  @Override
  public void workOnImages(String[] command) {
    String first = command[0].toLowerCase();
    if (!first.equals("#")
            && !first.equals("invisible")
            && !first.equals("saveall")) {
      String next = command[1];
      if (next.equals("mosaic") || next.equals("downsize")) {
        if (next.equals("downsize")) {
          this.adaptor.takeNextcommand(command[3]);
        }
        this.adaptor.takeNextcommand(command[2]);
      }
      this.adaptor.takeNextcommand(command[1]);
    }
    try {
      switch (first) {
        case "create":
          new Create(this.adaptor, model).apply();
          break;
        case "current":
          new Current(this.adaptor, model).apply();
          break;
        case "load":
          new Load(this.adaptor, model).apply();
          break;
        case "save":
          new Save(this.adaptor, model).apply();
          break;
        case "filter":
          new Filter(this.adaptor, model).apply();
          break;
        case "invisible":
          new Invisible(this.adaptor, model).apply();
          break;
        case "saveall":
          new SaveAll(this.adaptor, model).apply();
          break;
        default:
          break;
      }
    } catch (Exception e) {
      System.out.println(e + " caught in creating.");
      e.printStackTrace();
    }
  }

  @Override
  public void runProgram() {
    return;
  }

}