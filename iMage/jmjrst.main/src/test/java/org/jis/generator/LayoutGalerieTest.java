package org.jis.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LayoutGalerieTest {
	
	private LayoutGalerie galerieUnderTest;
	
	private static File fromFile;
	private File toFile; 
	
	private Generator generator;
	private BufferedImage testImage;
	private String imageName;
	private static final String IMAGE_FILE = "/image.jpg";
	
	
	/**
	 * question a2 c)
	 * Test method for {@link org.jis.generator.LayoutGalerie#copyFile(File, File)}.
	 */
	@Test
	public final void testCopyFile() throws URISyntaxException {
		
		galerieUnderTest = new LayoutGalerie(null, null);
		
		try {
			final File resourceFolder = new File(this.getClass().getResource(File.separator).toURI());
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			
			byte[] array = new byte[10];
			new Random().nextBytes(array);
			String randomString = new String(array);
		 			 
			fromFile.createNewFile();
			Path fromPath = FileSystems.getDefault().getPath(fromFile.getPath());
			Files.writeString(fromPath, randomString);
			 
			galerieUnderTest.copyFile(fromFile, toFile);
			 
			assertTrue(toFile.exists());
			 
			Path toPath = FileSystems.getDefault().getPath(toFile.getPath());
			String contents = Files.readString(toPath);
			 
		
			assertEquals(randomString, contents);
		 }
		 catch (IOException | URISyntaxException e) {
			fail();
		 }
		fromFile.delete();
		toFile.delete();
	}
	/**
	 * question a2 d) 
	 * @throws URISyntaxException, FileNotFoundException, IOException
	 */
	@Test(expected = FileNotFoundException.class)
	public final void testCopyFile2() throws URISyntaxException, FileNotFoundException, IOException {
		
		galerieUnderTest = new LayoutGalerie(null, null);
			final File resourceFolder = new File("/mywork/work.txt");
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			
			galerieUnderTest.copyFile(fromFile, toFile);
			
		fromFile.delete();
		toFile.delete();
	}
	/**
	 * question a2 d):to find out if the file is just catalog.
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test(expected = FileNotFoundException.class)
	public final void testCopyFile3() throws URISyntaxException, FileNotFoundException, IOException {
		
		    galerieUnderTest = new LayoutGalerie(null, null);
			final File resourceFolder = new File("/Users/apple/Desktop/swt2/iMage/jmjrst.main/src/test/resources/");
			fromFile = new File(resourceFolder, "from");
			toFile = new File(resourceFolder, "to");
			//use isDirectory().
            if(fromFile.isDirectory()) {
	            throw new FileNotFoundException();
             }
			galerieUnderTest.copyFile(fromFile, toFile);
			
		fromFile.delete();
		toFile.delete();
	}
}
	