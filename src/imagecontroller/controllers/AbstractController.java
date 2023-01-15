package imagecontroller.controllers;

/**
 * An abstract class that abstract the helper methods for the complex and simple
 * controller.
 */
public abstract class AbstractController implements ImageController {

  /**
   * Checks if the input command is a valid command that can be parsed by the controller.
   *
   * @param command the given command to check.
   * @return true if the command is acceptable, false if not.
   */
  protected static boolean isAcceptableCommand(String command) {
    switch (command) {
      case "create":
        break;
      case "current":
        break;
      case "#":
        break;
      case "load":
        break;
      case "save":
        break;
      case "filter":
        break;
      case "visible":
        break;
      case "invisible":
        break;
      case "saveall":
        break;
      default:
        return false;
    }
    return true;
  }
}
