package org.jis.generator;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;


public class GeneratorTest {

	private Generator generator;

	  private int imageHeight, imageWidth;
	  private static final File TEST1 = new File("target/test");
	  private static final String imageAdr= "/image.jpg";
	  private String imageName;
	  /**
	   * Input for test cases
	   */
	  private BufferedImage testImage;
	  /**
	   * Metadata for saving the image
	   */
	  private IIOMetadata imeta;
	  /**
	   * output from test cases
	   */
	  private BufferedImage TestResult;

	  /**
	   * Aufgabe 2 h) Teil 1: to make sure the output result is empty or existed
	   */
	  @BeforeClass
	  public static void beforeClass() {
	    if (TEST1.exists()) {
	      for (File f : TEST1.listFiles()) {
	        f.delete();
	      }
	    } else {
	      TEST1.mkdirs();
	    }
	  }

	  
	  /**
	   * Aufgabe 2 h) Teil 2: Automatisches Speichern von testImage.
	   */
	  @After
	  public void tearDown() {
	    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd_HH.mm.ss.SSS");
	    String time = sdf.format(new Date());

	    File outputFile = new File(
	        MessageFormat.format("{0}/{1}_rotated_{2}.jpg", TEST1, imageName, time));

	    if (this.TestResult != null) {
	      try (FileOutputStream fos = new FileOutputStream(outputFile);
	           ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
	        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
	        writer.setOutput(ios);

	        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
	        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // mode explicit necessary

	        // set JPEG Quality
	        iwparam.setCompressionQuality(1f);
	        writer.write(this.imeta, new IIOImage(this.TestResult, null, null), iwparam);
	        writer.dispose();
	      } catch (IOException e) {
	        fail();
	      }
	    }
	  }

	  /**
	   * Aufgabe 2 d) Teil 1
	   */
	  @Test
	  public void testRotateImage_RotateImage0() {
	    this.TestResult = this.generator.rotateImage(this.testImage, 0);

	    assertTrue(imageEquals(this.testImage, this.TestResult));
	  }

	  /**
	   * Aufgabe 2 d) Teil 2
	   */
	  @Test
	  public void testRotateImage_RotateNull0() {
	    this.TestResult = this.generator.rotateImage(null, 0);

	    assertNull(this.TestResult);
	  }

	  /**
	   * Aufgabe 2 e)
	   */
	  @Test(expected = IllegalArgumentException.class)
	  public void testRotateImage_Rotate042() {
	    this.generator.rotateImage(this.testImage, 0.42);
	  }

	  /**
	   * Aufgabe 2 f) Teil 1
	   */
	  @Test
	  public void testRotateImage_Rotate90() {
	    this.TestResult = this.generator.rotateImage(this.testImage, Generator.ROTATE_90);

	    assertEquals(this.testImage.getHeight(), this.TestResult.getWidth());
	    assertEquals(this.testImage.getWidth(), this.TestResult.getHeight());

	    for (int i = 0; i < this.imageHeight; i++) {
	      for (int j = 0; j < this.imageWidth; j++) {
	        assertEquals(this.testImage.getRGB(j, i), this.TestResult.getRGB(this.imageHeight - 1 - i, j));
	      }
	    }
	  }

	  /**
	   * Aufgabe 2 f) Teil 2
	   */
	  @Test
	  public void testRotateImage_Rotate270() {
	    this.TestResult = this.generator.rotateImage(this.testImage, Generator.ROTATE_270);

	    assertEquals(this.testImage.getHeight(), this.TestResult.getWidth());
	    assertEquals(this.testImage.getWidth(), this.TestResult.getHeight());

	    for (int i = 0; i < this.imageHeight; i++) {
	      for (int j = 0; j < this.imageWidth; j++) {
	        assertEquals(this.testImage.getRGB(j, i), this.TestResult.getRGB(i, this.imageWidth - 1 - j));
	      }
	    }
	  }

	  /**
	   * Aufgabe 2 g).1
	   */
	  @Test
	  public void testRotateImage_RotateM90() {
	    this.TestResult = this.generator.rotateImage(this.testImage, Math.toRadians(-90));

	    assertEquals(this.testImage.getHeight(), this.TestResult.getWidth());
	    assertEquals(this.testImage.getWidth(), this.TestResult.getHeight());

	    for (int i = 0; i < this.imageHeight; i++) {
	      for (int j = 0; j < this.imageWidth; j++) {
	        assertEquals(this.testImage.getRGB(j, i), this.TestResult.getRGB(i, this.imageWidth - 1 - j));
	      }
	    }
	  }

	  /**
	   * Aufgabe 2 g).2
	   */
	  @Test
	  public void testRotateImage_RotateM270() {
	    this.TestResult = this.generator.rotateImage(this.testImage, Math.toRadians(-270));

	    assertEquals(this.testImage.getHeight(), this.TestResult.getWidth());
	    assertEquals(this.testImage.getWidth(), this.TestResult.getHeight());

	    for (int i = 0; i < this.imageHeight; i++) {
	      for (int j = 0; j < this.imageWidth; j++) {
	        assertEquals(this.testImage.getRGB(j, i), this.TestResult.getRGB(this.imageHeight - 1 - i, j));
	      }
	    }
	  }

	  /**
	   * Check if two images are identical - pixel wise.
	   * 
	   * @param expected
	   *          the expected image
	   * @param actual
	   *          the actual image
	   * @return true if images are equal, false otherwise.
	   */
	  protected static boolean imageEquals(BufferedImage expected, BufferedImage actual) {
	    if (expected == null || actual == null) {
	      return false;
	    }

	    if (expected.getHeight() != actual.getHeight()) {
	      return false;
	    }

	    if (expected.getWidth() != actual.getWidth()) {
	      return false;
	    }

	    for (int i = 0; i < expected.getHeight(); i++) {
	      for (int j = 0; j < expected.getWidth(); j++) {
	        if (expected.getRGB(j, i) != actual.getRGB(j, i)) {
	          return false;
	        }
	      }
	    }

	    return true;
	  }

	}


