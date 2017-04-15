package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class ParkingLotSelector {

    private Spinner mSpinner;
    private ArrayAdapter arrayAdapter;

    public ParkingLotSelector(Spinner spinner, Context context) {
        mSpinner = spinner;


        String[] parkingLots = {"Main Square", "Charles Bridge", "Downtown"};
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, parkingLots);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
    }
}
