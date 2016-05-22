package heima.it.safe.bean;

import android.graphics.Bitmap;

/**
 * 作者：张琦 on 2016/5/21 09:50
 */
public class ContactInfo {
    private String name;
    private String address;
    private Bitmap head;
    public ContactInfo(){

    }
    public ContactInfo(String name, Bitmap head, String address) {
        this.name = name;
        this.head = head;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }
}
