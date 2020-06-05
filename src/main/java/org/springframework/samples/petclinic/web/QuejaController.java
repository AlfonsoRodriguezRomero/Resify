package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Queja;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.QuejaService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.web.validators.QuejaValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/quejas")
public class QuejaController {

	private static final String	VIEWS_QUEJA_CREATE_OR_UPDATE_FORM	= "quejas/createOrUpdateQuejaForm";

	@Autowired
	private QuejaService	quejaService;

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

	@InitBinder("queja")
	public void initQuejaBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new QuejaValidator());
	}

	@GetMapping()
	public String listQuejas(final Map<String, Object> model, final Principal p) {
		Iterable<Queja> quejas = new ArrayList<>();
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		quejas = this.quejaService.findQuejasByManager(manager);
		model.put("quejas", quejas);
		return "quejas/quejasList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		Queja queja = new Queja();
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		Double numQuejas = this.quejaService.countQuejasHoyByAnciano(anciano);
		Residencia residencia = this.residenciaService.findResidenciaByAnciano(anciano);
		model.put("hasMaxQuejas", numQuejas >= 3);
		model.put("hasResidencia", residencia != null);
		model.put("queja", queja);
		return QuejaController.VIEWS_QUEJA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Queja queja, final BindingResult result, final Map<String, Object> model, final Principal p) {
		Anciano anciano = this.ancianoService.findAncianoByUsername(p.getName());
		Residencia residencia = this.residenciaService.findResidenciaByAnciano(anciano);
		Double numQuejas = this.quejaService.countQuejasHoyByAnciano(anciano);
		if (result.hasErrors()) {
			model.put("hasMaxQuejas", numQuejas >= 3);
			model.put("hasResidencia", residencia != null);
			model.put("queja", queja);
			return QuejaController.VIEWS_QUEJA_CREATE_OR_UPDATE_FORM;
		} else {
			if(residencia == null || numQuejas >=3) {
				return "exception";
			}
			queja.setAnciano(anciano);
			Date fecha = Date.from(Instant.now().minusMillis(1));
			queja.setFecha(fecha);
			this.quejaService.saveQueja(queja);
			model.put("message", "Se ha enviado la queja correctamente");
			return "redirect:/";
		}
	}

	@GetMapping("/{quejaId}")
	public ModelAndView showQueja(@PathVariable("quejaId") final int quejaId, final Principal p) {
		Queja queja = this.quejaService.findQuejaById(quejaId);
		ModelAndView mav = new ModelAndView("quejas/quejasDetails");
		mav.addObject(queja);
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		Iterable<Queja> quejas = this.quejaService.findQuejasByManager(manager);
		if (StreamSupport.stream(quejas.spliterator(), false).noneMatch(x -> queja.equals(x))) {
			mav = new ModelAndView("exception");
		}
		return mav;
	}


}
