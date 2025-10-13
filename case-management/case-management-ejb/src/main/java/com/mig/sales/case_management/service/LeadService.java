package com.mig.sales.case_management.service;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.LeadHistory;
import com.mig.sales.case_management.model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class LeadService implements LeadServiceLocal {

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

    @Override
    public void updateLead(Lead lead) {
        em.merge(lead);
    }

    @Override
    public void addComment(Lead lead, User user, String commentText) {
        LeadHistory history = new LeadHistory();
        history.setLead(lead);
        history.setUser(user);
        history.setCommentText(commentText);
        history.setTimestamp(new java.util.Date());
        history.setAction("Comment Added");
        em.persist(history);
    }

    @Override
    public List<LeadHistory> getLeadHistory(Lead lead) {
        return em.createQuery("SELECT h FROM LeadHistory h WHERE h.lead = :lead ORDER BY h.timestamp DESC", LeadHistory.class)
                .setParameter("lead", lead)
                .getResultList();
    }
}