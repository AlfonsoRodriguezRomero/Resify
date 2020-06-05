package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Excursion;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Organizador;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.FeedbackService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.ExcursionValidator;
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
@RequestMapping("/excursiones")
public class ExcursionController {

	private static final String VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM = "excursiones/createOrUpdateExcursionForm";

	@Autowired
	private ExcursionService excursionService;

	@Autowired
	private OrganizadorService organizadorService;

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private AncianoService ancianoService;

	@Autowired
	private AuthoritiesService authoritiesService;
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("organizador")
	public void initOrganizadorBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("excursion")
	public void initExcursionBinder(WebDataBinder dataBinder) {
		dataBinder.addValidators(new ExcursionValidator());
	}

	@GetMapping()
	public String listExcursiones(Map<String, Object> model, Principal p) {
		String authority = authoritiesService.findAuthority(p.getName());
		Iterable<Excursion> excursiones = new ArrayList<>();
		if (authority.equals("organizador")) {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			excursiones = excursionService.findAllMine(organizador);
		} else if (authority.equals("manager")) {
			excursiones = excursionService.findAllPublished();
		} else if (authority.equals("anciano")) {
			Anciano anciano = ancianoService.findAncianoByUsername(p.getName());
			excursiones = excursionService.findAllMineAnciano(anciano);
		}
		model.put("excursiones", excursiones);
		return "excursiones/excursionesList";
	}
	
	@GetMapping("/feedback")
	public String listExcursionesAsisitidas(Map<String, Object> model, Principal p) {
		String authority = authoritiesService.findAuthority(p.getName());
		Iterable<Excursion> excursiones = new ArrayList<>();
		if (authority.equals("manager")) {
			Manager manager = managerService.findManagerByUsername(p.getName());
			Residencia res = residenciaService.findMine(manager);
			if(res == null) {
				return "redirect:../residencias/new";
			}
			excursiones = excursionService.findAllGone(res);
		}
		model.put("excursiones", excursiones);
		return "excursiones/excursionesList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Map<String, Object> model, Principal p) {
		Excursion excursion = new Excursion();
		model.put("excursion", excursion);
		return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Excursion excursion, BindingResult result, Map<String, Object> model,
			Principal p) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			excursion.setOrganizador(organizador);
			excursionService.saveExcursion(excursion);
			model.put("message", "Se ha registrado la excursion correctamente");
			return "redirect:/excursiones";
		}
	}

	@GetMapping(value = "/{excursionId}/edit")
	public String initUpdateExcursionForm(@PathVariable("excursionId") int excursionId, Model model, Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		if (!excursion.getOrganizador().equals(organizador)) {
			return "exception";
		}
		model.addAttribute(excursion);
		return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{excursionId}/edit")
	public String processUpdateExcursionForm(@Valid Excursion excursion, BindingResult result,
			@PathVariable("excursionId") int excursionId, final ModelMap model, Principal p) {
		if (result.hasErrors()) {
			model.put("excursion", excursion);
			return VIEWS_EXCURSION_CREATE_OR_UPDATE_FORM;
		} else {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			Excursion excursionToUpdate = this.excursionService.findExcursionById(excursionId);
			if (!excursionToUpdate.getOrganizador().equals(organizador) || excursionToUpdate.isFinalMode()) {
				return "exception";
			}
			BeanUtils.copyProperties(excursion, excursionToUpdate, "id", "organizador");
			this.excursionService.saveExcursion(excursionToUpdate);
			return "redirect:/excursiones/{excursionId}";
		}
	}

	@GetMapping("/{excursionId}")
	public ModelAndView showExcursionOrganizador(@PathVariable("excursionId") int excursionId, Principal p) {
		String authority = authoritiesService.findAuthority(p.getName());
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		ModelAndView mav = new ModelAndView("excursiones/excursionesDetails");
		mav.addObject(excursion);

		if (authority.equals("organizador")) {
			Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
			if (!excursion.getOrganizador().equals(organizador)) {
				mav = new ModelAndView("exception");
			}
		}

		else if (authority.equals("manager")) {
			Manager manager = managerService.findManagerByUserName(p.getName());
			Integer peticiones = peticionExcursionService.countPeticionesByExcursion(excursion, manager);
			Integer feedbacks = feedbackService.countFeedbacksByExcursion(excursion, manager);
			Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
			Integer peticionesAceptadas = peticionExcursionService.countPeticionesByExcursionAceptada(excursion, manager);
			if (!excursion.isFinalMode()) {
				mav = new ModelAndView("exception");
				return mav;
			}
			mav.addObject("hasPeticion", peticiones != 0);
			mav.addObject("hasResidencia", residencia != null);
			mav.addObject("hasFeedback", feedbacks != 0);
			mav.addObject("asistida",  excursion.getFechaFin().before(Date.valueOf(LocalDate.now())) && peticionesAceptadas.equals(1));
		}
		
		else if (authority.equals("anciano")) {
			Anciano anciano = ancianoService.findAncianoByUsername(p.getName());
			Iterable<Excursion> excursiones = excursionService.findAllMineAnciano(anciano);
			boolean suya = false;
			for (Excursion e : excursiones) {
				if(excursion.getId() == e.getId()) {
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

	@GetMapping("/{excursionId}/delete")
	public String deleteExcursion(@PathVariable("excursionId") int excursionId, Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Organizador organizador = organizadorService.findOrganizadorByUsername(p.getName());
		if (!excursion.getOrganizador().equals(organizador) || excursion.isFinalMode()) {
			return "exception";
		}
		this.excursionService.deleteExcursion(excursion);
		return "redirect:/excursiones";
	}

}