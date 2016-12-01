package cz.gdgjihlava.parkovani.parkovani_v_jihlave;

/**
 * Created by filip on 01.12.16.
 */

public interface SmsSubscribe {
    /**
     *
     * @param zone
     * @param duration in minutes
     */
    void startSubscribe(ParkingZone zone, int duration);

    void startSubscribe(ParkingZone zone);

    void stopSubscribe();

    boolean isSubscribed();
}
