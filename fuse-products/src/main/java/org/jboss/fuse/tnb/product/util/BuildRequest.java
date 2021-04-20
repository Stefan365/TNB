package org.jboss.fuse.tnb.product.util;

import org.jboss.fuse.tnb.common.config.TestConfiguration;
import org.jboss.fuse.tnb.common.utils.MapUtils;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BuildRequest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH-mm-ss-SSS").withZone(ZoneId.systemDefault());

    private File baseDirectory;
    private List<String> goals = new ArrayList<>();
    private List<String> profiles = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();
    private MavenOutputHandler outputHandler = new MavenOutputHandler();
    private boolean createLogFile = true;
    private Path logFile;

    private BuildRequest() {
        logFile = TestConfiguration.appLocation().resolve("maven-invocation-" + FORMATTER.format(Instant.now()) + ".log");
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public Properties getProperties() {
        return MapUtils.toProperties(properties);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public MavenOutputHandler getOutputHandler() {
        return outputHandler;
    }

    public void setOutputHandler(MavenOutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public boolean shouldCreateLogFile() {
        return createLogFile;
    }

    public void setCreateLogFile(boolean createLogFile) {
        this.createLogFile = createLogFile;
    }

    public Path getLogFile() {
        return logFile;
    }

    public void setLogFile(Path logFile) {
        this.logFile = logFile;
    }

    public static class Builder {
        private final BuildRequest request;

        public Builder() {
            request = new BuildRequest();
        }

        public Builder withBaseDirectory(File dir) {
            request.setBaseDirectory(dir);
            return this;
        }

        public Builder withBaseDirectory(Path dir) {
            request.setBaseDirectory(dir.toFile());
            return this;
        }

        public Builder withGoals(String... goals) {
            request.setGoals(Arrays.asList(goals));
            return this;
        }

        public Builder withProfiles(String... profiles) {
            request.setProfiles(Arrays.asList(profiles));
            return this;
        }

        public Builder withProperties(Map<String, String> properties) {
            request.setProperties(properties);
            return this;
        }

        public Builder withOutputHandler(MavenOutputHandler handler) {
            request.setOutputHandler(handler);
            return this;
        }

        public Builder withCreateLogFile(boolean create) {
            request.setCreateLogFile(create);
            return this;
        }

        public Builder withLogFile(Path logFile) {
            request.setLogFile(logFile);
            return this;
        }

        public BuildRequest build() {
            return request;
        }
    }
}
