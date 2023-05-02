package com.webserver.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ContentTypeDemo {
    public static void main(String[] args) throws IOException {
        String contentType=Files.probeContentType(
                new File("./index.mp4").toPath());
        System.out.println(contentType);
    }
}
