package darkerlist.objects;

import java.io.Serializable;
import java.util.List;

public class Config implements Serializable {
    private String mode;
    private String logChannel;
    private List<String> WhiteList;
    private String pingrole;

    public Config(String mode, String logChannel, List<String> WhiteList, String pingrole){
        this.mode = mode;
        this.logChannel = logChannel;
        this.WhiteList = WhiteList;
        this.pingrole = pingrole;
    }
    public String getMode(){
        return mode;
    }
    public void setMode(String mode){
        this.mode = mode;
    }
    public String getLogChannel(){
        return logChannel;
    }
    public void setLogChannel(String logChannel){
        this.logChannel = logChannel;
    }
    public List<String> getWhiteList(){
        return WhiteList;
    }
    public void setWhiteList(List<String> WhiteList){
        this.WhiteList = WhiteList;
    }
    public String getPingrole(){
        return pingrole;
    }
    public void setPingrole(String pingrole){
        this.pingrole = pingrole;
    }

}
