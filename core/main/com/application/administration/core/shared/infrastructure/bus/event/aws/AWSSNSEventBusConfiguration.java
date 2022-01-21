package com.application.administration.core.shared.infrastructure.bus.event.aws;

import com.application.administration.core.shared.infrastructure.config.Parameter;
import com.application.administration.core.shared.infrastructure.config.ParameterNotExist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AWSSNSEventBusConfiguration {
    private final Parameter config;

    public AWSSNSEventBusConfiguration(Parameter config)  {
        this.config = config;
    }

    @Bean
    public SnsClient snsClient() throws ParameterNotExist {
        return SnsClient.builder().region(Region.US_EAST_1).credentialsProvider(
                StaticCredentialsProvider
                        .create(
                            AwsBasicCredentials.create(config.get("AWS_ACCESS_KEY"),
                            config.get("AWS_SECRET_ACCESS_KEY")
                        )
                    )
                ).build();
    }
}
