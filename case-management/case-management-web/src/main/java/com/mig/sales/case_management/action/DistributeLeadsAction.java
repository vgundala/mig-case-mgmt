package com.mig.sales.case_management.action;

import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.LeadService;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DistributeLeadsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        // Ensure the user is a sales manager
        if (user != null && "SALES_MANAGER".equals(user.getRole())) {
            // Use the ServiceLocator to get the EJB
            LeadService leadService = (LeadService) new InitialContext().lookup("java:comp/env/ejb/LeadService");
            leadService.distributeLeads();
            return mapping.findForward("success");
        } else {
            // Not authorized
            return mapping.findForward("failure");
        }
    }
}