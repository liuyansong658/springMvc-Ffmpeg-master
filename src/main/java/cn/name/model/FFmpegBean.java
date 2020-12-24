package cn.name.model;

import org.springframework.stereotype.Component;

/**
 * SINA：刘岩松
 * 2020-12-26
 */
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
