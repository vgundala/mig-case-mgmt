<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Dashboard - Case Management</title>
</head>
<body>
    <h2>Lead Dashboard</h2>

    <p><a href="createLead.jsp">Create New Lead</a></p>

    <c:if test="${sessionScope.user.role == 'SALES_MANAGER'}">
        <p><html:link action="/distributeLeads">Distribute New Leads</html:link></p>
    </c:if>

    <table border="1">
        <thead>
            <tr>
                <th>Lead Name</th>
                <th>Company</th>
                <th>Potential Value</th>
                <th>Lead Score</th>
                <th>Status</th>
                <th>Assigned To</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${leads}" var="lead">
                <tr>
                    <td><html:link action="/viewLead?id=${lead.id}">${lead.leadName}</html:link></td>
                    <td>${lead.company}</td>
                    <td>${lead.potentialValue}</td>
                    <td>${lead.leadScore}</td>
                    <td>${lead.status}</td>
                    <td>
                        <c:if test="${not empty lead.assignedTo}">
                            ${lead.assignedTo.username}
                        </c:if>
                        <c:if test="${empty lead.assignedTo}">
                            Unassigned
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>