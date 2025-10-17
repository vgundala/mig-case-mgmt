package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.LeadForm;
import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.LeadServiceLocal;
import com.mig.sales.case_management.service.WorkflowServiceLocal;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateLeadAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LeadForm leadForm = (LeadForm) form;
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        // EJB Lookups
        InitialContext ctx = new InitialContext();
        LeadServiceLocal leadService = (LeadServiceLocal) ctx.lookup("java:comp/env/ejb/LeadService");
        WorkflowServiceLocal workflowService = (WorkflowServiceLocal) ctx.lookup("java:comp/env/ejb/WorkflowService");

        // Get the lead to update
        Lead lead = leadService.getLeadById(Long.parseLong(request.getParameter("id")));

        // Update the lead details
        lead.setLeadName(leadForm.getLeadName());
        lead.setCompany(leadForm.getCompany());
        lead.setEmail(leadForm.getEmail());
        lead.setPhone(leadForm.getPhone());
        lead.setPotentialValue(leadForm.getPotentialValue());
        lead.setLeadSource(leadForm.getLeadSource());

        leadService.updateLead(lead);

        return mapping.findForward("success");
    }
}