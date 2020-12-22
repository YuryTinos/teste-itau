#!/bin/bash

aws s3 cp \
      --endpoint-url=http://localstack:4566 \
      ./target/back-end-0.0.1-SNAPSHOT.jar s3://backend/
