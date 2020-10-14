package com.bridgelabz.EmployeePayroll;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	EmployeePayrollService employeePayrollService;
	@Before
	public void initialize()
	{
		EmployeePayrollData[] arrayOfEmps = {
				new EmployeePayrollData(1, "PR", 100.0),
				new EmployeePayrollData(2, "BO", 200.00)
		};
		
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
	}
	@Test
	public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries()
	{		
		employeePayrollService.writeData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		assertEquals(2,entries);
	}

}
