package net.ddns.sabr.support;

public class Session {

    public String date;
    public final String[][] entries;

    public Session(String date, String[][] entries){
        this.date = date;
        this.entries = entries;
    }

    public Session(){
        this.date = "";
        this.entries = new String[7][5];
    }

    public void setDate(String date){
        this.date = date;
    }

}
