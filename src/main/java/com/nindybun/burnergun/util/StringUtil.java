package com.nindybun.burnergun.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class StringUtil {
    private static  final Object[][] charPixels = {
            {" ", 4},
            {"A", 5},
            {"B", 5},
            {"C", 5},
            {"D", 5},
            {"E", 5},
            {"F", 5},
            {"G", 5},
            {"H", 5},
            {"I", 3},
            {"J", 5},
            {"K", 5},
            {"L", 5},
            {"M", 5},
            {"N", 5},
            {"O", 5},
            {"P", 5},
            {"Q", 5},
            {"R", 5},
            {"S", 5},
            {"T", 5},
            {"U", 5},
            {"V", 5},
            {"W", 5},
            {"X", 5},
            {"Y", 5},
            {"Z", 5},

            {"a", 5},
            {"b", 5},
            {"c", 5},
            {"d", 5},
            {"e", 5},
            {"f", 4},
            {"g", 5},
            {"h", 5},
            {"i", 1},
            {"j", 5},
            {"k", 4},
            {"l", 2},
            {"m", 5},
            {"n", 5},
            {"o", 5},
            {"p", 5},
            {"q", 5},
            {"r", 5},
            {"s", 5},
            {"t", 3},
            {"u", 5},
            {"v", 5},
            {"w", 5},
            {"x", 5},
            {"y", 5},
            {"z", 5},

            {"0", 5},
            {"1", 5},
            {"2", 5},
            {"3", 5},
            {"4", 5},
            {"5", 5},
            {"6", 5},
            {"7", 5},
            {"8", 5},
            {"9", 5},
    };

    public static int getStringPixelLength(String string){
        int pixel = (StringUtils.deleteWhitespace(string).length()-1);
        for (int i = 0; i < string.length(); i++) {
            String c = String.valueOf(string.charAt(i));
            for (int j = 0; j < charPixels.length; j++) {
                if (c.equals(charPixels[j][0])){
                    pixel += (int)charPixels[j][1];
                }
            }
        }
        return pixel;
    }



}
