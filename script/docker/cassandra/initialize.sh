#!/usr/bin/env bash
echo "Waiting for cassandra...";
sleep 5;

docker-compose exec cassandra1 /bin/bash;
echo "---A"
cqlsh
echo "---B"
CREATE KEYSPACE brocoeurkeyspace WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 } ;
echo "---C"
USE brocoeurkeyspace ;
CREATE TABLE Game( gameid int PRIMARY KEY, gamename text);
CREATE TABLE User( userid int PRIMARY KEY, pseudo text);
echo "---D"
CREATE TABLE UserWinLossByGame( game_id int, user_id int, gamename text, pseudo text, numberofwin int, numberofloss int, PRIMARY KEY ((game_id, user_id)));
CREATE TABLE UserMoney( userId int PRIMARY KEY, money int);
CREATE TABLE ServiceRequestStatus( jobId int PRIMARY KEY, status text, amountBlocked int, userId int, strategy text, insertionTimeMilliSecond int, ackTimeMilliSecond int);
echo "---E"



echo "Creating keyspace and table..."
cqlsh cassandra -u cassandra -p cassandra -e "CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};"
cqlsh cassandra -u cassandra -p cassandra -e "CREATE TABLE IF NOT EXISTS test.test (sensor_id uuid, registered_at timestamp, temperature int, PRIMARY KEY ((sensor_id), registered_at));"


while true; do
    echo "Writing sample data...";
    cqlsh cassandra -u cassandra -p cassandra -e "insert into test.test (sensor_id, registered_at, temperature) values (99051fe9-6a9c-46c2-b949-38ef78858dd0, toTimestamp(now()), $(shuf -i 18-32 -n 1));";
    cqlsh cassandra -u cassandra -p cassandra -e "insert into test.test (sensor_id, registered_at, temperature) values (99051fe9-6a9c-46c2-b949-38ef78858dd1, toTimestamp(now()), $(shuf -i 12-40 -n 1));";
    sleep 5;
done