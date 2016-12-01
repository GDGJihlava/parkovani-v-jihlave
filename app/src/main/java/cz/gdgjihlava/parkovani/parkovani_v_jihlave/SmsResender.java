package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filip on 01.12.16.
 */

public class SmsResender {

    /**
     * Safe const in minutes, to prevent late resend.
     * Better safe than sorry. :D
     */
    public static final int SAFE_CONST = 3;

    private Context context;
    private AlarmManager alarmMgr;
    private List<PendingIntent> alarmIntents;

    public SmsResender(Context context) {
        this.context = context;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmIntents = new ArrayList<>();
    }

    public void startResending(ParkingZone zone, int duration) {
        int loops = countSMS(zone, duration);

        for(int i = 0; i < loops; i++){
            scheduleAlarm(toMillis((duration - SAFE_CONST) + duration * i), false);
        }

    }

    public void startResending(ParkingZone zone) {
        scheduleAlarm(toMillis(zone.getInterval() - SAFE_CONST), true);
    }

    private int countSMS(ParkingZone zone, int duration) {
        return duration / zone.getInterval() + 1;

    }

    public void cancelResending() {
        for (PendingIntent pe: alarmIntents) {
            if (alarmMgr!= null) {
                alarmMgr.cancel(pe);
            }
        }
    }

    private long toMillis(long minutes){
        return minutes * 60 * 1000;
    }

    private void scheduleAlarm(long durationMillis, boolean repeat) {
        Intent alarmIntent = new Intent(context, SmsAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmIntents.size(), alarmIntent, 0);
        if (repeat) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, durationMillis, durationMillis, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, durationMillis, pendingIntent);
        }
        alarmIntents.add(pendingIntent);
    }
}
