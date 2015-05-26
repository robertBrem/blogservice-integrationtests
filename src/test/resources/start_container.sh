#!/usr/bin/env bash

echo "start blogservice-db-test"
docker run -d -e POSTGRES_PASSWORD=postgres \
-e DB_NAME=blogservice \
--name blogservice-db-test postgres

echo "wait for postgres to start"
while ! docker exec -t blogservice-db-test ps auxw | grep "postgres: writer process"
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"

echo "create database blogservice"
docker exec -t blogservice-db-test createdb -U postgres blogservice

echo "wait for database to be created"
while ! docker exec -t blogservice-db-test psql -U postgres -l | grep blogservice
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"

echo "start blogservice-test"
docker run -p 8081:8080 -p 9991:9990 \
-e JNDI_NAME=java:jboss/jdbc/blogserviceDS \
-e NAME=BlogserviceDS \
-e CONNECTION_URL=jdbc:postgresql://blogservice-db:5432/blogservice \
-e DB_USER_NAME=postgres \
-e DB_PASSWORD=postgres \
-e DB_DRIVER_PATH=https://jdbc.postgresql.org/download \
-e DB_DRIVER_NAME=postgresql-9.4-1201.jdbc41.jar \
-e TEST_MODE=true \
-e DEPLOYMENT_ARTIFACT=http://optimist.engineer/blogservice.war \
-d --name blogservice-test --link blogservice-db-test:blogservice-db wildfly

echo " wait for wildfly to start"
while [ $(curl --write-out %{http_code} --silent --output /dev/null http://localhost:8081/blogservice/resources/entries) -ne "200" ]
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"