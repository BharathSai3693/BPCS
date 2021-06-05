import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Desktop;
import java.util.*;

public class Extractor {
    public static void main(String[] args) throws Exception {
        Scanner key = new Scanner(System.in);

        System.out.print("Enter name of image file with secret payload: ");
        ImageReader extraction = new ImageReader("StegResults/" + key.next());

        List<Segment> resultHiderSegments = extraction.getHiderSegments();

        System.out.println("Complex segments found: " + resultHiderSegments.size());
        System.out.println("Converted to bytes: " + resultHiderSegments.size() * 63 / 8192 + " KB");

        FileFinder secret = new FileFinder(resultHiderSegments);
        secret.constructFiles();
    }
}
