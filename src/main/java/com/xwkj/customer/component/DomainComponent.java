package com.xwkj.customer.component;

import com.xwkj.common.util.HTTPTool;
import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.domain.Domain;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
@EnableScheduling
public class DomainComponent {

    @Autowired
    private ConfigComponent config;

    @Autowired
    private DomainDao domainDao;

    /**
     * Monitoring domains every 10 minitus.
     */
//    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Scheduled(fixedRate = 1000 * 10)
    @Transactional
    public void monitoring() {
        System.out.println(new Date());
        String basePath = config.rootPath + config.ConfigPath;
        NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
        for (Domain domain : domainDao.findMonitoring()) {
            System.out.println(domain.getDomains());
            String html = null;
            String site = domain.getDomains().split(",")[0];
            if (site != null && !site.equals("")) {
                html = HTTPTool.httpRequest("http://" + site, domain.getCharset());
            }
            File file = new File(basePath + File.separator + domain.getDid() + File.separator + "index.html");
            String page = null;
            if (file.exists()) {
                try {
                    page = FileUtils.readFileToString(file, domain.getCharset());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (page == null) {
                continue;
            }
            // Replace the index file if the similarity is smaller than the value set before.
            if (levenshtein.similarity(html, page) * 100 < domain.getSimilarity()) {

            }
            // Update check time.
            domain.setCheckAt(System.currentTimeMillis());
            domainDao.update(domain);
        }
    }

}
