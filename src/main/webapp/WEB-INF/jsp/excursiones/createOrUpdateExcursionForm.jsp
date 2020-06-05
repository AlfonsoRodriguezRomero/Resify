<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="organizadores">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaInicio").datepicker({dateFormat: 'yy/mm/dd'});
                $("#fechaFin").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${excursion['new']}">Nueva </c:if> Excursion
        </h2>
        <form:form modelAttribute="excursion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${excursion.id}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Titulo" name="titulo"/>
                <resify:inputField label="Descripcion" name="descripcion"/>
                <resify:inputField label="Fecha Inicio" name="fechaInicio"/>
                <resify:inputField label="Hora Inicio" name="horaInicio"/>
                <resify:inputField label="Fecha Fin" name="fechaFin"/>
                <resify:inputField label="Hora Fin" name="horaFin"/>
                <resify:inputField label="NumeroResidencias" name="numeroResidencias"/>
                <resify:inputField label="Ratio de aceptacion" name="ratioAceptacion"/>
                <label class="col-sm-2 control-label"><input type="checkbox" name="finalMode"/>  ¿Publicar Excursion?</label>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${excursion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Excursion</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Excursion</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <c:if test="${!excursion['new']}">
        </c:if>
    </jsp:body>
</resify:layout>
