package com.impl.homesecurity.job.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
@Service
@Profile("dev")
public class RunScriptUpdateDockerImagesJobService {

    private final Logger log = LoggerFactory.getLogger("Scheduled");

    @Scheduled(fixedRate = 300000)
    public void runScript() throws IOException, InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug("Run Script Update");
        }
       ProcessBuilder processBuilder = new ProcessBuilder("/host.sh");

        processBuilder.inheritIO();
        Process process = processBuilder.start();

        if (log.isDebugEnabled()) {
            log.debug("Start host.sh");
        }

        int exitValue = process.waitFor();
        if (exitValue != 0) {
            if (log.isErrorEnabled()) {
                log.error("execution of script host.sh failed!");
            }
            new BufferedInputStream(process.getErrorStream());
            throw new RuntimeException("execution of script failed!");
        }
    }
}
