package com.xwkj.customer.component;

import com.xwkj.common.util.Debug;
import com.xwkj.common.util.HTTPTool;
import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.domain.Domain;
import com.xwkj.customer.domain.Server;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

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
                String cmd = "sshpass -p " + server.getPassword()
                        + " scp -oStrictHostKeyChecking=no " + basePath + File.separator + domain.getDid() + File.separator + "index.html "
                        + server.getUser() + "@" + server.getAddress() + ":" + domain.getPath();
                run(cmd);
                domain.setAlert(true);
            }
            // Update check time.
            domain.setCheckAt(now);
            domainDao.update(domain);
            Debug.log(site + " has been checked, similarity = " + similarity);
        }
    }

    private void run(String cmd) {
        String[] cmds = {"/bin/bash", "-c",  cmd};
        exec(cmds);
    }

    private void exec(String [] cmds) {
        try {
            Process process = Runtime.getRuntime().exec(cmds);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}