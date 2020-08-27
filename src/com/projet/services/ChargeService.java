package com.projet.services;

import com.projet.entities.AccountItem;
import com.projet.entities.Charge;
import com.projet.entities.FinancialYear;
import com.projet.entities.User;
import com.projet.enumeration.ChargeStatus;
import com.projet.utility.DateManager;

import java.util.*;

/**
 * =================================================================
 * Created by Intellij IDEA.
 *
 * @author lucas
 * @project Projet TFE
 * Date: 13/08/2020
 * Time: 19:08
 * =================================================================
 */
public class ChargeService extends Service<Charge> {


    public ChargeService(Class<?> ec) {
        super(ec);
    }

    @Override
    public Charge save(Charge charge) {
        if (charge.getId() == 0) {
            em.persist(charge);
        } else {
            charge = em.merge(charge);
        }

        return charge;
    }

    public List<Charge> getByUser(User user) {
        Map<String, User> param = new HashMap<>();
        param.put("user", user);

        return finder.findByNamedQuery("Charge.findByUser", param);
    }

    public List<Charge> getByUserOrderByDate(User user) {
        Map<String, User> param = new HashMap<>();
        param.put("user", user);

        return finder.findByNamedQuery("Charge.findByUserOrderByDate", param);
    }

    /**
     * Method finalize the charge creation. User and financialYear is added.
     *
     * @param charge charge who need to be completed
     * @param user user who is concerned by the charge
     * @return Charge ready to be persisted
     */
    public Charge createcharge(Charge charge, User user) {
        FinancialYearService faService = new FinancialYearService(FinancialYear.class);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(charge.getCreatedAt());

        // year of the created charge
        int financialYearDate = calendar.get(Calendar.YEAR);

        // financial with same year from database
        FinancialYear year = faService.getUserFinancialYearByDate(user, financialYearDate);

        // if there is no financialYear a new one is created
        if (year == null) {
            year = faService.createFinancialYear(financialYearDate, user);
        }

        // set the charge financialYear
        charge.setFinancialYear(em.merge(year));

        // set charge status
        charge = checkStatus(charge);

        // set user and return charge
        return user.addCharge(charge);
    }

    /**
     * check if the status need to be modify
     * @param charge charge to check
     * @return charge with correct status
     */
    private Charge checkStatus(Charge charge) {
        // if there is a due date and the status is not payed then it check if the status need to be modify
        if (charge.getDueAt() != null && !charge.getStatus().equals(ChargeStatus.PAYED)) {
            // if the due date is outdated then the status is modify
            if (DateManager.compareToCurrentDate(charge.getDueAt()) < 0 )
                charge.setStatus(ChargeStatus.LATE);
        }

        return charge;
    }

    /**
     * Calculate the sum of the charge in the list given in parameter. If a status is given
     * the sum depend of the status.
     * @param charges List to be calculate
     * @param status the status to sort the list
     * @return the sum of the charge
     */
    public double totalCharge(List<Charge> charges, ChargeStatus status) {
        double total = 0;

        //if no status is given return sum of all charge
        if (status == null) {
            for (Charge charge: charges) {
                total += charge.getAmount();
            }
            // return sum of charge with the status given
        } else {
            for (Charge charge: charges) {
                if (charge.getStatus().equals(status)) {
                    total += charge.getAmount();
                }
            }
        }

        return total;
    }

    public double totalDeductibleCharge(int year, User user) {
        FinancialYearService service = new FinancialYearService(FinancialYear.class);
        double total = 0;

        FinancialYear financialYear = service.getUserFinancialYearByDate(user, year);

        if (financialYear != null) {
            List<AccountItem> accountItems = financialYear.getAccountItems();

            if (!accountItems.isEmpty()) {
                for (AccountItem item: accountItems) {
                    double amount = item.getAmount();
                    double privatePart = item.getPrivatePart();
                    double deductible = item.getTaxDeductible();

                    total += ((amount - ((amount / 100) * privatePart)) / 100) * deductible;
                }
            }
        }

        return total;
    }


}
