# Development and Testing Guide <br/> Pip.Services Runtime for Java

This document provides high-level instructions on how to build and test the microservice.

* [Environment Setup](#setup)
* [Installing](#install)
* [Building](#build)
* [Testing](#test)
* [Release](#release)
* [Contributing](#contrib) 

## <a name="setup"></a> Environment Setup

This is a Java project and you have to install JDK and Maven build tool. 
JDK can be downloaded from official Oracle website: http://www.oracle.com/technetwork/java/javase/downloads/index.html
Maven can download them from official Apache website: https://maven.apache.org/download.cgi

To work with GitHub code repository you need to install Git from: https://git-scm.com/downloads

If you are planning to develop and test using persistent storages other than flat files
you may need to install database servers:
- Download and install MongoDB database from https://www.mongodb.org/downloads

## <a name="install"></a> Installing

After your environment is ready you can check out source code from the Github repository:
```bash
git clone git@github.com:pip-services/pip-services-runtime-java.git
```

## <a name="build"></a> Building

To compile the project run the following command

```bash
mvn compile
```

To install runtime into local repository run

```bash
mvn install
```

## <a name="test"></a> Testing

Before you execute tests you need to set configuration options in config.json file.
As a starting point you can use example from config.example.json:

```bash
copy config/config.example.json config/config.json
``` 

After that check all configuration options. Specifically, pay attention to connection options
for database and dependent microservices. For more information check [Configuration Guide](Configuration.md) 

Command to run unit tests:
```bash
mvn test
```

## <a name="release"></a> Release

Create ${user.home}/.m2/settings.xml file with your credentials for deployment:

```xml
<settings>
  ...
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-user-name</username>
      <password>your-user-password</password>
    </server>
  </servers>
  ...
</settings>
```

Formal release process consistents of few steps. 
First of all it is required to tag guthub repository with a version number:

```bash
git tag vx.y.y
git push origin master --tags
```

Then the release can be pushed to the central Maven repository. 
To be able to make the release contributor must have an account with proper permissions.

```bash
mvn clean deploy
```

With the property autoReleaseAfterClose set to false you can manually inspect the staging repository 
in the Nexus Repository Manager and trigger a release of the staging repository later withЖ

```bash
mvn nexus-staging:release
```

If you find something went wrong you can drop the staging repository with
```bash
mvn nexus-staging:drop
```

Microservice releases additionally require generation and publishing 
binary packages at http://downloads.pipservices.org


## <a name="contrib"></a> Contributing

Developers interested in contributing should read the following instructions:

- [How to Contribute](http://www.pipservices.org/contribute/)
- [Guidelines](http://www.pipservices.org/contribute/guidelines)
- [Styleguide](http://www.pipservices.org/contribute/styleguide)
- [ChangeLog](../CHANGELOG.md)

> Please do **not** ask general questions in an issue. Issues are only to report bugs, request
  enhancements, or request new features. For general questions and discussions, use the
  [Contributors Forum](http://www.pipservices.org/forums/forum/contributors/).

It is important to note that for each release, the [ChangeLog](../CHANGELOG.md) is a resource that will
itemize all:

- Bug Fixes
- New Features
- Breaking Changes