package com.bridgelabz.EmployeePayroll;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class EmployeeData {
	public int id;
	public String name;
	public LocalDate startDate;
	public String phone_number;
	public String gender;
	public String address;
	public double salary;
	public int[] departmentId;
	public String[] departmentName;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmployeeData that = (EmployeeData) o;
		return id == that.id &&
				Double.compare(that.salary, salary) == 0 &&
				Objects.equals(name, that.name) &&
				Objects.equals(startDate, that.startDate) &&
				Objects.equals(phone_number, that.phone_number) &&
				Objects.equals(gender, that.gender) &&
				Objects.equals(address, that.address) &&
				Arrays.equals(departmentId, that.departmentId) &&
				Arrays.equals(departmentName, that.departmentName);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(id, name, startDate, phone_number, gender, address, salary);
		result = 31 * result + Arrays.hashCode(departmentId);
		result = 31 * result + Arrays.hashCode(departmentName);
		return result;
	}

	public EmployeeData(int id, String name, LocalDate startDate, String phone_number, String gender, String address, double salary, int[] departmentId, String[] departmentName) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.phone_number = phone_number;
		this.gender = gender;
		this.address = address;
		this.salary = salary;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
	}

	public EmployeeData(int id, String name, LocalDate startDate, String phone_number, String gender, String address) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.phone_number = phone_number;
		this.gender = gender;
		this.address = address;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData{" +
				"id=" + id +
				", name='" + name + '\'' +
				", startDate=" + startDate +
				", phone_number='" + phone_number + '\'' +
				", gender='" + gender + '\'' +
				", address='" + address + '\'' +
				'}';
	}

}
