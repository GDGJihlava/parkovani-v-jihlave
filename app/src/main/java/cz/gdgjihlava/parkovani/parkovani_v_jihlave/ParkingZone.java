package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

/**
 * Created by filip on 01.12.16.
 */
public enum ParkingZone {
    ZONA1(30),
    ZONA2(60),
    ZONA3(90);

    private int interval;

    ParkingZone(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
