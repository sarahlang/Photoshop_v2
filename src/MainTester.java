import imagecontroller.controllers.ComplexImageController;
import imagecontroller.controllers.ImageController;
import imagecontroller.controllers.SimpleImageController;
import imagemodel.model.MultiImageModel;
import imageview.swing.ImageViewSwing;

import javax.swing.JFrame;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Main class to test the program.
 */
public class MainTester {

  /**
   * The main driver to make the program run.
   *
   * @param arg argument to read in
   */
  public static void main(String[] arg) {
    if (arg.length > 0) {
      if (arg[0].equals("-text")) {
        MultiImageModel model = new MultiImageModel();
        ImageController simpleControl = new SimpleImageController(model,
                new InputStreamReader(System.in), System.out);
        try {
          simpleControl.runProgram();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (arg[0].equals("-interactive")) {
        //GUI interface
        ImageViewSwing.setDefaultLookAndFeelDecorated(false);
        MultiImageModel model1 = new MultiImageModel();
        ComplexImageController controller = new ComplexImageController(model1);
        ImageViewSwing frame = new ImageViewSwing(controller);
        controller.setView(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      } else if (arg[0].equals("-script")) {
        if (arg[1] != null) {
          try {
            Readable in = new FileReader(arg[1]);
            StringBuilder out = new StringBuilder();
            MultiImageModel model = new MultiImageModel();
            ImageController a_controller = new SimpleImageController(model, in, System.out);
            a_controller.runProgram();
          } catch (FileNotFoundException e) {
            System.out.println("file not found");
          }
        }
      } else {
        System.out.println("Argument invalid");
      }
    }

  }
}
