package imagecontroller.controllers;

/**
 * The controller interface that manipulate the operation on to the current image or layer object.
 */
public interface ImageController {

  /**
   * Imports, exports, filters, and saves multiple layers of image according to user command.
   *
   * @param command the list of commands to parse.
   */
  void workOnImages(String[] command);

  /**
   * Runs the program for the Complex Image controller.
   */
  void runProgram();

  /**
   * Take in a script file and construct the commands.
   * @param fileName the script to be worked on
   */
  void readScript(String fileName);
}