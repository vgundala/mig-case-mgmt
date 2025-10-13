package com.mig.sales.case_management.action;

import com.mig.sales.case_management.action.form.RegisterForm;
import com.mig.sales.case_management.model.User;
import com.mig.sales.case_management.service.UserService;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        RegisterForm registerForm = (RegisterForm) form;

        // Use the ServiceLocator to get the EJB
        UserService userService = (UserService) new InitialContext().lookup("java:comp/env/ejb/UserService");

        // Create a new User entity
        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setPassword(registerForm.getPassword());
        user.setRole(registerForm.getRole());

        // Call the EJB to register the user
        userService.registerUser(user);

        // Forward to a success page (e.g., the login page)
        return mapping.findForward("success");
    }
}