<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">

    <h2>Información de la inscripción</h2>


    <table class="table table-striped">
    	<tr>
            <th>Anciano</th>
            <td><b><c:out value="${inscripcion.anciano.nombre} ${inscripcion.anciano.apellidos}"/></b></td>
        </tr>
        <tr>
            <th>Edad</th>
            <td><c:out value="${inscripcion.anciano.edad}"/></td>
        </tr>
        <tr>
            <th>Declaración</th>
            <td><c:out value="${inscripcion.declaracion}"/></td>
        </tr>
        <tr>
            <th>Fecha</th>
            <td><c:out value="${inscripcion.fecha}"/></td>
        </tr>
        <tr>
            <th>Estado</th>
            <td><c:out value="${inscripcion.estado}"/></td>
        </tr>
        <c:if test="${inscripcion.estado == 'rechazada'}">
        <tr>
            <th>Justificación</th>
            <td><c:out value="${inscripcion.justificacion}"/></td>
        </tr>
        </c:if>
        <tr>
        	<th>Tiene dependencia grave</th>
        <c:choose>
        	<c:when test="${inscripcion.anciano.tieneDependenciaGrave}">
            	<td>Sí</td>
            </c:when>
            <c:otherwise>
            	<td>No</td>
           	</c:otherwise>
        </c:choose>       
        </tr>
    </table>
    
    <c:if test="${pageContext['request'].userPrincipal.name == inscripcion.residencia.manager.user.username }"> 
    <spring:url value="{inscripcionId}/edit" var="editUrl">
        <spring:param name="inscripcionId" value="${inscripcion.id}"/>
    </spring:url>
    	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar inscripción</a>
    </c:if>

</resify:layout>
