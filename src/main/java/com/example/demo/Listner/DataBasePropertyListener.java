package com.example.demo.Listner;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Properties;

@Component
public class DataBasePropertyListener implements ApplicationListener<ApplicationPreparedEvent> {

    private final static String SPRING_DATASOURCE_DATABASE = "spring.data.mongodb.database";

    private final static String SPRING_MONGO_USERNAME = "spring.data.mongodb.username";

    private final static String SPRING_MONGO_PASSWORD = "spring.data.mongodb.password";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        Properties props = new Properties();
        String mongoCredentialsJson = getSecret("mongodb-credentials");
        if(mongoCredentialsJson != null){
            String mongoDatabaseName = getString(mongoCredentialsJson, "mongo-database");
            String mongoUserName = getString(mongoCredentialsJson,"mongo-username");
            String mongoPassword = getString(mongoCredentialsJson,"mongo-password");
            props.put(SPRING_DATASOURCE_DATABASE, mongoDatabaseName);
            props.put(SPRING_MONGO_USERNAME,mongoUserName);
            props.put(SPRING_MONGO_PASSWORD,mongoPassword);
            environment.getPropertySources().addFirst(new PropertiesPropertySource("aws.secret.manager", props));
        }
    }

    private String getString(String mongoCredentialsJson, String s) {
        try {
            JsonNode root = objectMapper.readTree(mongoCredentialsJson);
            return root.path(s).asText();
        } catch (IOException e) {
            System.out.println("Can't get {} from json {}");
            return null;
        }
    }

    private String getSecret(String secretMangerName) {
        String region = "ap-south-1";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new ClasspathPropertiesFileCredentialsProvider())
                .build();

        String secret;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretMangerName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException | InternalServiceErrorException | InvalidParameterException |
                 InvalidRequestException | ResourceNotFoundException e) {
            throw e;
        }
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();

        } else {
            return null;
        }
        return secret != null ? secret : "";
    }
}
