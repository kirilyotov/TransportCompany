package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.DriverTransportStatsDto;
import org.example.entity.Transport;
import org.example.entity.enums.TransportStatus;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransportDao extends BaseDao<Transport, Integer> {
	public TransportDao() {
		super(Transport.class);
	}

	public List<Transport> findByDestination(String destination) {
		String pattern = "%" + destination.toLowerCase() + "%";
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Transport t where lower(t.endPoint) like :pattern", Transport.class)
					.setParameter("pattern", pattern)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to search Transport by destination {}", destination, ex);
			throw new BusinessLogicException("Could not search transports by destination: " + destination, ex);
		}
	}

	public List<Transport> findByStatus(TransportStatus status) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Transport t where t.status = :status", Transport.class)
					.setParameter("status", status)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Transport by status {}", status, ex);
			throw new BusinessLogicException("Could not fetch transports by status: " + status, ex);
		}
	}

	public List<Transport> findByCompanyId(Integer companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Transport t where t.company.id = :companyId", Transport.class)
					.setParameter("companyId", companyId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Transport by companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not fetch transports by companyId: " + companyId, ex);
		}
	}

	public List<Transport> findByDriverId(Integer driverId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Transport t where t.driver.id = :driverId", Transport.class)
					.setParameter("driverId", driverId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Transport by driverId {}", driverId, ex);
			throw new BusinessLogicException("Could not fetch transports by driverId: " + driverId, ex);
		}
	}

	public List<Transport> findByClientId(Integer clientId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Transport t where t.client.id = :clientId", Transport.class)
					.setParameter("clientId", clientId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Transport by clientId {}", clientId, ex);
			throw new BusinessLogicException("Could not fetch transports by clientId: " + clientId, ex);
		}
	}

	public List<Transport> findBetweenDates(LocalDateTime from, LocalDateTime to) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from Transport t where (:from is null or t.departureDate >= :from) and (:to is null or t.arrivalDate <= :to)",
					Transport.class)
					.setParameter("from", from)
					.setParameter("to", to)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Transport between dates {} and {}", from, to, ex);
			throw new BusinessLogicException("Could not fetch transports by date range", ex);
		}
	}

	public long countByCompanyId(Integer companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("select count(t) from Transport t where t.company.id = :companyId", Long.class)
					.setParameter("companyId", companyId)
					.getSingleResult();
		} catch (Exception ex) {
			log.error("Failed to count Transport for companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not count transports for companyId: " + companyId, ex);
		}
	}

	public BigDecimal sumRevenueByCompanyBetween(Integer companyId, LocalDateTime from, LocalDateTime to) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			BigDecimal result = session.createQuery(
					"select coalesce(sum(t.price), 0) from Transport t " +
					"where t.company.id = :companyId and (:from is null or t.departureDate >= :from) and (:to is null or t.departureDate <= :to)",
					BigDecimal.class)
					.setParameter("companyId", companyId)
					.setParameter("from", from)
					.setParameter("to", to)
					.getSingleResult();
			return result == null ? BigDecimal.ZERO : result;
		} catch (Exception ex) {
			log.error("Failed to sum Transport revenue for companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not sum transport revenue for companyId: " + companyId, ex);
		}
	}

	public List<DriverTransportStatsDto> driverStatsByCompany(Integer companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"select new org.example.dto.DriverTransportStatsDto(t.driver.id, t.driver.name, count(t), coalesce(sum(t.price), 0)) " +
					"from Transport t where t.company.id = :companyId " +
					"group by t.driver.id, t.driver.name " +
					"order by count(t) desc",
					DriverTransportStatsDto.class)
					.setParameter("companyId", companyId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to build driver stats for companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not fetch driver transport stats for companyId: " + companyId, ex);
		}
	}
}
