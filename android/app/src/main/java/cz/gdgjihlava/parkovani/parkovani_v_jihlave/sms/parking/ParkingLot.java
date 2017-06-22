package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking;

import android.content.Context;

import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;

public class ParkingLot {

    private String name;
    private String zoneId;

    public ParkingLot(String name, String zoneId) {
        this.name = name;
        this.zoneId = zoneId;
    }

    public String getName() {
        return name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String toString() {
        return getName();
    }
}
