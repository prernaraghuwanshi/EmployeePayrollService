package com.bridgelabz.EmployeePayroll;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO
    }

    public static List<EmployeeData> employeePayrollList;

    // Parameterized constructor
    public EmployeePayrollService(List<EmployeeData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    // Default constructor
    public EmployeePayrollService() {
        this.employeePayrollList = new ArrayList<>();
    }

    // Read data from Console or File
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
