package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by filip on 01.12.16.
 */

public class Subscriber implements SmsSubscribe {

    public static final int SMS_SUBSCRIBER = 0;
    public static final int SAFE_CONST = 3; // rezerva vyrizeni predplaceni

    private Context context;
    private AlarmManager alarmMgr;
    private List<PendingIntent> alarmIntents;
    private  boolean subscribing;

    public Subscriber(Context context) {
        this.context = context;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntents = new ArrayList<>();
        subscribing = false;
    }

    @Override
    public void startSubscribe(ParkingZone zone, int duration) {
        subscribing = true;
        int loops = countSMS(zone, duration);

        for(int i = 0; i < loops; i++){
            scheduleAlarm(toMilis((duration - SAFE_CONST) + duration * i), false);
        }

    }

    private int countSMS(ParkingZone zone, int duration) {
        return duration / zone.getInterval()+1;

    }

    @Override
    public void startSubscribe(ParkingZone zone) {
        subscribing = true;
        scheduleAlarm(toMilis(zone.getInterval() - SAFE_CONST), true);
    }

    @Override
    public void stopSubscribe() {
        subscribing = false;
        for (PendingIntent pe: alarmIntents) {
            if (alarmMgr!= null) {
                alarmMgr.cancel(pe);
            }
        }
    }

    @Override
    public boolean isSubscribed() {
        return  subscribing;
    }

    private long toMilis(long minutes){
        return minutes * 60 * 1000;
    }

    private void scheduleAlarm(long durationMillis, boolean repeat) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)System.currentTimeMillis(), alarmIntent, 0);
        if (repeat) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, durationMillis, durationMillis, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, durationMillis, pendingIntent);
        }
        alarmIntents.add(pendingIntent);
    }
}
