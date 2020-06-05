<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="incidencias">
    <h2>Incidencias</h2>

    <table id="incidenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Titulo</th>
            <th style="width: 200px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${incidencias}" var="incidencia">
            <tr>
                <td>
               		<spring:url value="/incidencias/{incidenciaId}" var="incidenciaUrl">
                        <spring:param name="incidenciaId" value="${incidencia.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(incidenciaUrl)}"><c:out value="${incidencia.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${incidencia.fecha}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <c:if test="${noTieneResi == false}">
    	<spring:url value="/incidencias/new" var="addUrl">
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir nueva incidencia</a>
    </c:if>
    
</resify:layout>
