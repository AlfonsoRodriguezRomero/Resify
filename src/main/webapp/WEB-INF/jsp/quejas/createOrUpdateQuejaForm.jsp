<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="quejas">
    <jsp:body>
        <h2>
            Nueva Queja
        </h2>
        <c:if test="${!hasMaxQuejas && hasResidencia}">
        	<form:form modelAttribute="queja" class="form-horizontal">
        	    <div class="form-group has-feedback">
        	        <resify:inputField label="Titulo" name="titulo"/>
        	        <resify:inputField label="Descripcion" name="descripcion"/>
        	        <label class="col-sm-2 control-label"><input type="checkbox" name="anonimo"/>  ¿Desea que sea anónima?</label>
        	    </div>
       	     	<div class="form-group">
        	        <div class="col-sm-offset-2 col-sm-10">
        	      		<button class="btn btn-default" type="submit">Enviar Queja</button>
        	        </div>
            	</div>
        	</form:form>
        </c:if>
        <c:if test="${hasMaxQuejas && hasResidencia}">
   			<td><c:out value="YA LE HA ENVIADO TRES QUEJAS"/></td>
    	</c:if>
    
    	<c:if test="${!hasResidencia}">
    		<td><c:out value="NO ESTÁ INSCRITO EN NINGUNA RESIDENCIA"/></td>
    	</c:if>
        
    </jsp:body>
</resify:layout>
