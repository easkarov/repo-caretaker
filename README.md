<img src="https://github.com/EmiAsk/repo-caretaker/assets/74973350/255b1cf9-cadf-4327-b669-a546fb6ae0b1" alt="Github looks after repositories :3" width="300px">

# Repository Caretaker


[![Bot module](https://github.com/EmiAsk/repo-caretaker/actions/workflows/bot.yml/badge.svg)](https://github.com/EmiAsk/repo-caretaker/actions/workflows/bot.yml)
[![Scrapper module](https://github.com/EmiAsk/repo-caretaker/actions/workflows/scrapper.yml/badge.svg)](https://github.com/EmiAsk/repo-caretaker/actions/workflows/scrapper.yml)


## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
Telegram Bot that monitors changes in specified GitHub repositories and StackOverflow questions, and notifies you about them. Feel free to add, remove links on repos and questions, get detailed information about update including what exactly has changed (issues count, new commit and etc).

Be sure to turn on the notifications so bot won't let you oversleep :)
	
## Technologies
Project is created with:
* Spring 5
* Hibernate 5
* PostgreSQL 13
* RabbitMQ 3
* Telegram Bot API
* Liquibase
* Prometheus
* Grafana
	
## Setup
### Pre-reqs
* Make sure you have installed:
	* JDK 17+: https://www.oracle.com/java/technologies/downloads/#java17
	* Maven: https://maven.apache.org/download.cgi
	* Docker: https://www.docker.com/products/docker-desktop/

* Set environment variables before running as shown in ***.env.example***

### Build
To build project, type in project root:

```$ mvn clean install```

### Run
To run project, type in project root:

```$ docker compose up```

## Usage
> ***Following examples show how to work with Telegram Bot***

### Supported Commands:
#### /track
> Store specified link to keep track

![image](https://github.com/EmiAsk/repo-caretaker/assets/74973350/ecb39cb1-c244-4a86-a7ab-0b8a2a37eedc)

#### /list
> List all tracked links


