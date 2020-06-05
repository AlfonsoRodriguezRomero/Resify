<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<resify:layout pageName="peticionesExcursion">
    <h2>
        <c:if test="${peticionExcursion['new']}">Nueva </c:if>Petición De Excursion
    </h2>

    <table id="residenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Residencia</th>
             <th style="width: 200px;">Estado</th>
            <th style="width: 200px;">Excursión</th>
            <security:authorize access="hasAuthority('organizador')"> 
               <th style="width: 200px;">Fecha</th>
            </security:authorize>
            <security:authorize access="hasAuthority('manager')"> 
               <th style="width: 200px;">Justificación</th>
            </security:authorize>
            
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${peticionesExcursion}" var="peticionExcursion">
            <tr>
               	<security:authorize access="hasAuthority('organizador')">
            
                <td>
               		<spring:url value="/peticiones-excursion/{peticionExcursionId}/edit" var="peticionExcursionUrl">
                        <spring:param name="peticionExcursionId" value="${peticionExcursion.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(peticionExcursionUrl)}"><c:out value="${peticionExcursion.residencia.nombre}"/></a>
                </td>
                </security:authorize>
                <security:authorize access="hasAuthority('manager')"> 
                <td>
                    <c:out value="${peticionExcursion.residencia.nombre}"/>
                </td>
                </security:authorize> 
                <td>
                    <c:out value="${peticionExcursion.estado}"/>
                </td>
                <td>
                    <c:out value="${peticionExcursion.excursion.titulo}"/>
                </td>
                <security:authorize access="hasAuthority('organizador')"> 
                <td>
                    <fmt:formatDate value="${peticionExcursion.fecha}" pattern="yyyy/MM/dd"/>
                </td>
                </security:authorize>
                <security:authorize access="hasAuthority('manager')">
                <td>
                    <c:out value="${peticionExcursion.justificacion}"/>
                </td>
                </security:authorize>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>    
</resify:layout>
