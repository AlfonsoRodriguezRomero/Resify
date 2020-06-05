<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="managers">
    <jsp:body>
        <h2>
            <c:if test="${residencia['new']}">Nueva </c:if> Residencia
        </h2>
        <h2>
            <c:out value="${manager}"/>
        </h2>
        <form:form modelAttribute="residencia"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${residencia.id}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Nombre" name="nombre"/>
                <resify:inputField label="Direccion" name="direccion"/>
                <resify:inputField label="Descripcion" name="descripcion"/>
                <resify:inputField label="Aforo" name="aforo"/>
                <resify:inputField label="Mas Info" name="masInfo"/>
                <resify:inputField label="Telefono" name="telefono"/>
                <resify:inputField label="Correo" name="correo"/>
                <resify:inputField label="Hora Apertura" name="horaApertura"/>
                <resify:inputField label="Hora Cierre" name="horaCierre"/>
                <resify:inputField label="Edad Maxima" name="edadMaxima"/>
                <label class="col-sm-2 control-label">¿Acepta personas con dependecia grave?</label><input type="checkbox" name="aceptaDependenciaGrave"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${residencia['new']}">
                            <button class="btn btn-default" type="submit">Registrar Residencia</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Residencia</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!residencia['new']}">
        </c:if>
    </jsp:body>
</resify:layout>
