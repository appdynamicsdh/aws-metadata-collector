package com.appdynamics;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AwsMetadataCollectorTest {

    public static final String AWS_METADATA = "{  \"devpayProductCodes\" : null,  \"marketplaceProductCodes\" : null,  \"privateIp\" : \"172.31.6.226\",  \"version\" : \"2017-09-30\",  \"instanceId\" : \"i-0a3da0048f5c16ff5\",  \"billingProducts\" : null,  \"instanceType\" : \"t2.xlarge\",  \"imageId\" : \"ami-5e8bb23b\",  \"availabilityZone\" : \"us-east-2a\",  \"kernelId\" : null,  \"ramdiskId\" : null,  \"accountId\" : \"975944588697\",  \"architecture\" : \"x86_64\",  \"pendingTime\" : \"2018-08-08T14:12:24Z\",  \"region\" : \"us-east-2\"}";


    @Test
    public void testE2E() {
        assertEquals(AWS_METADATA,makeMockHttpRequest());
    }

    @Test
    public void testFailureCondition() throws Exception{

        HttpRequest httpRequest = new HttpRequest();

        assertThrows(Exception.class, () -> {
            httpRequest.requestDataFromUrl("http://localhost:8000/api/endpoint");
        });
    }

    @Test
    public void testJsonProcessing() {
        HttpRequest httpRequest = new HttpRequest();
        HashMap<String,String> result = httpRequest.parseJson(makeMockHttpRequest());
        assertEquals(result.get("privateIp"),"172.31.6.226");
    }

    public String makeMockHttpRequest(){
        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
            httpServer.createContext("/api/endpoint", new HttpHandler() {
                public void handle(HttpExchange exchange) throws IOException {
                    byte[] response = AWS_METADATA.getBytes();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                }
            });

            httpServer.start();
            HttpRequest httpRequest = new HttpRequest();
            return httpRequest.requestDataFromUrl("http://localhost:8000/api/endpoint");



        } catch (Exception e) {
        } finally {
            httpServer.stop(0);
        }
        return null;
    }

}
