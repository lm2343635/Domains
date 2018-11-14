package com.xwkj.customer.component;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;

@Component
public class AliyunOSSComponent {

    @Autowired
    private ConfigComponent config;

    public void upload(File file, String path) {
        OSSClient ossClient = new OSSClient(config.aliyunOSS.endpoint, config.aliyunOSS.accessKeyId, config.aliyunOSS.accessKeySecret);
        String content = "Hello OSS";
        ossClient.putObject(config.aliyunOSS.bucketName, path, new ByteArrayInputStream(content.getBytes()));
        ossClient.shutdown();
    }

}
