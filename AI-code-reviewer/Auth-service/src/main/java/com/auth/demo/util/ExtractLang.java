package com.auth.demo.util;

public class ExtractLang {
	
	public static String getLanguage(String filename) {

	    if (filename.endsWith(".java")) {
	        return "java";
	    }

	    if (filename.endsWith(".py")) {
	        return "python";
	    }

	    if (filename.endsWith(".js")) {
	        return "javascript";
	    }

	    if (filename.endsWith(".ts")) {
	        return "typescript";
	    }

	    if (filename.endsWith(".cpp")) {
	        return "cpp";
	    }

	    if (filename.endsWith(".c")) {
	        return "c";
	    }

	    if (filename.endsWith(".cs")) {
	        return "csharp";
	    }

	    return "text";   // default
	}

}
