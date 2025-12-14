package view.Information;

public class Info{
    String terminalName;
    String terminalLocation;
    String busStatus;
    String departureTime;

    public Info(String terminalName, String terminalLocation, String busStatus, String departureTime){
        this.terminalName = terminalName;
        this.terminalLocation = terminalLocation;
        this.busStatus = busStatus;
        this.departureTime = departureTime;
    }

    public String getTerminalName(){
        return this.terminalName;
    }

    public String getTerminalLocation(){
        return this.terminalLocation;
    }

    public String getBusStatus(){
        return this.busStatus;
    }

    public String getDepartureTime(){
        return this.departureTime;
    }
}
