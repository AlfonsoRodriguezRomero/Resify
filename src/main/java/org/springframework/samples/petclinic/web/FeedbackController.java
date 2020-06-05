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
import org.springframework.samples.petclinic.model.Feedback;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.FeedbackService;
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
public class FeedbackController {

	private static final String VIEWS_FEEDBACK_CREATE_OR_UPDATE_FORM = "feedbacks/createOrUpdateFeedbackForm";

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ExcursionService excursionService;

	@Autowired
	private OrganizadorService organizadorService;
	
	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@ModelAttribute("estados")
	public Collection<String> getEstados() {
		Collection<String> estados = new ArrayList<String>();
		estados.add("pendiente");
		estados.add("aceptada");
		estados.add("rechazada");
		return estados;
	}

	@InitBinder("feedback")
	public void initFeedbackBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("excursion");
	}

	@GetMapping(value = "/feedbacks")
	public String listFeedbacksExcursion(final Map<String, Object> model, final Principal p) {
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		Iterable<Feedback> feedbacks;
		if (organizador != null) {
			feedbacks = this.feedbackService.findAllMineOrganizador(organizador);
		} else {
			return "exception";
		}
		model.put("feedbacks", feedbacks);
		return "feedbacks/feedbackList";
	}

	@GetMapping(value = "/excursiones/{excursionId}/feedbacks/new")
	public String initCreationForm(@PathVariable("excursionId") final int excursionId, final ModelMap model,
			final Principal p) {
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
		Integer feedbacks = this.feedbackService.countFeedbacksByExcursion(excursion, manager);
		Integer peticion = this.peticionExcursionService.countPeticionesByExcursionAceptada(excursion, manager);
		Feedback feedback = new Feedback();

		feedback.setExcursion(excursion);
		Date fecha = new Date(System.currentTimeMillis() - 1);
		feedback.setResidencia(residencia);

		//Si la excursión no está en finalMode o no se ha finalizado o si no se ha aceptado
		if (!(feedback.getExcursion().isFinalMode())
				|| feedback.getExcursion().getFechaFin().after(fecha) || peticion!=1) {
			return "exception";
		}

		model.put("hasFeedback", feedbacks != 0);
		model.put("hasResidencia", residencia != null);
		
		model.put("feedback", feedback);
		return VIEWS_FEEDBACK_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/excursiones/{excursionId}/feedbacks/new")
	public String processCreationForm(@PathVariable("excursionId") final int excursionId,
			@Valid final Feedback feedback, final BindingResult result, final ModelMap model,
			final Principal p) {
		Residencia residencia = managerService.findResidenciaByManagerUsername(p.getName());
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Date fecha = new Date(System.currentTimeMillis() - 1);
		Excursion excursion = this.excursionService.findExcursionById(excursionId);
		Integer peticion = this.peticionExcursionService.countPeticionesByExcursionAceptada(excursion, manager);
		feedback.setExcursion(excursion);
		feedback.setResidencia(residencia);

		if (!(feedback.getExcursion().isFinalMode())
				|| feedback.getExcursion().getFechaFin().after(fecha) || !peticion.equals(1)) {
			return "exception";

		} else if (result.hasErrors()) {
			Integer feedbacks = this.feedbackService.countFeedbacksByExcursion(excursion, manager);

			model.put("hasFeedback", feedbacks != 0);
			model.put("hasResidencia", residencia != null);

			model.put("feedback", feedback);
			return VIEWS_FEEDBACK_CREATE_OR_UPDATE_FORM;

		} else {
			feedbackService.save(feedback);
			model.put("message", "Se ha enviado el feedback correctamente");
			return "redirect:/excursiones/{excursionId}";
		}
	}

	@GetMapping(value = "/feedbacks/{feedbackId}/edit")
	public String initUpdateFeedbackForm(@PathVariable("feedbackId") final int feedbackId,
			final ModelMap model, final Principal p) {
		Feedback feedback = this.feedbackService
				.findFeedbackById(feedbackId);
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		Date fecha = new Date(System.currentTimeMillis() - 1);
		if ((!(feedback.getExcursion().getOrganizador() == organizador)||!(feedback.getExcursion().isFinalMode())
				|| feedback.getExcursion().getFechaFin().after(fecha))) {
			return "exception";
		}
		model.addAttribute("feedback", feedback);
		return VIEWS_FEEDBACK_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/feedbacks/{feedbackId}/edit")
	public String processUpdateFeedbackForm(@Valid final Feedback feedback,
			final BindingResult result, @PathVariable("feedbackId") final int feedbackId,
			final ModelMap model, final Principal p) {
		Organizador organizador = this.organizadorService.findOrganizadorByUsername(p.getName());
		Feedback feedbackToUpdate = this.feedbackService
				.findFeedbackById(feedbackId);

		feedback.setExcursion(feedbackToUpdate.getExcursion());
		Date fecha = new Date(System.currentTimeMillis() - 1);

		if ((!(feedback.getExcursion().getOrganizador() == organizador)||!(feedback.getExcursion().isFinalMode())
				|| feedback.getExcursion().getFechaFin().after(fecha))) {
			return "exception";
		}
		if (result.hasErrors()) {
			model.put("feedback", feedback);
			return VIEWS_FEEDBACK_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(feedback, feedbackToUpdate, "id", "residencia", "excursion");

			this.feedbackService.save(feedbackToUpdate);
			return "redirect:/feedbacks/";
		}
	}
}