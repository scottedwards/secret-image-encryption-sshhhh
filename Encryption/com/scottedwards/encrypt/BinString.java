package com.scottedwards.encrypt;

public class BinString {
    private String asciiString;
    private int[] binString;

    public BinString(String asciiString) {
        this.asciiString = asciiString;
        this.binString = convertToBinary(asciiString);
    }

    public BinString(int[] binString) {
        this.binString = binString;
        this.asciiString = convertToAscii(binString);
    }

    public String getAscii() {
        return asciiString;
    }

    public int[] getBinary() {
        return binString;
    }

    private String convertToAscii(int[] binString) {
        String ascii = "";
        int asciiNumber;    
        //loop through the numbers that will signify each word (8 bits) in the string
        for (int i = 0; i < (binString.length / 8); i++) {
            asciiNumber = 0;
            int index = 7;
            //now loop through each bit in the current byte (signified by the value of i)
            //and convert it to a number
            for (int j = (i * 8); j < ((i + 1) * 8); j++) asciiNumber += binString[j] * Math.pow(2, index--);
            ascii += (char) asciiNumber;
        }
        return ascii;
    }

    private int[] convertToBinary(String asciiString) {
        int bin[] = new int[asciiString.length() * 8];
        int letterCount = 0;
        //loop through each letter in the string
        for (char letter : asciiString.toCharArray()) {
            //convert the current letter to the number in its text representation (ascii)
            int asciiNumber = (int) letter;
            for (int i = 7; i >= 0; i--) {
                //if  the value of asciiNumber is greater than 2^i then make the next index in bin[] = 1 (otherwise its 0)
                bin[(8 * letterCount) + (7 - i)] = (asciiNumber - Math.pow(2, i) >= 0) ?  1 : 0; 
                //Also, if the above statement was true then take away 2^i from the asciiNumber
                if (asciiNumber - Math.pow(2, i) >= 0) asciiNumber -= Math.pow(2, i);
            }
            letterCount++;
        }
        return bin;
    }
}
