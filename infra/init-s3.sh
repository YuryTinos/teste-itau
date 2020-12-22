#!/bin/bash

aws s3 mb --endpoint-url=http://localstack:4566 s3://frontend
aws s3 mb --endpoint-url=http://localstack:4566 s3://backend

aws s3 website s3://frontend/ \
    --endpoint-url=http://localstack:4566 \
    --index-document index.html \
    --error-document index.html
