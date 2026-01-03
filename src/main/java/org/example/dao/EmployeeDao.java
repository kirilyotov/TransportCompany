package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Employee;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeDao extends BaseDao<Employee, Integer> {
	public EmployeeDao() {
		super(Employee.class);
	}

	public List<Employee> findByCompanyId(Integer companyId) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Employee e where e.company.id = :companyId", Employee.class)
					.setParameter("companyId", companyId)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Employees by companyId {}", companyId, ex);
			throw new BusinessLogicException("Could not fetch employees by companyId: " + companyId, ex);
		}
	}

	public List<Employee> findByQualificationContains(String term) {
		String pattern = "%" + term.toLowerCase() + "%";
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery("from Employee e where lower(e.qualifications) like :pattern", Employee.class)
					.setParameter("pattern", pattern)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to search Employees by qualification term {}", term, ex);
			throw new BusinessLogicException("Could not search employees by qualification: " + term, ex);
		}
	}

	public List<Employee> findBySalaryBetween(BigDecimal min, BigDecimal max) {
		try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
			return session.createQuery(
					"from Employee e where (:min is null or e.salary >= :min) and (:max is null or e.salary <= :max) order by e.salary",
					Employee.class)
					.setParameter("min", min)
					.setParameter("max", max)
					.getResultList();
		} catch (Exception ex) {
			log.error("Failed to fetch Employees by salary range {} - {}", min, max, ex);
			throw new BusinessLogicException("Could not fetch employees by salary range", ex);
		}
	}

    public boolean employeeExists(int employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("select  case when exists (select 1 from Employee e where e.id = :id) then 1 else 0 end", Long.class)
                    .setParameter("id", employeeId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception ex) {
            log.error("Failed to check existence of Employee with id {}", employeeId, ex);
            throw new BusinessLogicException("Could not check employee existence", ex);
        }
    }
}