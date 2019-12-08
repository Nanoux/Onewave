package com.nanowheel.nanoux.nanowheel.util;

import android.app.UiModeManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import java.util.BitSet;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Created by kwatts on 6/15/16.
 */
public class Util {

    /* Helper methods. For dealing with bytes see:
       http://www.roseindia.net/java/master-java/bitwise-bitshift-operators.shtml
       https://calleerlandsson.com/2014/02/06/rubys-bitwise-operators/
     */
    public static short byteToShort(byte[] v) {
        return (short)((v[1] << 8) + (v[0] & 0xff));
    }

    private static final byte[] twoBytes = new byte[2];
    public static byte[] intToShortBytes(@IntRange(from = -32768, to = 32767) int v) {

        twoBytes[0] = (byte) (v >> 8 & 0xff);
        twoBytes[1] = (byte) (v & 0xff);

        return twoBytes;
    }
    public static byte[] boolToShortBytes(boolean b) {
        return new byte[0];
    }

    // 1 byte/8-bit signed two's complement (-128 to 127)
    // returns an int, 32-bit signed two's complement
    public static int unsignedByte(byte var0) {
        return var0 & 255;
    }

    // short = 2 bytes/16-bit signed two's complement (-32,768 to 32,767)
    public static int unsignedShort(byte[] var0) {
        int var1;
        if(var0.length < 2) {
            var1 = -1;
        } else {
            var1 = (unsignedByte(var0[0]) << 8) + unsignedByte(var0[1]);
        }
        // ByteBuffer.wrap(v_bytes).getShort() also works
        // or
        // ByteBuffer bb = ByteBuffer.allocate(2);
        // bb.order(ByteOrder.LITTLE_ENDIAN);
        // bb.put(var0[0]);
        // bb.put(var0[1]);
        // short shortVal = bb.getShort(0);
        return var1;
    }

    // double is 64 bit and used for decimals
    public static double cel2far(double celsius) {
        return (9.0/5.0)*celsius + 32;
    }
    public static double far2cel(double far) {
        return (5.0/9.0)*(far - 32);
    }
    public static double milesToKilometers(double paramDouble) {
        return paramDouble * 1.609344;

    }
    public static double revolutionsToKilometers(double paramDouble, int owType)
    {
        double diameter = 35.0d;
        if (owType > 1){
            diameter = 33.0d;
        }
        return paramDouble * diameter/ 39370.099999999999D;
    }

    public static double revolutionsToMiles(double paramDouble, int owType)
    {
        double diameter = 35.0d;
        if (owType > 1){
            diameter = 33.0d;
        }
        return paramDouble * diameter / 63360.0D;
    }
    public static double rpmToKilometersPerHour(double paramDouble, int owType)
    {
        double diameter = 35.0d;
        if (owType > 1){
            diameter = 33.0d;
        }
        return 60.0D * (diameter * paramDouble) / 39370.099999999999D;
    }

    public static double rpmToMilesPerHour(double paramDouble, int owType)
    {
        double diameter = 35.0d;
        if (owType > 1){
            diameter = 33.0d;
        }
        return 60.0D * (diameter * paramDouble) / 63360.0D;
    }

    public static void printBits(String prompt, BitSet b, int numBits) {
        System.out.print(prompt + " ");
        for (int i = 0; i < numBits; i++) {
            System.out.print(b.get(i) ? "1" : "0");
        }
        System.out.println();
    }

    public static String bytesToHex(byte c[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : c) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * If your number X falls between A and B, and you would like Y to fall between C and D, you can apply the following linear transform:
     *
     * Y = (X-A)/(B-A) * (D-C) + C
     *
     * https://stackoverflow.com/a/345204/247325
     */
    public static float linearTransform(float x, float a, float b, float c, float d) {
        return (x-a)/(b-a) * (d-c) + c;
    }
    /**
     * If your number X falls between A and B, and you would like Y to fall between C and D, you can apply the following linear transform:
     *
     * Y = (X-A)/(B-A) * (D-C) + C
     *
     * https://stackoverflow.com/a/345204/247325
     */
    public static double linearTransform(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a) * (d-c) + c;
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }


    public static double parseD (String s){
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            Log.e("PARSE ERROR","" + s);
            return -1;
        }
    }

    public static float parseF (String s){
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            Log.e("PARSE ERROR","" + s);
            return -1;
        }
    }

    public static int parseI (String s){
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Log.e("PARSE ERROR",""+s);
            return -1;
        }
    }

    public static byte[] StringToByteArrayFastest(String hex)
    {
        hex = hex.replace(":", "");
        hex = hex.replace("-", "");
        hex = hex.toUpperCase();

        if (hex.length() % 2 == 1) {
            return null;
            // throw new Exception("The binary key cannot have an odd number of digits");
        }

        byte[] arr = new byte[hex.length() >> 1];

        char[] c = hex.toCharArray();

        for (int i = 0; i < hex.length() >> 1; ++i)
        {
            arr[i] = (byte)((GetHexVal(c[i << 1]) << 4) + (GetHexVal(c[(i << 1) + 1])));
        }

        return arr;
    }

    public static int GetHexVal(char hex)
    {
        int val = (int)hex;
        //For uppercase A-F letters:
        return val - (val < 58 ? 48 : 55);
        //For lowercase a-f letters:
        //return val - (val < 58 ? 48 : 87);
        //Or the two combined, but a bit slower:
        //return val - (val < 58 ? 48 : (val < 97 ? 55 : 87));
    }

    public static boolean isDark(Context context){
        int state = AppCompatDelegate.getDefaultNightMode();
        if (state == AppCompatDelegate.MODE_NIGHT_NO){
            return false;
        }else if (state == AppCompatDelegate.MODE_NIGHT_YES){
            return true;
        }else if (state == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY){
            PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            return (powerManager != null && powerManager.isPowerSaveMode());
        }else if (state == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            UiModeManager uiManager = (UiModeManager)context.getSystemService(Context.UI_MODE_SERVICE);
            return (uiManager != null && uiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES);
        }else{
            return false;
        }
    }
}
