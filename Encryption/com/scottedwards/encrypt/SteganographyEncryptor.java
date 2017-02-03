package com.scottedwards.encrypt;
import java.lang.IllegalStateException;
import java.awt.image.*;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;

public class SteganographyEncryptor {

    //So icons don't pop up whilst running
    static {System.setProperty("java.awt.headless", "true");}

    private int[] plainText;
    private int[] lengthBits;
    private BufferedImage cipherHolder;
    
    //MANY CONSTRUCTORS FROM NOW ON
    public SteganographyEncryptor() {
        this.plainText = null;
        this.cipherHolder = null;
        this.lengthBits = new int[32];
    }

    public SteganographyEncryptor(String plainText) {
        this();
        setPlainText(plainText);
    }

    public SteganographyEncryptor(int[] plainText) {
        this();
        setPlainText(plainText);
    }

    public SteganographyEncryptor(File image) {
        this();
        setCipherHolder(image);
    }

    public SteganographyEncryptor(int[] plainText, File image) {
        this();
        setCipherHolder(image);
        setPlainText(plainText);
    }

    public void setPlainText(int[] plainText) {
        this.plainText = new int[plainText.length];
        this.lengthBits = convertToBinary(plainText.length);
        for (int i = 0; i < plainText.length; i++) this.plainText[i] = plainText[i];
    }

    public void setPlainText(String plainText) {
        BinString text = new BinString(plainText);
        setPlainText(text.getBinary());
    }

    public void setCipherHolder(File image) {
        cipherHolder = null;
        try {
            cipherHolder = ImageIO.read(image);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /** Returns an image that has the message encrypted withing.
     * Does this by looping through the range [16, n + 16] pixels where n is the length of the plain text
     * (the first 16 bits is to signify the length of the message), and changing the last bit of each pixel's
     * RGB value to the current bit being written to the picture.
     * 
     */
    public BufferedImage getCipherText() throws IllegalStateException {
        BufferedImage cipherText = deepCopy(cipherHolder);
        if (plainText != null && cipherHolder != null) {
            if(isImageBigEnough()) {
                //set the variables
                int width = cipherHolder.getWidth(), height = cipherHolder.getHeight();
                int bit = 0;

                /*JUST TESTING NEW METHOD */
                for (int j = 0; j < plainText.length + 16; j++) {
                    try {
                        long pixelColor = cipherHolder.getRGB(j % (width), (int) Math.floor(j / (width)));
                        long dataBit = (bit < 16) ? lengthBits[bit++] : plainText[bit++ - 16];
                        if (Math.abs(pixelColor % 2) != dataBit) pixelColor++;
                        cipherText.setRGB(j % (width), (int) Math.floor(j / (width)), (int) pixelColor);
                    } catch (Exception e) {
                        System.out.println("Error at: ("+j % (width)+", "+(int) Math.floor(j / (width))+")");
                    }
                }
            } else {
                throw new IllegalStateException("The image selected is not big enought to conatin the message!");
            }
        } else {
            throw new IllegalStateException("Plain text or cipher holder not set!");
        }
        return cipherText;
    }

    /**Converts a decimal number to an array of integers that represent the number in binary.
     */
    private static int[] convertToBinary(int num) {
        int[] binary = new int[16];
        int pow = (int) Math.pow(2, 15);
        for (int i = 15; i > 0; i--) {
            pow -= Math.pow(2, i - 1);
            if (num >= pow) {
                binary[16 - i] = 1;    
                num -= pow;
            } else {
                binary[16 - i] = 0; 
            }
        } 
        return binary;
    }

    /** Provides a way to copy an image to a new element without it referencing the old one.
     * Might not be needed but it could be usefull if you're using the same object multiple times with different messages.
     */
    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private boolean isImageBigEnough() {
        return (this.plainText.length + 16 > cipherHolder.getWidth() * cipherHolder.getHeight()) ? false : true;
    }
}
