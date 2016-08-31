package org.eientei.progress.bundles.progress.log.adapter;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Alexander Tumin on 2016-08-30
 */
public class LogServiceProvider implements LogService {
    private List<LogService> services = new CopyOnWriteArrayList<>();
    private Queue<MissedMessage> misses = new LinkedBlockingQueue<>();

    public synchronized void flush() {
        MissedMessage msg;
        while ((msg = misses.poll()) != null) {
            if (msg.getThrowable() == null) {
                for (LogService service : services) {
                    service.log(msg.getSeverirty(), msg.getMsg());
                }
            } else {
                for (LogService service : services) {
                    service.log(msg.getSeverirty(), msg.getMsg(), msg.getThrowable());
                }
            }
        }
    }

    public void addService(LogService service) {
        services.add(service);
        flush();
    }

    public void removeService(LogService service) {
        services.remove(service);
    }

    @Override
    public synchronized void log(int i, String s) {
        if (services.isEmpty()) {
            misses.add(new MissedMessage(i,s));
        } else {
            for (LogService service : services) {
                service.log(i, s);
            }
        }
    }

    @Override
    public synchronized void log(int i, String s, Throwable throwable) {
        if (services.isEmpty()) {
            misses.add(new MissedMessage(i,s,throwable));
        } else {
            for (LogService service : services) {
                service.log(i, s, throwable);
            }
        }
    }

    @Override
    public void log(ServiceReference serviceReference, int i, String s) {
        log(i,s);
    }

    @Override
    public void log(ServiceReference serviceReference, int i, String s, Throwable throwable) {
        log(i,s,throwable);
    }
}
