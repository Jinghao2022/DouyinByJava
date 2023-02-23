package com.bytedance.douyinbyjava.controller;

import com.bytedance.douyinbyjava.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Value("${douyin.imagePath}")
    private String imagePath;

    @Value("${douyin.videoPath}")
    private String videoPath;

    private String[] imageSuffix = new String[]{
            ".bmp", ".jpeg", ".jpg", ".png"
    };

    private String[] videoSuffix = new String[]{
            ".avi", ".mov", ".movie"
    };

    private Map<String, String> map = new HashMap<String, String>() {
        {
            put(".bmp", "/bmp");
            put(".jpeg", "/jpeg");
            put(".jpg", "/jpeg");
            put(".png", "/pug");
            put(".avi", "/x-msvideo");
            put(".mov", "/quicktime");
            put(".movie", "/x-sgi-movie");
        }
    };

    public boolean check (String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) return true;
        }
        return false;
    }

    @PostMapping("/upload")
    public ResponseDto<String> upload(MultipartFile file) {
        log.info("上传文件" + file.toString());

        String originFileName = file.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + suffix;

        String path;
        if (check(imageSuffix, suffix)) {
            path = imagePath;
        } else if (check(videoSuffix, suffix)) {
            path = videoPath;
        } else {
            return ResponseDto.failure("上传内容格式不正确！");
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(path + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseDto.success(filename);
    }

    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response) {
        String path, type;
        String suffix = name.substring(name.lastIndexOf('.'));
        String typePl = map.get(suffix);
        if (check(imageSuffix, suffix)) {
            path = imagePath;
            type = "image";
        } else if (check(videoSuffix, suffix)) {
            path = videoPath;
            type = "video";
        } else {
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(new File(path + name));
             ServletOutputStream outputStream = response.getOutputStream();) {
            response.setContentType(type + typePl);
            byte[] bytes = new byte[1024];
            int tmp;
            while ((tmp = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, tmp);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
