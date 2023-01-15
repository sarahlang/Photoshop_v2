# Image transformer: A Image filtering application

In this application, we are offering image processing application that is able to accept multiple png, jpeg, ppm files, apply filter such as blur, sharp, monochrome and sepia. In additionally user is able to export images as layers in one separate folders in the png format and individual image any format they desire

# Using this program
Navigate to the MainTester.java, edit the configuration to select the argument to be either "-text", "-interactive" or "-script example_scrip_file_2.txt" to see the subsequent output of the program. Further script description can be found on USEME.

# class layout 

The interface included in this application is in following:

| Interface Name | General Purpose |
| ------------- |----------------------------|
| ImageModel.java  | An image model that manages image object. |
| ImageFilterModel.java | The Interface that apply the filters onto the image. |
| ImageController | The controller interface that manipulate the operation on to the current image or layer object. |
| CommandAdapator | An adaptor that communicates either simple or complex controller to text or interactive view.|
| UserImageCommand.java | The utility Interface that work with the controller to initiate command from the users. |
| ImageView | The view interface to render the result of the image processing program. |

#### ImageModel

MultiImageModel implements ImageModel and manage multiple images within its layers.
Image class give user ability to read into the specs of the image such as width height and rgb values.
| Class Name | General Purpose |
| ---------- |---------------- |
| MultiImageModel.java | A class that manages multiple layers of images that contains a current layer. If the current layer is 0, that means the current layer has not been set; otherwise the current number is the number of layer that is the current layer|

#### ImageFilterModel

*AbstractImageFilter* implements ImageFilterModel to supply helper functions that shared with all the subclass supplying filter under

In such, two abstract class inherit the *AbstractImageFilter* Class
| Abstract class Name | General Purpose | Interface inherited from |
| ------------- |----------------------------| ----------------------|
| AbstractFiltering.java | Apply blurring and sharpening filter onto the image | AbstractImageFilter.java |
| AbstractColorTransformation.java |  A color transformation modifies the color of a pixel based on its own color | AbstractImageFilter.java |

Under the two abstract class, there are four classes inherit them to create different filters for different purposes
| Class Name | General Purpose | Abstract class inherit from |
| ---------- |---------------- | ----------------------------|
| MonoChrome.java | Create Grey Scale filter | AbstractColorTransformation.java |
| Sepia.java |  Create Sepia filter | AbstractColorTransformation.java |
| Blur.java | Blurring the Image | AbstractFiltering.java |
| Sharp.java|  Sharping the Image | AbstractFiltering.java |

In additionally for the extra credit, we have implemented **Mosaic** and **Downsizing** class that extends from the AbstractImageFilter. To implement them, we did not change any of our previous design to do so. 
| Class Name | General Purpose | Abstract class inherit from |
| ---------- |---------------- | ----------------------------|
| Downsizing.java |  A filter to downsize the original image according to the height and width of user input. | AbstractImageFilter.java |
| Mosaic.java |  A filter to make a mosaic version of the given image. | AbstractImageFilter.java |

### ImageController

Under the image controller interface we implements simpleImageController which is in charge of the accpting command from outside and communicate langauge to the multiimage model. The controller interface and two classes that implemented from, complexController which in charge of taking command from the GUI and simpleController which is in charge of taking command from the textual view.
| Class Name | General Purpose |
| ---------- |---------------- |
| ComplexImageController.java | A complex image controller that communicates with the model and the interactive view to parse commands for a graphical user interface.|
| SimpleImageController.java | A simple version of the image controller that handles commands like importing, exporting filtering, layering image from the textual view|


#### UserImageCommand

In the userImageCommand interface we have various utility class to manage different kinds of user command.
| Class Name | General Purpose |
| ---------- |---------------- |
| Create .java | Create however many layers the user need for this program | 
| Current.java |  Sets a layer as the current layer to be worked on. |
| Load.java | Loads an image onto the current layer. |
| Filter.java|  Apply filter onto the image on the current layer, including the blur sharp mosiac downsizing sepia and monochrome. |
| Invisible.java| A command to set an layer invisible. |
| Save.java| Saves the current layer of image in res/ folder. |
| SaveAll.java| Saves all layers of images. |

#### CommandAdaptor
CommandAdaptor is an interface that we have designed to adjust the change needed from supporting both a GUI interface and textual interface.
| Class Name | General Purpose |
| ---------- |---------------- |
| ComplexControllerAdaptor.java | An adaptor for complex controller to talk to the GUI view.|
| SimpleControllerAdaptor.java | An adaptor for the simple controller to talk to the view.|

### ImageView
ImageTextView implements ImageView and is able to render the responses from the program.
ImageViewSwing implements ActionListener, InteractiveImageView and extends JFrame to support GUI operation.

## Design Change and justifiation
For the last version, we added the support for an user friendly interactive interface GUI to support all the opration we supported with textual view in the previous version. To adapt to this change, we implemented InteractiveImageView interface to design the GUI and also designed the adaptor interface to accept command from readable in the textual view as well as accepting array of string from the GUI when an action is triggered. 

## Citation

Bear. (2021, April 12). [Png]. Https://Www.Kickassfacts.Com/25-Kickass-Random-Facts-List-619/. https://www.kickassfacts.com/wp-content/uploads/2021/04/bear.jpgMaraschiello, D. (2019, June 10). 

Crocodile. (n.d.). [JPG]. Stockland.Com. https://www.stockland.com.au/~/media/shopping-centre/common/everyday-ideas/kids/crocodile/0518_stocklandnational_crocodiles_900x6753.ashx

Sloth [Png]. Wikipedia.Org. https://www.google.com/imgres?imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fb%2Fbe%2FBicho-pregui%25C3%25A7a_3.jpg&imgrefurl=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FSloth&tbnid=qDFUTTuQIunkBM&vet=12ahUKEwjF4IPcsovxAhWECd8KHY8jCNEQMygAegUIARDXAQ..i&docid=Cql23qqKPWLqnM&w=3000&h=2000&q=sloth&ved=2ahUKEwjF4IPcsovxAhWECd8KHY8jCNEQMygAegUIARDXAQ