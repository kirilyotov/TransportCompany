package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Client;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.util.List;

public class ClientDao extends BaseDao<Client, Integer> {
	public ClientDao() {
		super(Client.class);
	}

	public List<Client> searchByNameOrSurname(String term) {
		String pattern = "%" + term.toLowerCase() + "%";
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from Client c where lower(c.name) like :pattern or lower(c.surname) like :pattern",
					Client.class)
					.setParameter("pattern", pattern)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to search Client by term {}", term, ex);
			throw new BusinessLogicException("Could not search clients by term: " + term, ex);
		}
	}

	public List<Client> findByCompanyId(Integer companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Client c where c.company.id = :companyId", Client.class)
					.setParameter("companyId", companyId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Client by companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not fetch clients by companyId: " + companyId, ex);
		}
	}

	public boolean clientExists(int clientId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			Integer result = session.createQuery(
					"select case when exists (select 1 from Client c where c.id = :clientId) then 1 else 0 end",
					Integer.class)
					.setParameter("clientId", clientId)
					.uniqueResult();
			return result != null && result == 1;
		} catch (Exception ex) {
			log.error("Failed to check existence of Client with id {}", clientId, ex);
			throw new BusinessLogicException("Could not check client existence for id: " + clientId, ex);
		}
	}
}