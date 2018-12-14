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

1. Download ".jar" artifact (link is TBD).
1. Navigate Terminal to the downloded and type in: <code>java -jar money-transfer-1.0.jar</code>.
1. Open in browser following link: http://localhost:8080/money-transfer.
  1. This would open up Swagger-friendly UI where user can issue one or several commands.
  1. Please note, that by default this RESTful API is backed by some sample data which you can find here: [sample-data](src/main/resources/sample-data.json)

## Developer Guide

In order to build this project one should run: <code>mvn clean install</code>
<br/>
In order to run the application one should run: <code>java -jar ./target/money-transfer-1.0-SNAPSHOT.jar</code>
