<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="inscripciones">
    <jsp:body>
    	<c:choose>
    		<c:when test="${aforoMaximo == true}">
    			<p>Lo sentimos, la residencia se encuentra sin plazas disponibles</p>
    		</c:when>
    		
    		<c:when test="${tieneAceptada == true}">
    			<p>Usted ya se encuentra registrado en una residencia</p>
    		</c:when>
    		
    		<c:when test="${tienePendiente == true}">
    			<p>Aún tiene una inscripción pendiente en esta residencia, por favor, espere una respuesta</p>
    		</c:when>
    		
    		<c:otherwise>
  
    		
    		<h2>
            <c:if test="${inscripcion['new']}">Nueva </c:if> Inscripcion
        </h2>
        <form:form modelAttribute="inscripcion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${inscripcion.id}"/>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<input type="hidden" name="estado" value="${inscripcion.estado}"/>
				</c:when>
				<c:otherwise>
					<div class="form-group has-feedback">
						<resify:selectField label="Estado" name="estado" size="3" names="${estados}"/>
					</div>
				</c:otherwise>
            </c:choose>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<input type="hidden" name="justificacion" value="${inscripcion.justificacion}"/>
				</c:when>
				<c:otherwise>
					<div class="form-group has-feedback">
						<resify:inputField label="Justificacion" name="justificacion"/>
					</div>
				</c:otherwise>
            </c:choose>
            <c:choose>
				<c:when test="${inscripcion['new']}">
					<div class="form-group has-feedback">
                		<resify:inputField label="Declaracion" name="declaracion"/>
            		</div>
				</c:when>
				<c:otherwise>
					<input type="hidden" name="declaracion" value="${inscripcion.declaracion}"/>
				</c:otherwise>
            </c:choose>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${inscripcion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Inscripcion</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Inscripcion</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!residencia['new']}">
        </c:if>
        
        
    		</c:otherwise>
    	</c:choose>

        
    </jsp:body>
</resify:layout>
