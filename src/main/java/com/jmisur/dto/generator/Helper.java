package com.jmisur.dto.generator;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

	private Helper() {
		// hide the constructor
	}

	private static InputStream getInputStream(final String sourceFolder, final Class<?> clazz) {
		String name = "src/main/java/" + clazz.getName().replace('.', '/') + ".java";
		System.out.println(name);
		FileInputStream absolutePath;
		try {
			absolutePath = new FileInputStream(name);
			return absolutePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static CompilationUnit parserClass(final String sourceFolder, final Class<?> clazz) throws ParseException {
		return JavaParser.parse(getInputStream(sourceFolder, clazz));
	}

	public static CompilationUnit parserClass(final InputStream inputStream) throws ParseException {
		return JavaParser.parse(inputStream);
	}

	public static CompilationUnit parserString(final String source) throws ParseException {
		// FIXME potential encoding bug in getBytes()
		return JavaParser.parse(new ByteArrayInputStream(source.getBytes()));
	}

	public static String readStream(final InputStream inputStream) throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			final StringBuilder ret = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				ret.append(line);
				ret.append("\n");
			}
			return ret.toString();
		} finally {
			reader.close();
		}
	}

	@Deprecated
	public static String readClass(final String sourceFolder, final Class<?> clazz) throws IOException {
		return readStream(getInputStream(sourceFolder, clazz));
	}

}
