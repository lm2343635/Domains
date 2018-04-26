package com.xwkj.customer.component;

import com.xwkj.common.util.JSONTool;
import org.springframework.stereotype.Component;

@Component
public class ConfigComponent {

    // Default file upload folder.
    public String UploadFolder = "/files";

    // Public document folder.
    public String PublicDocumentFolder = "/files/public";

    // Public document folder.
    public String PublicIndexFolder = "/files/index";

    public String PublicKeyFolder = "/WEB-INF/publickeys";

    // Limitation of uploaded file.
    public int FileMaxSize = 512 * 1024 * 1024;


    public String ConfigPath = "/WEB-INF/config.json";
    public String rootPath;
    public JSONTool configTool = null;

    public ConfigComponent() {
        rootPath = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
        load();
    }

    public void load() {
        String pathname = rootPath + ConfigPath;
        configTool = new JSONTool(pathname);
    }

}
