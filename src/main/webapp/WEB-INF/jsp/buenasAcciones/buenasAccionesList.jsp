<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="buenasAcciones">
    <h2>Buenas Acciones</h2>

    <table id="buenasAccionesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Titulo</th>
            <th style="width: 200px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${buenasAcciones}" var="buenaAccion">
            <tr>
                <td>
               		<spring:url value="/buenas-acciones/{buenaAccionId}" var="buenaAccionUrl">
                        <spring:param name="buenaAccionId" value="${buenaAccion.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(buenaAccionUrl)}"><c:out value="${buenaAccion.titulo}"/></a>
                </td>
                <td>
                    <c:out value="${buenaAccion.fecha}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <c:if test="${noTieneResi == false}">
    	<spring:url value="/buenas-acciones/new" var="addUrl">
	    </spring:url>
	    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir nueva buena accion</a>
    </c:if>
    
</resify:layout>
