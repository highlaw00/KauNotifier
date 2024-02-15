package kauproject.kaunotifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@PropertySource("classpath:/application-local.properties")
@RequiredArgsConstructor
public class EmailConfig {

    private final Environment environment;
    @Bean
    public SesV2Client client() {
        String accessKey = environment.getProperty("aws.accessKeyId");
        String privateKey = environment.getProperty("aws.secretAccessKey");
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, privateKey);
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(credentials);
        return SesV2Client.builder().region(Region.AP_NORTHEAST_2).credentialsProvider(staticCredentialsProvider).build();
    }
}
