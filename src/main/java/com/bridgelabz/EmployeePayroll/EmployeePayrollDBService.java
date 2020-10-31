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

    private Connection getConnection() {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?allowPublicKeyRetrieval=true&useSSL=false";
        String userName = "root";
        String password = "root";
        Connection con = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            con = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!!!!!" + con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public List<EmployeeData> readData() throws SQLException {
        return this.getEmployeePayrollDataUsingDB(query);
    }

    public EmployeeData addEmployeeToPayroll(String name, String phone, String address, String gender, LocalDate startDate, double salary, int[] departmentId, String[] departmentName) {
        int employee_id = -1;
        Connection connection = null;
        EmployeeData employeeData = null;
        connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into employee (name,phone_number,address,gender,start)" +
                    "values('%s','%s','%s','%s','%s')", name, phone, address, gender, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employee_id = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
                return employeeData;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try(Statement statement = connection.createStatement()){
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("insert into payroll (employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) values" +
                    "('%s','%s','%s','%s','%s','%s')",employee_id,salary,deductions,taxablePay,tax,netPay);
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
                return employeeData;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try(Statement statement = connection.createStatement()){
            for(int i=0; i< departmentName.length;i++){
                String sql = String.format("insert into department values ('%s','%s','%s')",departmentId[i],departmentName[i],employee_id);
                int rowAffected = statement.executeUpdate(sql);
                if (rowAffected == 1) {
                    employeeData = new EmployeeData(employee_id,name,startDate,phone,gender,address);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return employeeData;
    }

    public void deleteEmployee(String name) {
        String query = String.format("update employee set is_active = 'False' where name = '%s'",name);
        try(Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        return this.getEmployeePayrollDataUsingDB(query);
    }

    public int countOfEmployeesByGender(String gender) {
        String query = String.format("select count(gender) as count from employee where gender = '%s' group by gender;", gender);
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public double sumOfSalaryByGender(String gender) {
        String query = String.format("select sum(basic_pay) as salary from employee e, payroll p where e.employee_id = p.employee_id and e.gender = '%s' group by gender;", gender);
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getDouble("salary");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private List<EmployeeData> getEmployeePayrollDataUsingDB(String query) {
        List<EmployeeData> employeeDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            employeeDataList = this.getEmployeePayrollData(result);
        } catch (SQLException e) {
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
