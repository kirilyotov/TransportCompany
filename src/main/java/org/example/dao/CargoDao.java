package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Cargo;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.util.List;

public class CargoDao extends BaseDao<Cargo, Integer> {
	public CargoDao() {
		super(Cargo.class);
	}

	public List<Cargo> findByTransportId(Integer transportId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Cargo c where c.transport.id = :transportId", Cargo.class)
					.setParameter("transportId", transportId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Cargo by transportId {}", transportId, ex);
			throw new BusinessLogicException("Could not fetch Cargo by transportId: " + transportId, ex);
		}
	}
}
