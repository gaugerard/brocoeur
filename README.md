# BROCOEUR : How to test Nerima (producer) - Games (consumer) RabbitMq

In order to test the application features using a real RabbitMq queueing system, follow the below steps.
### Prerequisites :
- Having `docker` installed and running.


### Steps :
1) Execute the `docker-compose up` command in the `script` folder.
2) Run `nerima` main method using a mvn configuration (nerima will create the necessary exchange and queues for RabbitMq).
3) Run `games` main method using a mvn configuration (both `nerima` and `games` needs to be running in parallel).
4) Go on `http://localhost:15672/` to see information about rabbitMq (which is available when the docker container for rabbitMq is up and running thanks to the docker-compose up command).
5) Using `PostMan` you can post `ServiceRequest` to the following endpoint : `http://localhost:9080/api/nerima/gamble`.

#### DIRECT ServiceRequest
```json
{
  "serviceRequestTypes": "DIRECT",
  "playerRequestList": [
    {
      "userId": "8",
      "gameStrategyTypes": "COIN_TOSS_RANDOM",
      "offlineGameStrategyTypes": null,
      "amountToGamble": "50",
      "linkedJobId": null
    }
  ],
  "timeToLive": null
}
```

#### OFFLINE ServiceRequest
```json
{
  "serviceRequestTypes": "OFFLINE",
  "playerRequestList": [
    {
      "userId": "8",
      "gameStrategyTypes": null,
      "offlineGameStrategyTypes": "OFFLINE_COIN_TOSS_RANDOM",
      "amountToGamble": "100",
      "linkedJobId": null
    }
  ],
  "timeToLive": 3
}
```

6) You can see that the `ServiceRequest` is correctly send from `nerima` to `games` and you can also see if the specified user won or lost in the logs of the services.



For Cassandra:
1) Execute the `docker-compose up` command in the `script\docker\cassandra` folder.
2) Run `docker-compose exec cassandra1 /bin/bash;`.
3) Run `cqlsh`.
4) Run `cqlsh> CREATE KEYSPACE brocoeurkeyspace   WITH REPLICATION = {     'class' : 'SimpleStrategy', 'replication_factor' : 1    } ;`.
5) Run `cqlsh> USE brocoeurkeyspace ;`.
6) Run `cqlsh:brocoeurkeyspace> CREATE TABLE Game( gameid int PRIMARY KEY, gamename text);`.
7) Run `cqlsh:brocoeurkeyspace> CREATE TABLE User( userid int PRIMARY KEY, pseudo text);`.
8) Run `cqlsh:brocoeurkeyspace> CREATE TABLE UserWinLossByGame( game_id int, user_id int, gamename text, pseudo text, numberofwin int, numberofloss int, PRIMARY KEY ((game_id, user_id)));`.
8) Run `cqlsh:brocoeurkeyspace> CREATE TABLE UserMoney( userId int PRIMARY KEY, money int);`.
8) Run `cqlsh:brocoeurkeyspace> CREATE TABLE ServiceRequestStatus( jobId int PRIMARY KEY, status text, amountBlocked int, userId int, strategy text, insertionTimeMilliSecond bigint, ackTimeMilliSecond bigint);`.
9) Run `analytics` main method using a mvn configuration (analytics will add dummy data in Cassandra (TODO: Make keyspace and table creation automatic)).
