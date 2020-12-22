package br.com.itau.teste.melhorcerveja.config;

import br.com.itau.teste.melhorcerveja.entity.Cerveja;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories
        (basePackages = "br.com.itau.teste.melhorcerveja.repositories")
public class DynamoDBConfig {

    @Value("${spring.data.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

    @Value("${spring.data.dynamodb.region}")
    private String region;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(dynamoDbEndpoint, region))
                        .withCredentials(awsCredentialsProvider())
                .build();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(Cerveja.class);

        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));

        boolean exists = TableUtils.createTableIfNotExists(client, tableRequest);

        return client;
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new EnvironmentVariableCredentialsProvider();
    }
}
