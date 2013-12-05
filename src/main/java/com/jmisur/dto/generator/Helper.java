package com.jmisur.dto.generator;

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
		FileInputStream absolutePath;
		try {
			absolutePath = new FileInputStream(name);
			return absolutePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static CompilationUnit parserClass(String sourceFolder, Class<?> clazz) throws ParseException {
		return JavaParser.parse(getInputStream(sourceFolder, clazz));
	}

}
