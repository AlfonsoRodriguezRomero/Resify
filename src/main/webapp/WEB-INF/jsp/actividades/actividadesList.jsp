<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="actividades">
    <h2>Actividades</h2>

    <table id="actividadesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Titulo</th>
            <th style="width: 200px;">Fecha inicio</th>
            <th style="width: 120px">Hora inicio</th>
            <th style="width: 120px">Hora fin<th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${actividades}" var="actividad">
            <tr>
                <td>
               		<spring:url value="/actividades/{actividadId}" var="actividadUrl">
                        <spring:param name="actividadId" value="${actividad.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(actividadUrl)}"><c:out value="${actividad.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${actividad.fechaInicio}"/>
                </td>
                <td>
                    <c:out value="${actividad.horaInicio}"/>
                </td>
                <td>
                    <c:out value="${actividad.horaFin}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <security:authorize access="hasAuthority('manager')">
    	<c:if test="${noTieneResi == false}">
	    	<spring:url value="/actividades/new" var="addUrl">
		    </spring:url>
		    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir nueva actividad</a>
		</c:if>
	</security:authorize>
    
    
</resify:layout>
