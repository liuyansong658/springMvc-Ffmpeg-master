package cn.name.model;
/**
 * SINA：刘岩松
 * 2020-12-26
 */
public class Sux {

    /**
     * 公约数
     */
    private int divisor;
    /**
     * 倍数
     */
    private int multiple;
    /**
     * 裁剪后的长度
     */
    private int tailorHeight;
    /**
     * 视频长度
     */
    private long duration;
    private int tempWeight = 1080;
    private int tempHeight = 1920;
    /**
     * 视频比例
     */
    private String proportion;

    public Sux() {
    }

    public Sux(int tempWeight, int tempHeight, String proportion) {
        this.tempWeight = tempWeight;
        this.tempHeight = tempHeight;
        this.proportion = proportion;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getTailorHeight() {
        return tailorHeight;
    }

    public void setTailorHeight(int tailorHeight) {
        this.tailorHeight = tailorHeight;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
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

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }
}
