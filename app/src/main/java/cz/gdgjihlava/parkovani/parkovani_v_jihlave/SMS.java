package cz.gdgjihlava.parkovani.parkovani_v_jihlave;



public class SMS {
    private String zone;
    private String spz;
    private String smsText;

    public void create(String zone, String spz) {
        this.zone = zone;
        this.spz = spz;
        smsText = zone + " " +spz;
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
