package cn.name.controller;

import cn.name.config.ProcessClearStream;
import cn.name.model.FFmpegBean;
import cn.name.model.FileU;
import cn.name.model.Sux;
import org.apache.commons.io.FileUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import java.util.Random;
import java.util.UUID;

import it.sauronsoftware.jave.Encoder;

/**
 * SINA：刘岩松
 * 2020-12-26
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
    public ModelAndView do_upload(MultipartFile myFile,String music,String multi, HttpServletRequest request) throws Exception{
        //文件名
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"-"+myFile.getOriginalFilename();

        /**
         * 抽象方法
         */
        List<FileU> lists = new ArrayList<FileU>();
        if(myFile != null && myFile.getSize() > 0){
            fileName = convertVIido(music, multi, request, fileName, myFile);
            FileU fileU=new FileU();
            fileU.setFileName(fileName);
            lists.add(fileU);
        }

        ModelAndView mav=new ModelAndView();
        mav.addObject("list",lists);
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
    public String ajax_upload(MultipartFile ajaxfile,String music,String multi, HttpServletRequest request) throws Exception{
        //文件夹路径
        if(ajaxfile == null){
            return null;
        }
        //文件名
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"-"+ajaxfile.getOriginalFilename();
        /**
         * 抽象方法
         */
        fileName = convertVIido(music, multi, request, fileName, ajaxfile);

        return "{'url':'"+fileName+"'}";
    }

    private String convertVIido(String music, String multi, HttpServletRequest request, String fileName, MultipartFile myFile) throws Exception{

        //文件夹路径
        String url=request.getSession().getServletContext().getRealPath("/upload");
        if(!new File(url).exists()){
            new File(url).mkdir();
        }
        //临时文件夹路径
        String tempUploadUrl=request.getSession().getServletContext().getRealPath("/tempUpload");
        if(!new File(tempUploadUrl).exists()){
            new File(tempUploadUrl).mkdir();
        }
        File file = new File(tempUploadUrl+ File.separator+fileName);
        FileUtils.copyInputStreamToFile(myFile.getInputStream(),file);

        /**
         * 第一步:开始裁剪，并且是否去除音乐
         */
        Sux sux = new Sux();
        String tempUrl = "temp-"+fileName;
        try {
            //获取视频宽度和高度
            Encoder encoder = new Encoder();
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(file);
            int height = m.getVideo().getSize().getHeight();
            int weight = m.getVideo().getSize().getWidth();
            long ls = m.getDuration();
            System.out.println("此视频时长为:" + ls / 60000 + "分" + (ls) / 1000 + "秒！");
            sux.setTempHeight(height);
            sux.setTempWeight(weight);
            sux.setDuration((ls) / 1000);
//            System.out.println("此视频格式为:" + m.getFormat());
//            FileInputStream fis = new FileInputStream(source);
//            fc = fis.getChannel();
//            BigDecimal fileSize = new BigDecimal(fc.size());
//            size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
//            System.out.println("此视频大小为" + size);
            /* 需要裁剪的公约数倍数  */
            sux = getDivisor(sux);
            /*获取倍数和计算裁剪视频长度  */
            sux = getHeight(sux,multi);
            /* 得到上下裁剪的长度 */
            int cutLength = (int) sux.getDivisor() * sux.getMultiple() / 2 ;

            List<String> command = new ArrayList<String>();
            command.add(fFmpegBean.getFfmpegUrl()); // 添加转换工具路径
            command.add("-threads 4 -y -i"); // 添加参数＂-i＂，该参数指定要转换的文件
            command.add(tempUploadUrl+ File.separator+fileName); // 添加要转换格式的视频文件的路径

            command.add("-metadata:s:v rotate=\"0\" -vf crop="+weight+":"+sux.getTailorHeight()+":"+cutLength+":"+cutLength+" -preset ultrafast -tune zerolatency -r 25 -vcodec libx264 -acodec copy");
            if("on".equals(music) || "true".equals(music)){
                //如果选中随机音乐 则随机选出一个音乐进行添加。-an 先删除音乐
                command.add("-an");
            }
            command.add(tempUploadUrl+ File.separator+tempUrl);
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

        /**
         * 第二步:开始音乐
         */
        File tempMusicFile = null;
        File tempFile  = new File(tempUploadUrl+ File.separator+tempUrl);
        if("on".equals(music) || "true".equals(music)) {
            String tempMusicUrl = "tempMusic-" + fileName;
            tempMusicFile = new File(tempUploadUrl + File.separator + tempMusicUrl);
            try {
                List<String> command = new ArrayList<String>();
                command.add(fFmpegBean.getFfmpegUrl()); // 添加转换工具路径
                command.add("-i");
                Random r = new Random(1);
                command.add(request.getSession().getServletContext().getRealPath("/music") + File.separator + r.nextInt(4) + ".mp3");    //音乐地址
                command.add("-i");
                command.add(tempUploadUrl + File.separator + tempUrl);   //裁剪后去除音乐后的视频
                command.add("-t " + sux.getDuration() + " -y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
                command.add(tempUploadUrl + File.separator + tempMusicUrl);
                //调用线程命令启动转码
                Process process = Runtime.getRuntime().exec(getCommandStr(command));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                new ProcessClearStream(process.getInputStream(), "INFO").start();
                new ProcessClearStream(process.getErrorStream(), "ERROR").start();
                int status = process.waitFor();
                if (status == 0) {
                    tempUrl = tempMusicUrl;
                }
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        /**
         * 第三步:开始添加滤镜
         */
        String endUrl = "convert-"+fileName;
        try {
            /* 计算比例 */
            String proportion = sux.getTempWeight() / sux.getDivisor() + ":" + sux.getTempHeight() / sux.getDivisor();
            List<String> command = new ArrayList<String>();
            command.add(fFmpegBean.getFfmpegUrl()); // 添加转换工具路径
            command.add("-i");
            command.add(tempUploadUrl+ File.separator+tempUrl);
            command.add("-vf");
            command.add("\"split[a][b];[a]scale="+sux.getTempWeight()+":"+sux.getTempHeight()+",boxblur=10:5[1];[b]scale="+sux.getTempWeight()+":"+sux.getTailorHeight()+"[2];[1][2]overlay=0:(H-h)/2\" -c:v libx264 -crf 18 -preset veryfast -aspect "+ proportion +" -f mp4"); // 添加参数＂-i＂，该参数指定要转换的文件
            command.add(url+ File.separator+endUrl);
            command.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
            //调用线程命令启动转码
            Process process = Runtime.getRuntime().exec(getCommandStr(command));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            new ProcessClearStream(process.getInputStream(), "INFO").start();
            new ProcessClearStream(process.getErrorStream(), "ERROR").start();
            int status = process.waitFor();
            if(status == 0){
                fileName = endUrl;
                file.delete();
                if(tempMusicFile != null){
                    tempMusicFile.delete();
                }
                tempFile.delete();
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 获取公约数
     * @return
     */
    public Sux getDivisor(Sux sux){
        int divisor = 0;
        for(int i = 99;i > 1;i--){
            if((sux.getTempWeight() + sux.getTempHeight()) % i == 0){
                divisor = i;
                break;
            }
        }
        sux.setDivisor(divisor);
        return sux;
    }


    /**
     * 需要裁剪的长度为多少，( 长度 - 公约数*倍数 ) 大于 宽度
     * @param multi  上下裁剪的倍数,默认为1倍
     * @return
     */
    public Sux getHeight(Sux sux,String multi){
        int tailorHeight = sux.getTempHeight();
        Double tempMulti = null;
        try {
            tempMulti = Double.parseDouble(multi);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(tempMulti == null){
            tempMulti = 1.0;
        }
        for(int i = 99 ; i >= 1 ; i--){
            if((sux.getTempHeight() - sux.getDivisor() * i) > (sux.getTempWeight() / tempMulti)){
                tailorHeight = sux.getTempHeight() - sux.getDivisor() * i;
                sux.setMultiple(i);
                break;
            }
        }
        sux.setTailorHeight(tailorHeight);
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


}
