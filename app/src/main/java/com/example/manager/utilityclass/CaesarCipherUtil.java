package com.example.manager.utilityclass;

public class CaesarCipherUtil {

    private static int shift = 47;
    public static String encode(String plainText)
    {
        StringBuilder encrypted = new StringBuilder();
        for(int i=0;i<plainText.length();i++)
        {
            char c = (char)(33+(plainText.charAt(i)-33+shift)%93);
            encrypted.append(c);
        }
        return encrypted.toString();
    }

    public static String decode(String encrypted)
    {
        StringBuilder decrypted = new StringBuilder();
        for(int i=0;i<encrypted.length();i++)
        {
            char c = (char)(33+(93+encrypted.charAt(i)-33-shift)%93);
            decrypted.append(c);
        }
        return decrypted.toString();
    }
}
