package com.bridgelabz.EmployeePayroll;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;


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
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.REST_IO);
        Assert.assertEquals(4,entries);
    }
}
