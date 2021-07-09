package de.uni_s.ipvs.mcl.assignment3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    public static String executeGet(String targetURL){
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(targetURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is;
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = conn.getErrorStream();
            else
                is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null){
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            if(conn != null){
                conn.disconnect();
            }
        }
    }
}
