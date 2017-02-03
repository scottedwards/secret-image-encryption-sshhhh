package com.scottedwards.decrypt;
import java.awt.image.*;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;

public class SteganographyDecryptor {

    static {System.setProperty("java.awt.headless", "true");}

    private BufferedImage cipherText;

    //CONSTRUCTERS

    public SteganographyDecryptor() {
        this.cipherText = null;
    }

    public SteganographyDecryptor(String imagePath) {
        setCipherText(imagePath);
    }

    /** Sets the cipher text as the passed file.
     */
    public void setCipherText(String imagePath) {
        try {
            this.cipherText = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /** Retrieves the encrypted message from the image.
     * Does this by looping through the range [16, n + 16] where n is the number denoted
     * by the first 16 bit number. It gets the last number by modding 2 (even or odd)
     * and adds it to the plainText array. It then uses the BinString class to return legible 
     * text.
     */
    public String getPlainText() {
        int width = cipherText.getWidth(), height = cipherText.getHeight();
        int plainText[] = new int[getPlainTextSize(width, height)];
        for (int j = 16; j < plainText.length + 16; j++) {
            int pixelColor = cipherText.getRGB(j % (width), (int) Math.floor(j / (width)));
            plainText[j - 16] = Math.abs(pixelColor % 2);
        }
        BinString decryptedMessage = new BinString(plainText);
        return decryptedMessage.getAscii();
    }

    /** Gets the size of the encrypted message.
     * Goes throught the first 16 pixels, getting the last bit of each RGB color value, adding
     * it to an int array to make a binary string, then converting the binary string to decimal.
     */
    private int getPlainTextSize(int width, int height) {
        int lengthBits[] = new int[16];
        int size = 0, counter = 0; 
        for (int i = 0; i < 16; i++) {
            lengthBits[i] = Math.abs(cipherText.getRGB(i % (width + 1), (int) Math.floor(i / (width + 1)))) % 2;
        }
        for (int i = 15; i >= 0; i--) size += (int) Math.pow(lengthBits[i] * 2, counter++);
        return size;
    }
}
