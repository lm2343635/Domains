package com.xwkj.customer.component;

import com.xwkj.customer.component.config.Admin;
import com.xwkj.customer.component.config.AliyunOSS;
import com.xwkj.customer.dao.ConfigDao;
import com.xwkj.customer.domain.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigComponent {

    // Default file upload folder.
    public final static String UploadFolder = "files";

    // Public document folder.
    public final static String PublicDocumentFolder = "files/public";

    // Public document folder.
    public final static String PublicIndexFolder = "files/index";

    public final static String PublicKeyFolder = "WEB-INF/publickeys";

    // Limitation of uploaded file.
    public final static int FileMaxSize = 512 * 1024 * 1024;

    @Autowired
    private ConfigDao configDao;

    public ConfigDao getConfigDao() {
        return configDao;
    }

    private AliyunOSS aliyunOSS;
    private Admin admin;

    @Transactional
    public AliyunOSS getAliyunOSS() {
        if (aliyunOSS == null) {
            aliyunOSS = new AliyunOSS();
            loadFor(aliyunOSS, AliyunOSS.class);
        }
        return aliyunOSS;
    }

    @Transactional
    public Admin getAdmin() {
        if (admin == null) {
            admin = new Admin();
            loadFor(admin, Admin.class);
        }
        return admin;
    }

    private void loadFor(Object object, Class clazz) {
        Map<String, String> configs = new HashMap<>();
        configDao.findByClazz(clazz.getName()).forEach(config -> {
            configs.put(config.getName(), config.getContent());
        });
        for (Field field : clazz.getFields()) {
            String content = configs.get(field.getName());
            if (content == null) {
                content = "";
                Config config = new Config();
                config.setClazz(clazz.getName());
                config.setName(field.getName());
                config.setContent(content);
                configDao.save(config);
            }

            try {
                if (field.getType().equals(String.class)) {
                    field.set(object, content);
                } else if (field.getType().equals(boolean.class)) {
                    field.setBoolean(object, Boolean.valueOf(content));
                } else if (field.getType().equals(int.class)) {
                    if (content.equals("")) {
                        field.setInt(object, 0);
                    } else {
                        field.setInt(object, Integer.valueOf(content));
                    }
                } else if (field.getType().equals(double.class)) {
                    if (content.equals("")) {
                        field.setInt(object, 0);
                    } else {
                        field.setDouble(object, Double.valueOf(content));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

}
