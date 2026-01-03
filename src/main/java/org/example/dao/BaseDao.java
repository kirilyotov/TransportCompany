package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.configuration.SessionFactoryUtil;
import org.example.exception.BusinessLogicException;
import org.example.exception.DatabaseConstraintException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.ValidationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jakarta.persistence.RollbackException;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO with basic CRUD operations and logging.
 */
public abstract class BaseDao<T, ID extends Serializable> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;
    protected final Logger log = LogManager.getLogger(getClass());

    protected BaseDao(Class<T> clazz) {
        this.clazz = clazz;
        this.sessionFactory = SessionFactoryUtil.getSessionFactory();
    }

    public T save(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            session.refresh(entity); 
            log.info("Saved {}", clazz.getSimpleName());
            return entity;
        } catch (jakarta.validation.ConstraintViolationException ex) {
            rollback(tx);
            log.debug("Validation failed for {}: {}", clazz.getSimpleName(), ex.getMessage());
            throw ValidationException.fromConstraintViolations(ex);
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            rollback(tx);
            log.debug("Database constraint violation for {}: {}", clazz.getSimpleName(), ex.getSQLException().getMessage());
            throw DatabaseConstraintException.fromHibernateConstraintViolation(ex);
        } catch (Exception ex) {
            rollback(tx);
            log.error("Failed to save {}", clazz.getSimpleName(), ex);
            throw new BusinessLogicException("Could not save " + clazz.getSimpleName(), ex);
        }
    }

    public T findById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            T result = session.find(clazz, id);
            if (result == null) {
                throw new EntityNotFoundException(clazz.getSimpleName() + " not found with id: " + id);
            }
            return result;
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to find {} by id {}", clazz.getSimpleName(), id, ex);
            throw new BusinessLogicException("Could not find " + clazz.getSimpleName() + " by id: " + id, ex);
        }
    }

    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        } catch (Exception ex) {
            log.error("Failed to fetch all {}", clazz.getSimpleName(), ex);
            throw new BusinessLogicException("Could not fetch all " + clazz.getSimpleName(), ex);
        }
    }

    public T update(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            T merged = (T) session.merge(entity);
            tx.commit();
            session.refresh(merged);  // Refresh to get database-generated values
            log.info("Updated {}", clazz.getSimpleName());
            return merged;
            
        } catch(IllegalStateException ex) {
            // Connection might be closed, rollback not needed
            log.debug("Transaction already closed, rollback skipped for {}", clazz.getSimpleName());
            throw ex;
        } catch (RollbackException ex) {
            rollback(tx);
            log.debug("Transaction rollback for {}: {}", clazz.getSimpleName(), ex.getMessage());
            throw new BusinessLogicException("Transaction rollback for " + clazz.getSimpleName(), ex);
        }
        catch (jakarta.validation.ConstraintViolationException ex) {
            rollback(tx);
            log.error("Validation failed for {}", clazz.getSimpleName(), ex);
            throw ValidationException.fromConstraintViolations(ex);
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            rollback(tx);
            log.debug("Database constraint violation for {}: {}", clazz.getSimpleName(), ex.getSQLException().getMessage());
            throw DatabaseConstraintException.fromHibernateConstraintViolation(ex);
        } catch (Exception ex) {
            rollback(tx);
            log.error("Failed to update {}", clazz.getSimpleName(), ex);
            throw new BusinessLogicException("Could not update " + clazz.getSimpleName(), ex);
        }
    }

    public void deleteById(ID id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            T entity = session.find(clazz, id);
            if (entity == null) {
                throw new EntityNotFoundException(clazz.getSimpleName() + " not found with id: " + id);
            }
            session.remove(entity);
            tx.commit();
            log.info("Deleted {} with id {}", clazz.getSimpleName(), id);
        } catch (EntityNotFoundException ex) {
            rollback(tx);
            log.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            rollback(tx);
            log.error("Failed to delete {} with id {}", clazz.getSimpleName(), id, ex);
            throw new BusinessLogicException("Could not delete " + clazz.getSimpleName() + " with id: " + id, ex);
        }
    }

    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
            log.info("Deleted {}", clazz.getSimpleName());
        } catch (Exception ex) {
            rollback(tx);
            log.error("Failed to delete {}", clazz.getSimpleName(), ex);
            throw new BusinessLogicException("Could not delete " + clazz.getSimpleName(), ex);
        }
    }

    private void rollback(Transaction tx) {
        if (tx != null && tx.getStatus().canRollback()) {
            try {
                tx.rollback();
            } catch (IllegalStateException ex) {
                // Connection already closed, rollback not needed
                log.debug("Transaction already closed, rollback skipped for {}", clazz.getSimpleName());
            } catch (Exception ex) {
                log.error("Rollback failed for {}", clazz.getSimpleName(), ex);
            }
        }
    }
}
