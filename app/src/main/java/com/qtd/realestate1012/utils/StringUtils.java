package com.qtd.realestate1012.utils;

import com.qtd.realestate1012.constant.ApiConstant;

/**
 * Created by Dell on 8/8/2016.
 */
public class StringUtils {
    public static int getPriceNumber(String s) {
        String[] temp = s.split(" ");
        switch (temp[1]) {
            case ApiConstant.MILLION: {
                return (int) (Float.parseFloat(temp[0]) * 1000000);

            }
            case ApiConstant.BILLION: {
                return (int) (Float.parseFloat(temp[0]) * 1000000000);
            }
            case "VNĐ": {
                String a[] = temp[0].split("\\.");
                String result = "";
                for (int i = 0; i < a.length; i++) {
                    result += a[i];
                }
                return Integer.parseInt(result);
            }
            default:
                return 0;
        }
    }

    public static String getAreaNumber(String s) {
        String[] temp = s.split(" ");
        return temp[0];
    }
}
