import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Desktop;
import java.util.*;

public class Hider {
    public static void main(String[] args) throws Exception {
        Scanner key = new Scanner(System.in);

        System.out.print("Enter name of vessel image: ");
        ImageReader vessel = new ImageReader("Vessels/" + key.next());
        System.out.println("Vessel processed.");

        List<Segment> vesselHiderSegments = vessel.getHiderSegments();
        //now list of complex segments from ALL planes has been constructed
        System.out.println("Complex segments available: " + vesselHiderSegments.size());
        System.out.println("Converted to bytes: " + vesselHiderSegments.size() * 63 / 8192 + " KB");

        System.out.print("Enter names of payload files (separated by semicolons): ");
        PayloadFileProcessor payload = new PayloadFileProcessor(key.next().split(";"));

        System.out.println("Payload processed.");
        System.out.println("Total # of blocks: " + payload.blockLength());
        System.out.println("Converted to bytes: " + payload.blockLength() * 63 / 8192 + " KB");
        System.out.println(payload.getNumOfConjugated() + " blocks needed to be conjugated.");

        if(payload.blockLength() > vesselHiderSegments.size()) throw new Exception("Payload too big!");

        for(int j = 0; j < payload.blockLength(); j++) vesselHiderSegments.get(j).replaceWith(payload.getBlock(j));
        //replace complex segments in order with data blocks that are already conjugated or left the same
        System.out.println("Data blocks hidden.");

        System.out.print("Enter name of result image: ");
        StegResultProcessor result = new StegResultProcessor(key.next());

        result.processPlanes(vessel.getRGBPlanes(), vessel.getAlphaPlanes()); //planes have already been modified
        result.constructImage();

        System.out.println("Result image generated!");
    }
}
