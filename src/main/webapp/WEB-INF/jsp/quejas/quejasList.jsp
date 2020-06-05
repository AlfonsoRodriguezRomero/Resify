<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="quejas">
    <h2>Quejas</h2>

    <table id="quejasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 200px;">Queja</th>
            <th style="width: 200px">Anciano</th>
            <th style="width: 180px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${quejas}" var="queja">
            <tr>
                <td>
               		<spring:url value="/quejas/{quejaId}" var="quejaUrl">
                        <spring:param name="quejaId" value="${queja.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(quejaUrl)}"><c:out value="${queja.titulo}"/></a>
                </td>
                <td>
 	                <c:choose>
	                	<c:when test="${queja.anonimo}">
	                        <c:out value="ANÓNIMO"/>
	                    </c:when>
	                    <c:otherwise>
	                        <c:out value="${queja.anciano.nombre}"/>
	                        <c:out value=" ${queja.anciano.apellidos}"/>
	                    </c:otherwise>
	                </c:choose>
                </td>
                <td>
                    <fmt:formatDate value="${queja.fecha}" pattern="yyyy/MM/dd"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>  
    
</resify:layout>
