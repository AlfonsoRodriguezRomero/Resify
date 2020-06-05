<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<resify:layout pageName="managers">
	<jsp:body>
		<c:choose>
		<c:when test="${noDependencia}">
			<c:out value="${noAncianosConDependencia }" />
		</c:when>
		<c:otherwise>
        <h2>
            Nueva Visita Sanitaria
        </h2>
        <form:form modelAttribute="visitaSanitaria"
			class="form-horizontal">
            <input type="hidden" name="id" value="${visitaSanitaria.id}" />
            <div class="form-group has-feedback">
            	<resify:inputField label="Motivo" name="motivo" />
                <resify:inputField label="Descripcion"
					name="descripcion" />
                <resify:inputField label="Sanitario" name="sanitario" />
                <resify:inputField label="Hora Inicio" name="horaInicio" />
                <resify:inputField label="Hora Fin" name="horaFin" />
                <br>
					<div class="form-group has-feedback"><br>Seleccionar Anciano
					<form:select path="anciano.id">
						<jstl:forEach items="${ancianos}" var="anciano">
							<form:option value="${anciano.id }"
								label="${anciano.nombre} ${anciano.apellidos}"></form:option>
						</jstl:forEach>
					</form:select>
            		</div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                            <button class="btn btn-default"
						type="submit">Registrar Visita</button>
                </div>
            </div>
        </form:form>
        <c:if test="${!visitaSanitaria['new']}">
        </c:if>
        </c:otherwise>
        </c:choose>
    </jsp:body>
</resify:layout>
