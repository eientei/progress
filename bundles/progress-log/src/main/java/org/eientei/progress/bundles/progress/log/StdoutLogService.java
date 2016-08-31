package org.eientei.progress.bundles.progress.log;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * Created by Alexander Tumin on 2016-08-31
 */
@Component
@Provides
@Instantiate
public class StdoutLogService implements LogService {
    private static long START_TIME = System.currentTimeMillis();

    @Override
    public void log(int i, String s) {
        log(i, s, null);
    }

    @Override
    public void log(int i, String s, Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%6d", System.currentTimeMillis() - START_TIME));
        switch (i) {
            case LOG_DEBUG:
                sb.append(" [DEBUG] ");
                break;
            case LOG_ERROR:
                sb.append(" [ERROR] ");
                break;
            case LOG_INFO:
                sb.append(" [INFO]  ");
                break;
            case LOG_WARNING:
                sb.append(" [WARN]  ");
                break;
        }
        sb.append(s);
        System.out.println(sb.toString());
        if (throwable != null) {
            throwable.printStackTrace(System.out);
        }
    }

    @Override
    public void log(ServiceReference serviceReference, int i, String s) {
        log(i, s, null);
    }

    @Override
    public void log(ServiceReference serviceReference, int i, String s, Throwable throwable) {
        log(i, s, throwable);
    }
}
