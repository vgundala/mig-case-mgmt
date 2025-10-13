<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Lead Details - Case Management</title>
</head>
<body>
    <h2>Lead Details</h2>

    <html:form action="/updateLead">
        <html:hidden property="id" value="${lead.id}"/>
        <table>
            <tr>
                <td>Lead Name:</td>
                <td><html:text property="leadName" value="${lead.leadName}"/></td>
            </tr>
            <tr>
                <td>Company:</td>
                <td><html:text property="company" value="${lead.company}"/></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><html:text property="email" value="${lead.email}"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><html:text property="phone" value="${lead.phone}"/></td>
            </tr>
            <tr>
                <td>Potential Value:</td>
                <td><html:text property="potentialValue" value="${lead.potentialValue}"/></td>
            </tr>
            <tr>
                <td>Lead Source:</td>
                <td>
                    <html:select property="leadSource" value="${lead.leadSource}">
                        <html:option value="Partner Referral">Partner Referral</html:option>
                        <html:option value="Webinar">Webinar</html:option>
                        <html:option value="Website Signup">Website Signup</html:option>
                        <html:option value="Cold Call">Cold Call</html:option>
                    </html:select>
                </td>
            </tr>
        </table>

        <c:if test="${sessionScope.user.role == 'SALES_PERSON' && lead.status == 'IN_PROGRESS' && lead.potentialValue > 1000000}">
            <html:submit property="submit" value="escalate">Escalate to Manager</html:submit>
        </c:if>

        <c:if test="${sessionScope.user.role == 'SALES_MANAGER' && lead.status == 'PRE_CONVERSION'}">
            <html:submit property="submit" value="approve">Approve Lead</html:submit>
        </c:if>

        <html:submit property="submit" value="update">Update Lead</html:submit>
    </html:form>

    <p><a href="dashboard.do">Back to Dashboard</a></p>
</body>
</html>