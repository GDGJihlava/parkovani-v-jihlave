package cz.gdgjihlava.parkovani.parkovani_v_jihlave.sms;

public class Zone {

    private int code;

    private int ticketDurationInMinutes;
    private int ticketPriceInCZK;

    public Zone(int code, int ticketDurationInMinutes, int ticketPriceInCZK) {
        this.code = code;
        this.ticketDurationInMinutes = ticketDurationInMinutes;
        this.ticketPriceInCZK = ticketPriceInCZK;
    }

    public int getCode() {
        return code;
    }

    public int getTicketDurationInMinutes() {
        return ticketDurationInMinutes;
    }

    public int getTicketPriceInCZK() {
        return ticketPriceInCZK;
    }
}
