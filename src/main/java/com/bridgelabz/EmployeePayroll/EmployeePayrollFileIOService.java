package com.bridgelabz.EmployeePayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {

	public static String PAYROLL_FILE_NAME = "payroll-file.txt";

	// Write to file
	public void writeData(List<EmployeePayrollData> employeePayrollList) {

		StringBuffer empBuffer = new StringBuffer();
		employeePayrollList.forEach(employee -> {
			String employeeDataString = employee.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
		} catch (IOException e) {
		}
	}

	// Count entries file
	public long countEntries() {
		long entries = 0;
		try {
			entries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
		} catch (IOException e) {
		}
		return entries;
	}

	// Print data from file
	public void printData() {
		try {
			Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
		} catch (IOException e) {
		}
	}

	// Read data from File
	public ArrayList<EmployeePayrollData> readData() {
		ArrayList<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
		try {
			Files.lines(Paths.get(PAYROLL_FILE_NAME)).forEach(line -> {
				line = line.trim();
				line = line.replace(":", "").replace("  ", " ");
				String[] arr = line.split(" ");
				employeePayrollDataList
						.add(new EmployeePayrollData(Integer.parseInt(arr[1]), arr[3], Double.parseDouble(arr[5])));
			});
		} catch (IOException e) {

		}
		return employeePayrollDataList;
	}
}
