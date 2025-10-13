<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Case Management</title>
</head>
<body>
    <h2>Mega Investment Group - Case Management</h2>
    <h3>Login</h3>
    <%-- Basic login form, will be replaced with Struts form later --%>
    <form action="login.do" method="post">
        Username: <input type="text" name="username"><br/>
        Password: <input type="password" name="password"><br/>
        <input type="submit" value="Login">
    </form>
    <p><a href="register.jsp">Register</a></p>
</body>
</html>