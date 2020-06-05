<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Inicio</span>
				</petclinic:menuItem>

				<sec:authorize access="hasAuthority('anciano')">
					<petclinic:menuItem active="${name eq 'residencias'}"
						url="/residencias" title="find residencias">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Residencias</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'inscripciones'}"
						url="/inscripciones" title="inscripciones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Inscripciones</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'excursiones'}"
						url="/excursiones" title="find excursiones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Excursiones</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'actividades'}"
						url="/actividades" title="find actividades">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Actividades</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'quejas'}"
						url="/quejas/new" title="quejas">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Quejas</span>
					</petclinic:menuItem>
				</sec:authorize>
				
				
				<!-- 				------------------------------------------------------------ -->

				<sec:authorize access="hasAuthority('organizador')">
					<petclinic:menuItem active="${name eq 'excursiones'}"
						url="/excursiones" title="find excursiones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Excursiones</span>
					</petclinic:menuItem>

					<petclinic:menuItem active="${name eq 'peticionesExcursion'}"
						url="/peticiones-excursion" title="find peticiones excursion">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Peticiones de excursion</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'residencias'}"
						url="/residencias" title="find residencias">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Residencias</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'feedbacks'}"
						url="/feedbacks" title="find feedbacks">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Feedback de excursiones</span>
					</petclinic:menuItem>
				</sec:authorize>

				<!-- 				------------------------------------------------------------ -->

				<sec:authorize access="hasAuthority('manager')">
					<petclinic:menuItem active="${name eq 'inscripciones'}"
						url="/inscripciones" title="inscripciones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Inscripciones</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'excursiones'}"
						url="/excursiones" title="find excursiones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Excursiones</span>
					</petclinic:menuItem>

					<petclinic:menuItem active="${name eq 'peticionesExcursion'}"
						url="/peticiones-excursion" title="find peticiones excursion">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Peticiones excursion</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'feedbacks'}"
						url="/excursiones/feedback" title="write feedbacks">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Feedback para organizadores</span>
					</petclinic:menuItem>

					<petclinic:menuItem active="${name eq 'residencia'}"
						url="/residencias/new" title="residencia">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Residencia</span>
					</petclinic:menuItem>

					<petclinic:menuItem active="${name eq 'actividades'}"
						url="/actividades" title="find actividades">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Actividades</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'buenasAcciones'}"
						url="/buenas-acciones" title="buenas acciones">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Buenas acciones</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'incidencias'}"
						url="/incidencias" title="incidencias">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Incidencias</span>
					</petclinic:menuItem>
					
					<petclinic:menuItem active="${name eq 'quejas'}"
						url="/quejas" title="quejas">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Quejas</span>
            		</petclinic:menuItem>
            
					<petclinic:menuItem active="${name eq 'visitasSanitarias'}"
						url="/visitas-sanitarias" title="visitas sanitarias">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Visitas sanitarias</span>
					</petclinic:menuItem>
          
				</sec:authorize>
				
				<!-- 				------------------------------------------------------------ -->

 				<sec:authorize access="hasAuthority('admin')"> --%>
<%-- 					<petclinic:menuItem active="${name eq 'ancianos'}" --%>
<%-- 						url="/ancianos" title="find ancianos"> --%>
<!-- 						<span class="glyphicon glyphicon-search" aria-hidden="true"></span> -->
<!-- 						<span>Ancianos</span> -->
<%-- 					</petclinic:menuItem> --%>

<%-- 					<petclinic:menuItem active="${name eq 'managers'}" --%>
<%-- 						url="/managers" title="find managers"> --%>
<!-- 						<span class="glyphicon glyphicon-search" aria-hidden="true"></span> -->
<!-- 						<span>Managers</span> -->
<%-- 					</petclinic:menuItem> --%>
					
<%-- 					<petclinic:menuItem active="${name eq 'organizadores'}" --%>
<%-- 						url="/organizadores" title="find organizadores"> --%>
<!-- 						<span class="glyphicon glyphicon-search" aria-hidden="true"></span> -->
<!-- 						<span>Organizadores</span> -->
<%-- 					</petclinic:menuItem> --%>
					
					<petclinic:menuItem active="${name eq 'dashboard'}"
						url="/dashboard" title="find dashboard">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						<span>Dashboard</span>
					</petclinic:menuItem>
					
				</sec:authorize>
			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/usuarios/new" />">Registrate</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span> 
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
							<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
