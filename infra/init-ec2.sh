#!/bin/bash

# Keys de acesso a AWS
mkdir ~/.aws
echo "[default]" >> ~/.aws/credentials
echo "aws_access_key_id = teste" >> ~/.aws/credentials
echo "aws_secret_access_key = teste+" >> ~/.aws/credentials

# Downloads Aplicaçoes S3
aws s3 cp --endpoint-url http://localhost:4566 s3://backend .

# Instalaçao JAVA
yum install java-1.8.0-openjdk -y
yum update -y

# Start dos serviços
nohup java -jar ./backend.jar > back-end.log &
