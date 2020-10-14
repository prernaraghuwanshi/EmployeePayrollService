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

	// Read data from Console or File
	public void readData(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO)) {
			Scanner consoleInputReader = new Scanner(System.in);
			System.out.println("Enter Employee ID: ");
			int id = consoleInputReader.nextInt();
			System.out.println("Enter Employee Name: ");
			String name = consoleInputReader.next();
			System.out.println("Enter Employee Salary: ");
			double salary = consoleInputReader.nextDouble();
			employeePayrollList.add(new EmployeePayrollData(id, name, salary));
		} else if (ioservice.equals(IOService.FILE_IO))
			employeePayrollList = new EmployeePayrollFileIOService().readData();
	}

	// Write data either to console or file
	public void writeData(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO))
			System.out.println("Writing to console\n" + employeePayrollList);
		else if (ioservice.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
	}

	// Counting entries of employees when written to file or console
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

	// Count of employees when read from file and stored to employeePayrollList
	public long employeeCount() {
		return employeePayrollList.size();
	}

}
