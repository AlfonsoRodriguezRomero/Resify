<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">
    <h2>Inscripciones</h2>

    <table id="residenciasTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
             <th style="width: 200px;">Estado</th>
            <th style="width: 200px;">Residencia</th>
            <th style="width: 200px;">Fecha</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${inscripciones}" var="inscripcion">
            <tr>
                <td>
               		<spring:url value="/inscripciones/{inscripcionId}" var="inscripcionUrl">
                        <spring:param name="inscripcionId" value="${inscripcion.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(inscripcionUrl)}"><c:out value="${inscripcion.anciano.nombre} ${inscripcion.anciano.apellidos}"/></a>
                </td>
                <td>
                    <c:out value="${inscripcion.estado}"/>
                </td>
                <td>
                    <c:out value="${inscripcion.residencia.nombre}"/>
                </td>
                <td>
                    <fmt:formatDate value="${inscripcion.fecha}" pattern="yyyy/MM/dd"/>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>    
</resify:layout>
