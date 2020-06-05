package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Inscripcion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.ResidenciaValidator;
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
@RequestMapping("/residencias")
public class ResidenciaController {

	private static final String	VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM	= "residencias/createOrUpdateResidenciaForm";

	@Autowired
	private AuthoritiesService	authoritiesService;

	@Autowired
	private ResidenciaService	residenciaService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private AncianoService		ancianoService;

	@Autowired
	private InscripcionService	inscripcionService;

	@Autowired
	private OrganizadorService	organizadorService;

	private boolean				tienePendiente							= false;
	private boolean				tieneAceptada							= false;


	@InitBinder("manager")
	public void initManagerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("residencia")
	public void initResidenciaBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("manager");
		dataBinder.setValidator(new ResidenciaValidator());
	}

	@GetMapping()
	public String listResidencias(final Map<String, Object> model, final Principal p) {
		String auth = this.authoritiesService.findAuthority(p.getName());
		if (auth.equals("manager")) {
			Residencia residencia = this.residenciaService.findMine(this.managerService.findManagerByUsername(p.getName()));
			if (residencia == null) {
				return "redirect:residencias/new";
			}
			model.put("residencia", residencia);
			return "residencias/residenciasDetails";
		} else {
			if (auth.equals("organizador")) {
				model.put("puedeVerOrganizador", true);
			} else {
				Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
				Iterable<Inscripcion> inscripciones = this.inscripcionService.findAllMineAnciano(anciano);
				for (Inscripcion i : inscripciones) {
					if (i.getEstado().equals("aceptada")) {
						return "redirect:residencias/" + i.getResidencia().getId();
					}
				}
				model.put("puedeVerAnciano", true);
			}
			Iterable<Residencia> residencias = this.residenciaService.findAll();
			model.put("residencias", residencias);
			return "residencias/residenciasList";
		}

	}

	@GetMapping(value = "/top")
	public String listTopResidencias(final Map<String, Object> model, final Principal p) {
		Iterable<Residencia> residencias = this.residenciaService.findTop(5);
		model.put("residencias", residencias);
		return "residencias/residenciasList";
	}
	
	@GetMapping(value = "/ratio")
	public String listRatioResidencias(final Map<String, Object> model, final Principal p) {
		Iterable<Residencia> residencias = this.residenciaService.findAll();
		List<Residencia> resi = new ArrayList<>();
		List<Double> ratiosResi = new ArrayList<>();
		for (Residencia res : residencias) {
			resi.add(res);
		}
		for (int i=0; i<resi.size(); i++) {
			ratiosResi.add(this.residenciaService.getRatio(resi.get(i)));
		}		
		model.put("residencias", residencias);
		model.put("ratiosResi", ratiosResi);
		return "residencias/residenciasListRatio";
	}

	@GetMapping(value = "/no-participantes")
	public String listResidenciasNoParticipantes(final Map<String, Object> model, final Principal p) {
		Organizador o = this.organizadorService.findOrganizadorByUsername(p.getName());
		Iterable<Residencia> residencias = this.residenciaService.findResidenciasNoParticipantes(o);
		model.put("residencias", residencias);
		return "residencias/residenciasList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		boolean tieneResidencia = true;
		Residencia res = this.residenciaService.findMine(manager);
		if (res == null) {
			tieneResidencia = false;
		}
		
		if (tieneResidencia == true) {
			return "redirect:../residencias/" + res.getId();
		}

		Residencia residencia = new Residencia();
		residencia.setAceptaDependenciaGrave(false);
		residencia.setAforo(10);
		model.put("residencia", residencia);
		return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Residencia residencia, final BindingResult result, final Map<String, Object> model, final Principal p) {
		if (result.hasErrors()) {
			model.put("residencia", residencia);
			return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
		} else {
			Manager manager = this.managerService.findManagerByUsername(p.getName());
			residencia.setManager(manager);
			this.residenciaService.saveResidencia(residencia);
			model.put("message", "Se ha registrado la residencia correctamente");
			return "redirect:../residencias/" + residencia.getId();
		}
	}

	@GetMapping(value = "/{residenciaId}/edit")
	public String initUpdateResidenciaForm(@PathVariable("residenciaId") final int residenciaId, final Model model, final Principal p) {
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!residencia.getManager().equals(manager)) {
			return "exception";
		}
		model.addAttribute(residencia);
		return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{residenciaId}/edit")
	public String processUpdateResidenciaForm(@Valid final Residencia residencia, final BindingResult result, @PathVariable("residenciaId") final int residenciaId, final ModelMap model, final Principal p) {
		Residencia residenciaToUpdate = this.residenciaService.findResidenciaById(residenciaId);
		residencia.setManager(residenciaToUpdate.getManager());
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (!residencia.getManager().equals(manager)) {
			return "exception";
		}
		if (result.hasErrors()) {
			model.put("residencia", residencia);
			return ResidenciaController.VIEWS_RESIDENCIA_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(residencia, residenciaToUpdate, "id", "manager");
			this.residenciaService.saveResidencia(residenciaToUpdate);
			return "redirect:../../";
		}
	}

	@GetMapping("/{residenciaId}")
	public ModelAndView showResidencia(@PathVariable("residenciaId") final int residenciaId, final Principal p) {
		Residencia residencia = this.residenciaService.findResidenciaById(residenciaId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		ModelAndView mav = new ModelAndView("residencias/residenciasDetails");
		if (manager != null && !residencia.getManager().equals(manager)) {
			mav = new ModelAndView("exception");
		}
		if (anciano != null) {
			Iterable<Inscripcion> ins = this.inscripcionService.findAllMineAnciano(anciano);

			for (Inscripcion i : ins) {
				if (i.getEstado().equals("pendiente") && i.getResidencia().equals(residencia)) {
					this.tienePendiente = true;
					break;
				} else {
					this.tienePendiente = false;

				}
				if (i.getEstado().equals("aceptada")) {
					this.tieneAceptada = true;
					break;
				} else {
					this.tieneAceptada = false;
				}
			}
			mav.addObject("tienePendiente", this.tienePendiente);
			mav.addObject("tieneAceptada", this.tieneAceptada);

		}
		mav.addObject(this.residenciaService.findResidenciaById(residenciaId));
		return mav;
	}

}
