import java.util.*;
import java.io.*;

public class PayloadFileProcessor {
    private int[] bitForm;
    private List<Byte> allBytes;
    private Block[] blocks;

    public PayloadFileProcessor(String[] fileNames) throws IOException, FileNotFoundException {
        allBytes = new ArrayList<Byte>();
        allBytes.add((byte) (fileNames.length - 128)); // first byte records the number of files hidden

        System.out.println(fileNames.length + " files hidden.");

        for (String fileName : fileNames) {
            byte[] temp = buildByteFile(fileName);
            for (byte chunk : temp)
                allBytes.add(chunk);
        }

        bitify();
        blockify();
    }

    public PayloadFileProcessor() throws IOException, FileNotFoundException {
        this(new String[] { "Payload.txt" });
    }

    public byte[] buildByteFile(String fileName) throws IOException, FileNotFoundException {
        File payload = new File("Payloads/" + fileName);
        int fileLength = (int) payload.length();

        System.out.println("File size: " + (fileLength / 1024) + " KB");
        System.out.println("File name: " + fileName);

        FileInputStream fis = new FileInputStream(payload);

        byte[] byteFile = new byte[fileLength + fileName.length() + 5];
        int contentStart = buildHeader(fileLength, fileName, byteFile); // simply modifies payload byteFile header
        fis.read(byteFile, contentStart, (int) payload.length());

        return byteFile;
    }

    public int buildHeader(int fileLength, String fileName, byte[] byteForm) {
        for (int k = 0; k < 4; k++)
            byteForm[k] = (byte) (((fileLength >> (24 - k * 8)) & 0xFF) - 128);
        // reads in the length of the file

        byteForm[4] = (byte) (fileName.length() - 128); // stores length of fileName

        int j = 0;
        for (int k = 5; k < 5 + fileName.length(); k++)
            byteForm[k] = (byte) (fileName.charAt(j++) - 128);
        // reads in the name of the file

        return fileName.length() + 5; // returns where to continue storing
    }

    public void bitify() {
        int q = 0; // counter
        bitForm = new int[allBytes.size() * 8];
        for (byte val : allBytes)
            for (int k = 0; k < 8; k++)
                bitForm[q++] = ((val + 128) >> (7 - k)) % 2; // converts each char into 8 bits
    }

    public void blockify() {
        int p = 0; // counter
        blocks = new Block[(int) Math.ceil(bitForm.length / 63.0)];
        for (int j = 0; j < bitForm.length; j += 63) {
            int[] feed = new int[64];
            for (int k = 1; (j + k - 1) < bitForm.length && k < 64; k++)
                feed[k] = bitForm[j + k - 1];
            blocks[p++] = new Block(feed);
        }
    }

    public int blockLength() {
        return blocks.length;
    }

    public Block getBlock(int i) {
        return blocks[i];
    }

    public int getNumOfConjugated() {
        int count = 0;
        for (Block block : blocks)
            if (block.getConjugated())
                count++;
        return count;
    }
}
