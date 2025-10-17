<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html>
<head>
    <title>Login - Case Management</title>
</head>
<body>
    <h2>Mega Investment Group - Case Management</h2>
    <h3>Login</h3>

    <html:form action="/login">
        <table>
            <tr>
                <td>Username:</td>
                <td><html:text property="username"/></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><html:password property="password"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <html:submit value="Login"/>
                </td>
            </tr>
        </table>
    </html:form>

    <p><a href="register.jsp">Register</a> | <a href="createLead.jsp">Create a Lead</a></p>
</body>
</html>