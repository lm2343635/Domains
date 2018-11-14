package com.xwkj.customer.component.config;

import net.sf.json.JSONObject;

public class AliyunOSS {

    public String endpoint;
    public String accessKeyId;
    public String accessKeySecret;
    public String bucketName;

    public AliyunOSS(JSONObject object) {
        this.endpoint = object.getString("endpoint");
        this.accessKeyId = object.getString("accessKeyId");
        this.accessKeySecret = object.getString("accessKeySecret");
        this.bucketName = object.getString("bucketName");
    }
}
