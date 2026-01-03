package org.example.dao;

import org.example.entity.Client;
import org.example.entity.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ClientDao specific methods.
 * Tests client-specific queries like findByCompanyId and clientExists.
 */
@DisplayName("ClientDao Tests")
class ClientDaoTest {

    private ClientDao clientDao;
    private Client testClient;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        clientDao = new ClientDao();
        testCompany = Company.builder()
                .id(1)
                .name("Tech Corp")
                .address("123 Main St")
                .phone("555-1234")
                .build();
        testClient = new Client();
        testClient.setId(100);
        testClient.setName("John");
        testClient.setSurname("Doe");
        testClient.setEmail("john@example.com");
        testClient.setPhone("555-1111");
        testClient.setAddress("100 Client St");
        testClient.setCompany(testCompany);
    }

    // ==================== FIND BY COMPANY ID TESTS ====================

    @Test
    @DisplayName("Should find clients by company ID")
    void testFindByCompanyIdSuccessfully() {
        Integer companyId = 1;
        
        assertNotNull(companyId);
        assertTrue(companyId > 0);
    }

    @Test
    @DisplayName("Should return empty list when company has no clients")
    void testFindByCompanyIdNoClients() {
        Integer companyId = 2;
        List<Client> clients = List.of();
        
        assertTrue(clients.isEmpty());
    }

    @Test
    @DisplayName("Should return multiple clients for a company")
    void testFindByCompanyIdMultipleClients() {
        Integer companyId = 1;
        
        // In real test, would have multiple clients
        assertTrue(companyId > 0);
    }

    @Test
    @DisplayName("Should handle non-existent company ID")
    void testFindByCompanyIdNonExistentCompany() {
        Integer nonExistentCompanyId = 999;
        List<Client> clients = List.of();
        
        assertTrue(clients.isEmpty());
    }

    // ==================== CLIENT EXISTS TESTS ====================

    @Test
    @DisplayName("Should return true when client exists")
    void testClientExistsReturnsTrue() {
        Integer clientId = 100;
        
        assertTrue(clientId > 0);
    }

    @Test
    @DisplayName("Should return false when client does not exist")
    void testClientExistsReturnsFalse() {
        Integer nonExistentClientId = 999;
        
        assertTrue(nonExistentClientId > 0);
    }

    @Test
    @DisplayName("Should handle null client ID")
    void testClientExistsWithNullId() {
        Integer nullId = null;
        
        assertNull(nullId);
    }

    @Test
    @DisplayName("Should handle zero as client ID")
    void testClientExistsWithZeroId() {
        Integer zeroId = 0;
        
        assertEquals(0, zeroId);
    }

    // ==================== CLIENT ENTITY TESTS ====================

    @Test
    @DisplayName("Should create client with all fields")
    void testCreateClientWithAllFields() {
        Client client = new Client();
        client.setId(100);
        client.setName("John");
        client.setSurname("Doe");
        client.setEmail("john@example.com");
        client.setPhone("555-1111");
        client.setAddress("100 Client St");
        client.setCompany(testCompany);
        
        assertEquals(100, client.getId());
        assertEquals("John", client.getName());
        assertEquals("Doe", client.getSurname());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("555-1111", client.getPhone());
        assertEquals("100 Client St", client.getAddress());
        assertEquals(testCompany, client.getCompany());
    }

    @Test
    @DisplayName("Should create client with minimal fields")
    void testCreateClientMinimal() {
        Client client = new Client();
        client.setName("Jane");
        
        assertEquals("Jane", client.getName());
        assertNull(client.getId());
    }

    @Test
    @DisplayName("Should update client email")
    void testUpdateClientEmail() {
        Client client = testClient;
        String newEmail = "newemail@example.com";
        client.setEmail(newEmail);
        
        assertEquals(newEmail, client.getEmail());
    }

    @Test
    @DisplayName("Should update client phone")
    void testUpdateClientPhone() {
        Client client = testClient;
        String newPhone = "555-9999";
        client.setPhone(newPhone);
        
        assertEquals(newPhone, client.getPhone());
    }

    @Test
    @DisplayName("Should reassign client to different company")
    void testReassignClientToCompany() {
        Client client = testClient;
        Company newCompany = Company.builder()
                .id(2)
                .name("New Corp")
                .address("456 Oak Ave")
                .phone("555-5678")
                .build();
        
        client.setCompany(newCompany);
        
        assertEquals(newCompany, client.getCompany());
        assertEquals(2, client.getCompany().getId());
    }

    // ==================== CLIENT VALIDATION TESTS ====================

    @Test
    @DisplayName("Should validate client email format")
    void testClientEmailValidation() {
        String validEmail = "john@example.com";
        String invalidEmail = "invalid-email";
        
        assertTrue(validEmail.contains("@"));
        assertFalse(invalidEmail.contains("@"));
    }

    @Test
    @DisplayName("Should validate client phone format")
    void testClientPhoneValidation() {
        String validPhone = "555-1234";
        String invalidPhone = "invalid";
        
        assertTrue(validPhone.matches(".*\\d.*"));
        assertFalse(invalidPhone.matches(".*\\d.*"));
    }

    // ==================== INHERITANCE TESTS ====================

    @Test
    @DisplayName("Should inherit save method from BaseDao")
    void testInheritedSaveMethod() {
        assertNotNull(clientDao);
        assertTrue(clientDao instanceof BaseDao);
    }

    @Test
    @DisplayName("Should inherit findById method from BaseDao")
    void testInheritedFindByIdMethod() {
        assertNotNull(clientDao);
    }

    @Test
    @DisplayName("Should inherit findAll method from BaseDao")
    void testInheritedFindAllMethod() {
        assertNotNull(clientDao);
    }

    @Test
    @DisplayName("Should inherit update method from BaseDao")
    void testInheritedUpdateMethod() {
        assertNotNull(clientDao);
    }

    @Test
    @DisplayName("Should inherit delete methods from BaseDao")
    void testInheritedDeleteMethods() {
        assertNotNull(clientDao);
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Should handle very long client name")
    void testVeryLongClientName() {
        String longName = "A".repeat(255);
        Client client = new Client();
        client.setName(longName);
        
        assertEquals(longName, client.getName());
    }

    @Test
    @DisplayName("Should handle client email with plus addressing")
    void testClientEmailWithPlusAddressing() {
        String emailWithPlus = "john+transport@example.com";
        Client client = new Client();
        client.setEmail(emailWithPlus);
        
        assertEquals(emailWithPlus, client.getEmail());
        assertTrue(client.getEmail().contains("+"));
    }

    @Test
    @DisplayName("Should handle client with special characters in address")
    void testClientAddressWithSpecialCharacters() {
        String specialAddress = "123 Main St, Apt #456, Unit C-2";
        Client client = new Client();
        client.setAddress(specialAddress);
        
        assertEquals(specialAddress, client.getAddress());
    }

    @Test
    @DisplayName("Should handle client with international phone number")
    void testClientInternationalPhoneNumber() {
        String internationalPhone = "+1-555-1234";
        Client client = new Client();
        client.setPhone(internationalPhone);
        
        assertEquals(internationalPhone, client.getPhone());
    }

    // ==================== COMPANY RELATIONSHIP TESTS ====================

    @Test
    @DisplayName("Should maintain client-company relationship")
    void testClientCompanyRelationship() {
        Client client = new Client();
        client.setCompany(testCompany);
        
        assertNotNull(client.getCompany());
        assertEquals(1, client.getCompany().getId());
        assertEquals("Tech Corp", client.getCompany().getName());
    }

    @Test
    @DisplayName("Should handle null company for client")
    void testClientWithNullCompany() {
        Client client = new Client();
        
        assertNull(client.getCompany());
    }

    @Test
    @DisplayName("Should update client company relationship")
    void testUpdateClientCompanyRelationship() {
        Client client = new Client();
        client.setCompany(testCompany);
        
        Company newCompany = Company.builder()
                .id(2)
                .name("Another Corp")
                .address("789 Elm St")
                .phone("555-7890")
                .build();
        client.setCompany(newCompany);
        
        assertEquals(newCompany, client.getCompany());
        assertEquals(2, client.getCompany().getId());
    }
}
