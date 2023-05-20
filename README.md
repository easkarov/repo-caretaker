# Repository Caretaker


[![Bot module](https://github.com/EmiAsk/repo-caretaker/actions/workflows/bot.yml/badge.svg)](https://github.com/EmiAsk/repo-caretaker/actions/workflows/bot.yml)
[![Scrapper module](https://github.com/EmiAsk/repo-caretaker/actions/workflows/scrapper.yml/badge.svg)](https://github.com/EmiAsk/repo-caretaker/actions/workflows/scrapper.yml)
Used techonologies: Spring, PostgreSQL, Liquibase, Docker, Kafka, RabbitMQ, Lib for Telegram Bot, Prometheus, Grafana.

<img src="https://github.com/EmiAsk/repo-caretaker/assets/74973350/255b1cf9-cadf-4327-b669-a546fb6ae0b1" alt="Github looks after repositories :3" width="300px">


## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is simple Lorem ipsum dolor generator.
	
## Technologies
Project is created with:
* Lorem version: 12.3
* Ipsum version: 2.33
* Ament library version: 999
	
## Setup
#### Pre-reqs:
Make sure you have installed:
1) JDK 17+: https://www.oracle.com/java/technologies/downloads/#java17
2) Maven: https://maven.apache.org/download.cgi
3) Docker: https://www.docker.com/products/docker-desktop/

Set environment variables: 


#### Building
To build project, type in project root:

```$ mvn clean install```

#### Running
To run project, type in project root:

```$ docker compose up```

```
$ cd ../lorem
$ npm install
$ npm start
