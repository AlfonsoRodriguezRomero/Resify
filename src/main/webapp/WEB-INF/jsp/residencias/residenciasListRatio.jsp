<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<resify:layout pageName="residencias">
	<h2>Ratio de las residencias</h2>

	<table id="residenciasTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Nombre</th>
				<th style="width: 200px;">Ratio Buenas acciones/Incidencias</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${residencias}" var="residencia" varStatus="status">
				<tr>
					<td><spring:url value="/residencias/{ratio}" var="residenciaUrl">
							<spring:param name="residenciaId" value="${residencia.id}" />
						</spring:url> 
						<a href="${fn:escapeXml(residenciaUrl)}"> <c:out value="${residencia.nombre}" /></a>
						<td><c:out value="${ratiosResi[status.index]}" /></td>
				</tr>
			</c:forEach>			
		</tbody>
	</table>
</resify:layout>
