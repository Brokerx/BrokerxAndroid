package com.firstidea.android.brokerx.utils;

import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by Govind on 20-Nov-16.
 */

public class StringUtils {

    public static final List<String> getListOfComaValues(String values) {
        String splitValues[] = values.split(",");
        List<String> list = Arrays.asList(splitValues);
        return list;
    }

    public static final List<Integer> getListOfComaValuesInInteger(String values) {
        String splitValues[] = values.split(",");
        List<String> list = Arrays.asList(splitValues);
        List<Integer> intList = new ArrayList<Integer>();
        for (String value : list) {
            if (!TextUtils.isEmpty(value)) {
                intList.add(Integer.valueOf(value));
            }
        }
        return intList;
    }

    public static final List<Integer> getListOfComaValuesInBigInteger(String values) {
        String splitValues[] = values.split(",");
        List<String> list = Arrays.asList(splitValues);
        List<Integer> intList = new ArrayList<Integer>();
        for (String value : list) {
            if (value.trim().length() > 0) {
                intList.add(Integer.parseInt(value));
            }
        }
        return intList;
    }

    public static String getComaSeparatedStringFromList(List<Integer> ids) {
        String comaSeparatedString = "";
        for (int i = 0; i < ids.size(); i++) {
            comaSeparatedString += ids.get(i).toString();
            if (i < ids.size() - 1) {
                comaSeparatedString += ",";
            }
        }

        return comaSeparatedString;
    }

    public static String getComaSeparatedStringFromStringList(List<String> ids) {
        String comaSeparatedString = "";
        for (int i = 0; i < ids.size(); i++) {
            comaSeparatedString += ids.get(i);
            if (i < ids.size() - 1) {
                comaSeparatedString += ",";
            }
        }

        return comaSeparatedString;
    }

    public static String createRandomString() {
        String alNums = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(alNums.charAt(rnd.nextInt(alNums.length())));
        }
        return sb.toString();
    }

    public static String createRandomString(int length) {
        String alNums = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alNums.charAt(rnd.nextInt(alNums.length())));
        }
        return sb.toString();
    }

    public static String createAuthTokenString(int length) {
        String alNums = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alNums.charAt(rnd.nextInt(alNums.length())));
        }
        return sb.toString();
    }

    public static String createRandomNumber(int length) {
        String alNums = "123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alNums.charAt(rnd.nextInt(alNums.length())));
        }
        return sb.toString();
    }

    public static boolean isUnicode(String v) {
        byte bytearray[] = v.getBytes();
        CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
        try {
            CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
            r.toString();
        } catch (CharacterCodingException e) {
            return true;
        }
        return false;
    }
}
