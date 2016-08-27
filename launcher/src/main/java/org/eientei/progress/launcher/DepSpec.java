package org.eientei.progress.launcher;

import java.net.URL;

/**
 * Created by Alexander Tumin on 2016-08-26
 */
public class DepSpec {
    public final static String MAVEN_CENTRAL = "https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy";

    private String groupid;
    private String artifactid;
    private String version = "LATEST";
    private String classifier = null;
    private String packaging = null;

    public DepSpec(String spec) {
        parseSpec(spec);
    }

    private void parseSpec(String spec) {
        String[] parts = spec.split(":");

        groupid = parts[0];
        artifactid = parts[1];

        if (parts.length > 2) {
            version = parts[2];
        }

        if (parts.length > 3) {
            packaging = parts[3];
        }

        if (parts.length > 4) {
            classifier = parts[4];
        }
    }

    public URL constructUrl() {
        return constructUrl(MAVEN_CENTRAL);
    }

    public URL constructUrl(String site) {
        StringBuilder sb = new StringBuilder();
        sb.append(site)
                .append("&g=").append(groupid)
                .append("&a=").append(artifactid)
                .append("&v=").append(version);
        if (classifier != null) {
            sb.append("&c=").append(classifier);
        }
        if (packaging != null) {
            sb.append("&p=").append(packaging);
        }

        try {
            return new URL(sb.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(groupid)
                .append(":").append(artifactid)
                .append(":").append(version);
        if (packaging != null) {
            sb.append(":").append(packaging);
        }
        if (classifier != null) {
            sb.append(":").append(classifier);
        }
        return sb.toString();
    }

    public String getGroupid() {
        return groupid;
    }

    public String getArtifactid() {
        return artifactid;
    }

    public String getVersion() {
        return version;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getPackaging() {
        return packaging;
    }
}
