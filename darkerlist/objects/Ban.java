package darkerlist.objects;

import java.io.Serializable;
import java.util.Date;

public class Ban implements Serializable {

    private String reason;
    private String proof;
    private Date date;


    public Ban(String reason, String proof, Date date) {
        this.date = date;
        this.proof = proof;
        this.reason = reason;
    }

    public Ban() {
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
}
