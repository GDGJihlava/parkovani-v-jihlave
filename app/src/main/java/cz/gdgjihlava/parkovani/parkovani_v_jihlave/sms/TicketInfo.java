package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.widget.TextView;


public class TicketInfo {

    private TextView zoneCodeValue;
    private TextView ticketDurationValue;
    private TextView ticketPriceValue;

    public TicketInfo(TextView zoneCodeValue, TextView ticketDurationValue, TextView ticketPriceValue) {
        this.zoneCodeValue = zoneCodeValue;
        this.ticketDurationValue = ticketDurationValue;
        this.ticketPriceValue = ticketPriceValue;
    }

    public void setTicketInfo(int zoneCode, int ticketDuration, int ticketPrice) {
        // TODO - Remove warning
        zoneCodeValue.setText(Integer.toString(zoneCode));
        ticketDurationValue.setText(Integer.toString(ticketDuration));
        ticketPriceValue.setText(Integer.toString(ticketPrice));
    }
}
