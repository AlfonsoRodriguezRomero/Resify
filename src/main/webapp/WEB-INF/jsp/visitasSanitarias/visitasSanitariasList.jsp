<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="excursiones">
	<h2>Visitas Sanitarias</h2>

	<table id="excursionesTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Motivo</th>
				<th style="width: 200px;">Fecha</th>
				<th style="width: 120px">Hora inicio</th>
				<th style="width: 120px">Hora fin
				<th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${visitasSanitarias}" var="visitaSanitaria">
				<tr>
					<td><spring:url value="/visitas-sanitarias/{visitaSanitariaId}"
							var="visitaSanitariaUrl">
							<spring:param name="visitaSanitariaId" value="${visitaSanitaria.id}" />
						</spring:url>
					<a href="${fn:escapeXml(visitaSanitariaUrl)}">
					<c:out value="${visitaSanitaria.motivo}" /></a></td>
					<td> <fmt:formatDate value="${visitaSanitaria.fecha}" pattern="yyyy/MM/dd"/></td>
					<td><c:out value="${visitaSanitaria.horaInicio}" /></td>
					<td><c:out value="${visitaSanitaria.horaFin}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

		<spring:url value="visitas-sanitarias/new" var="addUrl">
		</spring:url>
		<a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Añadir
			nueva visita</a>

</resify:layout>
