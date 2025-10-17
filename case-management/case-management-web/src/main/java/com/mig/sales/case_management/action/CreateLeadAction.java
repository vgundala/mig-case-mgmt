package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.LeadForm;
import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.service.LeadServiceLocal;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateLeadAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LeadForm leadForm = (LeadForm) form;

        // Use the ServiceLocator to get the EJB
        LeadServiceLocal leadService = (LeadServiceLocal) new InitialContext().lookup("java:comp/env/ejb/LeadService");

        // Create a new Lead entity
        Lead lead = new Lead();
        lead.setLeadName(leadForm.getLeadName());
        lead.setCompany(leadForm.getCompany());
        lead.setEmail(leadForm.getEmail());
        lead.setPhone(leadForm.getPhone());
        lead.setPotentialValue(leadForm.getPotentialValue());
        lead.setLeadSource(leadForm.getLeadSource());

        // Call the EJB to create the lead
        leadService.createLead(lead);

        // Forward to a success page (e.g., the dashboard)
        return mapping.findForward("success");
    }
}