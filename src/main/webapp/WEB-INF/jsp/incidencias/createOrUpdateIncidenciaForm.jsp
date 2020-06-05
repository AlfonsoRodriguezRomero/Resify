<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="managers">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#fecha").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${incidencia['new']}">Nueva </c:if> Incidencia
        </h2>
        <form:form modelAttribute="incidencia"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${incidencia.id}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Título" name="titulo"/>
                <resify:inputField label="Descripcion" name="descripcion"/>
                <resify:inputField label="Fecha" name="fecha"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${incidencia['new']}">
                            <button class="btn btn-default" type="submit">Registrar Incidencia</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Incidencia</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</resify:layout>
