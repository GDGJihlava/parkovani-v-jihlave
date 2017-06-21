package cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.MainActivity;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.ParkingLot;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.Ticket;

/**
 * Created by horm on 1.12.16.
 */
public class OngoingNotification {

    private Context context;
    private static final int NOTIFICATION_ID = 42;

    public OngoingNotification(Context context) {
        this.context = context;
    }

    public void showCurrentTicket(Ticket ticketParkingLot) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
            NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle("Lístek byl zakoupen")
            .setContentText("Tvůj lístek expiruje v " + getExpirationTime(ticketParkingLot.getDuration()))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification_car)
            //.addAction(R.drawable.ic_notification_cancel, getStringFromResources(R.string.cancel), pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE);
        Notification n;

        n = builder.build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
            .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, n);
    }

    private String getExpirationTime(int ticketDuration) {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.MINUTE, ticketDuration);

        return new SimpleDateFormat("HH:mm").format(expirationDate.getTime());
    }

    private String getStringFromResources(int id) {
        return context.getResources().getString(id);
    }
}
