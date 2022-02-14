package org.jboss.fuse.tnb.product.csb.application;

import org.jboss.fuse.tnb.common.config.TestConfiguration;
import org.jboss.fuse.tnb.common.utils.WaitUtils;
import org.jboss.fuse.tnb.product.integration.IntegrationBuilder;
import org.jboss.fuse.tnb.product.log.FileLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalSpringBootApp extends SpringBootApp {
    private static final Logger LOG = LoggerFactory.getLogger(LocalSpringBootApp.class);
    private final Path logFile;
    private Process appProcess;

    public LocalSpringBootApp(IntegrationBuilder integrationBuilder) {
        super(integrationBuilder);
        logFile = TestConfiguration.appLocation().resolve(name + ".log");
    }

    @Override
    public void start() {
        ProcessBuilder processBuilder = new ProcessBuilder(getCommand()).redirectOutput(logFile.toFile());

        LOG.info("Starting integration {}", name);
        try {
            appProcess = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("Unable to start integration process: ", e);
        }
        WaitUtils.waitFor(() -> logFile.toFile().exists(), "Waiting until the logfile is created");
        log = new FileLog(logFile);
    }

    @Override
    public void stop() {
        if (appProcess != null) {
            LOG.info("Stopping integration {}", name);
            if (appProcess.isAlive()) {
                LOG.debug("Killing integration process");
                appProcess.destroy();
            }
        }
    }

    @Override
    public boolean isReady() {
        return appProcess.isAlive();
    }

    @Override
    public boolean isFailed() {
        return !appProcess.isAlive();
    }

    private List<String> getCommand() {
        String fileName;
        Path integrationTarget = TestConfiguration.appLocation().resolve(name).resolve("target");

        List<String> cmd = new ArrayList<>(Arrays.asList(System.getProperty("java.home") + "/bin/java", "-jar"));
        fileName = integrationTarget.resolve(name + "-1.0.0-SNAPSHOT.jar").toAbsolutePath().toString();

        cmd.add(fileName);

        if (!new File(fileName).exists()) {
            throw new IllegalArgumentException("Expected file " + fileName + " does not exist, check if the maven build was successful");
        }

        LOG.debug("ProcessBuilder command: " + String.join(" ", cmd));
        return cmd;
    }
}