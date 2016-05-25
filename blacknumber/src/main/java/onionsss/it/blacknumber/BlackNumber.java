package onionsss.it.blacknumber;

import java.io.Serializable;

/**
 * 作者：张琦 on 2016/5/24 19:52
 */
public class BlackNumber implements Serializable{
    private String name;
    private String phone;
    private String mode;

    public BlackNumber() {
    }

    public BlackNumber(String name, String mode, String phone) {
        this.name = name;
        this.mode = mode;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
