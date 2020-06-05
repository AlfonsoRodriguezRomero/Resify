<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<resify:layout pageName="peticionesExcursion">
	<jsp:body>
    	<c:choose>
    		<c:when test="${aforoCompleto == true}">
    			<p>Lo sentimos, el aforo está completo</p>
    		</c:when>
    		    		
    		<c:otherwise>
    		<c:choose>
    		<c:when test="${noCumpleRatio == true}">
    			<p>Lo sentimos, no cumple con el ratio de aceptación</p>
    		</c:when>
    		
    		<c:otherwise>
    		
        <h2>
            Petición de Excursion
        </h2>
        <security:authorize access="hasAuthority('manager')">
        <c:if test="${!hasPeticion && hasResidencia}">
        <form:form modelAttribute="peticionExcursion"
									class="form-horizontal">
			<input type="hidden" name="estado"
										value="${peticionExcursion.estado}" />
           
            <div class="form-group has-feedback">
                <resify:inputField label="Declaracion"
											name="declaracion" />
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Enviar Petición</button>
                </div>
            </div>
        </form:form>
        </c:if>
    <c:if test="${hasPeticion && hasResidencia}">
    <p>ya le ha enviado una petición</p>
    </c:if>
    
    <c:if test="${!hasResidencia}">
    <p>no tiene una residencia que inscribir</p>
    </c:if>
    </security:authorize>
        
    <security:authorize access="hasAuthority('organizador')">
     <form:form modelAttribute="peticionExcursion"
								class="form-horizontal">
        <input type="hidden" name="id" value="${peticionExcursion.id}" />
     
        <input type="hidden" name="fecha"
									value="${peticionExcursion.fecha}" />
        <input type="hidden" name="declaracion"
									value="${peticionExcursion.declaracion}" />

		<div class="form-group row">
			<label class="col-sm-2 control-label">Declaracion</label>
			<div class="col-sm-10">
     			<input type="text" readonly
											style="border: 0px; background-color: #f1f1f1;"
											class="form-control-plaintext"
											value="${peticionExcursion.declaracion }">
    		</div>
								</div>
			
			<div class="form-group row">
			<label class="col-sm-2 control-label">Numero ancianos</label>
			<div class="col-sm-10">
     			<input type="text" readonly
											style="border: 0px; background-color: #f1f1f1;"
											class="form-control-plaintext" value="${nancianos}">
    		</div>
    		</div>
			
			<div class="form-group row">
			<label class="col-sm-2 control-label">Ratio acciones</label>
			<div class="col-sm-10">
     			<input type="text" readonly
											style="border: 0px; background-color: #f1f1f1;"
											class="form-control-plaintext" value="${ratio}">
    		</div>
		</div>
		
    	<div class="form-group has-feedback">
			<resify:selectField label="Estado" name="estado" size="3"
										names="${estados}" />
		</div>
		<div class="form-group has-feedback">
			<resify:inputField label="Justificación" name="justificacion" />
		</div>
		<button class="btn btn-default" type="submit">Actualizar Petición</button>
		
		
	</form:form>
    </security:authorize>
    
    </c:otherwise>
    	</c:choose>
    </c:otherwise>
    	</c:choose>
    </jsp:body>
</resify:layout>