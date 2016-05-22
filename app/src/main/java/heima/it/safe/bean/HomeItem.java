package heima.it.safe.bean;

/**
 * 作者：张琦 on 2016/5/18 16:52
 */
public class HomeItem {
    private String title;
    private String desc;
    private int image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public HomeItem(String title, int image, String desc) {
        this.title = title;
        this.image = image;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
