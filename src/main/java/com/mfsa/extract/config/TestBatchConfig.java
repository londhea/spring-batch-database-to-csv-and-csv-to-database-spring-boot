package com.mfsa.extract.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.mfsa.extract.model.Test;
import com.mfsa.extract.processor.TestItemProcessor;

@Configuration
@EnableBatchProcessing
public class TestBatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Bean
	public FlatFileItemReader<Test> csvReader() {

		FlatFileItemReader<Test> reader = new FlatFileItemReader<Test>();

		reader.setResource(new FileSystemResource("C:\\file\\test.csv"));

		reader.setLineMapper(new DefaultLineMapper<Test>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "personId", "firstName", "lastName", "email", "age" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Test>() {
					{
						setTargetType(Test.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public TestItemProcessor csvProcessor() {
		return new TestItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Test> csvWriter() {
		JdbcBatchItemWriter<Test> writer = new JdbcBatchItemWriter<Test>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Test>());
		writer.setSql(
				"INSERT INTO test (person_id,first_name, last_name,email,age) VALUES (:personId,:firstName, :lastName,:email,:age)");
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(csvStep1()).end().build();
	}

	@Bean
	public Step csvStep1() {
		return stepBuilderFactory.get("csvStep1").<Test, Test>chunk(10).reader(csvReader()).processor(csvProcessor())
				.writer(csvWriter()).build();
	}
}