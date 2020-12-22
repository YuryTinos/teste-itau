#!/bin/bash

aws s3 cp \
      --endpoint-url=http://localstack:4566 \
      --recursive \
      ./fron-end/dist/front-end/ s3://frontend/
