package com.bridgelabz.EmployeePayroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;
    List<EmployeeData> employeeDataList;

    @Before
    public void initialize() {
        EmployeeData[] arrayOfEmps = {
                new EmployeeData(1, "Harry", LocalDate.of(2020, 9, 9), "9090909090", "M", "London"),
                new EmployeeData(2, "Taylor", LocalDate.of(2018, 8, 7), "2323232323", "F", "Malibu"),
                new EmployeeData(3, "Zayn", LocalDate.of(2019, 1, 1), "3434343434", "M", "New York")
        };

        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeeDataList = new ArrayList<>();
        employeeDataList.add(new EmployeeData(1, "Harry", LocalDate.of(2020, 9, 9), "9090909090", "M", "London"));
        employeeDataList.add(new EmployeeData(2, "Taylor", LocalDate.of(2018, 8, 7), "2323232323", "F", "Malibu"));
        employeeDataList.add(new EmployeeData(3, "Zayn", LocalDate.of(2019, 1, 1), "3434343434", "M", "New York"));
    }

    @Test
    public void given3EmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
        employeePayrollService.writeData(IOService.FILE_IO);
        employeePayrollService.printData(IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(IOService.FILE_IO);
        assertEquals(3, entries);
    }

    @Test
    public void givenFileWhenReadingFromFileShouldMatchEmployeeCount() throws SQLException {
        employeePayrollService.readData(IOService.FILE_IO);
        long entries = employeePayrollService.employeeCount();
        assertEquals(3, entries);
    }

    @Test
    public void givenEmployeeInDB_whenRetrieved_shouldMatchEmployeeCount() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        long entries = employeePayrollService.employeeCount();
        assertEquals(3, entries);
    }

    @Test
    public void givenNewNumberForEmployee_whenUpdated_shouldSyncWithDB() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        employeePayrollService.updateEmployeeNumber("Taylor", "1111333390", IOService.DB_IO);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Taylor");
        Assert.assertTrue(result);
    }

    @Test
    public void givenNewNumberForEmployee_whenUpdatedUsingPreparedStatement_shouldSyncWithDB() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        employeePayrollService.updateEmployeeNumberUsingPreparedStatement("Taylor", "0111333395");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Taylor");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployeeDB_whenRetrivingOnDateRange_shouldReturnEmployeesWithStartDatesInRange() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        List<EmployeeData> employeeData = employeePayrollService.retrieveEmployeesInDateRange(LocalDate.of(2019, 01, 01), LocalDate.of(2020, 12, 31));
        Assert.assertEquals(2, employeeData.size());
    }

    @Test
    public void givenEmployeeDB_whenUsingCountAggregateFunction_shouldReturnCountByGender() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        int count = employeePayrollService.getCountByGender("M");
        Assert.assertEquals(2,count);
    }

    @Test
    public void givenEmployeeDB_whenUsingSumAggregateFunction_shouldReturnSumOfSalaryByGender() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        double sumSalary = employeePayrollService.getSumOfSalaryByGender("M");
        Assert.assertEquals(5998,sumSalary,0.0);
    }

    @Test
    public void givenNewEmployee_whenAdded_shouldBeInSyncWithDB() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        int[] departmentId = {101,102};
        String[] departmentName = {"Dept1","Dept2"};
        employeePayrollService.addEmployeeToPayroll("Ria","2345678901","India","F",LocalDate.now(),50000.00,departmentId,departmentName);
        boolean result= employeePayrollService.checkEmployeePayrollInSyncWithDB("Ria");
        Assert.assertTrue(result);
    }

    @Test
    public void givenEmployee_whenDeleted_shouldBeInSyncWithDB() throws SQLException {
        employeePayrollService.readData(IOService.DB_IO);
        int result = employeePayrollService.deleteEmployee("Ria", IOService.DB_IO);
        Assert.assertEquals(4,result);
    }

    @Test
    public void given3Employees_whenAddedAsEmployee_shouldMatchEmployeeEntries() throws SQLException, InterruptedException {
        EmployeeData[] arrayOfEmps = {
                new EmployeeData(0,"Halsey",LocalDate.now(),"1234123412","F","Chicago"),
                new EmployeeData(0,"Mark",LocalDate.now(),"33333333333","M","Tokyo"),
                new EmployeeData(0,"Beyonce",LocalDate.now(),"2222222222","F","San Francisco")
        };
        employeePayrollService.readData(IOService.DB_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeeToDB(Arrays.asList(arrayOfEmps));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: "+ Duration.between(start,end));
        employeePayrollService.readData(IOService.DB_IO);
        Instant startThread = Instant.now();
        employeePayrollService.addEmployeeToDBWithThreads(Arrays.asList(arrayOfEmps));
        Thread.sleep(500);
        Instant endThread = Instant.now();
        System.out.println("Duration with Thread: "+ Duration.between(startThread,endThread));
        Assert.assertEquals(9,employeePayrollService.countEntries(IOService.DB_IO));
    }

    @Test
    public void given2Employees_whenAddedToDB_shouldMatchEmployeeEntries() throws SQLException, InterruptedException {
        EmployeeData[] arrayOfEmployees = {
                new EmployeeData(0,"Ria",LocalDate.now(),"2345678901","F","Jaipur",50000.00, new int[]{102}, new String[]{"Dept2"}),
                new EmployeeData(0, "Sanya", LocalDate.now(),"34343434", "F","Bhopal", 3000000.0 , new int[]{101}, new String[]{"Dept1"}),
        };
        employeePayrollService.readData(IOService.DB_IO);
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeeWithThreads(Arrays.asList(arrayOfEmployees));
        Thread.sleep(6000);
        Instant threadEnd = Instant.now();
        System.out.println("Duration for complete execution with Threads: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(11, employeePayrollService.countEntries(IOService.DB_IO));
    }
}
