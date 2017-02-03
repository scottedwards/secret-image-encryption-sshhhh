package com.scottedwards.decrypt;
import java.util.Scanner;
import java.io.File;

public class Decrypt {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the file name of the encrypted picture");

        //Keep looping untill user enter's a file that exists
        File pathTest;
        String targetImage;
        do {
            targetImage = sc.nextLine();
            pathTest = new File(targetImage);
            if (!pathTest.exists()) System.out.println("Image does not exist! Please try again.");
        } while(!pathTest.exists());

        System.out.println("Creating decryption class...");
        SteganographyDecryptor decrypt = new SteganographyDecryptor(targetImage);
        System.out.println("Decrypting image and dumping message to output stream...");
        String plainText = decrypt.getPlainText();
        System.out.println("<BEGIN MESSAGE>");
        System.out.println();
        System.out.println(plainText);
        System.out.println();
        System.out.println("<END MESSAGE>");
    }
}
