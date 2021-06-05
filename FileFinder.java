import java.util.*;
import java.io.*;

public class FileFinder {
    private int[] bitForm;
    private List<Character> text;

    public FileFinder(List<Segment> segments) { //if type == true, then will read as text
        bitForm = new int[segments.size() * 63];

        int q = 0; //counter
        for(Segment seg : segments) {
            int[][] temp = seg.getSegArr();

            if(temp[0][0] == 1) { //conjugate again to get back
                for(int r = 0; r < 8; r++) for(int c = 0; c < 8; c++) temp[r][c] ^= ((r % 2 == 0)? ((c % 2 == 0)? 0 : 1) : ((c % 2 == 0)? 1 : 0));
            }

            for(int j = 0; j < 8; j++) for(int k = (j == 0)? 1 : 0; k < 8; k++) bitForm[q++] = temp[j][k]; //read 63 bits from each segment
        }
    }

    public void constructFiles() throws IOException {
        int fileCount = getNumOfFiles();
        System.out.println(fileCount + " files hidden.");
        int q = 8; //record current index
        for(int j = 0; j < fileCount; j++) {
            int bitsTaken = constructFile(q);
            q += bitsTaken;
        }

        System.out.println("Hidden files extracted, in directory ExtractedPayloads!");
    }

    public int constructFile(int loc) throws IOException { //also returns fileLength
        int fileLength = getLength(loc); //in bytes
        String fileName = getFileName(loc);

        int prefixLength = 40 + fileName.length() * 8; //in bits

        System.out.println("File size: " + (fileLength / 1024) + " KB");
        System.out.println("File name: " + fileName);

        byte[] byteForm = new byte[fileLength];

        for(int m = loc + prefixLength; m < loc + prefixLength + fileLength * 8; m += 8) {
            int store = 0;
            for(int n = 0; n < 8; n++) store += bitForm[m + n] << (7 - n);
            byteForm[(m - prefixLength - loc) / 8] = (byte) (store - 128);
        }

        FileOutputStream fos = new FileOutputStream("ExtractedPayloads/" + fileName);
        fos.write(byteForm);
        fos.close();

        return prefixLength + fileLength * 8;
    }

    public int getNumOfFiles() {
        int numOfFiles = 0;
        for(int m = 0; m < 8; m++) numOfFiles += bitForm[m] << (7 - m);
        return numOfFiles;
    }

    public int getLength(int startDex) { //obtains fileLength from header
        int fileLength = 0;
        for(int m = 0; m < 32; m++) fileLength += bitForm[startDex + m] << (31 - m);
        return fileLength;
    }

    public String getFileName(int startDex) { //obtains fileExtension from header
        int lengthOfName = 0;
        for(int m = 32; m < 40; m++) lengthOfName += bitForm[startDex + m] << (7 - m);

        String fileName = "";
        for(int m = 40; m < 40 + lengthOfName * 8; m += 8) {
            char store = 0;
            for(int n = 0; n < 8; n++) store += bitForm[startDex + m + n] << (7 - n);
            fileName += store;
        }
        return fileName;
    }
}
