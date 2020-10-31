package com.bridgelabz.EmployeePayroll;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO
    }

    public static List<EmployeeData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;

    // Default constructor
    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    //Parameterized Constructor
    public EmployeePayrollService(List<EmployeeData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    // Read data from Console or File or Database
    public void readData(IOService ioservice) throws SQLException {
        if (ioservice.equals(IOService.CONSOLE_IO)) {
            Scanner consoleInputReader = new Scanner(System.in);
            System.out.println("Enter Employee Id: ");
            int id = consoleInputReader.nextInt();
            System.out.println("Enter Employee Year Month & Date: ");
            int year = consoleInputReader.nextInt();
            int month = consoleInputReader.nextInt();
            int date = consoleInputReader.nextInt();
            LocalDate startDate = LocalDate.of(year, month, date);
            System.out.println("Enter Employee Name: ");
            String name = consoleInputReader.next();
            System.out.println("Enter Employee Number: ");
            String phone_number = consoleInputReader.next();
            System.out.println("Enter Employee Gender: ");
            String gender = consoleInputReader.next();
            System.out.println("Enter Employee Address: ");
            String address = consoleInputReader.next();
            employeePayrollList.add(new EmployeeData(id, name, startDate, phone_number, gender, address));
        } else if (ioservice.equals(IOService.FILE_IO))
            employeePayrollList = new EmployeePayrollFileIOService().readData();
        else if (ioservice.equals(IOService.DB_IO))
            employeePayrollList = employeePayrollDBService.readData();
    }

    // Write data either to console or file
    public void writeData(IOService ioservice) {
        if (ioservice.equals(IOService.CONSOLE_IO))
            System.out.println("Writing to console\n" + employeePayrollList);
        else if (ioservice.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    public void addEmployeeToPayroll(String name, String phone, String address, String gender, LocalDate startDate) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name,phone,address,gender,startDate));
    }


    public void updateEmployeeNumber(String name, String newNumber) throws SQLException {

        int result = employeePayrollDBService.updateEmployeeDataUsingStatement(name, newNumber);
        if (result == 0) return;
        EmployeeData employeePayrollData = this.getEmployeePayRollData(name);
        if (employeePayrollData != null) employeePayrollData.phone_number = newNumber;
    }

    public void updateEmployeeNumberUsingPreparedStatement(String name, String newNumber) throws SQLException {
        int result = employeePayrollDBService.updateEmployeeDataUsingPreparedStatement(name, newNumber);
        if (result == 0) return;
        EmployeeData employeePayrollData = this.getEmployeePayRollData(name);
        if (employeePayrollData != null) employeePayrollData.phone_number = newNumber;
    }

    public List<EmployeeData> retrieveEmployeesInDateRange(LocalDate startDate, LocalDate endDate) {
        return employeePayrollDBService.employeeInDateRange(startDate, endDate);
    }

    public int getCountByGender(String gender) {
        return employeePayrollDBService.countOfEmployeesByGender(gender);
    }

    public double getSumOfSalaryByGender(String gender) {
        return  employeePayrollDBService.sumOfSalaryByGender(gender);
    }


    private EmployeeData getEmployeePayRollData(String name) {
        return employeePayrollList.stream()
                .filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeeData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayRollData(name));
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
