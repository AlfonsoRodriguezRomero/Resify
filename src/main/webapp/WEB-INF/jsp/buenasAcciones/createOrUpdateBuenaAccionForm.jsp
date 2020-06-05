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
            <c:if test="${buenaAccion['new']}">Nueva </c:if> Buena Accion
        </h2>
        <form:form modelAttribute="buenaAccion"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${buenaAccion.id}"/>
            <div class="form-group has-feedback">
                <resify:inputField label="Título" name="titulo"/>
                <resify:inputField label="Descripcion" name="descripcion"/>
                <resify:inputField label="Fecha" name="fecha"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${buenaAccion['new']}">
                            <button class="btn btn-default" type="submit">Registrar Buena accion</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Actualizar Buena accion</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</resify:layout>
