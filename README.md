# Elide Test

This is code I have been using to tinker with Elide. It is a mess at this point as I've been trying a lot of different things, and this is test code that I didn't intend to keep - so I figured if things got to a workable state, then I'd clean it up and move the relevant parts into an actual code base.

I was trying to make it work with a Noop Data Store as our data queries will need some tinkering, so that seemed to be the right way to go - though I couldn't find any example code actually using a Noop Data Store so I am not quite sure how to add methods to it - but figured I'd get to that when I got to that!

I am using a base pom, so to share this I just got the effective pom and used it. I am running it in IntelliJ with the arguments 
```server src/main/resources/config.yaml ```

It runs on port 8089. That comes up, I can see the dictionary having the test classes, but can't get any Elide URLs to work. I also tried bringing the Elide standalone server code in and just running a second Jetty on a second port just to see if that would work. It comes up, but on the Elide port I just see "Failed to load API definition" errors.

For the comments below, all packages/classes are under the com.usermind.elidetest package.

In the spring package I'm creating the NoopDataStoreIn the SpringConfiguration class. You can also see the NoopBean from one of your sample programs, as well as the ControllerProperties and ElideConfigProperties classes. 

In the dropwizard package I have an ElideBundle. This is basically the sample ElideBundle from your old Dropwizard package, I just tried to put in the NoopDataStore.

There is a DropWizardService class in the same package. 
 * On line 64 is a commented out section using that bundle
 * On line 117 is a commented out section trying to just run ElideStandalone
 * On ine 161 is a section creating JsonApiEndpoint and registering it with Jersey
 
 

