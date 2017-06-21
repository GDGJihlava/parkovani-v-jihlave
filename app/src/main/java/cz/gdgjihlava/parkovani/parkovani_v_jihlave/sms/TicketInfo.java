package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;


public class TicketInfo {

    public static final String TAG = "TicketInfo";
    @BindView(R.id.zone_code_value) private TextView zoneCodeValue;
    @BindView(R.id.ticket_duration_value) private TextView ticketDurationValue;
    @BindView(R.id.ticket_price_value) private TextView ticketPriceValue;

    public TicketInfo(Activity activity) {
        ButterKnife.bind(activity);
    }

    public void setTicketInfo(int zoneCode, int ticketDuration, int ticketPrice) {

        // TODO - Sometimes not show zero (show 3 instead of 30)
        zoneCodeValue.setText(String.valueOf(zoneCode));
        ticketDurationValue.setText(String.valueOf(ticketDuration));
        ticketPriceValue.setText(String.valueOf(ticketPrice));
    }
}
