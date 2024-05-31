package io.apitoolkit.springboot;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Utils {

    public static HashMap<String, String> redactHeaders(HashMap<String, String> headers, List<String> redactedHeaders) {
        HashMap<String, String> redactedHeadersMap = new HashMap<>(headers);

        for (String headerName : headers.keySet()) {
            if (redactedHeaders.contains(headerName) || redactedHeaders.contains(headerName.toLowerCase())) {
                redactedHeadersMap.put(headerName, "[CLIENT_REDACTED]");
            }
        }
        return redactedHeadersMap;
    }

    public static byte[] redactJson(byte[] data, List<String> jsonPaths, Boolean debug) {
        if (jsonPaths == null || jsonPaths.isEmpty() || data.length == 0) {
            return data;
        }
        try {

            String jsonData = new String(data, StandardCharsets.UTF_8);
            DocumentContext jsonObject = JsonPath.parse(jsonData);
            for (String path : jsonPaths) {
                try {
                    jsonObject = jsonObject.set(path, "[CLIENT_REDACTED]");
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }
            }
            String redactedJson = jsonObject.jsonString();
            return redactedJson.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
            }
            return data;
        }

    }
}
