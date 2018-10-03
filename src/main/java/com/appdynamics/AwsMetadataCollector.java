package com.appdynamics;

import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.template.ADataCollector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import com.appdynamics.instrumentation.sdk.Rule;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AwsMetadataCollector extends ADataCollector {

    private static Map awsResponse;
    private static final String CLASS_TO_INSTRUMENT = System.getProperty("isdk.aws.metadata.collector.class");
    private static final String METHOD_TO_INSTRUMENT = System.getProperty("isdk.aws.metadata.data.collector.method");


    @Override
    public List<Rule> initializeRules() {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        rules.add(new Rule.Builder(CLASS_TO_INSTRUMENT).methodMatchString(METHOD_TO_INSTRUMENT).build());
        return rules;
    }

    @Override
    public void storeData(Object invokedObject, String className, String methodName, Object[] paramValues,
            Throwable thrownException, Object returnValue, ISDKDataContext sdkContext) throws ReflectorException {
        try {

            if(awsResponse==null){
                try{
                    getLogger().info("Init AwsMetadataCollector");
                    HttpRequest httpRequest = new HttpRequest();
                    String jsonResponse = httpRequest.requestDataFromUrl("http://169.254.169.254/latest/dynamic/instance-identity/document");
                    awsResponse = Collections.unmodifiableMap(httpRequest.parseJson(jsonResponse));
                    getLogger().info("AWS Response " + awsResponse);
                }catch(Exception e){
                    awsResponse = Collections.EMPTY_MAP;
                    getLogger().info("Exception retrieving data from AWS", e);
                }
            }

            sdkContext.storeData("privateIp", awsResponse.get("privateIp"));
            sdkContext.storeData("version", awsResponse.get("version"));
            sdkContext.storeData("instanceId", awsResponse.get("instanceId"));
            sdkContext.storeData("instanceType", awsResponse.get("instanceType"));
            sdkContext.storeData("availabilityZone", awsResponse.get("availabilityZone"));
            sdkContext.storeData("region", awsResponse.get("region"));

        }catch(Exception e){
            getLogger().info("EXCEPTION",e);
        }
    }

    public boolean addToSnapshot() {
        return true;
    }

    public boolean addToAnalytics() {
        return true;
    }
}
