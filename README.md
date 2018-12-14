# Money Transfer
This is very simple project which aims to demo lightweight RESTful API designed for money transfers between accounts.

[![Build Status](https://travis-ci.org/pvasilyev/money-transfer.svg?branch=master)](https://travis-ci.org/pvasilyev/money-transfer)

## Overview
Current data model assumes, that:
* There are Users (with ids, names, accounts references)
* There are Accounts (with ids, money info and some meta info)
* Class-diagram might look like this:

![Data Model](src/main/resources/docs/data-model.png?raw=true "Data Model")

## User Guide

In order to run the project one should follow the steps:

1. Download ".jar" artifact (link for v1.0 release is [here](../../releases/download/v1.0/money-transfer-1.0.jar)).
1. Navigate Terminal to the downloded and type in: <code>java -jar money-transfer-1.0.jar</code>.
1. Open in browser following link: http://localhost:8080/money-transfer.
   1. This would open up Swagger-friendly UI where user can issue one or several commands.
   1. Please note, that by default this RESTful API is loaded with some sample data which you can find here: [sample-data](src/main/resources/sample-data.json)
1. To retrieve some user (e.g. Clark Kent) - find corresponding method and supply <code>ZJFCHF2539</code> as user-id.
   1. Another user-id that can be used - is <code>DGND1Y82FF</code> (Bruce Wayne).
   1. Simple scenario might be to transfer money from Bruce's Savings Account (<code>ZW4LIHK67K</code>) to Clark's Mortgage Account (<code>ZDO2OMTS87</code>)

## Developer Guide

In order to build this project one should run: <code>mvn clean install</code>
<br/>
In order to run the application one should run: <code>java -jar ./target/money-transfer-1.1-SNAPSHOT.jar</code>

### Implementation Considerations

This section provides several rationales on the design / code decisions that were made while implementing Money Transfer API.

* RESTful API implemented in Java language and primary make use of following technologies:
  * JAX-RS as a Java API for RESTful Web Services.
  * Jersey as an implementation of JAX-RS spec.
  * Jetty as a light-weight servlet container which is running in [embedded mode](https://www.eclipse.org/jetty/documentation/9.4.x/embedding-jetty.html).
  * HK2 as a light-weight framework to make use of dependency-injection. Spring as a framework is way too heavy.
  * Swagger / Swagger UI as a framework for documenting RESTful interfaces.
  * Log4j/Slf4j is used as a logging framework which by default prints out all log-entries to the console.
* For sake of demoing RESTful API all the objects are stored in-memory. I.e. server is not backed by any database and doesn't do any data persistance. However it can be enhanced for future needs (one should make another implementation of [BankDao](src/main/java/com/money/transfer/dao/BankDao.java)).
  * Upon the bootstrapping of embedded Jetty the static content ((sample-data)[src/main/resources/sample-data.json]) is loaded into memory. This is solely for demo purposes.
* In order to make this program working as a standalone program - the RESTful API itself is running in embedded Jetty.
* The functionality provided in the main (BankV1Resource)[src/main/java/com/money/transfer/api/BankV1Resource.java] is covered by JUnit tests which internally bootstrap similar embedded jetty and exposes RESTful endpoints. (BankV1ResourceTest)
[src/test/java/com/money/transfer/BankV1ResourceTest.java] can be used for more details.

### Known Implementation Features

1. Currently the RESTful API provides only write-once semantics for User/Account. I.e. one can create User/Account, but there is no **update** operation exposed yet. It can be enhanced easily in future.
1. All modification-operations (such as create-user or create-account) are <code>synchronized</code>. That's due to following rationale. It's not so frequent that sytem will receieve many concurrent create-requests (or update) - like to update User's <code>firstName</code>/<code>lastName</code>.
1. On the other hand the <code>transfer</code> operation is going to be the most contended operation. So it's not wrapped with <code>synchronized</code> - but locks are rather taken on Account objects itself. Classical approach for that is used. It's when first lock is obtained on account with smaller id and then on the second account.
1. Money Transfer API is keeps all data in-memory, so depending on the startup options the limits of total users / accounts that are kept in-memory will vary. This is easy tunable / extendable.
1. Server is always running on 8080 port. For sake of sampleness of this project it's always 8080, but it can be easily enhanced or be made configurable.
