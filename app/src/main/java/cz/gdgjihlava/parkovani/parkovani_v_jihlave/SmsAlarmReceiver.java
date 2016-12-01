package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by filip on 01.12.16.
 */

public class SmsAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: Send SMS.
        Toast.makeText(context, "Sending SMS", Toast.LENGTH_SHORT).show();
    }
}
