package bean;

/**
 * Created by admin on 2016/3/23.
 */
public class OptionsItem {
    private String title;
    private int iconId;


    public OptionsItem(String title, int iconId) {
        this.title = title;
        this.iconId = iconId;

    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
