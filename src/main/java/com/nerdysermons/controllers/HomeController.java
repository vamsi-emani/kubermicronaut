package com.nerdysermons.controllers;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.management.endpoint.health.DetailsVisibility;
import io.micronaut.management.endpoint.health.HealthEndpoint;
import io.micronaut.management.health.indicator.diskspace.DiskSpaceIndicatorConfiguration;
import jakarta.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller("/home")
public class HomeController {

    @Inject
    ApplicationContext appContext;

    @Get("/stats")
    public Object index() {
        try {
            HealthEndpoint endpoint = appContext.getBean(HealthEndpoint.class);
            endpoint.setDetailsVisible(DetailsVisibility.ANONYMOUS);
            DiskSpaceIndicatorConfiguration config = appContext.getBean(DiskSpaceIndicatorConfiguration.class);
            Map map = getDiskSpaceStats(config);
            String hostname = InetAddress.getLocalHost().getHostName();
            map.put("hostname", hostname);
            return map;
        }
        catch (Exception e) {
            return "Hello world!";
        }
    }

    @Get("/filecopy")
    public Object memory(){
        try {
            ClassPathResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get();
            byte[] buffer = loader.getResourceAsStream("classpath:holmes.pdf").get().readAllBytes();
            File targetFile = new File("Holmes " + System.currentTimeMillis() + ".pdf");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            outStream.close();
            return targetFile.getPath();
        }
        catch (Exception e) {
            return "FILE-NOT-COPIED";
        }
    }


    Map getDiskSpaceStats(DiskSpaceIndicatorConfiguration  configuration) {
        File path = configuration.getPath();
        long threshold = configuration.getThreshold();
        long freeSpace = path.getUsableSpace();
        Map<String, Object> detail = new LinkedHashMap<>(3);

        if (freeSpace >= threshold) {
            detail.put("total-disk-space", path.getTotalSpace());
            detail.put("free-disk-space", freeSpace);
            detail.put("threshold", threshold);
        } else {
            detail.put("error", String.format(
                    "Free disk space below threshold. "
                            + "Available: %d bytes (threshold: %d bytes)",
                    freeSpace, threshold));
        }
        return detail;
    }

}
