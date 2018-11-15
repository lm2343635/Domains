package com.xwkj.customer.component;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;

@Component
public class AliyunOSSComponent {

    @Autowired
    private ConfigComponent config;

    public void upload(File file, String path) {
        OSSClient ossClient = new OSSClient(config.getAliyunOSS().endpoint, config.getAliyunOSS().accessKeyId, config.getAliyunOSS().accessKeySecret);
        String content = "Hello OSS";
        ossClient.putObject(config.getAliyunOSS().bucketName, path, new ByteArrayInputStream(content.getBytes()));
        ossClient.shutdown();
    }

    @Scheduled(fixedRate = 1000 * 3600)
    @Transactional
    public void monitoring() {
        System.out.println(config.getAliyunOSS().enable);
        System.out.println(config.getAliyunOSS().endpoint);
        System.out.println(config.getAliyunOSS().accessKeyId);
        System.out.println(config.getAliyunOSS().accessKeySecret);
        System.out.println(config.getAliyunOSS().bucketName);
    }

}
