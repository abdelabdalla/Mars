package net.ddns.sabr.session;

public class Session {

    public String date;
    public String[][] entries;

    public Session(String date, String[][] entries){
        this.date = date;
        this.entries = entries;
    }

    public Session(){
        this.date = "";
        this.entries = new String[6][5];
    }

    public void setDate(String date){
        this.date = date;
    }

}
