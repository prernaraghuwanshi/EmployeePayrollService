package com.bridgelabz.EmployeePayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    String query = "Select * from employee;";
    private static PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    public List<EmployeeData> readData() throws SQLException {
        List<EmployeeData> employeePayrollDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                int id = result.getInt("employee_id");
                String name = result.getString("name");
                String phone_number = result.getString("phone_number");
                LocalDate startDate = result.getDate("start").toLocalDate();
                String gender = result.getString("gender");
                String address = result.getString("address");
                employeePayrollDataList.add(new EmployeeData(id, name, startDate, phone_number, gender, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private Connection getConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "root";
        Connection con = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!" + con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public int updateEmployeeDataUsingStatement(String name, String newNumber) throws SQLException {
        String query = String.format("update employee set phone_number='%s' where name='%s';", newNumber, name);
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateEmployeeDataUsingPreparedStatement(String name, String newNumber) throws SQLException {
        String query = "update employee set phone_number= ? where name= ?";
        try (Connection connection = this.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newNumber);
            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeeData> employeeInDateRange(LocalDate startDate, LocalDate endDate) {
        String query = String.format("select * from employee where start between '%s' and '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        List<EmployeeData> employeeDataList = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            employeeDataList = this.getEmployeePayrollData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeDataList;
    }

    public List<EmployeeData> getEmployeePayrollData(String name) {
        List<EmployeeData> employeePayrollDataList = null;
        if (employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeeData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeeData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                String name = resultSet.getString("name");
                String number = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                String gender = resultSet.getString("gender");
                employeePayrollDataList.add(new EmployeeData(id, name, startDate, number, gender, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String query = "select * from employee where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
