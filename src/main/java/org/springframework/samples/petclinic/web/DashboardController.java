package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Dashboard;
import org.springframework.samples.petclinic.service.ActividadService;
import org.springframework.samples.petclinic.service.AncianoService;
import org.springframework.samples.petclinic.service.BuenaAccionService;
import org.springframework.samples.petclinic.service.ExcursionService;
import org.springframework.samples.petclinic.service.FeedbackService;
import org.springframework.samples.petclinic.service.IncidenciaService;
import org.springframework.samples.petclinic.service.InscripcionService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OrganizadorService;
import org.springframework.samples.petclinic.service.PeticionExcursionService;
import org.springframework.samples.petclinic.service.QuejaService;
import org.springframework.samples.petclinic.service.ResidenciaService;
import org.springframework.samples.petclinic.service.VisitaSanitariaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	private static final String	VIEWS_DASHBOARD	= "dashboard";

	@Autowired
	private ManagerService		managerService;
	
	@Autowired
	private OrganizadorService	organizadorService;

	@Autowired
	private AncianoService		ancianoService;

	@Autowired
	private ActividadService	actividadService;
	
	@Autowired
	private ResidenciaService	residenciaService;
	
	@Autowired
	private BuenaAccionService	buenaAccionService;
	
	@Autowired
	private IncidenciaService incidenciaService;
	
	@Autowired
	private QuejaService quejaService;
	
	@Autowired
	private InscripcionService inscripcionService;

	@Autowired
	private VisitaSanitariaService visitasanitariaService;

	@Autowired
	private ExcursionService excursionService;
	
	@Autowired
	private PeticionExcursionService peticionExcursionService;

	@Autowired
	private FeedbackService feedbackService;

	@GetMapping()
	public String listActividades(final Map<String, Object> model, final Principal p) {
		Dashboard dashboard = new Dashboard();
		//USUARIOS
		dashboard.setManagers(managerService.countManagers());
		dashboard.setOrganizadores(organizadorService.countOrganizadores());
		dashboard.setAncianos(ancianoService.countAncianos());
		//RESIDENCIAS
		dashboard.setResidencias(residenciaService.countResidencias());
		dashboard.setResidenciasCompletas(residenciaService.countResidenciasCompletas());
		dashboard.setMediaAncianosPorResidencia(ancianoService.avgAncianosByResidencia());
		dashboard.setActividades(actividadService.countActividades());
		dashboard.setMediaActividadesPorResidencia(actividadService.avgActividadesByResidencia());
		dashboard.setBuenasAcciones(buenaAccionService.countBuenasAcciones());
		dashboard.setIncidencias(incidenciaService.countIncidencias());
		dashboard.setQuejas(quejaService.countQuejas());
		dashboard.setMediaQuejasPorResidencia(quejaService.avgQuejasByResidencia());
		dashboard.setMediaQuejasPorAnciano(quejaService.avgQuejasByAnciano());
		dashboard.setInscripciones(inscripcionService.countInscripciones());
		dashboard.setInscripcionesAceptadas(inscripcionService.countInscripcionesAceptadas());
		dashboard.setRatioInscripcionesAceptadas(inscripcionService.ratioInscripcionesAceptadas());
		dashboard.setInscripcionesRechazadas(inscripcionService.countInscripcionesRechazadas());
		dashboard.setRatioInscripcionesRechazadas(inscripcionService.ratioInscripcionesRechazadas());
		dashboard.setVisitasSanitarias(visitasanitariaService.countVisitasSanitarias());
		dashboard.setMediaVisitasPorResidencia(visitasanitariaService.avgVisitasSanitariasByResidencia());
		//EXCURCIONES
		dashboard.setExcursiones(excursionService.countExcursiones());
		dashboard.setMediaExcursionesPorOrganizador(excursionService.avgExcursionesByOrganizador());
		dashboard.setPeticionesExcursion(peticionExcursionService.countPeticionesExcursion());
		dashboard.setMediaPeticionesPorExcursion(peticionExcursionService.avgPeticionesExcursionByExcursion());
		dashboard.setPeticionesAceptadas(peticionExcursionService.countPeticionesExcursionAceptadas());
		dashboard.setRatioPeticionesAceptadas(peticionExcursionService.ratioPeticionesExcursionAceptadas());
		dashboard.setPeticionesRechazadas(peticionExcursionService.countPeticionesExcursionRechazadas());
		dashboard.setRatioPeticionesRechazadas(peticionExcursionService.ratioPeticionesExcursionRechazadas());
		dashboard.setFeedbacks(feedbackService.countFeedbacks());
		dashboard.setMediaFeedbacksPorExcursion(feedbackService.avgFeedbacksByExcursion());
		
		model.put("dashboard", dashboard);
		return VIEWS_DASHBOARD;
	}


}
