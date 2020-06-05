package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.PeticionExcursion;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.PeticionExcursionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PeticionExcursionController {

	private static final String VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM = "peticionesExcursion/createOrUpdatePeticionExcursionForm";

	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ExcursionService excursionService;

	@Autowired
	private OrganizadorService organizadorService;

	@Autowired
	private ResidenciaService residenciaService;

	@Autowired
	private AncianoService ancianoService;

	@ModelAttribute("estados")
	public Collection<String> getEstados() {
		Collection<String> estados = new ArrayList<String>();
		estados.add("pendiente");
		estados.add("aceptada");
		estados.add("rechazada");
		return estados;
	}

	@InitBinder("peticionExcursion")
	public void initInscripcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("excursion");
		dataBinder.setDisallowedFields("fecha");
		dataBinder.setValidator(new PeticionExcursionValidator());
	}

	@GetMapping(value = "/peticiones-excursion")
	public String listPeticionesExcursion(final Map<String, Object> model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		Iterable<PeticionExcursion> peticionesExcursion;
		if (manager != null) {
			Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
			if (residencia == null) {
				return "redirect:excursiones";
			}
			peticionesExcursion = this.peticionExcursionService.findAllMineResidencia(residencia);
		} else if (organizador != null) {
			peticionesExcursion = this.peticionExcursionService.findAllMineOrganizador(organizador);
		} else {
			return "exception";
		}
		model.put("peticionesExcursion", peticionesExcursion);
		return "peticionesExcursion/peticionExcursionList";
	}

	@GetMapping(value = "/excursiones/{excursionId}/peticiones-excursion/new")
	public String initCreationForm(@PathVariable("excursionId") final int excursionId, final ModelMap model,
			final Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Manager manager = this.managerService.findManagerByUserName(p.getName());
		Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
		Integer peticiones = this.peticionExcursionService.countPeticionesByExcursion(excursion, manager);
		PeticionExcursion peticionExcursion = new PeticionExcursion();

		peticionExcursion.setEstado("pendiente");
		peticionExcursion.setExcursion(excursion);
		Date fecha = new Date(System.currentTimeMillis() - 1);
		peticionExcursion.setFecha(fecha);
		peticionExcursion.setResidencia(residencia);

		if (!peticionExcursion.getExcursion().isFinalMode()
				|| peticionExcursion.getExcursion().getFechaInicio().before(peticionExcursion.getFecha())) {
			return "exception";
		}

		model.put("hasPeticion", peticiones != 0);
		model.put("hasResidencia", residencia != null);

		if (this.residenciaService.getRatio(residencia) < excursion.getRatioAceptacion()) {
			model.put("noCumpleRatio", true);
		}

		if (excursion.getNumeroResidencias() <= this.peticionExcursionService
				.countPeticionExcursionAceptadaByExcursion(excursion)) {
			model.put("aforoCompleto", true);
		}

		model.put("peticionExcursion", peticionExcursion);
		return PeticionExcursionController.VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/excursiones/{excursionId}/peticiones-excursion/new")
	public String processCreationForm(@PathVariable("excursionId") final int excursionId,
			@Valid final PeticionExcursion peticionExcursion, final BindingResult result, final ModelMap model,
			final Principal p) {
		Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
		Date fecha = new Date(System.currentTimeMillis() - 1);
		peticionExcursion.setFecha(fecha);
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		peticionExcursion.setExcursion(excursion);
		peticionExcursion.setResidencia(residencia);
		peticionExcursion.setEstado("pendiente");

		if (!peticionExcursion.getExcursion().isFinalMode()
				|| peticionExcursion.getExcursion().getFechaInicio().before(peticionExcursion.getFecha())
				|| peticionExcursion.getFecha().after(peticionExcursion.getExcursion().getFechaFin())
				|| this.residenciaService.getRatio(residencia) < excursion.getRatioAceptacion()
				|| residencia.getAforo() <= this.peticionExcursionService
						.countPeticionExcursionAceptadaByExcursion(excursion)) {
			return "exception";

		} else if (result.hasErrors()) {
			Manager manager = this.managerService.findManagerByUserName(p.getName());
			Integer peticiones = this.peticionExcursionService.countPeticionesByExcursion(excursion, manager);

			model.put("hasPeticion", peticiones != 0);
			model.put("hasResidencia", residencia != null);

			model.put("peticionExcursion", peticionExcursion);
			return PeticionExcursionController.VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;

		} else {
			this.peticionExcursionService.save(peticionExcursion);
			model.put("message", "Se ha enviado la peticion correctamente");
			return "redirect:/excursiones/{excursionId}";
		}
	}

	@GetMapping(value = "/peticiones-excursion/{peticionExcursionId}/edit")
	public String initUpdatePeticionExcursionForm(@PathVariable("peticionExcursionId") final int peticionExcursionId, final ModelMap model, final Principal p) {
		PeticionExcursion peticionExcursion = this.peticionExcursionService.findPeticionExcursionById(peticionExcursionId);
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		if (peticionExcursion.getEstado().equals("aceptada") || peticionExcursion.getEstado().equals("rechazada")) {
			return "exception";
		}
		if (!(peticionExcursion.getExcursion().getOrganizador() == organizador) || peticionExcursion.getExcursion().getFechaFin().before(peticionExcursion.getFecha())) {
			return "exception";
		}
		model.addAttribute("nancianos", this.ancianoService.countAncianosMiResidencia(peticionExcursion.getResidencia()));
		model.addAttribute("ratio", this.residenciaService.getRatio(peticionExcursion.getResidencia()));
		model.addAttribute("peticionExcursion", peticionExcursion);
		return PeticionExcursionController.VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/peticiones-excursion/{peticionExcursionId}/edit")
	public String processUpdatePeticionExcursionForm(@Valid final PeticionExcursion peticionExcursion,
			final BindingResult result, @PathVariable("peticionExcursionId") final int peticionExcursionId,
			final ModelMap model, final Principal p) {
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		PeticionExcursion peticionExcursionToUpdate = this.peticionExcursionService
				.findPeticionExcursionById(peticionExcursionId);

		peticionExcursion.setExcursion(peticionExcursionToUpdate.getExcursion());

		if (!peticionExcursion.getExcursion().getOrganizador().equals(organizador)
				|| peticionExcursion.getExcursion().getFechaFin().before(peticionExcursionToUpdate.getFecha())) {
			return "exception";
		}
		if (peticionExcursion.getEstado().equals("aceptada")) {
			if (this.residenciaService.getRatio(peticionExcursionToUpdate.getResidencia()) < peticionExcursionToUpdate
					.getExcursion().getRatioAceptacion()) {
				result.rejectValue("estado",
						"la excursion no acepta residencias con un ratio menor de "
								+ peticionExcursionToUpdate.getExcursion().getRatioAceptacion(),
						"la excursion no acepta residencias con un ratio menor de "
								+ peticionExcursionToUpdate.getExcursion().getRatioAceptacion());
			}
			if (this.peticionExcursionService.countPeticionExcursionAceptadaByExcursion(
					peticionExcursion.getExcursion()) >= peticionExcursion.getExcursion().getNumeroResidencias()) {
				result.rejectValue("estado",
						"la excursión no acepta más residencias, número máximo de residencias alcanzado",
						"la excursión no acepta más residencias, número máximo de residencias alcanzado");
			}
		}
		if (result.hasErrors()) {
			model.put("peticionExcursion", peticionExcursion);
			model.addAttribute("nancianos", this.ancianoService.countAncianosMiResidencia(peticionExcursionToUpdate.getResidencia()));
			model.addAttribute("ratio", this.residenciaService.getRatio(peticionExcursionToUpdate.getResidencia()));
			return PeticionExcursionController.VIEWS_PETICION_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(peticionExcursion, peticionExcursionToUpdate, "id", "residencia", "excursion",
					"fecha");

			this.peticionExcursionService.save(peticionExcursionToUpdate);
			return "redirect:/peticiones-excursion/";
		}
	}

}
