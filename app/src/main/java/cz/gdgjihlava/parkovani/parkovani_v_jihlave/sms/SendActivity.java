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
import android.view.View;
import android.widget.*;
import android.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SMS;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.SaveSPZ;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.notifications.OngoingNotification;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.ParkingLot;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.Ticket;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.Zone;


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

    private static final String PARKING_LOTS_KEY = "parking_lots";
    private static final String NAME_KEY = "name";
    private static final String ZONE_KEY = "zone";
    private static final String ZONES_KEY = "zones";
    private static final String ZONE_CODE_KEY = "code";
    private static final String TICKET_DURATION_KEY = "ticketDurationInMinutes";
    private static final String TICKET_PRICE_KEY = "ticketPriceInCZK";

    private TicketView mTicketView;

    private ArrayAdapter<ParkingLot> parkingLots;
    private FirebaseDatabase mDatabase;

    private Zone currentZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mTicketView = new TicketView(this);

        loadSPZ();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference(PARKING_LOTS_KEY).addValueEventListener(getParkingLotFirebaseEventListener());

        parkingLotSpinner.setOnItemSelectedListener(getParkingLotSelectedListener());
    }

    @NonNull
    private ValueEventListener getParkingLotFirebaseEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addParkingLotsToSpinner(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void addParkingLotsToSpinner(DataSnapshot dataSnapshot) {
        parkingLots = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item);
        for (DataSnapshot chilDsnapshot : dataSnapshot.getChildren()) {
            addParkingLotToAdapter(chilDsnapshot);
        }
        parkingLots.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parkingLotSpinner.setAdapter(parkingLots);
    }

    private void addParkingLotToAdapter(DataSnapshot parkingLotSnapshot) {
        String name = parkingLotSnapshot.child(NAME_KEY).getValue(String.class);
        String zoneID = parkingLotSnapshot.child(ZONE_KEY).getValue(String.class);
        parkingLots.add(new ParkingLot(name, zoneID));
    }

    @NonNull
    private AdapterView.OnItemSelectedListener getParkingLotSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ParkingLot selectedParkingLot = getSelectedParking();
                mDatabase.getReference(ZONES_KEY + "/" + selectedParkingLot.getZoneId())
                    .addListenerForSingleValueEvent(getZoneFirebaseEventListener());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private ParkingLot getSelectedParking() {
        return (ParkingLot) parkingLotSpinner.getSelectedItem();
    }

    @NonNull
    private ValueEventListener getZoneFirebaseEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int zoneCode = dataSnapshot.child(ZONE_CODE_KEY).getValue(Integer.class);
                int ticketDurationInMinutes = dataSnapshot.child(TICKET_DURATION_KEY).getValue(Integer.class);
                int ticketPriceInCZK = dataSnapshot.child(TICKET_PRICE_KEY).getValue(Integer.class);
                currentZone = new Zone(zoneCode, ticketDurationInMinutes, ticketPriceInCZK);
                mTicketView.setTicketInfo(currentZone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void loadSPZ() {
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

        final SMS sms = new SMS(this, currentZone.getCode(), idInput.getText().toString());

        new AlertDialog.Builder(this)
            .setTitle(R.string.are_you_sure_to_buy)
            .setMessage(getFormattedTicketInfo(getSelectedParking(), currentZone))
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    sms.send();
                    showToast(R.string.sms_sent);
                    OngoingNotification ongoingNotification = new OngoingNotification(getApplicationContext());
                    ongoingNotification.showCurrentTicket(new Ticket(getSelectedParking().getName(), currentZone
                        .getCode(), currentZone.getTicketPriceInCZK(), currentZone.getTicketDurationInMinutes()));
                }
            })
            .setNegativeButton(android.R.string.no, null).show();

    }

    public String getFormattedTicketInfo(ParkingLot parkingLot, Zone zone) {
        return getResources().getString(R.string.parking_lot) + " " + parkingLot.getName() + "\n"
            + getString(R.string.zone_code) + " " + zone.getCode() + "\n"
            + getString(R.string.ticket_duration) + " " + zone.getTicketDurationInMinutes() + "\n"
            + getString(R.string.ticket_price) + " " + zone.getTicketDurationInMinutes() + "\n";
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

