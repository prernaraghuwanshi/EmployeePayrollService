package com.bridgelabz.EmployeePayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {

    public static String PAYROLL_FILE_NAME = "payroll-file.txt";

    // Write to file
    public void writeData(List<EmployeeData> employeePayrollList) {
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
    public ArrayList<EmployeeData> readData() {
        ArrayList<EmployeeData> employeeDataList = new ArrayList<>();
        try {
            Files.lines(Paths.get(PAYROLL_FILE_NAME)).forEach(line -> {
                line = line.trim();
                line = line.replace(":", "").replace("  ", " ").replace("'", "").replace("=", "= ").replace(",", " ,").replace("}", " }").replace("-", " - ");
                String[] arr = line.split(" ");
                employeeDataList
                        .add(new EmployeeData(Integer.parseInt(arr[1]), arr[4], LocalDate.of(Integer.parseInt(arr[7]), Integer.parseInt(arr[9]), Integer.parseInt(arr[11])), arr[14], arr[17], arr[20]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeDataList;
    }
}
