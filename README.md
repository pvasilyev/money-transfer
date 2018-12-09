# money-transfer
This is very simple project which aims to demo lightweight RESTful API designed for money transfers between accounts.

[![Build Status](https://travis-ci.org/pvasilyev/money-transfer.svg?branch=master)](https://travis-ci.org/pvasilyev/money-transfer)

Current data model assumes, that:
* There are Users (with ids, names, accounts references)
* There are Accounts (with ids, money info and some meta info)

In order to build this project one should run: <code>mvn clean install</code>
<br/>
In order to run the application one should run: <code>java -jar ./target/money-transfer-1.0-SNAPSHOT.jar</code>
