package com.mig.sales.case_management.action;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.service.LeadServiceLocal;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewLeadAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        // EJB Lookup
        LeadServiceLocal leadService = (LeadServiceLocal) new InitialContext().lookup("java:comp/env/ejb/LeadService");

        // Get the lead to view
        Lead lead = leadService.getLeadById(Long.parseLong(request.getParameter("id")));

        // Set the lead as a request attribute
        request.setAttribute("lead", lead);
        request.setAttribute("leadHistory", leadService.getLeadHistory(lead));

        // Forward to the lead details JSP
        return mapping.findForward("success");
    }
}