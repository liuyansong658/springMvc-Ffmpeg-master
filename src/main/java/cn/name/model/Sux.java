package cn.name.model;

public class Sux {

    int tempWeight = 1080;
    int tempHeight = 1920;
    String tempbl = "9:16";

    public Sux() {
    }

    public Sux(int tempWeight, int tempHeight, String tempbl) {
        this.tempWeight = tempWeight;
        this.tempHeight = tempHeight;
        this.tempbl = tempbl;
    }

    public int getTempWeight() {
        return tempWeight;
    }

    public void setTempWeight(int tempWeight) {
        this.tempWeight = tempWeight;
    }

    public int getTempHeight() {
        return tempHeight;
    }

    public void setTempHeight(int tempHeight) {
        this.tempHeight = tempHeight;
    }

    public String getTempbl() {
        return tempbl;
    }

    public void setTempbl(String tempbl) {
        this.tempbl = tempbl;
    }
}
