package com.mfsa.extract.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.mfsa.extract.model.Person;
import com.mfsa.extract.model.Test;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(Person person) throws Exception {
		return person;
	}

	public Test databaseProcess(final Test test) throws Exception {

		int id = test.getPersonId();
		String firstName = test.getFirstName().toUpperCase();
		String lastName = test.getLastName().toUpperCase();
		String email = test.getEmail().toUpperCase();
		int age = test.getAge();

		final Test transformedTest = new Test(id,firstName, lastName,email,age);

		log.info("Converting (" + test + ") into (" + transformedTest + ")");

		return transformedTest;
	}

}