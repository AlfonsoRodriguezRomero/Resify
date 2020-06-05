<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="residencias">

    <h2>Información de la residencia</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${residencia.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Dirección</th>
            <td><c:out value="${residencia.direccion}"/></td>
        </tr>
        <tr>
            <th>Descripción</th>
            <td><c:out value="${residencia.descripcion}"/></td>
        </tr>
        <tr>
            <th>Aforo</th>
            <td><c:out value="${residencia.aforo}"/></td>
        </tr>
        <tr>
            <th>Más Info</th>
            <td><c:out value="${residencia.masInfo}"/></td>
        </tr>
        <tr>
            <th>Teléfono</th>
            <td><c:out value="${residencia.telefono}"/></td>
        </tr>
        <tr>
            <th>Correo</th>
            <td><c:out value="${residencia.correo}"/></td>
        </tr>
        <tr>
            <th>Hora de apertura</th>
            <td><c:out value="${residencia.horaApertura}"/></td>
        </tr>
        <tr>
            <th>Hora de cierre</th>
            <td><c:out value="${residencia.horaCierre}"/></td>
        </tr>
        <tr>
            <th>Edad máxima</th>
            <td><c:out value="${residencia.edadMaxima}"/></td>
        </tr>
        <tr>
            <th>¿Acepta personas con dependencia grave?</th>
            <c:if test="${residencia.aceptaDependenciaGrave == true}">
            <td><c:out value="Sí"/></td>
            </c:if>
            <c:if test="${residencia.aceptaDependenciaGrave == false}">
            <td><c:out value="No"/></td>
            </c:if>
        </tr>
    </table>
    
    <c:if test="${pageContext['request'].userPrincipal.name == residencia.manager.user.username}"> 
    	<spring:url value="/residencias/{residenciaId}/edit" var="editUrl">
       		<spring:param name="residenciaId" value="${residencia.id}"/>
    	</spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar residencia</a>
    </c:if>
    <c:if test="${fn:contains(pageContext['request'].userPrincipal.authorities, 'anciano') && tienePendiente == false && tieneAceptada == false}"> 
    	<spring:url value="../inscripciones/new/{residenciaId}" var="inscribirseUrl">
    		<spring:param name="residenciaId" value="${residencia.id}"/>
    	</spring:url>
   		<a href="${inscribirseUrl}" class="btn btn-default">Inscribirse en la residencia</a>
    </c:if>
    <c:if test="${fn:contains(pageContext['request'].userPrincipal.authorities, 'anciano') && tienePendiente == true}">
    	<td><c:out value="TIENE UNA INSCRIPCIÓN PENDIENTE EN ESTA RESIDENCIA" /></td>
    </c:if>

</resify:layout>
