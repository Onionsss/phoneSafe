package heima.it.safe.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张琦 on 2016/5/27 19:54
 */
public class FindPhoneGroup {
    private String name;
    private int idx;
    public List<FindPhoneChild> list = new ArrayList<>();
    public FindPhoneGroup() {
    }

    public FindPhoneGroup(String name, int idx) {
        this.name = name;
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
