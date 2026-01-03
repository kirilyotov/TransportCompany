package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Payment;
import org.example.entity.enums.PaymentStatus;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.util.List;

public class PaymentDao extends BaseDao<Payment, Integer> {
	public PaymentDao() {
		super(Payment.class);
	}

	public List<Payment> findByClientId(Integer clientId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Payment p where p.client.id = :clientId", Payment.class)
					.setParameter("clientId", clientId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Payments by clientId {}", clientId, ex);
			throw new BusinessLogicException("Could not fetch payments by clientId: " + clientId, ex);
		}
	}

	public List<Payment> findByTransportId(Integer transportId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Payment p where p.transport.id = :transportId", Payment.class)
					.setParameter("transportId", transportId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Payments by transportId {}", transportId, ex);
			throw new BusinessLogicException("Could not fetch payments by transportId: " + transportId, ex);
		}
	}

	public List<Payment> findByStatus(PaymentStatus status) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Payment p where p.status = :status", Payment.class)
					.setParameter("status", status)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Payments by status {}", status, ex);
			throw new BusinessLogicException("Could not fetch payments by status: " + status, ex);
		}
	}

	public List<Payment> findOutstandingByClient(Integer clientId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from Payment p where p.client.id = :clientId and (p.status is null or p.status <> :paid)",
					Payment.class)
					.setParameter("clientId", clientId)
					.setParameter("paid", PaymentStatus.paid)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch outstanding Payments for clientId {}", clientId, ex);
			throw new BusinessLogicException("Could not fetch outstanding payments for clientId: " + clientId, ex);
		}
	}
}
