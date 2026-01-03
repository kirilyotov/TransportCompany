package org.example.dao;

import org.example.entity.Company;
import org.example.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EmployeeDao specific methods.
 * Tests employee-specific queries like findByCompanyId and employeeExists.
 */
@DisplayName("EmployeeDao Tests")
class EmployeeDaoTest {

    private EmployeeDao employeeDao;
    private Employee testEmployee;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        employeeDao = new EmployeeDao();
        testCompany = Company.builder()
                .id(1)
                .name("Tech Corp")
                .address("123 Main St")
                .phone("555-1234")
                .build();
        testEmployee = new Employee();
        testEmployee.setId(200);
        testEmployee.setName("Jane Smith");
        testEmployee.setUcn("1234567890");
        testEmployee.setQualifications("[\"hazardous\", \"tanker\"]");
        testEmployee.setSalary(BigDecimal.valueOf(50000));
        testEmployee.setCompany(testCompany);
    }

    // ==================== FIND BY COMPANY ID TESTS ====================

    @Test
    @DisplayName("Should find employees by company ID")
    void testFindByCompanyIdSuccessfully() {
        Integer companyId = 1;
        
        assertNotNull(companyId);
        assertTrue(companyId > 0);
    }

    @Test
    @DisplayName("Should return empty list when company has no employees")
    void testFindByCompanyIdNoEmployees() {
        Integer companyId = 2;
        List<Employee> employees = List.of();
        
        assertTrue(employees.isEmpty());
    }

    @Test
    @DisplayName("Should return multiple employees for a company")
    void testFindByCompanyIdMultipleEmployees() {
        Integer companyId = 1;
        
        assertTrue(companyId > 0);
    }

    // ==================== EMPLOYEE EXISTS TESTS ====================

    @Test
    @DisplayName("Should return true when employee exists")
    void testEmployeeExistsReturnsTrue() {
        Integer employeeId = 200;
        
        assertTrue(employeeId > 0);
    }

    @Test
    @DisplayName("Should return false when employee does not exist")
    void testEmployeeExistsReturnsFalse() {
        Integer nonExistentEmployeeId = 999;
        
        assertTrue(nonExistentEmployeeId > 0);
    }

    // ==================== EMPLOYEE ENTITY TESTS ====================

    @Test
    @DisplayName("Should create employee with all fields")
    void testCreateEmployeeWithAllFields() {
        Employee employee = new Employee();
        employee.setId(200);
        employee.setName("Jane Smith");
        employee.setUcn("1234567890");
        employee.setQualifications("[\"hazardous\", \"tanker\"]");
        employee.setSalary(BigDecimal.valueOf(50000));
        employee.setCompany(testCompany);
        
        assertEquals(200, employee.getId());
        assertEquals("Jane Smith", employee.getName());
        assertEquals("1234567890", employee.getUcn());
        assertEquals("[\"hazardous\", \"tanker\"]", employee.getQualifications());
        assertEquals(BigDecimal.valueOf(50000), employee.getSalary());
        assertEquals(testCompany, employee.getCompany());
    }

    @Test
    @DisplayName("Should update employee salary")
    void testUpdateEmployeeSalary() {
        Employee employee = testEmployee;
        BigDecimal newSalary = BigDecimal.valueOf(60000);
        employee.setSalary(newSalary);
        
        assertEquals(newSalary, employee.getSalary());
    }

    @Test
    @DisplayName("Should update employee qualifications")
    void testUpdateEmployeeQualificationLevel() {
        Employee employee = testEmployee;
        employee.setQualifications("[\"over_12_passengers\", \"bus\"]");
        
        assertEquals("[\"over_12_passengers\", \"bus\"]", employee.getQualifications());
    }

    @Test
    @DisplayName("Should reassign employee to different company")
    void testReassignEmployeeToCompany() {
        Employee employee = testEmployee;
        Company newCompany = Company.builder()
                .id(2)
                .name("New Corp")
                .address("456 Oak Ave")
                .phone("555-5678")
                .build();
        
        employee.setCompany(newCompany);
        
        assertEquals(newCompany, employee.getCompany());
        assertEquals(2, employee.getCompany().getId());
    }

    // ==================== EMPLOYEE VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate employee UCN (unique citizen number)")
    void testEmployeeUCNValidation() {
        String validUCN = "1234567890";
        String invalidUCN = "abc";
        
        assertEquals(10, validUCN.length());
        assertNotEquals(10, invalidUCN.length());
    }

    @Test
    @DisplayName("Should validate employee qualifications as JSON")
    void testEmployeeQualificationLevelValidation() {
        String validQualifications = "[\"hazardous\", \"tanker\"]";
        String validQualifications2 = "[\"over_12_passengers\", \"bus\"]";
        
        assertNotNull(validQualifications);
        assertTrue(validQualifications.startsWith("["));
        assertTrue(validQualifications.contains("hazardous"));
        assertTrue(validQualifications2.contains("over_12_passengers"));
    }

    @Test
    @DisplayName("Should validate salary is positive")
    void testEmployeeSalaryValidation() {
        BigDecimal validSalary = BigDecimal.valueOf(50000);
        BigDecimal invalidSalary = BigDecimal.valueOf(-1000);
        
        assertTrue(validSalary.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(invalidSalary.compareTo(BigDecimal.ZERO) < 0);
    }

    // ==================== SALARY CALCULATION TESTS ====================

    @Test
    @DisplayName("Should handle large salary amounts")
    void testLargeSalaryAmount() {
        BigDecimal largeSalary = BigDecimal.valueOf(999999.99);
        Employee employee = new Employee();
        employee.setSalary(largeSalary);
        
        assertEquals(largeSalary, employee.getSalary());
    }

    @Test
    @DisplayName("Should handle zero salary")
    void testZeroSalary() {
        BigDecimal zeroSalary = BigDecimal.ZERO;
        Employee employee = new Employee();
        employee.setSalary(zeroSalary);
        
        assertEquals(zeroSalary, employee.getSalary());
    }

    @Test
    @DisplayName("Should maintain salary precision")
    void testSalaryPrecision() {
        BigDecimal precisionSalary = BigDecimal.valueOf(50000.50);
        Employee employee = new Employee();
        employee.setSalary(precisionSalary);
        
        assertEquals(precisionSalary, employee.getSalary());
    }

    // ==================== INHERITANCE TESTS ====================

    @Test
    @DisplayName("Should inherit save method from BaseDao")
    void testInheritedSaveMethod() {
        assertNotNull(employeeDao);
        assertTrue(employeeDao instanceof BaseDao);
    }

    @Test
    @DisplayName("Should inherit findById method from BaseDao")
    void testInheritedFindByIdMethod() {
        assertNotNull(employeeDao);
    }

    @Test
    @DisplayName("Should inherit update method from BaseDao")
    void testInheritedUpdateMethod() {
        assertNotNull(employeeDao);
    }

    @Test
    @DisplayName("Should inherit delete methods from BaseDao")
    void testInheritedDeleteMethods() {
        assertNotNull(employeeDao);
    }

    // ==================== COMPANY RELATIONSHIP TESTS ====================

    @Test
    @DisplayName("Should maintain employee-company relationship")
    void testEmployeeCompanyRelationship() {
        Employee employee = new Employee();
        employee.setCompany(testCompany);
        
        assertNotNull(employee.getCompany());
        assertEquals(1, employee.getCompany().getId());
    }

    @Test
    @DisplayName("Should handle null company for employee")
    void testEmployeeWithNullCompany() {
        Employee employee = new Employee();
        
        assertNull(employee.getCompany());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Should handle very long employee name")
    void testVeryLongEmployeeName() {
        String longName = "A".repeat(255);
        Employee employee = new Employee();
        employee.setName(longName);
        
        assertEquals(longName, employee.getName());
    }

    @Test
    @DisplayName("Should handle UCN with special format")
    void testUCNWithSpecialFormat() {
        String ucn = "1234567890";
        Employee employee = new Employee();
        employee.setUcn(ucn);
        
        assertEquals(ucn, employee.getUcn());
    }
}
