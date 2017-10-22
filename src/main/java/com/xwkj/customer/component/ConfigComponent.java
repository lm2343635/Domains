package com.xwkj.customer.component;

import com.xwkj.common.util.JsonTool;
import org.springframework.stereotype.Component;

@Component
public class ConfigComponent {

    // Default file upload folder.
    public String UploadFolder = "/files";

    // Picture format.
    public String ImageFormat = ".jpg";

    // Limitation of uploaded file.
    public int FileMaxSize = 512 * 1024 * 1024;

    // Max image width.
    public int MaxImageWidth = 1440;

    public String ConfigPath = "/WEB-INF/config.json";
    public String rootPath;
    public JsonTool configTool = null;

    public ConfigComponent() {
        rootPath = this.getClass().getClassLoader().getResource("/").getPath().split("WEB-INF")[0];
        load();
    }

    public void load() {
        String pathname = rootPath + ConfigPath;
        configTool = new JsonTool(pathname);
    }

}
