package com.scottedwards.encrypt;
import java.awt.image.*;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Encrypt {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File targetImage;
        System.out.println("Enter the file name of the picture you want to use: ");
        do {
            targetImage = new File(sc.nextLine()); 
            if (!targetImage.exists()) System.out.println("Image does not exist! Please try again.");
        } while (!targetImage.exists());

        System.out.println("Enter the text you want to encrypt into the image: ");
        String message = sc.nextLine();

        try {
            System.out.println("Creating new encryption class...");
            SteganographyEncryptor encrypt = new SteganographyEncryptor();
            encrypt.setCipherHolder(targetImage);
            encrypt.setPlainText(message);

            System.out.println("Encrypting message...");
            BufferedImage image = encrypt.getCipherText();

            // retrieve image
            File outputfile = new File(targetImage.getName().substring(0, targetImage.getName().length() - 4)+"-enc.png");
            System.out.println("Writing encrypted image to -> " + outputfile);
            ImageIO.write(image, "png", outputfile); 
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
