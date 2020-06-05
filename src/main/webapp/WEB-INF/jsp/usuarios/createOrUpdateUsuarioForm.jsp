<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="usuarios">
	<c:if test="${not empty anciano }">
		<h2>Registarse como anciano</h2>
		<form:form modelAttribute="anciano" class="form-horizontal"
			id="add-anciano-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Edad" name="edad" />
				<petclinic:inputField label="Carta de Presentacion"
					name="cartaPresentacion" />
				<petclinic:inputField label="Username" name="user.username" />
				<petclinic:inputField label="Password" name="user.password" />
				<label class="col-sm-2 control-label"><input type="checkbox"
					name="tieneDependenciaGrave" /> ¿Tiene dependencia grave?</label>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit">Registrar
						Anciano</button>
				</div>
			</div>
		</form:form>
	</c:if>

	<c:if test="${not empty organizador }">
		<h2>Registrarse como organizador</h2>
		<form:form modelAttribute="organizador" class="form-horizontal"
			id="add-organizador-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Companya" name="companya" />
				<petclinic:inputField label="Sector" name="sector" />
				<petclinic:inputField label="Username" name="user.username" />
				<petclinic:inputField label="Password" name="user.password" />
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit">Registrar
						Organizador</button>
				</div>
			</div>
		</form:form>
	</c:if>

	<c:if test="${not empty manager }">
		<h2>Registarse como manager</h2>
		<form:form modelAttribute="manager" class="form-horizontal"
			id="add-manager-form">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Nombre" name="nombre" />
				<petclinic:inputField label="Apellidos" name="apellidos" />
				<petclinic:inputField label="Firma" name="firma" />
				<petclinic:inputField label="Declaracion de Responsabilidad"
					name="declaracionResponsabilidad" />
				<petclinic:inputField label="Username" name="user.username" />
				<petclinic:inputField label="Password" name="user.password" />
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit">Registrar
						Manager</button>
				</div>
			</div>
		</form:form>
	</c:if>

</petclinic:layout>
