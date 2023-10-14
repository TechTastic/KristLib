package io.github.techtastic.kristlib.util.http;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class KristHTTPHandler {
    @NotNull
    public static JsonObject getInfoFromHTTPWithContent(String urlString, HTTPRequestType type, Map<String, String> contents) {
        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> content : contents.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(content.getKey(), StandardCharsets.UTF_8));
                postData.append('=');
                postData.append(URLEncoder.encode(content.getValue(), StandardCharsets.UTF_8));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(type.getType());

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(postDataBytes);
            os.flush();
            os.close();

            // Get Response
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            con.disconnect();

            return KristUtil.validateResponse(KristUtil.DECODER.decode(sb.toString()), KristUtil.HTTP_LOGGER);
        } catch (Exception e) {
            KristUtil.HTTP_LOGGER.severe("Failed to retrieve response from HTTP request!");
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static JsonObject getInfoFromHTTP(String urlString, HTTPRequestType type) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod(type.getType());

            // Get Response
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            con.disconnect();

            return KristUtil.validateResponse(KristUtil.DECODER.decode(sb.toString()), KristUtil.HTTP_LOGGER);
        } catch (Exception e) {
            KristUtil.HTTP_LOGGER.severe("Failed to retrieve response from HTTP request!");
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static JsonObject getInfoFromHTTP(KristURLConstants url, HTTPRequestType type) {
        return getInfoFromHTTP(url.getUrl(), type);
    }

    @NotNull
    public static JsonObject getInfoFromHTTPWithContent(KristURLConstants url, HTTPRequestType type, Map<String, String> contents) {
        return getInfoFromHTTPWithContent(url.getUrl(), type, contents);
    }
}
