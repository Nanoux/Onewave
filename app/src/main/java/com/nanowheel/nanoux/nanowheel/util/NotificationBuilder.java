package com.nanowheel.nanoux.nanowheel.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.Observable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.nanowheel.nanoux.nanowheel.BubbleActivity;
import com.nanowheel.nanoux.nanowheel.BuildConfig;
import com.nanowheel.nanoux.nanowheel.MainActivity;
import com.nanowheel.nanoux.nanowheel.R;

import androidx.core.app.NotificationCompat;

import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicCharging;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad1;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad2;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryRemaining;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicSpeedRpm;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicStatusError;

@SuppressWarnings("ConstantConditions")
public class NotificationBuilder {

    static final int SATICJOBID = 2400;
    private static final int NORMJOBID = 2442;

    private static final int CHARGEWARNID = 2401;
    private static final int TURNWARNID = 2402;
    private static final int BATT1WARNID = 2403;
    private static final int BATT2WARNID = 2404;
    private static final int REGENWARNID = 2405;
    private static final int SPEEDRECID = 2406;
    private static final int SENSORWARNID = 2407;
    private static final int SPEEDWARNID = 2408;
    private static final int SPEED2WARNID = 2409;
    //private static final int GENERICID = 2410;

    private static int lastBatteryLevel = -1;

    static void setupCallabcks(Context context){
        canNotifySensor = true;
        canNotifySpeed = true;

        final Context mContext = context;
        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                chargeWarning(mContext);
                turnWarning(mContext);
                batt1Warning(mContext);
                batt2Warning(mContext);
                regenWarning(mContext);

                lastBatteryLevel = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            }
        });
    }

    static Notification createScanningNot(Context context, String text, boolean post){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelStatic(context);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,i,0);
        NotificationCompat.Builder mBuilder;

        Intent ic = new Intent(context,BroadcastReciever.class);
        ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
        PendingIntent pic = PendingIntent.getBroadcast(context,0,ic,0);
        mBuilder = new NotificationCompat.Builder(context, SATICJOBID + "")
                //.setContentTitle(text)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_status_bar_scan)
                .setChannelId(SATICJOBID + "")
                .setContentIntent(pi)
                .addAction(R.drawable.ic_bottom_diagnotics,"Cancel",pic)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification not = mBuilder.build();
        if(post){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(SATICJOBID, not);
        }
        return not;
    }

    static void createStatusNot(int percent, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelStatic(context);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder;

        int icon = getBatDrawable(percent,context);

        Intent ic = new Intent(context, BroadcastReciever.class);
        ic.setAction("com.nanowheel.nanoux.nanowheel.BLUETOOTH_CANCEL");
        PendingIntent pic = PendingIntent.getBroadcast(context, 0, ic, 0);
        //Notification.Action action = new Notification.Action(R.drawable.ic_status_bar_static, "Disconnect", pic);

        Intent ic2 = new Intent(context, BroadcastReciever.class);
        ic2.setAction("com.nanowheel.nanoux.nanowheel.LIGHTS");
        PendingIntent pic2 = PendingIntent.getBroadcast(context, 0, ic2, 0);
        //Notification.Action action2 = new Notification.Action(R.drawable.ic_status_bar_static, "Lights", pic2);

        mBuilder = new NotificationCompat.Builder(context, SATICJOBID + "")
                .setContentTitle("Battery: ")
                .setContentText(percent + "%")
                .setChannelId(SATICJOBID + "")
                .setContentIntent(pi)
                .addAction(R.drawable.ic_status_bar_static,"Disconnect",pic)
                .addAction(R.drawable.ic_status_bar_static,"Lights",pic2);

        if (context.getResources().getDrawable(icon) != null) {
            mBuilder.setSmallIcon(icon);
        }else{
            mBuilder.setSmallIcon(R.drawable.ic_status_bar_static);
        }
        mBuilder.setProgress(100, percent, false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(SATICJOBID, mBuilder.build());
    }

    private final static String CHANNEL_NAME = "Board Status Service";
    private final static String CHANNEL_DESCRIPTION = "Service notification that displays board status when connected.";
    private static void createChannelStatic(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(SATICJOBID + "", CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);
        channel.setSound(null,null);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(channel);
    }

    private final static String CHANNEL_NAME2 = "Board Status Notifications";
    private final static String CHANNEL_DESCRIPTION2 = "Notifications about board status, such as turn-around and battery warnings.";
    private static void createChannelNorm(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NORMJOBID + "", CHANNEL_NAME2, importance);
        channel.setDescription(CHANNEL_DESCRIPTION2);
        notificationManager.createNotificationChannel(channel);
    }
    private static int getBatDrawable(int battery, Context context){
        return context.getResources().getIdentifier("num"+battery,"drawable",context.getPackageName());
        //return context.getResources().getDrawable(id);
    }

    private static Icon getBatIcon(int battery, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return null;
        }
        char batteryL = (battery + "").charAt(0);
        char batteryR = (battery + "").charAt((battery + "").length() - 1);
        int leftID = R.drawable.num0;
        int rightID = R.drawable.num0;
        if (battery < 100) {
            switch (batteryR) {
                case '1':
                    rightID = R.drawable.num1;
                    break;
                case '2':
                    rightID = R.drawable.num2;
                    break;
                case '3':
                    rightID = R.drawable.num3;
                    break;
                case '4':
                    rightID = R.drawable.num4;
                    break;
                case '5':
                    rightID = R.drawable.num5;
                    break;
                case '6':
                    rightID = R.drawable.num6;
                    break;
                case '7':
                    rightID = R.drawable.num7;
                    break;
                case '8':
                    rightID = R.drawable.num8;
                    break;
                case '9':
                    rightID = R.drawable.num9;
                    break;
            }

            if (battery > 9) {
                switch (batteryL) {
                    case '1':
                        leftID = R.drawable.num1;
                        break;
                    case '2':
                        leftID = R.drawable.num2;
                        break;
                    case '3':
                        leftID = R.drawable.num3;
                        break;
                    case '4':
                        leftID = R.drawable.num4;
                        break;
                    case '5':
                        leftID = R.drawable.num5;
                        break;
                    case '6':
                        leftID = R.drawable.num6;
                        break;
                    case '7':
                        leftID = R.drawable.num7;
                        break;
                    case '8':
                        leftID = R.drawable.num8;
                        break;
                    case '9':
                        leftID = R.drawable.num9;
                        break;
                }
            }
            Bitmap left = BitmapFactory.decodeResource(context.getResources(), leftID);
            Bitmap right = BitmapFactory.decodeResource(context.getResources(), rightID);
            left = left.copy(Bitmap.Config.ARGB_8888, true);
            right = right.copy(Bitmap.Config.ARGB_8888, true);
            Canvas comboImage = new Canvas(left);
            comboImage.drawBitmap(right, 50f, 0f, null);

            return Icon.createWithBitmap(left);
        }else{
            return null;
        }
    }
    private static void chargeWarning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getChargeWarning() && lastBatteryLevel >= 0){
            String charging = BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicCharging).value.get();
            if (charging == null || Boolean.parseBoolean(charging)){
                int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
                int thresh = SharedPreferencesUtil.getPrefs(context).getChargeWarningValue();
                if (battery == thresh && lastBatteryLevel < thresh){
                    createChannelNorm(context);

                    Intent i = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                    NotificationCompat.Builder mBuilder;

                    mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                            //.setContentTitle(text)
                            .setContentText("Battery has charged to " + battery + "%!")
                            .setSmallIcon(R.drawable.ic_status_bar_batt_charge)
                            .setChannelId(NORMJOBID + "")
                            .setContentIntent(pi)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(CHARGEWARNID, mBuilder.build());
                }
            }
        }
    }

    private static void turnWarning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getTurnWarning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = SharedPreferencesUtil.getPrefs(context).getTurnWarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("Turn Around! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_turn)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(TURNWARNID, mBuilder.build());
            }
        }
    }

    private static void batt1Warning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getBatt1Warning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = SharedPreferencesUtil.getPrefs(context).getBatt1WarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("WARNING! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_warn1)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(BATT1WARNID, mBuilder.build());
            }
        }
    }

    private static void batt2Warning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getBatt2Warning() && lastBatteryLevel >= 0){
            int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            int thresh = SharedPreferencesUtil.getPrefs(context).getBatt2WarningValue();
            if (battery == thresh && lastBatteryLevel > thresh){
                createChannelNorm(context);

                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                NotificationCompat.Builder mBuilder;

                mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                        //.setContentTitle(text)
                        .setContentText("WARNING! Battery at " + battery + "%!")
                        .setSmallIcon(R.drawable.ic_status_bar_batt_warn2)
                        .setChannelId(NORMJOBID + "")
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(BATT2WARNID, mBuilder.build());
            }
        }
    }

    private static void regenWarning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getRegenWarning() && lastBatteryLevel >= 0){
            String charging = BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicCharging).value.get();
            if (charging != null && !Boolean.parseBoolean(charging)) {
                int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
                int thresh = SharedPreferencesUtil.getPrefs(context).getRegenWarningValue();
                if (battery == thresh && lastBatteryLevel < thresh) {
                    createChannelNorm(context);

                    Intent i = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                    NotificationCompat.Builder mBuilder;

                    mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                            //.setContentTitle(text)
                            .setContentText("Regeneration Warning! Battery at " + battery + "%!")
                            .setSmallIcon(R.drawable.ic_status_bar_batt_regen)
                            .setChannelId(NORMJOBID + "")
                            .setContentIntent(pi)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(REGENWARNID, mBuilder.build());
                }
            }
        }
    }

    private static boolean canWarnSpeed = true;
    public static void speedWarning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getSpeedWarning()){
            String speed = BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > SharedPreferencesUtil.getPrefs(context).getSpeedWarningValue()){
                    if (canWarnSpeed) {
                        canWarnSpeed = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("WARNING! Going too fast!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(SPEEDWARNID, mBuilder.build());
                    }
                }else{
                    canWarnSpeed = true;
                }
            }
        }
    }

    private static boolean canWarnSpeed2 = true;
    public static void speedWarning2(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getSpeedWarning2()){
            String speed = BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > SharedPreferencesUtil.getPrefs(context).getSpeedWarningValue2()){
                    if (canWarnSpeed2) {
                        canWarnSpeed2 = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("WARNING! Going too fast!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(SPEED2WARNID, mBuilder.build());
                    }
                }else{
                    canWarnSpeed2 = true;
                }
            }
        }
    }

    private static boolean canNotifySensor = true;
    public static void sensorWarning(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getSensorWarning()){
            String pad = BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicStatusError).value.get();
            String pad1 = BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicPad1).value.get();
            String pad2 = BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicPad2).value.get();
            String speed = BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (pad != null && pad1 != null && pad2 != null && speed != null){
                boolean s = Boolean.parseBoolean(pad);
                boolean s1 = Boolean.parseBoolean(pad1);
                boolean s2 = Boolean.parseBoolean(pad2);
                int sp = Util.parseI(speed);
                if (sp > 30 && (!s || !s1 || !s2)){
                    if (canNotifySensor) {
                        canNotifySensor = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("Warning, Footpad Sensor off at speed!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(SENSORWARNID, mBuilder.build());
                    }
                }else if (s && s1 && s2){
                    canNotifySensor = true;
                }
            }
        }
    }

    private static boolean canNotifySpeed = true;
    public static void speedNotification(Context context){
        if (SharedPreferencesUtil.getPrefs(context).getSpeedRecordNot()){
            String speed = BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicSpeedRpm).value.get();
            if (speed != null){
                int sp = Util.parseI(speed);
                if (sp > SharedPreferencesUtil.getPrefs(context).getSpeedRecord()){
                    if (canNotifySpeed) {
                        canNotifySpeed = false;
                        createChannelNorm(context);

                        Intent i = new Intent(context, MainActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                        NotificationCompat.Builder mBuilder;

                        mBuilder = new NotificationCompat.Builder(context, NORMJOBID + "")
                                //.setContentTitle(text)
                                .setContentText("Congrats, New Speed Record! Don't Die!")
                                .setSmallIcon(R.drawable.ic_status_bar_warn)
                                .setChannelId(NORMJOBID + "")
                                .setContentIntent(pi)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(SPEEDRECID, mBuilder.build());
                    }
                }else{
                    canNotifySpeed = true;
                }
            }
        }
    }

    public static void createBubble(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            return;
        }

        Intent target = new Intent(context, BubbleActivity.class);
        PendingIntent bubbleIntent = PendingIntent.getActivity(context, 0, target, 0 /* flags */);

        Notification.BubbleMetadata bubbleData = new Notification.BubbleMetadata.Builder()
                        .setDesiredHeight(600)
                        .setIcon(Icon.createWithResource(context, R.drawable.ic_bottom_diagnotics))
                        .setIntent(bubbleIntent)
                        .setAutoExpandBubble(true)
                        .build();

        Person chatBot = new Person.Builder()
                .setBot(true)
                .setName("BubbleBot")
                .setImportant(true)
                .build();

        Notification.Builder builder = new Notification.Builder(context, SATICJOBID+"")
                        .setContentTitle("Your to-do list is here")
                        .setContentText("Click to open to-do list")
                        .setContentIntent(bubbleIntent)
                        .setSmallIcon(R.drawable.ic_bottom_diagnotics)
                        .setBubbleMetadata(bubbleData)
                        .setAutoCancel(true)
                        .addPerson(chatBot);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2469, builder.build());
    }
}