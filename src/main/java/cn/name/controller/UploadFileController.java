package cn.name.controller;

import cn.name.config.ProcessClearStream;
import cn.name.model.FFmpegBean;
import cn.name.model.FileU;
import cn.name.model.Sux;
import org.apache.commons.io.FileUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.sauronsoftware.jave.Encoder;

/**
 * Created by Nominal on 2018/1/15 0015.
 * 微博：@李明易DY
 */
@Controller
public class UploadFileController {

    @GetMapping("to_upload")
    public String to_upload(){
        return "upload";
    }

    @Resource
    private FFmpegBean fFmpegBean;

    /**
     * 单文件上传
     * @param myFile
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("do_upload")
    public ModelAndView do_upload(MultipartFile myFile,String music, HttpServletRequest request) throws Exception{
        //文件夹路径
        String url=request.getSession().getServletContext().getRealPath("/upload");
        if(!new File(url).exists()){
            new File(url).mkdir();
        }
        //文件名
//        String fileName= UUID.randomUUID().toString().replaceAll("-","")+myFile.getOriginalFilename().substring(myFile.getOriginalFilename().lastIndexOf("."),myFile.getOriginalFilename().length());
        String spl = myFile.getOriginalFilename().substring(myFile.getOriginalFilename().lastIndexOf("."),myFile.getOriginalFilename().length());
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"-convert"+spl;
//        System.out.println(fileName);
        File file = new File(url+ File.separator+fileName);
        FileUtils.copyInputStreamToFile(myFile.getInputStream(),file);

        String tempUrl = url+ File.separator+"temp-"+fileName;
        try {
            //获取视频宽度和高度
            Encoder encoder = new Encoder();
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(file);
            int height = m.getVideo().getSize().getHeight();
            int weight = m.getVideo().getSize().getWidth();
//            long ls = m.getDuration();
//            System.out.println("此视频时长为:" + ls / 60000 + "分" + (ls) / 1000 + "秒！")
//            System.out.println("此视频格式为:" + m.getFormat());
//            FileInputStream fis = new FileInputStream(source);
//            fc = fis.getChannel();
//            BigDecimal fileSize = new BigDecimal(fc.size());
//            size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
//            System.out.println("此视频大小为" + size);
            List<String> command = new ArrayList<String>();
            command.add(fFmpegBean.getFfmpegUrl()); // 添加转换工具路径
            command.add("-threads 4 -y -i"); // 添加参数＂-i＂，该参数指定要转换的文件
            command.add(url+ File.separator+fileName); // 添加要转换格式的视频文件的路径
            if(weight>600){
                weight = 600;
            }
            if(height>900){
                height = 900;
            }
            command.add("-metadata:s:v rotate=\"0\" -vf crop="+weight+":"+height+":0:220 -preset ultrafast -tune zerolatency -r 25 -vcodec libx264 -acodec copy");
            command.add(tempUrl);
            command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
            //调用线程命令启动转码
            Process process = Runtime.getRuntime().exec(getCommandStr(command));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            new ProcessClearStream(process.getInputStream(), "INFO").start();
            new ProcessClearStream(process.getErrorStream(), "ERROR").start();
            int status = process.waitFor();
//            file.delete();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        String temp2Url = url+ File.separator+"temp2-"+fileName;
        File tempFile  = new File(tempUrl);
        try {
            //获取视频宽度和高度
            Encoder encoder = new Encoder();
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(tempFile);
            int height = m.getVideo().getSize().getHeight();
            int weight = m.getVideo().getSize().getWidth();

            Sux sux = sux(height,weight);

            List<String> command = new ArrayList<String>();
            command.add(fFmpegBean.getFfmpegUrl()); // 添加转换工具路径
            command.add("-i");
            command.add(tempUrl);
            command.add("-vf");
            command.add("\"split[a][b];[a]scale="+sux.getTempWeight()+":"+sux.getTempHeight()+",boxblur=10:5[1];[b]scale="+sux.getTempWeight()+":ih*600/iw[2];[1][2]overlay=0:(H-h)/2\" -c:v libx264 -crf 18 -preset veryfast -aspect "+sux.getTempbl()+" -f mp4"); // 添加参数＂-i＂，该参数指定要转换的文件
            command.add(temp2Url);
            command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
            //调用线程命令启动转码
            Process process = Runtime.getRuntime().exec(getCommandStr(command));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            new ProcessClearStream(process.getInputStream(), "INFO").start();
            new ProcessClearStream(process.getErrorStream(), "ERROR").start();
            int status = process.waitFor();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        ModelAndView mav=new ModelAndView();
        FileU fileU=new FileU();
        fileU.setFileName(fileName);
        List<FileU> lists = new ArrayList<FileU>();
        lists.add(fileU);
        mav.addObject("list",lists);
        mav.setViewName("success");
        return mav;
    }

    public Sux sux(int height,int weight){
        Sux sux = new Sux();
        if(height <= 300 && weight <= 200){
            sux = new Sux(360,1200,"3:10");
            return sux;
        }else if(height <= 400  && weight <= 300){
            sux = new Sux(480,1320,"4:11");
            return sux;
        }else if(height <= 500 && weight <= 400){
            sux = new Sux(600,1440,"5:12");
            return sux;
        }else if(height <= 600 && weight <= 500){
            sux = new Sux(720,1560,"6:13");
            return sux;
        }else if(height <= 700 && weight <= 600){
            sux = new Sux(840,1680,"7:14");
            return sux;
        }else if(height <= 800 && weight <= 700){
            sux = new Sux(960,1800,"8:15");
            return sux;
        }else if(height <= 1800 && weight <= 960){
            sux = new Sux(1080,1920,"9:16");
            return sux;
        }
        return sux;
    }

    private static String getCommandStr(List<String> command){
        String commandStr = "";
        for(String s : command){
            commandStr += s + " ";
        }
        return commandStr;
    }

    /**
     * 计算长宽比
     *
     * 先计算原始长宽比(目标宽高/原始宽高)，在乘上原始宽高，即：
     * 求高 -> 原始高 × ( 目标宽 ÷ 原始宽 )
     * 求宽 -> 原始宽 × ( 目标高 ÷ 原始高 )
     *
     * @param fromWidth         原始宽度
     * @param fromHeight        原始高度
     * @param toWidthOrHeight   目标宽度或高度
     * @param isWidth           <code>toWidthOrHeight</code>是宽度还是高度
     * @return                  <code>isWidth</code>为true则返回结果为高度，否则为宽度
     */
    public int calcAspectRatio(int fromWidth, int fromHeight,
                                      int toWidthOrHeight, boolean isWidth) {
        if( isWidth ) {
            return (int) ((double) fromHeight * ( (double) toWidthOrHeight / (double) fromWidth ));
        }else {
            return (int) ((double) fromWidth * ( (double) toWidthOrHeight / (double) fromHeight ));
        }
    }

    /**
     * 多文件上传
     * @param files
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("do_eupload")
    public ModelAndView do_eupload(MultipartFile[] files, HttpServletRequest request) throws Exception{
        ModelAndView mav=new ModelAndView();
        FileU fileU=new FileU();
        //文件夹路径
        String url=request.getSession().getServletContext().getRealPath("/upload");
        if(!new File(url).exists()){
            new File(url).mkdir();
        }
        System.out.println(url);

        for (MultipartFile myFile:files) {
            //文件名
            String fileName=UUID.randomUUID().toString().replaceAll("-","")+myFile.getOriginalFilename().substring(myFile.getOriginalFilename().lastIndexOf("."),myFile.getOriginalFilename().length());
            System.out.println(fileName);
            fileU.setFileName(fileName);
            FileUtils.copyInputStreamToFile(myFile.getInputStream(),new File(url+File.separator+fileName));
        }

        mav.addObject("list",fileU);
        mav.setViewName("success");
        return mav;
    }

    /**
     * 异步上传文件
     * @param ajaxfile
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("ajax_upload")
    @ResponseBody
    public String ajax_upload(MultipartFile ajaxfile, HttpServletRequest request) throws Exception{
        //文件夹路径
        String url=request.getSession().getServletContext().getRealPath("/upload");
        if(!new File(url).exists()){
            new File(url).mkdir();
        }

        //文件名
//        String fileName=UUID.randomUUID().toString().replaceAll("-","")+ajaxfile.getOriginalFilename().substring(ajaxfile.getOriginalFilename().lastIndexOf("."),ajaxfile.getOriginalFilename().length());
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"-转换后-"+ajaxfile.getOriginalFilename();
        FileUtils.copyInputStreamToFile(ajaxfile.getInputStream(),new File(url+File.separator+fileName));

        return "{'url':'"+fileName+"'}";
    }
}
