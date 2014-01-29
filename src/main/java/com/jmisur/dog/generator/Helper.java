package com.jmisur.dog.generator;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Helper {

	private Helper() {
	}

	private static InputStream getInputStream(String sourceFolder, Class<?> clazz) {
		String name = "src/main/java/" + clazz.getCanonicalName().replace('.', '/') + ".java";
		System.out.println("PARSING: " + name);
		try {
			return new FileInputStream(name);
		} catch (FileNotFoundException e) {
			// quick hack
			name = "src/test/java/" + clazz.getCanonicalName().replace('.', '/') + ".java";
			System.out.println("PARSING: " + name);
			try {
				return new FileInputStream(name);
			} catch (FileNotFoundException e1) {
				System.err.println("Cannot find source of " + clazz);
			}
		}
		return null;
	}

	public static CompilationUnit parserClass(String sourceFolder, Class<?> clazz) throws ParseException {
		return JavaParser.parse(getInputStream(sourceFolder, clazz));
	}

}
