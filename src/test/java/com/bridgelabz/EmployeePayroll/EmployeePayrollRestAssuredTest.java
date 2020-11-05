package com.bridgelabz.EmployeePayroll;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService.REST_IO;


public class EmployeePayrollRestAssuredTest {

    EmployeePayrollService employeePayrollService;
    @Before
    public void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeeData[] getEmployeeList(){
        Response response = RestAssured.get("/employees");
        System.out.println("Employee Data in JSON Server: \n"+response.asString());
        EmployeeData[] arrayOfEmps = new Gson().fromJson(response.asString(),EmployeeData[].class);
        return arrayOfEmps;
    }

    public Response addEmployeeToJsonServer(EmployeeData employeeData) {
        String empJson = new Gson().toJson(employeeData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(empJson);
        return request.post("/employees");
    }

    @Test
    public void givenEmployeeDataInJSONServer_whenRetrieved_shouldMatchTheCount(){
        EmployeeData[] arrayOfEmps = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(4,entries);
    }

    @Test
    public void givenNewEmployee_whenAdded_shouldMatch201ResponseAndCount(){
        EmployeeData[] arrayOfEmps = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        EmployeeData employeeData = new EmployeeData(0,"Ishita", LocalDate.now(),"3232345454","F","Hyderabad");
        Response response = addEmployeeToJsonServer(employeeData);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(201,statusCode);
        employeeData = new Gson().fromJson(response.asString(),EmployeeData.class);
        employeePayrollService.addEmployeeToPayroll(employeeData, REST_IO);
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(5,entries);
    }

    @Test
    public void givenListOfNewEmployees_whenAdded_shouldMatch201ResponseAndCount(){
        EmployeeData[] arrayOfEmps = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        EmployeeData[] arrayOfNewEmps = {
                new EmployeeData(0,"Halsey",LocalDate.now(),"1234123412","F","Chicago"),
                new EmployeeData(0,"Mark",LocalDate.now(),"33333333333","M","Tokyo"),
                new EmployeeData(0,"Beyonce",LocalDate.now(),"2222222222","F","San Francisco")
        };
        for(EmployeeData employeeData : arrayOfNewEmps){
            Response response = addEmployeeToJsonServer(employeeData);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(201,statusCode);
            employeeData = new Gson().fromJson(response.asString(),EmployeeData.class);
            employeePayrollService.addEmployeeToPayroll(employeeData, REST_IO);
        }
        long entries = employeePayrollService.countEntries(REST_IO);
        Assert.assertEquals(8,entries);
    }

    @Test
    public void givenNewNumberForEmployee_whenUpdated_shouldMatch200Response() {
        EmployeeData[] arrayOfEmps = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.updateEmployeeNumber("Halsey","2233112233",REST_IO);
        EmployeeData employeeData = employeePayrollService.getEmployeePayRollData("Halsey");

        String empJson = new Gson().toJson(employeeData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(empJson);
        Response response = request.put("/employees/"+employeeData.id);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200,statusCode);
    }

}
