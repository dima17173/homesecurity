package com.impl.homesecurity.utils;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.io.IOException;

public class JsonUtil {
    public static String readJsonFile(final String pathToFile) throws IOException, ParseException {
        final Resource resource = new FileSystemResource(pathToFile);
        final JSONParser parser = new JSONParser();
        final Object obj = parser.parse(new FileReader(resource.getFile()));
        return obj.toString();
    }
}
