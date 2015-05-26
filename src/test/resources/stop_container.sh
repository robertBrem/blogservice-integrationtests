#!/usr/bin/env bash

docker stop blogservice-db-test && docker rm blogservice-db-test
docker stop blogservice-test && docker rm blogservice-test