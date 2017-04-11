package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

public class Parking {

    private String name;
    private Zone mZone;

    public Parking(String name, Zone zone) {
        this.name = name;
        mZone = zone;
    }

    public String getName() {
        return name;
    }

    public Zone getZone() {
        return mZone;
    }
}
