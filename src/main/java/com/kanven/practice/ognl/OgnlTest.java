package com.kanven.practice.ognl;

import java.util.HashMap;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

public class OgnlTest {


	public void testContext() throws OgnlException {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("who", "who am I?");
		Student student = new Student();
		student.setId(1);
		student.setName("jyl");
		System.out.println(Ognl.getValue("#who", context, student));
		System.out.println(Ognl.getValue("#context.who", context, student));
	}

	public void testGetValue() throws OgnlException {
		Student student = new Student();
		student.setId(1);
		student.setName("jyl");
		System.out.println(Ognl.getValue("id", student));
		System.out.println(Ognl.getValue("name", student));
	}

	public void testSetValue() throws OgnlException {
		Student student = new Student();
		Ognl.setValue("id", student, 1);
		Ognl.setValue("name", student, "jyl");
		System.out.println(student);
	}

	public void testSetNestObject() throws OgnlException {
		Person person = new Person();
		person.setStudent(new Student()); // 需要先初始化实体类，是否可以不需要这一步呢？
		Ognl.setValue("id", person, 2);
		System.out.println(person);
		Ognl.setValue("student.name", person, "jyl");
		System.out.println(person);
	}

}
