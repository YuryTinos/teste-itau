@echo off

set userprofile=C:\Users\%username%

set AWS_DEFAULT_PROFILE=teste-itau
set AWS_CONFIG_FILE=.\infra\config
set AWS_DEFAULT_REGION=us-east-1

doskey aws=docker run --rm -it amazon/aws-cli ^
    -v ../infra/credenciais:/root/.aws/credentials ^
    -v ../infra/config:/root/.aws/config ^
    $*

call aws dynamodb create-table ^
    --table-name Cervejas ^
    --attribute-definitions AttributeName=id,AttributeType=S ^
    --key-schema AttributeName=id,KeyType=HASH ^
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1

call aws s3 mb s3://backend

call aws s3 cp ^
    .\back-end\target\back-end-0.0.1-SNAPSHOT.jar ^
    s3://backend/

call aws s3 mb s3://frontend

call aws s3 website s3://frontend/ ^
    ^
    --index-document index.html ^
    --error-document index.html
    
call aws s3 cp ^
    .\front-end\dist\front-end\ ^
    s3://frontend/ --recursive

FOR /F "tokens=*" %%g IN ('call aws ec2 create-security-group ^
      --group-name Teste-itau ^
      --description "Security Group criado para teste de deploy em EC2 para o Itaú" ^
      --output text') do (SET SECURITY_GROUP_ID=%%g)

call aws ec2 authorize-security-group-ingress ^
      --group-name Teste-itau ^
      --protocol tcp ^
      --port 22 ^
      --cidr 0.0.0.0/0

call aws ec2 create-key-pair ^
      --key-name Teste-itau ^
      --query 'KeyMaterial' ^
      --output text > test-key.pem

call aws ec2 run-instances ^
      --image-id ami-0d57c0143330e1fa7 ^
      --security-group-ids %SECURITY_GROUP_ID% ^
      --instance-type t2.micro ^
      --key-name Teste-itau ^
      --user-data file://infra/init-ec2.sh

doskey aws=aws



