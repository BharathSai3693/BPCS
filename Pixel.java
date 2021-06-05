import java.awt.Color;

public class Pixel {
    private int alpha, bpcRed, cgcRed, bpcGreen, cgcGreen, bpcBlue, cgcBlue;
    private int[] cgcBits, alphaBits; // from lsb to msb, from blue to red

    public Pixel(int rgba) {
        // read and convert each to cgc        
        alpha = (rgba >> 24) & 0xFF;
        alphaBits = new int[8];
        
        for (int j = 0; j < 8; j++) 
            alphaBits[j] = (alpha >> j) % 2;

        bpcRed = (rgba >> 16) & 0xFF;
        cgcRed = getCGC(bpcRed);

        bpcGreen = (rgba >> 8) & 0xFF;
        cgcGreen = getCGC(bpcGreen);

        bpcBlue = (rgba) & 0xFF;
        cgcBlue = getCGC(bpcBlue);

        cgcBits = new int[24];
        for (int j = 0; j < 24; j++) {
            if (j % 3 == 0)
                cgcBits[j] = (cgcBlue >> (j / 3)) % 2;
            else if (j % 3 == 1)
                cgcBits[j] = (cgcGreen >> (j / 3)) % 2;
            else
                cgcBits[j] = (cgcRed >> (j / 3)) % 2;
        }
        // operation shifts num j bits to the right, then masks everything except least
        // significant
        
    }

    public Pixel(int[] cgcBitsArr, int[] alphaBitsArr) {
        cgcBits = cgcBitsArr.clone();
        alphaBits = alphaBitsArr.clone();

        for (int j = 0; j < 24; j++) {
            if (j % 3 == 0)
                cgcBlue += (cgcBits[j] << (j / 3));
            else if (j % 3 == 1)
                cgcGreen += (cgcBits[j] << (j / 3));
            else
                cgcRed += (cgcBits[j] << (j / 3));
        }

        for (int j = 0; j < 8; j++)
            alpha += (alphaBits[j] << j);

        bpcRed = getBPC(cgcRed);
        bpcGreen = getBPC(cgcGreen);
        bpcBlue = getBPC(cgcBlue);
    }

    public int getBPC(int cgc) {
        int bpc = cgc;
        for (int mask = bpc >> 1; mask != 0; mask >>= 1)
            bpc ^= mask;
        return bpc;
    }

    public int getCGC(int bpc) {
        return bpc ^ (bpc >> 1);
    }

    public int getCGCBit(int dex) {
        return cgcBits[dex];
    }

    public int getAlphaBit(int dex) {
        return alphaBits[dex];
    }

    public int getRGB() {
        return (alpha << 24) | (bpcRed << 16) | (bpcGreen << 8) | (bpcBlue);
    }
}
