<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="residencias">
	<h2>Residencias</h2>

	<table id="residenciasTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Nombre</th>
				<th style="width: 200px;">Hora apertura</th>
				<th style="width: 200px;">Hora cierre</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${residencias}" var="residencia">
				<tr>
					<td><spring:url value="/residencias/{residenciaId}"
							var="residenciaUrl">
							<spring:param name="residenciaId" value="${residencia.id}" />
						</spring:url> <a href="${fn:escapeXml(residenciaUrl)}"><c:out
								value="${residencia.nombre}" /></a></td>
					<td><c:out value="${residencia.horaApertura}" /></td>
					<td><c:out value="${residencia.horaCierre}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<c:if test="${puedeVerAnciano }">
		<spring:url value="/residencias/top" var="topUrl">
		</spring:url>
		<a href="${fn:escapeXml(topUrl)}" class="btn btn-default">Ver Top
			5 Residencias</a>
	</c:if>

	<c:if test="${puedeVerOrganizador }">
		<spring:url value="/residencias/no-participantes"
			var="noParticipantesUrl">
		</spring:url>
		<a href="${fn:escapeXml(noParticipantesUrl)}" class="btn btn-default">Ver
			residencias que no han participado</a>
	</c:if>
		<c:if test="${puedeVerOrganizador }">
		<spring:url value="/residencias/ratio"
			var="ratioUrl">
		</spring:url>
		<a href="${fn:escapeXml(ratioUrl)}" class="btn btn-default">Ver
			ratio de las residencias</a>
	</c:if>



</resify:layout>
