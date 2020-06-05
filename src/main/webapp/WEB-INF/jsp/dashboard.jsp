<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="resify" tagdir="/WEB-INF/tags" %>

<resify:layout pageName="dashboard">

    <h2>Información general de Resify</h2>


    <table class="table table-striped">
        <tr>
            <th>Managers</th>
            <td><c:out value="${dashboard.managers}"/></td>
        </tr>
        <tr>
            <th>Organizadores</th>
            <td><c:out value="${dashboard.organizadores}"/></td>
        </tr>
        <tr>
            <th>Ancianos</th>
            <td><c:out value="${dashboard.ancianos}"/></td>
        </tr>
        <tr>
            <th>Residencias</th>
            <td><c:out value="${dashboard.residencias}"/></td>
        </tr>
        <tr>
            <th>Residencias completas</th>
            <td><c:out value="${dashboard.residenciasCompletas}"/></td>
        </tr>
        <tr>
            <th>Media de ancianos por residencia</th>
            <td><c:out value="${dashboard.mediaAncianosPorResidencia}"/></td>
        </tr>
        <tr>
            <th>Actividades</th>
            <td><c:out value="${dashboard.actividades}"/></td>
        </tr>
        <tr>
            <th>Media de actividades por residencia</th>
            <td><c:out value="${dashboard.mediaActividadesPorResidencia}"/></td>
        </tr>
        <tr>
            <th>Buenas acciones</th>
            <td><c:out value="${dashboard.buenasAcciones}"/></td>
        </tr>
        <tr>
            <th>Incidencias</th>
            <td><c:out value="${dashboard.incidencias}"/></td>
        </tr>
        <tr>
            <th>Quejas</th>
            <td><c:out value="${dashboard.quejas}"/></td>
        </tr>
        <tr>
            <th>Media de quejas por residencia</th>
            <td><c:out value="${dashboard.mediaQuejasPorResidencia}"/></td>
        </tr>
        <tr>
            <th>Media de quejas por anciano</th>
            <td><c:out value="${dashboard.mediaQuejasPorAnciano}"/></td>
        </tr>
        <tr>
            <th>Inscripciones</th>
            <td><c:out value="${dashboard.inscripciones}"/></td>
        </tr>
        <tr>
            <th>Inscripciones aceptadas</th>
            <td><c:out value="${dashboard.inscripcionesAceptadas}"/></td>
        </tr>
        <tr>
            <th>Ratio de inscripciones aceptadas</th>
            <td><c:out value="${dashboard.ratioInscripcionesAceptadas}"/></td>
        </tr>
        <tr>
            <th>Inscripciones rechazadas</th>
            <td><c:out value="${dashboard.inscripcionesRechazadas}"/></td>
        </tr>
        <tr>
            <th>Ratio de inscripciones rechazadas</th>
            <td><c:out value="${dashboard.ratioInscripcionesRechazadas}"/></td>
        </tr>
        <tr>
            <th>Visitas sanitarias</th>
            <td><c:out value="${dashboard.visitasSanitarias}"/></td>
        </tr>
        <tr>
            <th>Media de visitas por residencia</th>
            <td><c:out value="${dashboard.mediaVisitasPorResidencia}"/></td>
        </tr>
        <tr>
            <th>Excursiones</th>
            <td><c:out value="${dashboard.excursiones}"/></td>
        </tr>
        <tr>
            <th>Media de excursiones por organizador</th>
            <td><c:out value="${dashboard.mediaExcursionesPorOrganizador}"/></td>
        </tr>
        <tr>
            <th>Peticiones de excursión</th>
            <td><c:out value="${dashboard.peticionesExcursion}"/></td>
        </tr>
        <tr>
            <th>Media de peticiones por excursión</th>
            <td><c:out value="${dashboard.mediaPeticionesPorExcursion}"/></td>
        </tr>
        <tr>
            <th>Peticiones de excursión aceptadas</th>
            <td><c:out value="${dashboard.peticionesAceptadas}"/></td>
        </tr>
        <tr>
            <th>Ratio de peticiones de excursión aceptadas</th>
            <td><c:out value="${dashboard.ratioPeticionesAceptadas}"/></td>
        </tr>
        <tr>
            <th>Peticiones de excursión rechazadas</th>
            <td><c:out value="${dashboard.peticionesRechazadas}"/></td>
        </tr>
        <tr>
            <th>Ratio de peticiones de excursión rechazadas</th>
            <td><c:out value="${dashboard.ratioPeticionesRechazadas}"/></td>
        </tr>
        <tr>
            <th>Feedbacks</th>
            <td><c:out value="${dashboard.feedbacks}"/></td>
        </tr>
        <tr>
            <th>Media de feedbacks por excursión</th>
            <td><c:out value="${dashboard.mediaFeedbacksPorExcursion}"/></td>
        </tr>
    </table>
    
   	<a href="/" class="btn btn-default">Volver</a>

</resify:layout>
