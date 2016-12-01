package cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.MainActivity;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;

/**
 * Created by horm on 1.12.16.
 */
public class OngoingNotification {

    private Context context;
    private static final int NOTIFICATION_ID = 42;

    public OngoingNotification(Context context) {
        this.context = context;
    }

    public void showCurrentTicket() {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
            NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle("Notification Title")
            .setContentText("Sample Notification Content")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification_car)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_car))
            ;
        Notification n;

        n = builder.build();

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
            .NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, n);
    }
}
