<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="excursiones">

	<h2>Informacion de la visita sanitaria</h2>


	<table class="table table-striped">
		<tr>
			<th>Anciano</th>
			<td><b><c:out value="${visitaSanitaria.anciano.nombre} ${visitaSanitaria.anciano.apellidos }" /></b></td>
		</tr>
		<tr>
			<th>Motivo</th>
			<td><c:out value="${visitaSanitaria.motivo}" /></td>
		</tr>
		<tr>
			<th>Descripcion</th>
			<td><c:out value="${visitaSanitaria.descripcion}" /></td>
		</tr>
		<tr>
			<th>Sanitario</th>
			<td><c:out value="${visitaSanitaria.sanitario}" /></td>
		</tr>
		<tr>
			<th>Fecha</th>
			<td><fmt:formatDate value="${visitaSanitaria.fecha}"
					pattern="yyyy/MM/dd" /></td>
		</tr>
		<tr>
			<th>Hora Inicio</th>
			<td><c:out value="${visitaSanitaria.horaInicio}" /></td>
		</tr>
		<tr>
			<th>Hora Fin</th>
			<td><c:out value="${visitaSanitaria.horaFin}" /></td>
		</tr>
	</table>

	<spring:url value="{visitaSanitariaId}/delete" var="deleteUrl">
		<spring:param name="visitaSanitariaId" value="${visitaSanitaria.id}" />
	</spring:url>
	<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar
		visita sanitaria</a>

</resify:layout>
