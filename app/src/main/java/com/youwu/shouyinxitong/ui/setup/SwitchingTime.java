package com.youwu.shouyinxitong.ui.setup;

public class SwitchingTime {

    private String timeName;
    public SwitchingTime(){};
    private boolean isSelected = false;


    public String getTimeName() {
        return timeName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public SwitchingTime(String timeName){
        this.timeName = timeName;

    }


}
