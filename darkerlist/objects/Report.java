package darkerlist.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Report implements Serializable {

    private String reason;
    private String proof;
    private Date date;
    private String additional;


    public Report(String reason, String proof, Date date, String additional) {
        this.date = date;
        this.proof = proof;
        this.reason = reason;
        this.additional = additional;
    }
    public String getReason(){
        return reason;
    }

    public void setReason (String reason){
        this.reason = reason;
    }

    public String getProof(){
        return proof;
    }

    public void setProof(String proof){
        this.proof = proof;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public String getAdditional(){
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
