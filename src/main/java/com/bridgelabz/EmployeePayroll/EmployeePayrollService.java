package com.bridgelabz.EmployeePayroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO
	};

	private static List<EmployeePayrollData> employeePayrollList;

	// Parameterized constructor
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		super();
		this.employeePayrollList = employeePayrollList;
	}

	// Default constructor
	public EmployeePayrollService() {
		super();
		this.employeePayrollList = new ArrayList<>();
	}

	// Read data from Console
	private void readDataConsole(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary: ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	// Write data either to console or file
	public void writeData(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO))
			System.out.println("Writing to console\n" + employeePayrollList);
		else if (ioservice.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
	}

	// Counting entries of employees
	public long countEntries(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO))
			return employeePayrollList.size();
		else if (ioservice.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOService().countEntries();
		return 0;
	}

	// Print Data
	public void printData(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO))
			System.out.println("Writing to console\n" + employeePayrollList);
		else if (ioservice.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().printData();
	}

}
