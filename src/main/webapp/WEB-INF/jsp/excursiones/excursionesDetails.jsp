<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="excursiones">

	<h2>Informacion de la excursion</h2>


	<table class="table table-striped">
		<tr>
			<th>Titulo</th>
			<td><b><c:out value="${excursion.titulo}" /></b></td>
		</tr>
		<tr>
			<th>Descripcion</th>
			<td><c:out value="${excursion.descripcion}" /></td>
		</tr>
		<tr>
			<th>Fecha Inicio</th>
			<td><c:out value="${excursion.fechaInicio}" /></td>
		</tr>
		<tr>
			<th>Hora Inicio</th>
			<td><c:out value="${excursion.horaInicio}" /></td>
		</tr>
		<tr>
			<th>Fecha Fin</th>
			<td><c:out value="${excursion.fechaFin}" /></td>
		</tr>
		<tr>
			<th>Hora Fin</th>
			<td><c:out value="${excursion.horaFin}" /></td>
		</tr>
		<tr>
			<th>Ratio de aceptacion</th>
			<td><c:out value="${excursion.ratioAceptacion}" /></td>
		</tr>
		<tr>
			<th>Numero maximo de residencias</th>
			<td><c:out value="${excursion.numeroResidencias}" /></td>
		</tr>
	</table>

	<security:authorize access="hasAuthority('organizador')">
		<c:if test="${!excursion.finalMode}">
			<spring:url value="{excursionId}/edit" var="editUrl">
				<spring:param name="excursionId" value="${excursion.id}" />
			</spring:url>
			<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar
				excursion</a>

			<spring:url value="{excursionId}/delete" var="editUrl">
				<spring:param name="excursionId" value="${excursion.id}" />
			</spring:url>
			<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Eliminar
				excursion</a>
		</c:if>
		<c:if test="${excursion.finalMode}">
			Ya has publicado esta excursion
			</c:if>
	</security:authorize>


	<security:authorize access="hasAuthority('manager')">
		<c:if test="${hasResidencia }">
			<c:if test="${!hasPeticion }">
				<spring:url
					value="/excursiones/{excursionId}/peticiones-excursion/new"
					var="editUrl">
					<spring:param name="excursionId" value="${excursion.id}" />
				</spring:url>
				<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Inscribirse</a>
			</c:if>

			<c:if test="${hasPeticion}">
				<td><c:out value="YA LE HA ENVIADO UNA PETICION" /></td>
			</c:if>
		</c:if>

		<c:if test="${!hasResidencia}">
			<td><c:out value="NO TIENE UNA RESIDENCIA QUE INSCRIBIR" /></td>
		</c:if>
	</security:authorize>

	<security:authorize access="hasAuthority('manager')">
		<c:if test="${hasResidencia }">
			<c:if test="${!hasFeedback}">
				<c:if test="${asistida}">
					<spring:url value="/excursiones/{excursionId}/feedbacks/new"
						var="editUrl">
						<spring:param name="excursionId" value="${excursion.id}" />
					</spring:url>
					<br />
					<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Dar
						feedback</a>
				</c:if>
			</c:if>

			<c:if test="${hasFeedback}">
				<br />
				<td><c:out value="YA HA DADO FEEDBACK DE ESTA EXCURSIÓN" /></td>
			</c:if>
		</c:if>

	</security:authorize>

</resify:layout>
