package com.bridgelabz.EmployeePayroll;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    public Response addEmployeeToJsonServer(EmployeeData employeeData) {
        String empJson = new Gson().toJson(employeeData);
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type","application/json");
        request.body(empJson);
        return request.post("/employees");
    }
}
