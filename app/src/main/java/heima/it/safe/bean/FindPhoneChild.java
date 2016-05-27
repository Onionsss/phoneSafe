package heima.it.safe.bean;

/**
 * 作者：张琦 on 2016/5/27 20:03
 */
public class FindPhoneChild {
    private String name;
    private String number;
    private int id;

    public FindPhoneChild() {
    }

    public FindPhoneChild(String name, int id, String number) {
        this.name = name;
        this.id = id;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
