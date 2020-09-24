# Java Service Template

Simply get this repository, and check out the develop branch. Follow the initial steps below, then see the relevant section in this readme by clicking the link below.

This gives us one codebase to maintain, but that codebase can be easily modified to run with or without Dropwizard, and with or without Kubernetes and Docker as detailed in the sections below.

#Initial Steps
Then run templaterename.sh with two arguments, the name of your new service and the Kubernetes namespace:
```./templaterename.sh servicename namespace```

That will create a new web service, which will be created in a new directory. It will be fully runnable using the arguments ```server src/main/resources/config.yaml```, with multiple features built in:


#Run Dropwizard with Spring

This is what you get at the start, so no extra steps are required.

Dropwizard is running on port 8089 (search for that string to change the port, it is in more than one place)
An example healthcheck is enabled
Spring is configured and running
Configuration files are wired in - see src/main/resources/config.yaml and WorkerConfiguration.java
Logging is in JSON for easy input into ElasticSearch
All Jenkins, Docker, and Kubernetes items are fully enabled and working - see [this page](https://usermind.atlassian.net/wiki/spaces/LOH/pages/545587201/How+to+configure+Docker+and+Kubernetes) for more details

#Run a simple service without Dropwizard or Spring
 
This mainly consists of removing the Dropwizard and Spring files. To make that easy there is a script in the root directory, "removedropwizard.sh". Just run that. It doesn't touch AppMain, though, so after running it you'll need to open AppMain.java and just remove the contents of the main method and header lines. 

#Remove Kubernetes and/or Docker
1) Delete the docker directory
2) Delete the kubernetes directory
3) Remove the Docker plugin section from pom.xml
4) Copy noDockerJenkins.yaml to jenkins.yaml
5) In Jenkinsfile, delete all the stages after "Build". There is a comment there to mark the spot.

#Stop your build from creating a shaded jar
This is when you are building a library, and this would create a much bigger jar than useful.

Simply go into the pom.xml and remove the Maven Shade Plugin section.

