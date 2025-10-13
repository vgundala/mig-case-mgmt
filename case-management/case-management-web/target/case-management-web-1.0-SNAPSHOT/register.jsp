<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register - Case Management</title>
</head>
<body>
    <h2>Mega Investment Group - Case Management</h2>
    <h3>Register</h3>
    <%-- Basic registration form, will be replaced with Struts form later --%>
    <form action="register.do" method="post">
        Username: <input type="text" name="username"><br/>
        Password: <input type="password" name="password"><br/>
        Role:
        <select name="role">
            <option value="SALES_PERSON">Sales Person</option>
            <option value="SALES_MANAGER">Sales Manager</option>
        </select><br/>
        <input type="submit" value="Register">
    </form>
    <p><a href="login.jsp">Back to Login</a></p>
</body>
</html>