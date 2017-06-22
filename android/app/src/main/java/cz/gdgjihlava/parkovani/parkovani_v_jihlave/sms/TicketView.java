package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.R;
import cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms.parking.Zone;


public class TicketView {

    public static final String TAG = "TicketView";
     private TextView zoneCodeValue;
     private TextView ticketDurationValue;
     private TextView ticketPriceValue;

    public TicketView(Activity activity) {
        zoneCodeValue = (TextView) activity.findViewById(R.id.zone_code_value);
        ticketDurationValue = (TextView) activity.findViewById(R.id.ticket_duration_value);
        ticketPriceValue = (TextView) activity.findViewById(R.id.ticket_price_value);

    }

    public void setTicketInfo(Zone zone) {

        // TODO - Sometimes not show zero (show 3 instead of 30)
        zoneCodeValue.setText(String.valueOf(zone.getCode()));
        ticketDurationValue.setText(String.valueOf(zone.getTicketDurationInMinutes()));
        ticketPriceValue.setText(String.valueOf(zone.getTicketPriceInCZK()));
    }
}
