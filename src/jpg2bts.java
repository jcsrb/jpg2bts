import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

public class jpg2bts {
	final static int MAX_ZEILE = 576;
	final static int MAX_SPALTE = 768;
	final static int PIXEL_GESAMT = 442368;
	final static String TEMP_FILENAME = "temp.jpg";	
	static String input_name;
	static byte[] output = new byte[PIXEL_GESAMT];

	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			try {
				input_name = args[0];
				scale(args[0], MAX_SPALTE, MAX_ZEILE, TEMP_FILENAME);				
				read();
				write();
				 new File(TEMP_FILENAME).delete(); // delete temp file
			} catch (Exception e) {
				System.out.println(e);
			}
		} else
			System.out.println("Usage: java jpg2bts input \n");

	}

	// based on http://www.dreamincode.net/forums/showtopic59989.htm
	private static void read() throws Exception {
		File file = new File(TEMP_FILENAME);
		BufferedImage input = ImageIO.read(file);						
		for (int x = 0; x < MAX_SPALTE; x++) {
			for (int y = 0; y < MAX_ZEILE ; y++) {
				Color c = new Color(input.getRGB(x, y));
				//Grayscale add together 
				//30% of the red value, 
				//59% of the green value,
				//11% of the blue value
				//via http://en.wikipedia.org/wiki/Grayscale 
				output[y*MAX_SPALTE+x] = (byte)(c.getRed()*.30+c.getGreen()*.59+c.getBlue()*.11);				
			}
		}
		
	}

	// based on
	// http://www.java2s.com/Code/Java/File-Input-Output/Writesomedatainbinary.htm
	static void write() throws Exception {
		String FILENAME = input_name.substring(0, input_name.length()-3)+"bts";
		DataOutputStream os = new DataOutputStream(new FileOutputStream(
				FILENAME));

		os.write(output);
		os.close();
	}

	// based on
	// http://www.velocityreviews.com/forums/t148931-how-to-resize-a-jpg-image-file.html
	static void scale(String srcFile, int destWidth, int destHeight,
			String destFile) throws Exception {
		BufferedImage src = ImageIO.read(new File(srcFile));
		BufferedImage dest = new BufferedImage(destWidth, destHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = dest.createGraphics();
		AffineTransform at = AffineTransform.getScaleInstance(
				(double) destWidth / src.getWidth(), (double) destHeight
						/ src.getHeight());
		g.drawRenderedImage(src, at);
		ImageIO.write(dest, "JPG", new File(destFile));
	}

}
