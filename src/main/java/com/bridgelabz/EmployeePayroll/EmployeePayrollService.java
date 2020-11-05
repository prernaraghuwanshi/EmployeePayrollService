package com.bridgelabz.EmployeePayroll;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
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
        this.employeePayrollList = new ArrayList<>(employeePayrollList);
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

    public void addEmployeeToPayroll(EmployeeData employeeData, IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.addEmployeeToDB(employeeData.name, employeeData.phone_number, employeeData.address, employeeData.gender, employeeData.startDate);
        else
            employeePayrollList.add(employeeData);
    }

    public void addEmployeeToDB(List<EmployeeData> employeeDataList) {
        employeeDataList.forEach(employeeData -> {
            this.addEmployeeToDB(employeeData.name, employeeData.phone_number, employeeData.address, employeeData.gender, employeeData.startDate);
        });
    }

    public void addEmployeeToDBWithThreads(List<EmployeeData> employeeDataList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
        employeeDataList.forEach(employeeData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeeData.hashCode(), false);
                System.out.println("Employee being added: " + Thread.currentThread().getName());
                this.addEmployeeToDB(employeeData.name, employeeData.phone_number, employeeData.address, employeeData.gender, employeeData.startDate);
                employeeAdditionStatus.put(employeeData.hashCode(), true);
                System.out.println("Employee Added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeeData.name);
            thread.start();
        });
//        while (employeeAdditionStatus.containsValue(false)) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println(employeePayrollList);
    }

    public void addEmployeeWithThreads(List<EmployeeData> employeeDataList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
        employeeDataList.forEach(employeeData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeeData.hashCode(), false);
                System.out.println("Employee being added: " + Thread.currentThread().getName());
                this.addEmployeeToPayroll(employeeData.name, employeeData.phone_number, employeeData.address, employeeData.gender, employeeData.startDate, employeeData.salary, employeeData.departmentId, employeeData.departmentName);
                employeeAdditionStatus.put(employeeData.hashCode(), true);
                System.out.println("Employee Added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeeData.name);
            thread.start();
        });
        System.out.println(employeePayrollList);
    }

    public void addEmployeeToDB(String name, String phone, String address, String gender, LocalDate startDate) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToDB(name, phone, address, gender, startDate));
    }

    public void addEmployeeToPayroll(String name, String phone, String address, String gender, LocalDate startDate, double salary, int[] departmentId, String[] departmentName) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, phone, address, gender, startDate, salary, departmentId, departmentName));
    }

    public int deleteEmployee(String name) {
        employeePayrollDBService.deleteEmployee(name);
        employeePayrollList = employeePayrollList.stream().filter(emp -> !emp.name.equals(name)).collect(Collectors.toList());
        return employeePayrollList.size();
    }

    public void updateEmployeeNumber(String name, String newNumber, IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            int result = 0;
            try {
                result = employeePayrollDBService.updateEmployeeDataUsingStatement(name, newNumber);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (result == 0) return;
        }
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
        return employeePayrollDBService.sumOfSalaryByGender(gender);
    }


    public EmployeeData getEmployeePayRollData(String name) {
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
        if (ioservice.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        else
            return employeePayrollList.size();
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
