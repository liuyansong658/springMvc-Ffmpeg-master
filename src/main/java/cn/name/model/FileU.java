package cn.name.model;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * SINA：刘岩松
 * 2020-12-26
 */
@Component
public class FileU {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
