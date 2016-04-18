package net.ddns.sabr.backend;

/** The object model for the data we are sending through endpoints */
public class Bean {

    private String myData;
    private String file;

    public String getData() { return myData; }
    public String getFile() { return file; }

    public void setData(String data) {
        myData = data;
    }
    public void setFile(String f){ file = f; }
}