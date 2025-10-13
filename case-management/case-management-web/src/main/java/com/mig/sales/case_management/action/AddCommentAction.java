package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.CommentForm;
import com.mig.sales.case_management.model.Lead;
import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.LeadServiceLocal;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddCommentAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        CommentForm commentForm = (CommentForm) form;
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        // EJB Lookup
        LeadServiceLocal leadService = (LeadServiceLocal) new InitialContext().lookup("java:comp/env/ejb/LeadService");

        // Get the lead
        Lead lead = leadService.getLeadById(commentForm.getLeadId());

        // Add the comment
        leadService.addComment(lead, user, commentForm.getCommentText());

        // Forward back to the lead details page
        return new ActionForward(mapping.findForward("success").getPath() + "?id=" + lead.getId());
    }
}