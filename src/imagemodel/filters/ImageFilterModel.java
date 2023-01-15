package imagemodel.filters;


import imagemodel.model.Image;
import java.util.List;


/**
 * The Interface that apply the filters onto the image.
 */
public interface ImageFilterModel {

  /**
   * Get the Kernal from the specific filter model.
   *
   * @return kernal used for the specific model.
   */
  List<List<Float>> getKernel();

  /**
   * Applies a type of filter to the image. Apply works differently for different types of filters
   * (color transformation of filtering).
   */
  Image applyTo(Image image);

  /**
   * Ensure that the resulting image can be properly saved and displayed and not beyond their
   * range.
   *
   * @param transformedRGB an array of red, green, and blue values of one pixel of the new image
   */
  void clamp(int[] transformedRGB);
}
