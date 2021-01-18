package br.com.devinno.listedapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfiguration {

	@Value("${AWS_ACESS_KEY_ID}")
	private String key_id;
	
	@Value("${AWS_SECRET_ACESS_KEY}")
	private String secret_key;
	
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.SA_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials()))
				.build();
	}
	
	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(key_id, secret_key);
	}
}
