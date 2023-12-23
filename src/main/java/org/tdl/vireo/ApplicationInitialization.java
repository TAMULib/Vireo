package org.tdl.vireo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tdl.vireo.service.EntityControlledVocabularyService;
import org.tdl.vireo.service.SystemDataLoader;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("ApplicationInitialization")
@Profile("!test")
public class ApplicationInitialization implements InitializingBean {

    @Lazy
    @Autowired
    private SystemDataLoader systemDataLoader;

    @Lazy
    @Autowired
    private EntityControlledVocabularyService entityControlledVocabularyService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // load defaults first
        systemDataLoader.loadSystemData();
        // assumes one language defined in defaults
        entityControlledVocabularyService.scanForEntityControlledVocabularies();
    }

}
