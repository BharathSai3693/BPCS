import java.util.*;

public class Plane {
    private int layer;
    private int[][] plane;
    private List<Segment> segments;

    public Plane(int[][] pl, int i) {
        plane = pl.clone();

        layer = i;

        segments = new ArrayList<Segment>();

        // initializes a bunch of segments that can access the different parts of the
        // plane
        for (int r = 0; r < plane[1].length - 8; r += 8)
            for (int c = 0; c < plane[0].length - 8; c += 8)
                segments.add(new Segment(r, c, plane, layer));
    }

    public String toString() {
        String result = "Plane " + layer + "\r\n\r\nCGC Plane\r\n";
        for (int k = 0; k < plane.length; k++) {
            for (int j = 0; j < plane[0].length; j++)
                result += plane[k][j] + " ";
            result += "\r\n";
        }
        return result;
    }

    public int getBit(int r, int c) {// row r, column c, not xy coordinates
        return plane[r][c];
    }

    public int getWidth() {
        return plane[0].length;
    }

    public int getHeight() {
        return plane.length;
    }

    // number of segments
    public int size() {
        return segments.size();
    }

    public List<Segment> getAllSegments() {
        return segments;
    }

    public List<Segment> getComplexSegmentsOfPlane() {
        List<Segment> complexSegments = new ArrayList<Segment>();
        for (Segment segment : segments)
            if (segment.isComplex())
                complexSegments.add(segment);
        return complexSegments;
    }
}
