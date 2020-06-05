<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<resify:layout pageName="feedbacks">
    <jsp:body>    			
        <h2>
            Feedback
        </h2>
        <security:authorize access="hasAuthority('manager')">
        <c:if test="${!hasPeticion && hasResidencia && !hasFeedback}">
        <form:form modelAttribute="feedback"
                   class="form-horizontal">
			<input type="hidden" name="descartaFeedback" value="${feedback.descartaFeedback}"/>
           
            <div class="form-group has-feedback">
                <resify:inputField label="Descripcion" name="descripcion"/>
            </div>
             <div class="form-group has-feedback">
                <resify:inputField label="Valoracion" name="valoracion"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Enviar Feedback</button>
                </div>
            </div>
        </form:form>
        </c:if>
    <c:if test="${hasFeedback && hasResidencia}">
    <td><c:out value="YA LE HA ENVIADO UN FEEDBACK"/></td>
    </c:if>
    
    <c:if test="${!hasResidencia}">
    <td><c:out value="PRIMERO DEBERÁ TENER UNA RESIDENCIA"/></td>
    </c:if>
    </security:authorize>
        
    <security:authorize access="hasAuthority('organizador')">
     <form:form modelAttribute="feedback" class="form-horizontal">
        <input type="hidden" name="id" value="${feedback.id}"/>
     
            <input type="hidden" name="descripcion" value="${feedback.descripcion}"/>
            <input type="hidden" name="valoracion" value="${feedback.valoracion}"/>
            
            <label class="col-sm-2 control-label">¿Desea rechazar este feedback?</label><input type="checkbox" name="descartaFeedback"/> 
			<br/>
			<div class="col-sm-offset-2 col-sm-10">	
			<button class="btn btn-default" type="submit">Actualizar Feedback</button>
            </div>
            
	</form:form>
    </security:authorize>
    </jsp:body>
</resify:layout>