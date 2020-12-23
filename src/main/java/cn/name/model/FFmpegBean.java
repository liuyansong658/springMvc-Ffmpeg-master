package cn.name.model;

import org.springframework.stereotype.Component;

@Component
public class FFmpegBean {

    private String ffmpegUrl;

    public String getFfmpegUrl() {
        return ffmpegUrl;
    }

    public void setFfmpegUrl(String ffmpegUrl) {
        this.ffmpegUrl = ffmpegUrl;
    }
}
