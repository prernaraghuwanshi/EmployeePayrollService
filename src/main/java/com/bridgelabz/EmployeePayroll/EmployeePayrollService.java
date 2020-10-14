package com.bridgelabz.EmployeePayroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO
	};

	private static List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		super();
		this.employeePayrollList = employeePayrollList;
	}

	public EmployeePayrollService() {
		super();
		this.employeePayrollList = new ArrayList<>();
	}

	private void readDataConsole(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary: ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	public void writeData(IOService ioservice) {
		if (ioservice.equals(IOService.CONSOLE_IO))
			System.out.println("Writing to console\n" + employeePayrollList);
	}

	public static void main(String args[]) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService s = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputScanner = new Scanner(System.in);
		s.readDataConsole(consoleInputScanner);
		s.writeData(IOService.CONSOLE_IO);
	}
}
