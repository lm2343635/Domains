package com.xwkj.customer.component;

import com.xwkj.common.util.HTMLTool;
import com.xwkj.common.util.HTTPTool;
import com.xwkj.customer.dao.DomainDao;
import com.xwkj.customer.domain.Domain;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@EnableScheduling
public class DomainComponent {

    @Autowired
    private DomainDao domainDao;

    /**
     * Monitoring domains every 10 minitus.
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    @Transactional
    public void monitoring() {
        System.out.println(new Date());
        NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
        for (Domain domain : domainDao.findMonitoring()) {

            System.out.println(domain.getDomains());
            String html = null;
            String site = domain.getDomains().split(",")[0];
            if (site != null && !site.equals("")) {
                html = HTTPTool.httpRequest("http://" + site, domain.getCharset());
            }
            System.out.println(html);
            System.out.println(domain.getPage());
            System.out.println(html.equalsIgnoreCase(domain.getPage()) + ", " + levenshtein.similarity(html, domain.getPage()));
            // Update check time.
            domain.setCheckAt(System.currentTimeMillis());
            domainDao.update(domain);
        }
    }

}
