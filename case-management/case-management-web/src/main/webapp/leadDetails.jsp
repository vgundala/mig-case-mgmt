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

        <html:submit>Update Lead</html:submit>
    </html:form>

    <c:if test="${sessionScope.user.role == 'SALES_PERSON' && lead.status == 'IN_PROGRESS' && lead.potentialValue > 1000000}">
        <html:form action="/escalateLead">
            <html:hidden property="id" value="${lead.id}"/>
            <html:submit>Escalate to Manager</html:submit>
        </html:form>
    </c:if>

    <c:if test="${sessionScope.user.role == 'SALES_MANAGER' && lead.status == 'PRE_CONVERSION'}">
        <html:form action="/approveLead">
            <html:hidden property="id" value="${lead.id}"/>
            <html:submit>Approve Lead</html:submit>
        </html:form>
    </c:if>

    <hr/>
    <h3>Comments</h3>
    <html:form action="/addComment">
        <html:hidden property="leadId" value="${lead.id}"/>
        <html:textarea property="commentText" cols="50" rows="5"/>
        <html:submit>Add Comment</html:submit>
    </html:form>

    <table border="1">
        <thead>
            <tr>
                <th>User</th>
                <th>Comment</th>
                <th>Timestamp</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${leadHistory}" var="history">
                <tr>
                    <td>${history.user.username}</td>
                    <td>${history.commentText}</td>
                    <td>${history.timestamp}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <p><a href="dashboard.do">Back to Dashboard</a></p>
</body>
</html>