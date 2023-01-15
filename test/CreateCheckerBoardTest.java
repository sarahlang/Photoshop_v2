import static org.junit.Assert.assertEquals;

import imagemodel.model.Image;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for CreateCheckerBoard class.
 */

public class CreateCheckerBoardTest {

  private Image checkerBoard;

  @Before
  public void init() {
    checkerBoard = new Image()
            .createCheckerBoard(1, 25, new int[][]{
                new int[]{0, 0, 0},
                new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkInvalidSizeOfTile() {
    new Image().createCheckerBoard(-1, 1, new int[][]{
        new int[]{0, 0, 0},
        new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkLowNumberOfTile() {
    new Image().createCheckerBoard(1, 1, new int[][]{
        new int[]{0, 0, 0},
        new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkSquareRootNumberOfTile() {
    new Image().createCheckerBoard(1, 3, new int[][]{
        new int[]{0, 0, 0},
        new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkWrongColorValues() {
    new Image().createCheckerBoard(1, 3, new int[][]{
        new int[]{0, 0},
        new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkOneColorValues() {
    new Image().createCheckerBoard(1, 3, new int[][]{new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkOutOfBoundHighColorValues() {
    new Image()
            .createCheckerBoard(1, 3, new int[][]{new int[]{0, 0, 0}, new int[]{256, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkOutOfBoundLowColorValues() {
    new Image()
            .createCheckerBoard(1, 3, new int[][]{new int[]{-1, 0, 0}, new int[]{255, 255, 255}});
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkInvalidNumberOfTile() {
    new Image().createCheckerBoard(1, 1, new int[][]{
        new int[]{0, 0, 0},
        new int[]{255, 255, 255}});
  }

  @Test
  public void checkSquareCheckerBoardSize() {
    assertEquals(checkerBoard.getWidth(), checkerBoard.getHeight());
    assertEquals(5, checkerBoard.getHeight());
  }

  @Test
  public void checkAlternatingColor() {
    assertEquals(checkerBoard.getRGBVal(0, 0)[0], checkerBoard.getRGBVal(2, 0)[0]);

    for (int h = 0; h < 5; h = h + 1) { // height
      for (int w = 0; w < 5; w = w + 1) { // width
        if (h % 2 == 0 && w % 2 == 0 || h % 2 == 1 && w % 2 == 1) {
          for (int i = 0; i < checkerBoard.getRGBVal(w, h).length; i++) {
            assertEquals(0, checkerBoard.getRGBVal(w, h)[i]);
          }
        } else {
          for (int i = 0; i < checkerBoard.getRGBVal(w, h).length; i++) {
            assertEquals(255, checkerBoard.getRGBVal(w, h)[i]);
          }
        }
      }
    }
  }

  @Test
  public void checkColorOutOfBound() {
    for (int y = 0; y < checkerBoard.getHeight(); y++) {
      for (int x = 0; x < checkerBoard.getWidth(); x++) {
        for (int i = 0; i < 3; i++) {
          Assert.assertTrue(
                  checkerBoard.getRGBVal(x, y)[i] >= 0 && checkerBoard.getRGBVal(x, y)[i] <= 255);
        }
      }
    }
  }
}