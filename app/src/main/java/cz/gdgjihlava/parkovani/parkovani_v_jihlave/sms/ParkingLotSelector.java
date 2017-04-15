package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class ParkingLotSelector {

    private Spinner mSpinner;
    private ArrayAdapter<ParkingLot> parkingLots;

    public ParkingLotSelector(Spinner spinner, Context context) {
        mSpinner = spinner;


        Zone zone1 = new Zone(96, 90, 25);
        Zone zone2 = new Zone(97, 95, 30);

        parkingLots = new ArrayAdapter<ParkingLot>(context, android.R.layout.simple_spinner_item);
        parkingLots.add(new ParkingLot("Main Square", zone1));
        parkingLots.add(new ParkingLot("Downtown", zone2));
        parkingLots.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(parkingLots);
    }
}
