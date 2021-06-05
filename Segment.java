import java.util.*;

public class Segment implements Chunk {
    private int row, col, layer;
    private int[][] pl; //simply stores reference

    public Segment(int r, int c, int[][] plane, int i) {
        row = r;
        col = c;
        pl = plane; //stores reference to plane array, NOT clone
        layer = i; //stores what plane level it is
    }
    
    public int[][] getSegArr() {
        int[][] temp = new int[8][8];
        for(int r = row; r < row + 8; r++) for(int c = col; c < col + 8; c++) temp[r - row][c - col] = pl[r][c];
        return temp;
    }

    public String toString() {
        int[][] temp = getSegArr();
        String result = "Position: (" + row + ", " + col + ")\r\nPlane " + layer + "\r\n";
        for(int k = 0; k < 8; k++) {
            for(int j = 0; j < 8; j++) result += temp[k][j] + " ";
            result += "\r\n";
        }
        return result;
    }

    public int getBorder() {
        int changes = 0;

        //number of row changes
        for(int r = row; r < row + 8; r++) for(int c = col + 1; c < col + 8; c++) if(pl[r][c] != pl[r][c - 1]) changes++;

        //number of column changes
        for(int c = col; c < col + 8; c++) for(int r = row + 1; r < row + 8; r++) if(pl[r][c] != pl[r - 1][c]) changes++;

        return changes;
    }

    public boolean isComplex() {
        return (getBorder() / 64.0) > 0.3;
    }

    public void replaceWith(Block data) {
        int[][] temp = data.getBlock();

        for(int r = row; r < row + 8; r++) for(int c = col; c < col + 8; c++) pl[r][c] = temp[r - row][c - col]; //replace segment section with provided block
    }
}
