package org.eientei.progress.bundles.progress.log.adapter;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Unbind;
import org.osgi.service.log.LogService;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * Created by Alexander Tumin on 2016-08-31
 */
@Component
@Instantiate
public class Initializer {
    @Bind
    public void bindLogService(LogService log) {
        StaticLoggerBinder.getProvider().addService(log);
    }

    @Unbind
    public void unbindLogService(LogService log) {
        StaticLoggerBinder.getProvider().removeService(log);
    }
}