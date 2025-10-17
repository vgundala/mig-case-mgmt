<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html>
<head>
    <title>Create Lead - Case Management</title>
</head>
<body>
    <h2>Create New Lead</h2>

    <html:form action="/createLead">
        <table>
            <tr>
                <td>Lead Name:</td>
                <td><html:text property="leadName"/></td>
            </tr>
            <tr>
                <td>Company:</td>
                <td><html:text property="company"/></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><html:text property="email"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><html:text property="phone"/></td>
            </tr>
            <tr>
                <td>Potential Value:</td>
                <td><html:text property="potentialValue"/></td>
            </tr>
            <tr>
                <td>Lead Source:</td>
                <td>
                    <html:select property="leadSource">
                        <html:option value="Partner Referral">Partner Referral</html:option>
                        <html:option value="Webinar">Webinar</html:option>
                        <html:option value="Website Signup">Website Signup</html:option>
                        <html:option value="Cold Call">Cold Call</html:option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <html:submit value="Create Lead"/>
                </td>
            </tr>
        </table>
    </html:form>
</body>
</html>