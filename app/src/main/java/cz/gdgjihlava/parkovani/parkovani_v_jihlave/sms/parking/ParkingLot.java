package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking;

import android.content.Context;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;

public class ParkingLot {

    private String name;
    private Zone mZone;

    public ParkingLot(String name, Zone zone) {
        this.name = name;
        mZone = zone;
    }

    public String getName() {
        return name;
    }

    public Zone getZone() {
        return mZone;
    }

    public String toString() {
        return getName();
    }
    
    public String getFormattedTicketInfo(Context context) {
        return context.getResources().getString(R.string.parking_lot) + " " + name + "\n"
            + context.getString(R.string.zone_code) + " " + mZone.getCode() + "\n"
            + context.getString(R.string.ticket_duration) + " " + mZone.getTicketDurationInMinutes() + "\n"
            + context.getString(R.string.ticket_price) + " " + mZone.getTicketDurationInMinutes() + "\n";
    }
}
