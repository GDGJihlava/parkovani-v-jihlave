package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.widget.TextView;


public class TicketInfo {

    public static final String TAG = "TicketInfo";
    private TextView zoneCodeValue;
    private TextView ticketDurationValue;
    private TextView ticketPriceValue;

    public TicketInfo(TextView zoneCodeValue, TextView ticketDurationValue, TextView ticketPriceValue) {
        this.zoneCodeValue = zoneCodeValue;
        this.ticketDurationValue = ticketDurationValue;
        this.ticketPriceValue = ticketPriceValue;
    }

    public void setTicketInfo(int zoneCode, int ticketDuration, int ticketPrice) {

        // TODO - Sometimes not show zero (show 3 instead of 30)
        zoneCodeValue.setText(String.valueOf(zoneCode));
        ticketDurationValue.setText(String.valueOf(ticketDuration));
        ticketPriceValue.setText(String.valueOf(ticketPrice));
    }
}
