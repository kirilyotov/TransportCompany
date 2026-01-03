package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.exception.BusinessLogicException;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DAO for calling MySQL stored functions related to company revenue.
 * Uses native SQL to invoke database functions defined in liquibase changesets.
 */
public class CompanyRevenueFunctionDao {
    
    /**
     * Call MySQL function get_company_revenue to calculate revenue for a specific period.
     * Uses the stored function from changelog-1.11.
     *
     * @param companyId Company ID
     * @param startDateTime Start of period (inclusive)
     * @param endDateTime End of period (inclusive)
     * @return Total revenue from paid transports in the period
     */
    public BigDecimal getCompanyRevenue(Integer companyId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createNativeQuery(
                    "SELECT get_company_revenue(:companyId, :start, :end)", BigDecimal.class)
                    .setParameter("companyId", companyId)
                    .setParameter("start", startDateTime)
                    .setParameter("end", endDateTime)
                    .getSingleResult();
            
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception ex) {
            throw new BusinessLogicException(
                "Failed to call get_company_revenue function for company " + companyId, ex);
        }
    }
    
    /**
     * Call MySQL function get_company_revenue_period for calendar-based periods.
     * Uses the stored function from changelog-1.12.
     *
     * @param companyId Company ID
     * @param periodType Period type: 'day', 'week', 'month', 'quarter', 'year'
     * @param periodOffset How many periods back (0 = current, 1 = previous, etc.)
     * @return Total revenue from paid transports in the specified period
     */
    public BigDecimal getCompanyRevenuePeriod(Integer companyId, String periodType, Integer periodOffset) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            BigDecimal result = session.createNativeQuery(
                    "SELECT get_company_revenue_period(:companyId, :periodType, :offset)", BigDecimal.class)
                    .setParameter("companyId", companyId)
                    .setParameter("periodType", periodType)
                    .setParameter("offset", periodOffset)
                    .getSingleResult();
            
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception ex) {
            throw new BusinessLogicException(
                "Failed to call get_company_revenue_period function for company " + companyId, ex);
        }
    }

    /**
     * Get revenue for current day.
     */
    public BigDecimal getCurrentDayRevenue(Integer companyId) {
        return getCompanyRevenuePeriod(companyId, "day", 0);
    }
    
    /**
     * Get revenue for current month.
     */
    public BigDecimal getCurrentMonthRevenue(Integer companyId) {
        return getCompanyRevenuePeriod(companyId, "month", 0);
    }
    
    /**
     * Get revenue for previous month.
     */
    public BigDecimal getPreviousMonthRevenue(Integer companyId) {
        return getCompanyRevenuePeriod(companyId, "month", 1);
    }
    
    /**
     * Get revenue for current year.
     */
    public BigDecimal getCurrentYearRevenue(Integer companyId) {
        return getCompanyRevenuePeriod(companyId, "year", 0);
    }
    
    /**
     * Get revenue for current quarter.
     */
    public BigDecimal getCurrentQuarterRevenue(Integer companyId) {
        return getCompanyRevenuePeriod(companyId, "quarter", 0);
    }
}
