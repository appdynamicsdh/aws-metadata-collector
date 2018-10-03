# AppDynamics file based data collector

## Use Case

Takes the first line from a file and adds it as a "Version" Data Collector for use in AppDynamics BiQ and Transaction Snapshots

## Installation

1. Build this project using maven, take the jar output from the file-data-collector\target and copy to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins

2. Add -Dallow.unsigned.sdk.extension.jars=true -Disdk.file.based.data.collector.file.name=PATH -Disdk.file.based.data.collector.class=com.x.x.x.x -Disdk.file.based.data.collector.method=getXYZ to the command line process.

3. Restart the Java Agent process.

"# aws-metadata-collector" 
