package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Actividad;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.ActividadValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/actividades")
public class ActividadController {

	private static final String	VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM	= "actividades/createOrUpdateActividadForm";

	@Autowired
	private ActividadService	actividadService;

	@Autowired
	private AuthoritiesService	authoritiesService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private AncianoService		ancianoService;

	@Autowired
	private ResidenciaService	residenciaService;


	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("manager")
	public void initManagerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("actividad")
	public void initActividadBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new ActividadValidator());
	}

	@GetMapping()
	public String listActividades(final Map<String, Object> model, final Principal p) {
		String authority = this.authoritiesService.findAuthority(p.getName());
		Iterable<Actividad> actividades = new ArrayList<>();
		if (authority.equals("manager")) {
			Manager manager = this.managerService.findManagerByUsername(p.getName());
			actividades = this.actividadService.findAllMine(manager);
			if (this.residenciaService.findMine(manager) == null) {
				model.put("noTieneResi", true);
			} else {
				model.put("noTieneResi", false);
			}
		} else if (authority.equals("anciano")) {
			Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
			actividades = this.actividadService.findAllMineAnciano(anciano);
		}
		model.put("actividades", actividades);
		return "actividades/actividadesList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		Residencia resi = this.managerService.findResidenciaByManagerUsername(p.getName());
		if (resi == null) {
			return "exception";
		}
		Actividad actividad = new Actividad();
		model.put("actividad", actividad);
		return ActividadController.VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Actividad actividad, final BindingResult result, final Map<String, Object> model, final Principal p) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return ActividadController.VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
		} else {
			Residencia residencia = this.managerService.findResidenciaByManagerUsername(p.getName());
			actividad.setResidencia(residencia);
			this.actividadService.saveActividad(actividad);
			model.put("message", "Se ha registrado la actividad correctamente");
			return "redirect:/actividades";
		}
	}

	@GetMapping(value = "/{actividadId}/edit")
	public String initUpdateActividadForm(@PathVariable("actividadId") final int actividadId, final Model model, final Principal p) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!actividad.getResidencia().getManager().equals(manager)) {
			return "exception";
		}
		model.addAttribute(actividad);
		return ActividadController.VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{actividadId}/edit")
	public String processUpdateActividadForm(@Valid final Actividad actividad, final BindingResult result, @PathVariable("actividadId") final int actividadId, final ModelMap model, final Principal p) {
		if (result.hasErrors()) {
			model.put("actividad", actividad);
			return ActividadController.VIEWS_ACTIVIDAD_CREATE_OR_UPDATE_FORM;
		} else {
			Manager manager = this.managerService.findManagerByUsername(p.getName());
			Actividad actividadToUpdate = this.actividadService.findActividadById(actividadId);
			if (!actividadToUpdate.getResidencia().getManager().equals(manager)) {
				return "exception";
			}
			BeanUtils.copyProperties(actividad, actividadToUpdate, "id", "residencia");
			this.actividadService.saveActividad(actividadToUpdate);
			return "redirect:/actividades/{actividadId}";
		}
	}

	@GetMapping("/{actividadId}")
	public ModelAndView showActividad(@PathVariable("actividadId") final int actividadId, final Principal p) {
		String authority = this.authoritiesService.findAuthority(p.getName());
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		ModelAndView mav = new ModelAndView("actividades/actividadesDetails");
		mav.addObject(actividad);
		if (authority.equals("manager")) {
			Manager manager = this.managerService.findManagerByUsername(p.getName());
			if (!actividad.getResidencia().getManager().equals(manager)) {
				mav = new ModelAndView("exception");
			}
		} else if (authority.equals("anciano")) {
			Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
			Iterable<Actividad> actividades = this.actividadService.findAllMineAnciano(anciano);
			boolean suya = false;
			for (Actividad a : actividades) {
				if (actividad.getId() == a.getId()) {
					suya = true;
					break;
				}
			}
			if (suya == false) {
				mav = new ModelAndView("exception");
			}
		}

		return mav;
	}

	@GetMapping("/{actividadId}/delete")
	public String deleteActividad(@PathVariable("actividadId") final int actividadId, final Principal p) {
		Actividad actividad = this.actividadService.findActividadById(actividadId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!actividad.getResidencia().getManager().equals(manager)) {
			return "exception";
		}
		this.actividadService.deleteActividad(actividad);
		return "redirect:/actividades";
	}

}
