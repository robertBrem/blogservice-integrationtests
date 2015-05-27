#!/usr/bin/env bash

num_of_args_without_test=12
project_name=${1}
remote_address=${2}
remote_port=${3}
postgres_username=${4}
postgres_password=${5}
database_name=${6}
jndi_name=${7}
datasource_name=${8}
db_driver_path=${9}
db_driver_name=${10}
artifact_url=${11}
test_url=${12}
remote_management_port=${13}

echo "start ${project_name}-db-test"
docker run -d -e POSTGRES_PASSWORD=${postgres_password} \
--name ${project_name}-db-test postgres

echo "wait for postgres to start"
while ! docker exec -t ${project_name}-db-test ps auxw | grep "postgres: writer process"
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"

echo "create database ${database_name}"
docker exec -t ${project_name}-db-test createdb -U ${postgres_username} ${database_name}

echo "wait for database to be created"
while ! docker exec -t ${project_name}-db-test psql -U ${postgres_username} -l | grep ${database_name}
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"

echo "start ${project_name}-test"
if [ $# == ${num_of_args_without_test} ]; then
 echo "start without test mode"
 docker run -p ${remote_port}:8080 \
 -e JNDI_NAME=${jndi_name} \
 -e NAME=${datasource_name} \
 -e CONNECTION_URL=jdbc:postgresql://${project_name}-db:5432/${database_name} \
 -e DB_USER_NAME=${postgres_username} \
 -e DB_PASSWORD=${postgres_password} \
 -e DB_DRIVER_PATH=${db_driver_path} \
 -e DB_DRIVER_NAME=${db_driver_name} \
 -e DEPLOYMENT_ARTIFACT=${artifact_url} \
 -d --name ${project_name}-test --link ${project_name}-db-test:${project_name}-db wildfly
else
 echo "start with test mode"
 echo "remote_port : ${remote_port}"
 echo "remote_management_port : ${remote_management_port}"
 docker run -p ${remote_port}:8080 -p ${remote_management_port}:9990 \
 -e JNDI_NAME=${jndi_name} \
 -e NAME=${datasource_name} \
 -e CONNECTION_URL=jdbc:postgresql://${project_name}-db:5432/${database_name} \
 -e DB_USER_NAME=${postgres_username} \
 -e DB_PASSWORD=${postgres_password} \
 -e DB_DRIVER_PATH=${db_driver_path} \
 -e DB_DRIVER_NAME=${db_driver_name} \
 -e TEST_MODE=true \
 -e DEPLOYMENT_ARTIFACT=${artifact_url} \
 -d --name ${project_name}-test --link ${project_name}-db-test:${project_name}-db wildfly
fi

echo " wait for wildfly to start"
while [ $(curl --write-out %{http_code} --silent --output /dev/null http://${remote_address}:${remote_port}${test_url}) -ne "200" ]
do
 echo "$(date) - still trying"
 sleep 1
done
echo "$(date) - connected successfully"