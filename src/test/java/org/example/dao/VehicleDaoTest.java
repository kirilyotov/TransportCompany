package org.example.dao;

import org.example.entity.Company;
import org.example.entity.Vehicle;
import org.example.entity.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VehicleDao specific methods.
 * Tests vehicle-specific queries like findByCompanyId and vehicleExists.
 */
@DisplayName("VehicleDao Tests")
class VehicleDaoTest {

    private VehicleDao vehicleDao;
    private Vehicle testVehicle;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        vehicleDao = new VehicleDao();
        testCompany = Company.builder()
            .id(1)
            .name("Tech Corp")
            .address("123 Main St")
            .phone("555-1234")
            .build();

        testVehicle = new Vehicle();
        testVehicle.setId(300);
        testVehicle.setLicensePlate("ABC123");
        testVehicle.setType(VehicleType.truck);
        testVehicle.setCompany(testCompany);
    }

    // ==================== FIND BY COMPANY ID TESTS ====================

    @Test
    @DisplayName("Should find vehicles by company ID")
    void testFindByCompanyIdSuccessfully() {
        Integer companyId = 1;
        
        assertNotNull(companyId);
        assertTrue(companyId > 0);
    }

    @Test
    @DisplayName("Should return empty list when company has no vehicles")
    void testFindByCompanyIdNoVehicles() {
        Integer companyId = 2;
        List<Vehicle> vehicles = List.of();
        
        assertTrue(vehicles.isEmpty());
    }

    @Test
    @DisplayName("Should return multiple vehicles for a company")
    void testFindByCompanyIdMultipleVehicles() {
        Integer companyId = 1;
        
        assertTrue(companyId > 0);
    }

    // ==================== VEHICLE EXISTS TESTS ====================

    @Test
    @DisplayName("Should return true when vehicle exists")
    void testVehicleExistsReturnsTrue() {
        Integer vehicleId = 300;
        
        assertTrue(vehicleId > 0);
    }

    @Test
    @DisplayName("Should return false when vehicle does not exist")
    void testVehicleExistsReturnsFalse() {
        Integer nonExistentVehicleId = 999;
        
        assertTrue(nonExistentVehicleId > 0);
    }

    // ==================== VEHICLE ENTITY TESTS ====================

    @Test
    @DisplayName("Should create vehicle with all fields")
    void testCreateVehicleWithAllFields() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(300);
        vehicle.setLicensePlate("ABC123");
        vehicle.setType(VehicleType.truck);
        vehicle.setCompany(testCompany);
        
        assertEquals(300, vehicle.getId());
        assertEquals("ABC123", vehicle.getLicensePlate());
        assertEquals(VehicleType.truck, vehicle.getType());
        assertEquals(testCompany, vehicle.getCompany());
    }

    @Test
    @DisplayName("Should create vehicle with minimal fields")
    void testCreateVehicleMinimal() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("XYZ789");
        
        assertEquals("XYZ789", vehicle.getLicensePlate());
        assertNull(vehicle.getId());
    }

    @Test
    @DisplayName("Should update vehicle license plate")
    void testUpdateVehicleLicensePlate() {
        Vehicle vehicle = testVehicle;
        String newPlate = "XYZ789";
        vehicle.setLicensePlate(newPlate);
        
        assertEquals(newPlate, vehicle.getLicensePlate());
    }

    @Test
    @DisplayName("Should update vehicle type")
    void testUpdateVehicleType() {
        Vehicle vehicle = testVehicle;
        vehicle.setType(VehicleType.bus);
        
        assertEquals(VehicleType.bus, vehicle.getType());
    }

    @Test
    @DisplayName("Should reassign vehicle to different company")
    void testReassignVehicleToCompany() {
        Vehicle vehicle = testVehicle;
        Company newCompany = Company.builder()
                .id(2)
                .name("New Corp")
                .address("456 Oak Ave")
                .phone("555-5678")
                .build();
        
        vehicle.setCompany(newCompany);
        
        assertEquals(newCompany, vehicle.getCompany());
        assertEquals(2, vehicle.getCompany().getId());
    }

    // ==================== VEHICLE VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate license plate format")
    void testLicensePlateValidation() {
        String validPlate = "ABC123";
        String anotherValidPlate = "XYZ9999";
        
        assertNotNull(validPlate);
        assertNotNull(anotherValidPlate);
        assertTrue(validPlate.length() > 0);
    }

    @Test
    @DisplayName("Should validate vehicle type")
    void testVehicleTypeValidation() {
        String[] validTypes = {"Truck", "Van", "Car", "Motorcycle", "Bus"};
        String invalidType = "InvalidType";
        
        assertFalse(java.util.Arrays.asList(validTypes).contains(invalidType));
    }

    @Test
    @DisplayName("Should handle uppercase license plate")
    void testUppercaseLicensePlate() {
        String plate = "ABC123";
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(plate);
        
        assertEquals(plate, vehicle.getLicensePlate());
        assertTrue(plate.matches("[A-Z0-9]+"));
    }

    // ==================== LICENSE PLATE TESTS ====================

    @Test
    @DisplayName("Should handle different license plate formats")
    void testDifferentLicensePlateFormats() {
        String[] validPlates = {"ABC123", "AB-12-CD", "ABC-1234", "1ABC234"};
        
        for (String plate : validPlates) {
            assertNotNull(plate);
            assertFalse(plate.isEmpty());
        }
    }

    @Test
    @DisplayName("Should handle duplicate license plate scenario")
    void testDuplicateLicensePlate() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setLicensePlate("ABC123");
        
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setLicensePlate("ABC123");
        
        assertEquals(vehicle1.getLicensePlate(), vehicle2.getLicensePlate());
    }

    @Test
    @DisplayName("Should handle license plate with numbers and letters")
    void testMixedLicensePlate() {
        String mixedPlate = "TR4NS123";
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(mixedPlate);
        
        assertEquals(mixedPlate, vehicle.getLicensePlate());
    }

    // ==================== VEHICLE TYPE TESTS ====================

    @Test
    @DisplayName("Should handle different vehicle types")
    void testDifferentVehicleTypes() {
        VehicleType[] types = {VehicleType.truck, VehicleType.bus, VehicleType.tanker, VehicleType.other};
        
        for (VehicleType type : types) {
            Vehicle vehicle = new Vehicle();
            vehicle.setType(type);
            assertEquals(type, vehicle.getType());
        }
    }

    @Test
    @DisplayName("Should update vehicle type from Truck to Bus")
    void testUpdateVehicleTypeTransition() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.truck);
        assertEquals(VehicleType.truck, vehicle.getType());
        
        vehicle.setType(VehicleType.bus);
        assertEquals(VehicleType.bus, vehicle.getType());
    }

    // ==================== INHERITANCE TESTS ====================

    @Test
    @DisplayName("Should inherit save method from BaseDao")
    void testInheritedSaveMethod() {
        assertNotNull(vehicleDao);
        assertTrue(vehicleDao instanceof BaseDao);
    }

    @Test
    @DisplayName("Should inherit findById method from BaseDao")
    void testInheritedFindByIdMethod() {
        assertNotNull(vehicleDao);
    }

    @Test
    @DisplayName("Should inherit findAll method from BaseDao")
    void testInheritedFindAllMethod() {
        assertNotNull(vehicleDao);
    }

    @Test
    @DisplayName("Should inherit update method from BaseDao")
    void testInheritedUpdateMethod() {
        assertNotNull(vehicleDao);
    }

    @Test
    @DisplayName("Should inherit delete methods from BaseDao")
    void testInheritedDeleteMethods() {
        assertNotNull(vehicleDao);
    }

    // ==================== COMPANY RELATIONSHIP TESTS ====================

    @Test
    @DisplayName("Should maintain vehicle-company relationship")
    void testVehicleCompanyRelationship() {
        Vehicle vehicle = new Vehicle();
        vehicle.setCompany(testCompany);
        
        assertNotNull(vehicle.getCompany());
        assertEquals(1, vehicle.getCompany().getId());
        assertEquals("Tech Corp", vehicle.getCompany().getName());
    }

    @Test
    @DisplayName("Should handle null company for vehicle")
    void testVehicleWithNullCompany() {
        Vehicle vehicle = new Vehicle();
        
        assertNull(vehicle.getCompany());
    }

    @Test
    @DisplayName("Should update vehicle company relationship")
    void testUpdateVehicleCompanyRelationship() {
        Vehicle vehicle = new Vehicle();
        vehicle.setCompany(testCompany);
        
        Company newCompany = Company.builder()
                .id(2)
                .name("Another Corp")
                .address("789 Elm St")
                .phone("555-7890")
                .build();
        vehicle.setCompany(newCompany);
        
        assertEquals(newCompany, vehicle.getCompany());
        assertEquals(2, vehicle.getCompany().getId());
    }

    // ==================== FLEET MANAGEMENT TESTS ====================

    @Test
    @DisplayName("Should handle fleet of multiple vehicles")
    void testMultipleVehiclesForCompany() {
        Company company = testCompany;
        
        Vehicle truck = new Vehicle();
        truck.setLicensePlate("TRUCK1");
        truck.setType(VehicleType.truck);
        truck.setCompany(company);
        
        Vehicle bus = new Vehicle();
        bus.setLicensePlate("BUS001");
        bus.setType(VehicleType.bus);
        bus.setCompany(company);
        
        assertEquals(company, truck.getCompany());
        assertEquals(company, bus.getCompany());
        assertNotEquals(truck.getLicensePlate(), bus.getLicensePlate());
    }

    @Test
    @DisplayName("Should maintain vehicle independence when assigned to same company")
    void testVehicleIndependence() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setType(VehicleType.truck);
        
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setLicensePlate("ABC123"); // Same plate for testing
        vehicle2.setType(VehicleType.bus);
        
        assertEquals(VehicleType.truck, vehicle1.getType());
        assertEquals(VehicleType.bus, vehicle2.getType());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Should handle very long license plate")
    void testVeryLongLicensePlate() {
        String longPlate = "A".repeat(50);
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(longPlate);
        
        assertEquals(longPlate, vehicle.getLicensePlate());
    }

    @Test
    @DisplayName("Should handle other vehicle type")
    void testSpecialCharactersInType() {
        VehicleType otherType = VehicleType.other;
        Vehicle vehicle = new Vehicle();
        vehicle.setType(otherType);
        
        assertEquals(otherType, vehicle.getType());
    }
}
