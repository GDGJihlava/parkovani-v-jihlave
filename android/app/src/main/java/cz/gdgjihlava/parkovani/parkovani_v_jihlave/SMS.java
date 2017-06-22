package cz.gdgjihlava.parkovani.parkovani_v_jihlave;


import android.content.Context;
import android.telephony.SmsManager;

public class SMS {
    private String zone;
    private String spz;
    private String smsText;
    private Context context;
    private String smsNumber;


    public SMS(Context context, int zoneCode, String spz){
        this.context = context;
        smsNumber = context.getResources().getString(R.string.sms_service_number);
        this.zone = String.valueOf(zoneCode);
        this.spz = spz;
        smsText = zone + " " +spz;
    }

    public void create(String zone, String spz) {
    }

    public void send(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsNumber, null, this.getSMSText(), null, null);
    }

    public String getSMSText(){
        return smsText;
    }

    public String getZone(){
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSpz() {
        return spz;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }
}
