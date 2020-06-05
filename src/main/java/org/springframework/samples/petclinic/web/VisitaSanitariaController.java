package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Anciano;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Residencia;
import org.springframework.samples.petclinic.model.VisitaSanitaria;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.VisitaSanitariaService;
import org.springframework.samples.petclinic.web.validators.VisitaSanitariaValidator;
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
@RequestMapping("/visitas-sanitarias")
public class VisitaSanitariaController {

	private static final String		VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM	= "visitasSanitarias/createOrUpdateVisitaSanitariaForm";

	@Autowired
	private VisitaSanitariaService	visitaSanitariaService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ResidenciaService		residenciaService;

	@Autowired
	private AncianoService			ancianoService;


	@InitBinder("visitaSanitaria")
	public void initInscripcionBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		dataBinder.setDisallowedFields("residencia");
		dataBinder.setDisallowedFields("fecha");
		dataBinder.addValidators(new VisitaSanitariaValidator());
	}

	@GetMapping()
	public String listVisitaSanitarias(final Map<String, Object> model, final Principal p) {
		Manager manager = this.managerService.findManagerByUsername(p.getName());
		if (this.residenciaService.findMine(manager) == null) {
			return "redirect:residencias/new";
		}
		Iterable<VisitaSanitaria> visitasSanitarias = this.visitaSanitariaService.findAllMine(manager);
		model.put("visitasSanitarias", visitasSanitarias);
		return "visitasSanitarias/visitasSanitariasList";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Map<String, Object> model, final Principal p) {
		VisitaSanitaria visitaSanitaria = new VisitaSanitaria();
		model.put("visitaSanitaria", visitaSanitaria);

		Residencia residencia = this.residenciaService.findMine(this.managerService.findManagerByUsername(p.getName()));
		Iterable<Anciano> misAncianos = this.ancianoService.findAncianosMiResidencia(residencia);
		List<Anciano> ancianos = new ArrayList<>();
		for (Anciano anc : misAncianos) {
			if (anc.getTieneDependenciaGrave() == true) {
				ancianos.add(anc);
			}
		}
		if (ancianos.size() == 0) {
			model.put("noDependencia", true);
			model.put("noAncianosConDependencia", "* Su residencia no posee ningun anciano con dependencia grave");
		}
		model.put("ancianos", misAncianos);
		return VisitaSanitariaController.VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final VisitaSanitaria visitaSanitaria, final BindingResult result, final Map<String, Object> model, final Principal p) {
		Residencia residencia = this.residenciaService.findMine(this.managerService.findManagerByUsername(p.getName()));
		Date hoy = new Date(System.currentTimeMillis() - 1);
		Anciano anciano = this.ancianoService.findAncianoById(visitaSanitaria.getAnciano().getId());

		visitaSanitaria.setResidencia(residencia);
		visitaSanitaria.setAnciano(anciano);
		visitaSanitaria.setFecha(hoy);

		if (!this.tienePermiso(p, visitaSanitaria) || !visitaSanitaria.getAnciano().getTieneDependenciaGrave()) {
			return "exception";
		}

		if (result.hasErrors()) {
			Iterable<Anciano> misAncianos = this.ancianoService.findAncianosMiResidencia(residencia);
			List<Anciano> ancianos = new ArrayList<>();
			for (Anciano anc : misAncianos) {
				if (anc.getTieneDependenciaGrave() == true) {
					ancianos.add(anc);
				}
			}
			model.put("ancianos", ancianos);
			model.put("visitaSanitaria", visitaSanitaria);
			return VisitaSanitariaController.VIEWS_VISITA_SANITARIA_CREATE_OR_UPDATE_FORM;
		} else {
			this.visitaSanitariaService.saveVisitaSanitaria(visitaSanitaria);
			model.put("message", "Se ha registrado la visita sanitaria correctamente");
			return "redirect:/visitas-sanitarias";
		}
	}

	@GetMapping("/{visitaSanitariaId}")
	public ModelAndView showVisitaSanitariaOrganizador(@PathVariable("visitaSanitariaId") final int visitaSanitariaId, final Principal p) {
		VisitaSanitaria visitaSanitaria = this.visitaSanitariaService.findVisitaSanitariaById(visitaSanitariaId);
		ModelAndView mav = new ModelAndView("visitasSanitarias/visitasSanitariasDetails");
		mav.addObject(visitaSanitaria);

		if (!this.tienePermiso(p, visitaSanitaria)) {
			return new ModelAndView("exception");
		}

		return mav;
	}

	@GetMapping("/{visitaSanitariaId}/delete")
	public String deleteVisitaSanitaria(@PathVariable("visitaSanitariaId") final int visitaSanitariaId, final Principal p) {
		VisitaSanitaria visitaSanitaria = this.visitaSanitariaService.findVisitaSanitariaById(visitaSanitariaId);

		if (!this.tienePermiso(p, visitaSanitaria)) {
			return "exception";
		}

		this.visitaSanitariaService.deleteVisitaSanitaria(visitaSanitaria);
		return "redirect:/visitas-sanitarias";
	}

	public boolean tienePermiso(final Principal p, final VisitaSanitaria v) {
		Residencia residenciaPrincipal = this.residenciaService.findMine(this.managerService.findManagerByUsername(p.getName()));

		boolean esMiAnciano = false;
		Iterable<Anciano> misAncianos = this.ancianoService.findAncianosMiResidencia(residenciaPrincipal);
		for (Anciano anc : misAncianos) {
			if (anc.getTieneDependenciaGrave() == true) {
				if (anc.getId().equals(v.getAnciano().getId())) {
					esMiAnciano = true;
					break;
				}
			}
		}
		return v.getResidencia().equals(residenciaPrincipal) && esMiAnciano;
	}

}
