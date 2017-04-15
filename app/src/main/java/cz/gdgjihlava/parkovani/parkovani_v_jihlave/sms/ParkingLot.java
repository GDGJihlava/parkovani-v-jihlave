package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("zone", mZone);
        return result;
    }
}
