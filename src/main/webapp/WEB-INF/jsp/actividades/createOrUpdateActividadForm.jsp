<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="managers">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fechaInicio").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${actividad['new']}">Nueva </c:if> Actividad
        </h2>
        <form:form modelAttribute="actividad"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${actividad.id}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Titulo" name="titulo"/>
                <resify:inputField label="Descripcion" name="descripcion"/>
                <resify:inputField label="Fecha Inicio" name="fechaInicio"/>
                <resify:inputField label="Hora Inicio" name="horaInicio"/>
                <resify:inputField label="Hora Fin" name="horaFin"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${actividad['new']}">
                            <button class="btn btn-default" type="submit">Registrar Actividad</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Actividad</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</resify:layout>
