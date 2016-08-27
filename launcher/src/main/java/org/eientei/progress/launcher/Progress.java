package org.eientei.progress.launcher;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Alexander Tumin on 2016-08-15
 */
public class Progress {
    public static void main(String[] args) throws Exception {
        File rootdir = new File(Progress.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();
        Framework framework = startFramework();

        File specsdir = new File(rootdir, "specs");
        File bundlesdir = new File(rootdir, "bundles");
        File cachedir = new File(rootdir, "mvncache");
        File builddir = new File(rootdir, "build");

        if (specsdir.exists()) {
            cachedir.mkdirs();
            builddir.mkdirs();
        }

        List<BundleSpec> specs = listSpecs(specsdir);
        for (BundleSpec spec : specs) {
            spec.build(cachedir, builddir);
        }

        loadBundles(framework, builddir);
        loadBundles(framework, bundlesdir);

        System.out.println("Framework running");
        framework.waitForStop(0);
    }

    private static List<BundleSpec> listSpecs(File specsdir) throws IOException {
        return listSpecs(specsdir, new ArrayList<BundleSpec>());
    }

    private static List<BundleSpec> listSpecs(File specsdir, List<BundleSpec> specs) throws IOException {
        File[] files = specsdir.listFiles();
        if (files == null) {
            return specs;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                listSpecs(f, specs);
            } else if (f.isFile()) {
                if (f.getName().toLowerCase().endsWith(".bndspec")) {
                    specs.add(new BundleSpec(f));
                }
            }
        }
        return specs;
    }

    private static void loadBundles(Framework framework, File rootdir) throws Exception {
        File[] files = rootdir.listFiles();
        if (files == null) {
            return;
        }
        BundleContext context = framework.getBundleContext();
        List<Bundle> bundles = new ArrayList<>();
        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
                System.out.println("Installing bundle " + f.getName());
                bundles.add(context.installBundle("file:" + f.getAbsolutePath()));
            }
        }

        for (Bundle b : bundles) {
            System.out.println("Starting bundle " + b.getSymbolicName());
            b.start();
        }
    }

    /*
    private static void bootRefs(Framework framework, List<BundleSpec> boot) throws Exception {
        BundleContext context = framework.getBundleContext();
        List<Bundle> bundles = new ArrayList<>();
        for (BundleSpec ref : boot) {
            URL url = ref.constructUrl(BundleSpec.MAVEN_CENTRAL);
            String loc = ref.toString();
            if (context.getBundle(loc) == null) {
                System.out.println("Installing bundle " + loc);
                bundles.add(context.installBundle(loc, url.openStream()));
            }
        }

        for (Bundle b : bundles) {
            System.out.println("Starting bundle " + b.getLocation());
            b.start();
        }

    */
    private static Framework startFramework() throws BundleException {
        FrameworkFactory factory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        Map<String, String> config = new HashMap<>();
        File f = new File(Progress.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        File root = f.getParentFile();
        String cache = new File(root, "cache").getAbsolutePath();
        config.put(Constants.FRAMEWORK_STORAGE, cache);
        config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "slf4j.impl,org.slf4j");
        config.put(Constants.FRAMEWORK_STORAGE_CLEAN, "true");
        Framework framework = factory.newFramework(config);
        framework.start();
        System.out.println("Framework init");
        return framework;
    }
}
