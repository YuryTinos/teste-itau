#!/bin/bash

aws dynamodb create-table \
    --endpoint-url=http://localstack:4566 \
    --table-name Cervejas \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
