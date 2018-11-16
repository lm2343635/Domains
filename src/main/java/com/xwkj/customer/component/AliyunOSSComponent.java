package com.xwkj.customer.component;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.xwkj.customer.bean.DocumentBean;
import com.xwkj.customer.dao.DocumentDao;
import com.xwkj.customer.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

@Component
public class AliyunOSSComponent {

    @Autowired
    private ConfigComponent config;

    @Autowired
    private DocumentDao documentDao;

    private String rootPath;

    private String getRootPath() {
        if (rootPath == null) {
            rootPath = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
        }
        return rootPath;
    }

    @Transactional
    public boolean upload(Document document) {
        String path = document.getPath();
        File file = new File(getRootPath() + path);
        if (!file.exists()) {
            // Delete the database reference if the file is not existing.
            documentDao.delete(document);
            return false;
        }
        System.out.println("[" + new Date() + "] Uploading " + document.getFilename() + " to Aliyun OSS at " + path);
        OSSClient ossClient = new OSSClient(config.getAliyunOSS().endpoint, config.getAliyunOSS().accessKeyId, config.getAliyunOSS().accessKeySecret);
        try {
            ossClient.putObject(config.getAliyunOSS().bucketName, path, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ossClient.shutdown();
            return false;
        }
        ossClient.shutdown();
        document.setOss(true);
        documentDao.update(document);
        file.delete();
        return true;
    }

    public OSSObject getOSSObject(DocumentBean document, Date expiration) {
        OSSClient ossClient = new OSSClient(config.getAliyunOSS().endpoint, config.getAliyunOSS().accessKeyId, config.getAliyunOSS().accessKeySecret);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(config.getAliyunOSS().bucketName, document.getPath());
        request.setExpiration(expiration);
        request.setMethod(HttpMethod.GET);
        URL signedUrl = ossClient.generatePresignedUrl(request);
        return  ossClient.getObject(signedUrl, new HashMap<String, String>());
    }

    @Scheduled(fixedRate = 1000 * 3600 * 24)
    @Transactional
    public void monitoring() {
        if (config.getAliyunOSS().enable) {
            // Checking the documents which are not uploaded to Aliyun OSS
            documentDao.findByOSS(false).forEach(document -> {
                upload(document);
            });
        }

        // Checking unuseful files.
        deleteUnusefulFiles();
    }

    private void deleteUnusefulFiles() {
        File files = new File(rootPath + config.UploadFolder);
        if (!files.isDirectory()) {
            return;
        }
        for (File dir : files.listFiles()) {
            if (dir.getName().equals("index")) {
                continue;
            }
            System.out.println("[" + new Date() + "] " + dir + ", isDir = " + dir.isDirectory());

            for (File file : dir.listFiles()) {
                if (!file.isFile()) {
                    continue;
                }
                if (documentDao.getByStore(file.getName()) == null) {
                    System.out.println("    " + file.getName() + " has no database reference, delete it.");
                    file.delete();

                }
            }
            if (dir.listFiles().length == 0) {
                dir.delete();
                System.out.println("    empty dir, delete it.");
            }
        }
    }

}
