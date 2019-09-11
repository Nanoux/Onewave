package com.nanowheel.nanoux.nanowheel.util;

public class Battery {
    private static final double BATTERY_LiFePO4_16_VOLTAGE = 51.4;
    private static final double BATTERY_NMC_15_VOLTAGE = 55.5;

    private static int owPercent = 0;
    private static double medianVoltage = 0;
    private static double avgVolts = 0;
    private static double avgCells = 0.0;
    private static double ampAdjustment = 0.0;

    public static int remainingDefault() {
        return(owPercent);
    }

    public static int remainingByVoltage() {
        double remaining;

        remaining=99.9/(1.0+Math.pow(5, medianVoltage-avgVolts))+1;

        return((int)remaining);
    }

    public static int remainingFromCells() {
        double remaining;

        remaining=99.9/(1.0+Math.pow(6, medianVoltage-avgCells))+1;

        return((int)remaining);
    }

    public static int remainingForTwoX() {
        double remaining;

        if (owPercent>3) {
            remaining = owPercent*54.0/100.0+54;
        } else {
            remaining = remainingFromCells();
        }

        return((int)remaining);
    }

    public static void setHardware(int hver) {
        //TODO: Add Pint hver, or just make this better in general somehow
        if(hver>=4000) {
            medianVoltage=BATTERY_NMC_15_VOLTAGE;
        } else {
            medianVoltage=BATTERY_LiFePO4_16_VOLTAGE;
        }
    }

    public static boolean setAmps(double amps) {
	   ampAdjustment=amps/8;
        return(false);
    }

    public static boolean setRemaining(int level) {
        if(owPercent != level) {
            owPercent=level;
            return(true);
        } else {
            return(false);
        }
    }

    public static boolean setVoltage(double volts) {
        int voltChange = (int)Math.floor(avgVolts);

	   volts += ampAdjustment;

        if (avgVolts>0) {
            avgVolts = avgVolts*0.9 + volts*0.1;
        } else {
            avgVolts = volts;
        }

        voltChange -= (int)Math.floor(avgVolts);

	   return(voltChange!=0);
    }

    public static boolean setCells(double volts) {
        int voltChange = (int)Math.floor(avgCells);

	   volts += ampAdjustment;

        if (avgCells>0) {
            avgCells = avgCells*0.9 + volts*0.1;
        } else {
            avgCells = volts;
        }

        voltChange -= (int)Math.floor(avgCells);

	   return(voltChange!=0);
    }

}
