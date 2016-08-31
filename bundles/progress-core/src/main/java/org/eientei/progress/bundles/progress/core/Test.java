package org.eientei.progress.bundles.progress.core;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Alexander Tumin on 2016-08-30
 */
@Component
@Instantiate
public class Test {
    Logger log = LoggerFactory.getLogger(Test.class);

    @Validate
    public void starting() {
        log.info("Ololo");
    }
}
