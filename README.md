# BROCOEUR : How to test Nerima (producer) - Games (consumer) RabbitMq

In order to test the application features using a real RabbitMq queueing system, follow the below steps.
### Prerequisites :
- Having `docker` installed and running.


### Steps :
1) Execute the `docker-compose up` command in the `script` folder.
2) Run `nerima` main method using a mvn configuration (nerima will create the necessary exchange and queues for RabbitMq).
3) Run `games` main method using a mvn configuration (both `nerima` and `games` needs to be running in parallel).
4) Go on `http://localhost:15672/` to see information about rabbitMq (which is available when the docker container for rabbitMq is up and running thanks to the docker-compose up command).
5) Using `PostMan` you can post `ServiceRequest` to the following endpoint : `http://localhost:9080/api/nerima/play`.

```json
{
  "userId": "1",
  "gameStrategyTypes": "COIN_TOSS_RANDOM"
}
```

6) You can see that the `ServiceRequest` is correctly send from `nerima` to `games` and you can also see if the specified user won or lost in the logs of the services.

