package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.CompanyRevenueDto;
import org.example.entity.Company;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class CompanyDao extends BaseDao<Company, Integer> {
	public CompanyDao() {
		super(Company.class);
	}

	public List<Company> findByNameContaining(String term) {
		String pattern = "%" + term.toLowerCase() + "%";
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from Company c where lower(c.name) like :pattern order by c.name",
					Company.class
			).setParameter("pattern", pattern)
			.getResultList();
		}
	}


	public List<CompanyRevenueDto> fetchRevenueOrdered(LocalDateTime from, LocalDateTime to) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"select new org.example.dto.CompanyRevenueDto(c.id, c.name, coalesce(sum(t.price), 0)) " +
					"from Transport t join t.company c " +
					"where (:from is null or t.departureDate >= :from) and (:to is null or t.departureDate <= :to) " +
					"group by c.id, c.name " +
					"order by coalesce(sum(t.price), 0) desc",
					CompanyRevenueDto.class)
					.setParameter("from", from)
					.setParameter("to", to)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch revenue ordered for companies", ex);
			throw new BusinessLogicException("Could not fetch company revenues", ex);
		}
	}

	public boolean companyExists(int companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			Integer result = session.createQuery(
					"select case when exists (select 1 from Company c where c.id = :companyId) then 1 else 0 end",
					Integer.class)
					.setParameter("companyId", companyId)
					.uniqueResult();
			return result != null && result == 1;
		} catch (Exception ex) {
			log.error("Failed to check existence of Company with id {}", companyId, ex);
			throw new BusinessLogicException("Could not check company existence for id: " + companyId, ex);
		}
	}
}
