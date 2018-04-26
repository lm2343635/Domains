package com.xwkj.customer.component;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.FileTool;
import com.xwkj.common.util.HTTPTool;
import com.xwkj.common.util.RumtimeTool;
import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Server;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

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
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Transactional
    public void monitoring() {
        Long now = System.currentTimeMillis();
        String basePath = config.rootPath + config.PublicIndexFolder;
        NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
        for (Domain domain : domainDao.findMonitoring()) {
            long interval = Math.round((now - domain.getCheckAt()) * 1.0 / 1000);
            if (interval < domain.getFrequency() * 60 * 10) {
                continue;
            }
            String remote = null;
            String site = domain.getDomains().split(",")[0];
            if (site != null && !site.equals("")) {
                remote = HTTPTool.httpRequest("http://" + site, domain.getCharset());
            }
            if (remote == null || domain.getPage() == null) {
                continue;
            }
            double similarity = levenshtein.similarity(remote, domain.getPage());
            // Replace the index file if the similarity is smaller than the value set before.
            if (similarity * 100 < domain.getSimilarity()) {
                Server server = domain.getServer();
                String cmd = null;
                if (server.getUsingPublicKey()) {

                } else {
                    cmd = "sshpass -p " + server.getCredential()
                            + " scp -oStrictHostKeyChecking=no " + basePath + File.separator + domain.getDid() + File.separator + "index.html "
                            + server.getUser() + "@" + server.getAddress() + ":" + domain.getPath();
                }
                RumtimeTool.run(cmd);
                domain.setAlert(true);
            }
            // Update check time.
            domain.setCheckAt(now);
            domainDao.update(domain);
            Debug.log(site + " has been checked, similarity = " + similarity);
        }
    }


}
