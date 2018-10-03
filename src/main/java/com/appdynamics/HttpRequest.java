package com.appdynamics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class HttpRequest {

    public String requestDataFromUrl(String desiredUrl)
            throws Exception
    {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try
        {
            // create the HttpURLConnection
            url = new URL(desiredUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
        finally
        {
            // close the reader
            if (reader != null)
            {
                reader.close();
            }
        }
    }

    //rudimentary JSON processing. base java doesn't have any JSON processing libs (really!)
    public HashMap<String,String> parseJson(String json){

        HashMap<String,String> hashMap = new HashMap<>();
        String strings[] = json.split(",");
        strings[0] = strings[0].substring(1);
        String last = strings[strings.length - 1];
        strings[strings.length - 1] = last.substring(0, last.length() - 1);

        for(String i : strings){
            String key = i.split((":"))[0].replace("\"","");
            String value = i.split((":"))[1].replace("\"","");
            hashMap.put(key.replaceAll("\\s",""),value.replaceAll("\\s",""));
        }
        return hashMap;

    }
}
