# Collect AWS infrastructure data and add that to the transaction context in AppDynamics

## Use Case

Use Business iQ to query for specific transactions related to regions or AWS instances.

## Installation

1. Build this project using maven, take the jar output from the aws-metadata-collector\target and copy to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins

2. Add -Dallow.unsigned.sdk.extension.jars=true -Disdk.file.based.data.collector.file.name=PATH -Disdk.aws.metadata.collector.class=com.x.x.x.x -Disdk.aws.metadata.data.collector.method=getXYZ to the command line process.

3. Restart the Java Agent process.
