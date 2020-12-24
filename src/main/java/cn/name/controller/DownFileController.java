package cn.name.controller;

import cn.name.model.FileU;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SINA：刘岩松
 * 2020-12-26
 */
@Controller
public class DownFileController {

    @GetMapping("do_download")
    public ResponseEntity<byte[]> do_download(String filename, HttpServletRequest request) throws Exception{
        //下载文件路径
        String path = request.getServletContext().getRealPath("/upload");
        File file = new File(path + File.separator + filename);

        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");

        //通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }

    @GetMapping("getDownloads")
    public ModelAndView getDownloads(HttpServletRequest request) throws Exception{
        //下载文件路径
        String path = request.getServletContext().getRealPath("/upload");
        if(!new File(path).exists()){
            new File(path).mkdir();
        }
        ModelAndView mav=new ModelAndView();
        List<FileU> lists = new ArrayList<FileU>();
        this.getAllFileName(path,lists);
        if(lists.size() == 0){
            FileU fileU=new FileU();
            fileU.setFileName("no data!");
            lists.add(fileU);
        }
        mav.addObject("list",lists);
        mav.setViewName("success");
        return mav;
    }


    /**
     * 获取某个文件夹下的所有文件
     *
     * @param fileNameList 存放文件名称的list
     * @param path 文件夹的路径
     * @return
     */
    public void getAllFileName(String path, List<FileU> fileNameList) {
        //ArrayList<String> files = new ArrayList<String>();
        boolean flag = false;
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                FileU fileU=new FileU();
                fileU.setFileName(tempList[i].getName());
                fileNameList.add(fileU);
            }
            if (tempList[i].isDirectory()) {
//              System.out.println("文件夹：" + tempList[i]);
                getAllFileName(tempList[i].getAbsolutePath(),fileNameList);
            }
        }
        return;
    }
}
