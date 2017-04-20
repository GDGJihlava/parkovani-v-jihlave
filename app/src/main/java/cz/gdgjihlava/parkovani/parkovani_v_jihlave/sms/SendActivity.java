package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SMS;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SaveSPZ;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications.OngoingNotification;


public class SendActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private EditText zoneInput;
    private EditText idInput;
    private Spinner parkingLotSpinner;
    private Button sendButton;
    ParkingLotSelector parkingLotSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        parkingLotSpinner = (Spinner) findViewById(R.id.parking_lot_selector);
        parkingLotSelector = new ParkingLotSelector(parkingLotSpinner, this);


        idInput = (EditText) findViewById(R.id.id_input);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SendActivity.this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
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
        });

        SharedPreferences preferences = getSharedPreferences("Prefs", MODE_PRIVATE);
        final String spz = preferences.getString("Spz", "DEFAULT");


        if (!spz.equals("DEFAULT")) {
            idInput.setText(spz);
        }

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
                    Toast.makeText(SendActivity.this, R.string.sms_sent, Toast.LENGTH_LONG).show();
                    OngoingNotification ongoingNotification = new OngoingNotification(getApplicationContext());
                    ongoingNotification.showCurrentTicket(parkingLotSelector.getSelectedParking());
                }
            })
            .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.sms_failed, Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}

