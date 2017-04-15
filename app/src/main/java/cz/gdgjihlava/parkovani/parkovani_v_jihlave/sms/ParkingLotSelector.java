package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.*;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;


public class ParkingLotSelector {

    private static final String TAG = "ParkingLotSelector";
    private Spinner mSpinner;
    private ArrayAdapter<ParkingLot> parkingLots;

    public ParkingLotSelector(Spinner spinner, final Activity activity) {
        mSpinner = spinner;


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("parking_lots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                parkingLots = new ArrayAdapter<>(activity.getApplicationContext(), android.R.layout.simple_spinner_item);

                for (DataSnapshot parkingLotSnapshot: dataSnapshot.getChildren()) {
                    final String name = parkingLotSnapshot.child("name").getValue(String.class);
                    String zoneKey = parkingLotSnapshot.child("zone").getValue(String.class);

                    databaseReference.child("zones/" + zoneKey).addListenerForSingleValueEvent(new ValueEventListener
                        () {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int zoneCode = dataSnapshot.child("code").getValue(Integer.class);
                            Log.d(TAG, Integer.toString(zoneCode));
                            int ticketDurationInMinutes = dataSnapshot.child("ticketDurationInMinutes")
                                .getValue(Integer.class);
                            int ticketPriceinCZK = dataSnapshot.child("ticketPriceInCZK").getValue(Integer.class);

                            Zone zone = new Zone(zoneCode, ticketDurationInMinutes, ticketPriceinCZK);
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

    }

    public ParkingLot getSelectedParking() {
        ParkingLot parkingLotForReturn = (ParkingLot) mSpinner.getSelectedItem();
        Log.d(TAG, parkingLotForReturn.getName() + " - " + parkingLotForReturn.getZone().getCode());
        return parkingLotForReturn;
    }
}
