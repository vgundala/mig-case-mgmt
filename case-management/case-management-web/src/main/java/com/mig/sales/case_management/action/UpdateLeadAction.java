package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.LeadForm;
import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.LeadService;
import com.mig.sales.case_management.service.WorkflowService;
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
        LeadService leadService = (LeadService) ctx.lookup("java:comp/env/ejb/LeadService");
        WorkflowService workflowService = (WorkflowService) ctx.lookup("java:comp/env/ejb/WorkflowService");

        // Get the lead to update
        Lead lead = leadService.getLeadById(Long.parseLong(request.getParameter("id")));

        // Update the lead details
        lead.setLeadName(leadForm.getLeadName());
        lead.setCompany(leadForm.getCompany());
        lead.setEmail(leadForm.getEmail());
        lead.setPhone(leadForm.getPhone());
        lead.setPotentialValue(leadForm.getPotentialValue());
        lead.setLeadSource(leadForm.getLeadSource());

        // Check if the lead should be escalated
        if ("escalate".equals(request.getParameter("submit"))) {
            workflowService.escalateLead(lead, user);
        }

        // Check if the lead should be approved
        if ("approve".equals(request.getParameter("submit"))) {
            workflowService.approveLead(lead);
        }

        // Save the updated lead
        // The services should handle the merging

        return mapping.findForward("success");
    }
}