public class Block implements Chunk {
    private int[][] bl;
    private boolean conjugated;

    public Block(int[] block) {
        bl = new int[8][8];
        int q = 0; //counter variable
        for(int r = 0; r < 8; r++) for(int c = 0; c < 8; c++) bl[r][c] = block[q++];

        if(!isComplex()) conjugate(); //conjugate if not complex
    }

    public String toString() {
        String result = "";
        for(int k = 0; k < 8; k++) {
            for(int j = 0; j < 8; j++) result += bl[k][j] + " ";
            result += "\r\n";
        }
        return result;
    }

    public int getBorder() {
        int changes = 0;

        //number of row changes
        for(int r = 0; r < 8; r++) for(int c = 1; c < 8; c++) if(bl[r][c] != bl[r][c - 1]) changes++;

        //number of column changes
        for(int c = 0; c < 8; c++) for(int r = 1; r < 8; r++) if(bl[r][c] != bl[r - 1][c]) changes++;

        return changes;
    }

    public boolean isComplex() {
        return (getBorder() / 64.0) > 0.3;
    }

    public void conjugate() {
        for(int r = 0; r < 8; r++) for(int c = 0; c < 8; c++) bl[r][c] ^= ((r % 2 == 0)? ((c % 2 == 0)? 0 : 1) : ((c % 2 == 0)? 1 : 0));
        //applies XOR with checkerboard pattern
        bl[0][0] = 1; //sets conjugation bit to 1
        conjugated = true;
    }

    public boolean getConjugated() {
        return conjugated;
    }

    public int[][] getBlock() {
        return bl;
    }
}
