package br.com.devinno.listedapi.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfiguration {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	// TODO: Change the values of the 'setUsername' and 'setPassword' methods to match those on your machine.
	@Bean
	public DataSource datasource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(this.dbUrl);
		config.setUsername("postgres");
		config.setPassword("joaobanco");
		return new HikariDataSource(config);
	}
}
