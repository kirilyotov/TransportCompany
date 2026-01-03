package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.example.exception.BusinessLogicException;
import org.example.exception.DatabaseConstraintException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the generic BaseDao class.
 * These tests use a concrete implementation (CompanyDao) but focus on testing
 * the generic CRUD methods defined in BaseDao.
 */
@DisplayName("BaseDao Generic Tests")
class BaseDaoTest {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;

    private CompanyDao companyDao;

    private MockedStatic<SessionFactoryUtil> sessionFactoryUtilMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        sessionFactoryUtilMockedStatic = Mockito.mockStatic(SessionFactoryUtil.class);
        sessionFactoryUtilMockedStatic.when(SessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);

        companyDao = new CompanyDao();

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @AfterEach
    void tearDown() {
        sessionFactoryUtilMockedStatic.close();
    }

    @Test
    @DisplayName("Should save an entity successfully")
    void testSaveSuccessfully() {
        Company company = new Company();
        company.setName("Test Corp");

        Company savedCompany = companyDao.save(company);

        assertNotNull(savedCompany);
        verify(session).persist(any(Company.class));
        verify(transaction).commit();
        verify(session).refresh(any(Company.class));
    }

    @Test
    @DisplayName("Should throw DatabaseConstraintException on save with constraint violation")
    void testSaveThrowsConstraintException() {
        // Given
        Company company = new Company();
        company.setName("Valid Company Name"); 

        ConstraintViolationException mockException = new ConstraintViolationException(
                "Duplicate entry", new SQLException(), "companies_name_unique");
        
        // When the code tries to rollback, it needs a status to check
        when(transaction.getStatus()).thenReturn(TransactionStatus.ACTIVE);

        // Simulate the exception during the persist operation
        doAnswer(invocation -> {
            throw new jakarta.persistence.PersistenceException(mockException);
        }).when(session).persist(any(Company.class));

        // When & Then
        assertThrows(BusinessLogicException.class, () -> {
            companyDao.save(company);
        });

        verify(transaction).rollback();
    }

    @Test
    @DisplayName("Should find an entity by ID successfully")
    void testFindByIdSuccessfully() {
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);
        when(session.find(Company.class, companyId)).thenReturn(company);

        Company foundCompany = companyDao.findById(companyId);

        assertNotNull(foundCompany);
        assertEquals(companyId, foundCompany.getId());
        verify(session).find(Company.class, companyId);
    }

    @Test
    @DisplayName("Should update an entity successfully")
    void testUpdateSuccessfully() {
        Company company = new Company();
        company.setId(1);
        company.setName("Updated Name");

        when(session.merge(any(Company.class))).thenReturn(company);

        Company updatedCompany = companyDao.update(company);

        assertNotNull(updatedCompany);
        verify(session).merge(any(Company.class));
        verify(transaction).commit();
        verify(session).refresh(any(Company.class));
    }

    @Test
    @DisplayName("Should delete an entity by ID successfully")
    void testDeleteByIdSuccessfully() {
        int companyId = 1;
        Company company = new Company();
        when(session.find(Company.class, companyId)).thenReturn(company);

        companyDao.deleteById(companyId);

        verify(session).find(Company.class, companyId);
        verify(session).remove(any(Company.class));
        verify(transaction).commit();
    }
}
