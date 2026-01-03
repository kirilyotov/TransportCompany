package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Vehicle;
import org.example.entity.enums.VehicleStatus;
import org.example.entity.enums.VehicleType;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.util.List;

public class VehicleDao extends BaseDao<Vehicle, Integer> {
    public VehicleDao() {
        super(Vehicle.class);
    }

    public List<Vehicle> findByCompanyId(Integer companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vehicle v where v.company.id = :companyId", Vehicle.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Failed to fetch Vehicle by companyId {}", companyId, ex);
            throw new BusinessLogicException("Could not fetch vehicles by companyId: " + companyId, ex);
        }
    }

    public List<Vehicle> findByType(VehicleType type) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vehicle v where v.type = :type", Vehicle.class)
                    .setParameter("type", type)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Failed to fetch Vehicle by type {}", type, ex);
            throw new BusinessLogicException("Could not fetch vehicles by type: " + type, ex);
        }
    }

    public List<Vehicle> findByStatus(VehicleStatus status) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vehicle v where v.status = :status", Vehicle.class)
                    .setParameter("status", status)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Failed to fetch Vehicle by status {}", status, ex);
            throw new BusinessLogicException("Could not fetch vehicles by status: " + status, ex);
        }
    }

    public boolean vehicleExists(int vehicleId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Integer count = session.createQuery("select  case when exists (select 1 from Vehicle v where v.id = :id) then 1 else 0 end", Integer.class)
                    .setParameter("id", vehicleId)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception ex) {
            log.error("Failed to check existence of Vehicle with id {}", vehicleId, ex);
            throw new BusinessLogicException("Could not check vehicle existence for id: " + vehicleId, ex);
        }
    }
}
