package com.mig.sales.case_management.action;

import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.service.LeadService;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DashboardAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Use the ServiceLocator to get the EJB
        LeadService leadService = (LeadService) new InitialContext().lookup("java:comp/env/ejb/LeadService");

        // Get the list of leads
        List<Lead> leads = leadService.getAllLeads();

        // Set the leads as a request attribute
        request.setAttribute("leads", leads);

        // Forward to the dashboard JSP
        return mapping.findForward("success");
    }
}