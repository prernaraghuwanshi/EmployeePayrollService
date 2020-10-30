package com.bridgelabz.EmployeePayroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	EmployeePayrollService employeePayrollService;
	@Before
	public void initialize()
	{
		EmployeeData[] arrayOfEmps = {
				new EmployeeData(1, "Harry", LocalDate.of(2020, 9, 9), "9090909090", "M", "London"),
				new EmployeeData(2, "Taylor", LocalDate.of(2018, 8, 7), "2323232323", "F", "Malibu"),
				new EmployeeData(3, "Zayn", LocalDate.of(2019, 1, 1), "3434343434", "M", "New York")
		};

		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
	}
	@Test
	public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries()
	{		
		employeePayrollService.writeData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		assertEquals(3,entries);
	}
	@Test
	public void givenFileWhenReadingFromFileShouldMatchEmployeeCount() throws SQLException {
		employeePayrollService.readData(IOService.FILE_IO);
		long entries = employeePayrollService.employeeCount();
		assertEquals(3,entries);
	}

}
