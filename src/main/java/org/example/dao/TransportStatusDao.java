package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.TransportStatus;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.util.List;

public class TransportStatusDao extends BaseDao<TransportStatus, Integer> {
	public TransportStatusDao() {
		super(TransportStatus.class);
	}

	public List<TransportStatus> findHistoryForTransport(Integer transportId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from TransportStatus ts where ts.transport.id = :transportId order by ts.statusChange desc",
					TransportStatus.class)
					.setParameter("transportId", transportId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch TransportStatus history for transportId {}", transportId, ex);
			throw new BusinessLogicException("Could not fetch transport status history for transportId: " + transportId, ex);
		}
	}
}
