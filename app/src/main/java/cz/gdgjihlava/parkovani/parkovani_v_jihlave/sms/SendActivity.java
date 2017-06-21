package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SMS;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SaveSPZ;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications.OngoingNotification;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.ParkingLot;


public class SendActivity extends AppCompatActivity {

    // Constants
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String PREFS_KEY = "Prefs";
    public static final String SPZ_KEY = "Spz";
    public static final String DEFAULT_VALUE = "DEFAULT";

    // Views
    @BindView(R.id.id_input) EditText idInput;
    @BindView(R.id.parking_lot_selector) Spinner parkingLotSpinner;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.zone_code_value) TextView zoneCodeValue;
    @BindView(R.id.ticket_duration_value) TextView ticketDurationValue;
    @BindView(R.id.ticket_price_value) TextView ticketPriceValue;

    private ParkingLotSelector parkingLotSelector;
    private TicketInfo ticketInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ticketInfo = new TicketInfo(zoneCodeValue, ticketDurationValue, ticketPriceValue);
        parkingLotSelector = new ParkingLotSelector(this, parkingLotSpinner, ticketInfo);

        SharedPreferences preferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        final String spz = preferences.getString(SPZ_KEY, DEFAULT_VALUE);


        if (!spz.equals(DEFAULT_VALUE)) {
            idInput.setText(spz);
        }

    }

    @OnClick(R.id.send_button) void send() {
        if (isPermissionToSendSMSGranted()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SendActivity.this,
                Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(SendActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            sendSMS();
        }
    }

    private boolean isPermissionToSendSMSGranted() {
        return ContextCompat.checkSelfPermission(SendActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent spzSettingsIntent = new Intent(this, SaveSPZ.class);
            startActivity(spzSettingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendSMS() {
        ParkingLot selectedParkingLot = parkingLotSelector.getSelectedParking();
        String zoneCode = Integer.toString(selectedParkingLot.getZone().getCode());

        final SMS sms = new SMS(SendActivity.this, zoneCode, idInput.getText().toString());

        new AlertDialog.Builder(this)
            .setTitle(R.string.are_you_sure_to_buy)
            .setMessage(selectedParkingLot.getFormattedTicketInfo(this))
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    sms.send();
                    showToast(R.string.sms_sent);
                    OngoingNotification ongoingNotification = new OngoingNotification(getApplicationContext());
                    ongoingNotification.showCurrentTicket(parkingLotSelector.getSelectedParking());
                }
            })
            .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    showToast(R.string.sms_failed);
                }
            }
        }

    }

    private void showToast(@StringRes int stringID) {
        Toast.makeText(getApplicationContext(), stringID, Toast.LENGTH_LONG).show();
    }
}

