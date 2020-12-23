package cn.name.model;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by Nominal on 2018/1/15 0015.
 * 微博：@李明易DY
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
