package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.LoginForm;
import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.UserService;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        LoginForm loginForm = (LoginForm) form;

        // Use the ServiceLocator to get the EJB
        UserService userService = (UserService) new InitialContext().lookup("java:comp/env/ejb/UserService");

        // Attempt to log the user in
        User user = userService.login(loginForm.getUsername(), loginForm.getPassword());

        if (user != null) {
            // Store the user in the session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return mapping.findForward("success");
        } else {
            // Invalid credentials
            return mapping.findForward("failure");
        }
    }
}