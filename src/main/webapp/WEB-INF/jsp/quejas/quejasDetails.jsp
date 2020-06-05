<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="quejas">

	<h2>Informacion de la queja</h2>


	<table class="table table-striped">
		<tr>
			<th>Titulo</th>
			<td><b><c:out value="${queja.titulo}" /></b></td>
		</tr>
		<tr>
			<th>Descripcion</th>
			<td><c:out value="${queja.descripcion}" /></td>
		</tr>
		<tr>
			<th>Anciano</th>
			<c:choose>
	           	<c:when test="${queja.anonimo}">
	        		<td><c:out value="ANÓNIMO" /></td>
	        	</c:when>
	        	<c:otherwise>
	            	<td><c:out value="${queja.anciano.nombre}" />
	            	<c:out value=" ${queja.anciano.apellidos}" /></td>
	        	</c:otherwise>
	        </c:choose>
		</tr>
		<tr>
			<th>Fecha</th>
			<td><c:out value="${queja.fecha}" /></td>
		</tr>

	</table>

</resify:layout>
