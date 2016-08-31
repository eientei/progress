package org.eientei.progress.launcher;

import aQute.bnd.osgi.Builder;
import aQute.bnd.osgi.Constants;
import aQute.bnd.osgi.Jar;
import org.osgi.framework.launch.Framework;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alexander Tumin on 2016-08-16
 */
public class BundleSpec {
    private final static String MAVEN_ARTIFACTS = "Maven-Artifacts";

    private List<DepSpec> dependencies = new ArrayList<>();
    private Properties properties = new Properties();
    private File spec;

    public BundleSpec(File f) throws IOException {
        this(new FileInputStream(f));
        this.spec = f;
    }

    public BundleSpec(InputStream f) throws IOException {
        try (BufferedReader bufread = new BufferedReader(new InputStreamReader(f))) {
            for (String line; (line = bufread.readLine()) != null; ) {
                String proc = line.trim();
                if (proc.isEmpty()) {
                    continue;
                }

                String[] split = proc.split(":", 2);
                String instr = split[0].trim();
                String content = split[1].trim();
                while (content.endsWith(",")) {
                    line = bufread.readLine();
                    if (line == null) {
                        break;
                    }
                    content = content + line.trim();
                }

                if (instr.equals(MAVEN_ARTIFACTS)) {
                    String[] artifacts = content.split(",");
                    for (String artifact : artifacts) {
                        dependencies.add(new DepSpec(artifact.trim()));
                    }
                } else {
                    properties.put(instr, content);
                }
            }
        }
    }

    public void build(Framework framework, File cachedir, File builddir) throws Exception {
        File lock = null;
        if (spec != null) {
            String lockname = spec.toPath().getFileName().toString();
            lock = framework.getDataFile(lockname);
            if (lock.exists()) {
                return;
            }
        }

        if (dependencies.isEmpty()) {
            if (lock != null) {
                lock.createNewFile();
            }
            return;
        }

        if (properties.isEmpty()) {
            List<File> cp = resolveDeps(cachedir);
            for (File f : cp) {
                Path dest = builddir.toPath().resolve(f.toPath().getFileName());
                if (!dest.toFile().exists()) {
                    Files.copy(f.toPath(), dest);
                }
            }
            if (lock != null) {
                lock.createNewFile();
            }
            return;
        }

        if (!properties.containsKey(Constants.BUNDLE_NAME)) {
            properties.put(Constants.BUNDLE_NAME, dependencies.get(0).getGroupid() + "." + dependencies.get(0).getArtifactid());
        }

        if (!properties.containsKey(Constants.BUNDLE_SYMBOLICNAME)) {
            properties.put(Constants.BUNDLE_SYMBOLICNAME, properties.get(Constants.BUNDLE_NAME));
        }

        if (!properties.containsKey(Constants.BUNDLE_VERSION)) {
            properties.put(Constants.BUNDLE_VERSION, "0.0.0");
        }

        String resultName = properties.get(Constants.BUNDLE_NAME) + "-" + properties.get(Constants.BUNDLE_VERSION);

        File result = new File(builddir, resultName + ".jar");
        if (result.exists() && (spec != null && result.lastModified() < spec.lastModified())) {
            return;
        }

        List<File> cp = resolveDeps(cachedir);

        System.out.println("Building bundle " + resultName);
        Builder builder = new Builder();
        builder.setBase(builddir);
        builder.addClasspath(cp);

        for (Object keyo : properties.keySet()) {
            String key = keyo.toString();
            for (Object varo : properties.keySet()) {
                String var = varo.toString();
                properties.put(key, properties.get(key).toString().replace("${" + var + "}", (CharSequence) properties.get(var)));
            }
        }

        builder.setProperties(properties);

        Jar jar = builder.build();

        System.out.println("Bundle " + resultName + " built");
        System.out.println("Manifest:");
        jar.getManifest().write(System.out);
        System.out.println();
        jar.write(result);

        if (lock != null) {
            lock.createNewFile();
        }
    }

    private List<File> resolveDeps(File cachedir) throws IOException {
        List<File> cp = new ArrayList<>();

        for (DepSpec dep : dependencies) {
            System.out.println("Resolving dependency " + dep.toString());
            URL url = dep.constructUrl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            URL desturl = new URL(conn.getHeaderField("Location"));

            String dst = desturl.getFile().substring(desturl.getFile().lastIndexOf("/")+1);
            File out = new File(cachedir, dep.getGroupid() + "." + dst);

            if (!out.exists()) {
                System.out.print("Downloading dependency " + dst + " ... ");
                Files.copy(desturl.openStream(), out.toPath());
                System.out.println("done");
            }
            cp.add(out);
        }

        return cp;
    }
}
