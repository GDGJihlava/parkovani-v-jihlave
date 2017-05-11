package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.*;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.ParkingInJihlava;


public class ParkingLotSelector {

    private static final String ZONE_CODE_KEY = "code";
    private static final String TAG = "ParkingLotSelector";
    private static final String TICKET_DURATION_KEY = "ticketDurationInMinutes";
    private static final String TICKET_PRICE_KEY = "ticketPriceInCZK";
    private static final String PARKING_LOTS_KEY = "parking_lots";
    private static final String NAME_KEY = "name";
    private static final String ZONE_KEY = "zone";
    private static final String ZONES_KEY = "zones";

    private Spinner mSpinner;
    private ArrayAdapter<ParkingLot> parkingLots;
    private TicketInfo mTicketInfo;

    public ParkingLotSelector(final Activity activity, Spinner spinner, final TicketInfo ticketInfo) {
        mSpinner = spinner;

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child(PARKING_LOTS_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                parkingLots = new ArrayAdapter<>(activity.getBaseContext(), android.R.layout.simple_spinner_item);

                for (DataSnapshot parkingLotSnapshot : dataSnapshot.getChildren()) {
                    final String name = parkingLotSnapshot.child(NAME_KEY).getValue(String.class);
                    String zoneID = parkingLotSnapshot.child(ZONE_KEY).getValue(String.class);

                    databaseRef.child(ZONES_KEY + "/" + zoneID).addListenerForSingleValueEvent(new ValueEventListener
                        () {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int zoneCode = dataSnapshot.child(ZONE_CODE_KEY).getValue(Integer.class);
                            int ticketDurationInMinutes = dataSnapshot.child(TICKET_DURATION_KEY).getValue(Integer.class);
                            int ticketPriceInCZK = dataSnapshot.child(TICKET_PRICE_KEY).getValue(Integer.class);

                            Zone zone = new Zone(zoneCode, ticketDurationInMinutes, ticketPriceInCZK);
                            parkingLots.add(new ParkingLot(name, zone));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                parkingLots.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(parkingLots);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSpinner.setOnItemSelectedListener(getListener(ticketInfo));

    }

    @NonNull
    private AdapterView.OnItemSelectedListener getListener(final TicketInfo ticketInfo) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ParkingLot parkingLot = (ParkingLot) mSpinner.getSelectedItem();
                Zone zone = parkingLot.getZone();
                mSpinner.getSelectedItem();
                ticketInfo.setTicketInfo(zone.getCode(), zone.getTicketDurationInMinutes(), zone.getTicketPriceInCZK());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    public ParkingLot getSelectedParking() {
        ParkingLot parkingLotForReturn = (ParkingLot) mSpinner.getSelectedItem();
        Log.d(TAG, parkingLotForReturn.getName() + " - " + parkingLotForReturn.getZone().getCode());
        return parkingLotForReturn;
    }
}
