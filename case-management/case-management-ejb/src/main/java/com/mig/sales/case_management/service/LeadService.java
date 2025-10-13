package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class LeadService {

    @PersistenceContext(unitName = "case-management-pu")
    private EntityManager em;

    @EJB
    private LeadScoringService leadScoringService;

    public void createLead(Lead lead) {
        // Set initial status and creation date
        lead.setStatus("NEW");
        lead.setCreatedDate(new Date());

        // Calculate and set the lead score
        int score = leadScoringService.calculateScore(lead);
        lead.setLeadScore(score);

        em.persist(lead);
    }

    public Lead getLeadById(Long id) {
        return em.find(Lead.class, id);
    }

    public List<Lead> getAllLeads() {
        return em.createQuery("SELECT l FROM Lead l ORDER BY l.leadScore DESC", Lead.class).getResultList();
    }

    public void distributeLeads() {
        // Get all new leads
        List<Lead> newLeads = em.createQuery("SELECT l FROM Lead l WHERE l.status = 'NEW'", Lead.class).getResultList();

        // Get all sales people
        List<User> salesPeople = em.createQuery("SELECT u FROM User u WHERE u.role = 'SALES_PERSON'", User.class).getResultList();

        if (newLeads.isEmpty() || salesPeople.isEmpty()) {
            return; // Nothing to distribute
        }

        int salesPersonIndex = 0;
        for (Lead lead : newLeads) {
            User assignedUser = salesPeople.get(salesPersonIndex % salesPeople.size());
            lead.setAssignedTo(assignedUser);
            lead.setStatus("ASSIGNED");
            em.merge(lead);
            salesPersonIndex++;
        }
    }
}