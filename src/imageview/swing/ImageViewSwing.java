package imageview.swing;

import imagecontroller.controllers.ImageController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 * An Graphic User Interface where the user can see an interactive view and give commands in the
 * interactive window.
 */
public class ImageViewSwing extends JFrame implements ActionListener, InteractiveImageView {

  private JPanel imagePanel;
  private JLabel fileOpenDisplay;
  private JLabel fileSaveDisplay;
  private ImageController feature;

  /**
   * A contructor for the ImageViewSwing class that sets up the look of the user interface.
   *
   * @param feature a complex image controller that will be used to understand commands.
   */
  public ImageViewSwing(ImageController feature) {
    this.feature = feature;
    setTitle("Image view");
    setSize(700, 500);

    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
    //scroll bars around this main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    // create a menubar
    JMenuBar bar = new JMenuBar();

    // create menuitems
    JMenu create = new JMenu("Create");
    JMenuItem createItem = new JMenuItem("Create Layers");
    createItem.setActionCommand("create");
    createItem.addActionListener(this);
    create.add(createItem);

    JMenu current = new JMenu("Current");
    JMenuItem currentItem = new JMenuItem("Current Layer to select");
    currentItem.setActionCommand("current");
    currentItem.addActionListener(this);
    current.add(currentItem);

    JMenu load = new JMenu("Load");
    JMenuItem loadImage = new JMenuItem("Load image");
    loadImage.setActionCommand("load");
    loadImage.addActionListener(this);
    load.add(loadImage);
    JMenuItem loadFile = new JMenuItem("load script");
    loadFile.setActionCommand("read script");
    loadFile.addActionListener(this);
    load.add(loadFile);

    JMenu filter = new JMenu("Filter");
    // create menuitems
    JMenuItem blur = new JMenuItem("Blur");
    blur.setActionCommand("blur");
    blur.addActionListener(this);

    JMenuItem sharp = new JMenuItem("Sharp");
    sharp.setActionCommand("sharp");
    sharp.addActionListener(this);

    JMenuItem monochrome = new JMenuItem("Monochrome");
    monochrome.setActionCommand("monochrome");
    monochrome.addActionListener(this);

    JMenuItem downSize = new JMenuItem("DownSize");
    downSize.setActionCommand("downsize");
    downSize.addActionListener(this);

    JMenuItem mosaic = new JMenuItem("Mosaic");
    mosaic.setActionCommand("mosaic");
    mosaic.addActionListener(this);

    JMenuItem sepia = new JMenuItem("Sepia");
    sepia.setActionCommand("sepia");
    sepia.addActionListener(this);

    filter.add(blur);
    filter.add(sharp);
    filter.add(monochrome);
    filter.add(sepia);
    filter.add(downSize);
    filter.add(mosaic);

    JMenu invisibleAction = new JMenu("Invisible");
    JMenuItem invisible = new JMenuItem("Current Layer Invisible");
    invisible.setActionCommand("invisible");
    invisible.addActionListener(this);
    invisibleAction.add(invisible);

    JMenu save = new JMenu("Save/SaveAll");
    JMenuItem saveOne = new JMenuItem("Save Current Layer");
    saveOne.setActionCommand("save");
    saveOne.addActionListener(this);

    JMenuItem saveAll = new JMenuItem("Save All");
    saveAll.setActionCommand("saveAll");
    saveAll.addActionListener(this);
    save.add(saveOne);
    save.add(saveAll);

    bar.add(create);
    bar.add(current);
    bar.add(load);
    bar.add(filter);
    bar.add(invisibleAction);
    bar.add(save);

    this.setJMenuBar(bar);

    //file open
    JPanel fileopenPanel = new JPanel();
    fileOpenDisplay = new JLabel("File path will appear here");
    fileopenPanel.add(fileOpenDisplay);

    //show an image with a scrollbar
    this.imagePanel = new JPanel();
    //a border around the panel with a caption
    this.imagePanel.setBorder(BorderFactory.createTitledBorder("showing an image"));
    this.imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
    this.imagePanel.setMaximumSize(null);
    mainPanel.add(this.imagePanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "read script":
        final JFileChooser fchooser1 = new JFileChooser(".");
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fchooser1.setFileFilter(filter1);
        int retvalue2 = fchooser1.showOpenDialog(ImageViewSwing.this);
        File f1 = null;
        if (retvalue2 == JFileChooser.APPROVE_OPTION) {
          f1 = fchooser1.getSelectedFile();
          fileOpenDisplay.setText(f1.getAbsolutePath());
          this.feature.readScript(f1.getAbsolutePath());
        }
        break;
      case "create":
        String message = JOptionPane.showInputDialog("Please enter the number of layers to create");
        this.feature.workOnImages(new String[]{"create", message});
        break;
      case "current":
        String message_current = JOptionPane
            .showInputDialog("Please enter the index of the current layer to set");
        this.feature.workOnImages(new String[]{"current", message_current});
        break;
      case "load": {
        final JFileChooser fchooser = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPEG, PNG & PPM Images", "jpeg", "ppm", "png");
        fchooser.setFileFilter(filter);
        int retvalue = fchooser.showOpenDialog(ImageViewSwing.this);
        File f = null;
        if (retvalue == JFileChooser.APPROVE_OPTION) {
          f = fchooser.getSelectedFile();
          fileOpenDisplay.setText(f.getAbsolutePath());
        }
        this.feature.workOnImages(new String[]{"load", f.getAbsolutePath()});
      }
      break;
      case "blur":
        this.feature.workOnImages(new String[]{"filter", "blur"});
        break;
      case "sharp":
        this.feature.workOnImages(new String[]{"filter", "sharp"});
        break;
      case "sepia":
        this.feature.workOnImages(new String[]{"filter", "sepia"});
        break;
      case "monochrome":
        this.feature.workOnImages(new String[]{"filter", "monochrome"});
        break;
      case "mosaic":
        String message_mosaic = JOptionPane.showInputDialog("Please input"
            + " the mosaic seed you would like to input to set");
        this.feature.workOnImages(new String[]{"filter", "mosaic", message_mosaic});
        break;
      case "downsize":
        String message_downsize_width = JOptionPane.showInputDialog("Please input "
            + "the width you would like the image to downsize to");
        String message_downsize_height = JOptionPane.showInputDialog("Please input"
            + " the height you would like the image to downsize to");
        this.feature.workOnImages(new String[]{"filter",
            "downsize",
            message_downsize_width, message_downsize_height});
        break;
      case "invisible":
        JOptionPane
            .showMessageDialog(ImageViewSwing.this, "Setting the current layer to invisible");
        this.feature.workOnImages(new String[]{"invisible"});
        break;
      case "save": {
        String message_save = JOptionPane.showInputDialog(ImageViewSwing.this,
            "Please prove the image format you desire to be saved in");
        this.feature.workOnImages(new String[]{"save", message_save});

      }
      break;
      case "saveAll":
        this.feature.workOnImages(new String[]{"saveAll"});
        break;
      default:
        break;
    }

  }

  /**
   * Refreshing the image on the panel.
   *
   * @param image the processed image.
   */
  public void reFreshImage(BufferedImage image) {
    this.imagePanel.removeAll();
    this.imagePanel.revalidate();
    this.imagePanel.repaint();

    JLabel picLabel = new JLabel(new ImageIcon(image));
    this.imagePanel.add(picLabel);
  }


  /**
   * Poping a textbox on the screen to show confirmation and warning.
   *
   * @param output the message to output.
   */
  public void popTextBox(String output) {
    JOptionPane.showMessageDialog(ImageViewSwing.this, output);
  }

  /**
   * Remove everything on the board.
   */
  public void cleanBoard() {
    this.imagePanel.removeAll();
    this.imagePanel.revalidate();
    this.imagePanel.repaint();
  }
}
