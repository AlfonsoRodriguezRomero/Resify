<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="buenaAcciones">

    <h2>Informacion de la buena accion</h2>


    <table class="table table-striped">
        <tr>
            <th>Titulo</th>
            <td><b><c:out value="${buenaAccion.titulo}"/></b></td>
        </tr>
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${buenaAccion.descripcion}"/></td>
        </tr>
        <tr>
            <th>Fecha</th>
            <td><c:out value="${buenaAccion.fecha}"/></td>
        </tr>
    </table>
    
</resify:layout>
