#!/usr/bin/env bash

project_name=${1}

echo "stop ${project_name}-db-ttest"
docker stop ${project_name}-db-test && docker rm ${project_name}-db-test
echo "stop ${project_name}-test"
docker stop ${project_name}-test && docker rm ${project_name}-test