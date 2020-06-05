<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<resify:layout pageName="feedbacks">
    <h2>
        <c:if test="${feedback['new']}">Nuevo </c:if>Feedback
    </h2>

    <table id="residenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Residencia</th>
            <th style="width: 150px;">Excursión</th>
            <th style="width: 500;">Descripción</th>
            <th style="width: 100;">Valoración</th>
           
            <security:authorize access="hasAuthority('organizador')"> 
            </security:authorize>                       
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${feedbacks}" var="feedback">
            <tr>
               	<security:authorize access="hasAuthority('organizador')">
            
                <td>
               		<spring:url value="/feedbacks/{feedbackId}/edit" var="feedbackUrl">
                        <spring:param name="feedbackId" value="${feedback.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(feedbackUrl)}"><c:out value="${feedback.residencia.nombre}"/></a>
                </td>
                <td>
                    <c:out value="${feedback.excursion.titulo}"/>
                </td>
                <td>
                    <c:out value="${feedback.descripcion}"/>
                </td>
                
                <td>
                    <c:out value="${feedback.valoracion}"/>
                </td>
                </security:authorize>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>    
</resify:layout>
