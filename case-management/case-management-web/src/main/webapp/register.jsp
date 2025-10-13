<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html>
<head>
    <title>Register - Case Management</title>
</head>
<body>
    <h2>Mega Investment Group - Case Management</h2>
    <h3>Register</h3>

    <html:form action="/register">
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
                <td>Role:</td>
                <td>
                    <html:select property="role">
                        <html:option value="SALES_PERSON">Sales Person</html:option>
                        <html:option value="SALES_MANAGER">Sales Manager</html:option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <html:submit value="Register"/>
                </td>
            </tr>
        </table>
    </html:form>

    <p><a href="login.jsp">Back to Login</a></p>
</body>
</html>