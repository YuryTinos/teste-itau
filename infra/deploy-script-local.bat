@echo off

set userprofile=C:\Users\%username%

set AWS_DEFAULT_PROFILE=teste-itau
set AWS_CONFIG_FILE=fake-config
set AWS_DEFAULT_REGION=us-east-1

doskey aws=docker run --rm -it amazon/aws-cli ^
    -v ../infra/fake-credentials:/root/.aws/credentials ^
    -v ../infra/fake-config:/root/.aws/config ^
    $*

call aws dynamodb create-table ^
    --endpoint-url=http://localhost:4566 ^
    --table-name Cervejas ^
    --attribute-definitions AttributeName=id,AttributeType=S ^
    --key-schema AttributeName=id,KeyType=HASH ^
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

call aws s3 mb --endpoint-url=http://localhost:4566 s3://backend

call aws s3 cp --endpoint-url=http://localhost:4566 ^
    .\back-end\target\back-end-0.0.1-SNAPSHOT.jar ^
    s3://backend/

call aws s3 mb --endpoint-url=http://localhost:4566 s3://frontend

call aws s3 website s3://frontend/ ^
    --endpoint-url=http://localhost:4566 ^
    --index-document index.html ^
    --error-document index.html
    
call aws s3 cp --endpoint-url=http://localhost:4566 ^
    .\front-end\dist\front-end\ ^
    s3://frontend/ --recursive

FOR /F "tokens=*" %%g IN ('call aws ec2 create-security-group ^
      --endpoint-url http://localhost:4566 ^
      --group-name Teste-itau ^
      --description "Security Group criado para teste de deploy em EC2 para o Itaú" ^
      --output text') do (SET SECURITY_GROUP_ID=%%g)

call aws ec2 authorize-security-group-ingress ^
      --endpoint-url http://localhost:4566 ^
      --group-name Teste-itau ^
      --protocol tcp ^
      --port 22 ^
      --cidr 0.0.0.0/0

call aws ec2 create-key-pair ^
      --endpoint-url http://localhost:4566 ^
      --key-name Teste-itau ^
      --query 'KeyMaterial' ^
      --output text > test-key.pem

call aws ec2 run-instances ^
      --endpoint-url http://localhost:4566 ^
      --image-id ami-0d57c0143330e1fa7 ^
      --security-group-ids %SECURITY_GROUP_ID% ^
      --instance-type t2.micro ^
      --key-name Teste-itau ^
      --user-data file://infra/init-ec2.sh

doskey aws=aws



