package com.bridgelabz.EmployeePayroll;

import java.io.File;

public class FileUtils {

	public static boolean deleteFiles(File filesToBeDeleted) {
		File[] files = filesToBeDeleted.listFiles();
		if (files != null) {
			for (File file : files) {
				deleteFiles(file);
			}
		}
		return filesToBeDeleted.delete();
	}

}
