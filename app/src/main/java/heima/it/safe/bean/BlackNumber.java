package heima.it.safe.bean;

import java.io.Serializable;

/**
 * 作者：张琦 on 2016/5/23 18:48
 */
public class BlackNumber implements Serializable{
    private String name;
    private String phone;
    private String mode;

    public BlackNumber(String name, String phone, String mode) {
        this.name = name;
        this.phone = phone;
        this.mode = mode;
    }

    public BlackNumber() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
