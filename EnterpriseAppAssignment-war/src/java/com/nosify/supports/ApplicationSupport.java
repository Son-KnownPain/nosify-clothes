package com.nosify.supports;

import java.util.Random;

public class ApplicationSupport {
    public static String generateForgotPasswordCode() {
        // Define the characters that can be used in the random text
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Create a Random object
        Random random = new Random();

        // Initialize an empty string to store the random text
        StringBuilder randomText = new StringBuilder();

        // Generate 6 random characters and append them to the randomText string
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomText.append(randomChar);
        }
        
        return randomText.toString();
    }
}
