package org.springframework.samples.petclinic.model;

public class Dashboard {
	
	//USUARIOS
	private Long managers;
	private Long organizadores;
	private Long ancianos;
	
	//RESIDENCIAS
	private Long residencias;
	private Long residenciasCompletas;
	private Double mediaAncianosPorResidencia;
	private Long actividades;
	private Double mediaActividadesPorResidencia;
	private Long buenasAcciones;
	private Long incidencias;
	private Long quejas;
	private Double mediaQuejasPorResidencia;
	private Double mediaQuejasPorAnciano;
	private Long inscripciones;
	private Long inscripcionesAceptadas;
	private Double ratioInscripcionesAceptadas;
	private Long inscripcionesRechazadas;
	private Double ratioInscripcionesRechazadas;
	private Long visitasSanitarias;
	private Double mediaVisitasPorResidencia;

	//EXCURCIONES
	private Long excursiones;
	private Double mediaExcursionesPorOrganizador;
	private Long peticionesExcursion;
	private Double mediaPeticionesPorExcursion;
	private Long peticionesAceptadas;
	private Double ratioPeticionesAceptadas;
	private Long peticionesRechazadas;
	private Double ratioPeticionesRechazadas;
	private Long feedbacks;
	private Double mediaFeedbacksPorExcursion;
	
	public Long getManagers() {
		return managers;
	}
	public void setManagers(Long managers) {
		this.managers = managers;
	}
	public Long getOrganizadores() {
		return organizadores;
	}
	public void setOrganizadores(Long organizadores) {
		this.organizadores = organizadores;
	}
	public Long getAncianos() {
		return ancianos;
	}
	public void setAncianos(Long ancianos) {
		this.ancianos = ancianos;
	}
	public Long getResidencias() {
		return residencias;
	}
	public void setResidencias(Long residencias) {
		this.residencias = residencias;
	}
	public Long getResidenciasCompletas() {
		return residenciasCompletas;
	}
	public void setResidenciasCompletas(Long residenciasCompletas) {
		this.residenciasCompletas = residenciasCompletas;
	}
	public Double getMediaAncianosPorResidencia() {
		return mediaAncianosPorResidencia;
	}
	public void setMediaAncianosPorResidencia(Double mediaAncianosPorResidencia) {
		this.mediaAncianosPorResidencia = mediaAncianosPorResidencia;
	}
	public Long getActividades() {
		return actividades;
	}
	public void setActividades(Long actividades) {
		this.actividades = actividades;
	}
	public Double getMediaActividadesPorResidencia() {
		return mediaActividadesPorResidencia;
	}
	public void setMediaActividadesPorResidencia(Double mediaActividadesPorResidencia) {
		this.mediaActividadesPorResidencia = mediaActividadesPorResidencia;
	}
	public Long getBuenasAcciones() {
		return buenasAcciones;
	}
	public void setBuenasAcciones(Long buenasAcciones) {
		this.buenasAcciones = buenasAcciones;
	}
	public Long getIncidencias() {
		return incidencias;
	}
	public void setIncidencias(Long incidencias) {
		this.incidencias = incidencias;
	}
	public Long getQuejas() {
		return quejas;
	}
	public void setQuejas(Long quejas) {
		this.quejas = quejas;
	}
	public Double getMediaQuejasPorResidencia() {
		return mediaQuejasPorResidencia;
	}
	public void setMediaQuejasPorResidencia(Double mediaQuejasPorResidencia) {
		this.mediaQuejasPorResidencia = mediaQuejasPorResidencia;
	}
	public Double getMediaQuejasPorAnciano() {
		return mediaQuejasPorAnciano;
	}
	public void setMediaQuejasPorAnciano(Double mediaQuejasPorAnciano) {
		this.mediaQuejasPorAnciano = mediaQuejasPorAnciano;
	}
	public Long getInscripciones() {
		return inscripciones;
	}
	public void setInscripciones(Long inscripciones) {
		this.inscripciones = inscripciones;
	}
	public Long getInscripcionesAceptadas() {
		return inscripcionesAceptadas;
	}
	public void setInscripcionesAceptadas(Long inscripcionesAceptadas) {
		this.inscripcionesAceptadas = inscripcionesAceptadas;
	}
	public Double getRatioInscripcionesAceptadas() {
		return ratioInscripcionesAceptadas;
	}
	public void setRatioInscripcionesAceptadas(Double ratioInscripcionesAceptadas) {
		this.ratioInscripcionesAceptadas = ratioInscripcionesAceptadas;
	}
	public Long getInscripcionesRechazadas() {
		return inscripcionesRechazadas;
	}
	public void setInscripcionesRechazadas(Long inscripcionesRechazadas) {
		this.inscripcionesRechazadas = inscripcionesRechazadas;
	}
	public Double getRatioInscripcionesRechazadas() {
		return ratioInscripcionesRechazadas;
	}
	public void setRatioInscripcionesRechazadas(Double ratioInscripcionesRechazadas) {
		this.ratioInscripcionesRechazadas = ratioInscripcionesRechazadas;
	}
	public Long getVisitasSanitarias() {
		return visitasSanitarias;
	}
	public void setVisitasSanitarias(Long visitasSanitarias) {
		this.visitasSanitarias = visitasSanitarias;
	}
	public Double getMediaVisitasPorResidencia() {
		return mediaVisitasPorResidencia;
	}
	public void setMediaVisitasPorResidencia(Double mediaVisitasPorResidencia) {
		this.mediaVisitasPorResidencia = mediaVisitasPorResidencia;
	}
	public Long getExcursiones() {
		return excursiones;
	}
	public void setExcursiones(Long excursiones) {
		this.excursiones = excursiones;
	}
	public Double getMediaExcursionesPorOrganizador() {
		return mediaExcursionesPorOrganizador;
	}
	public void setMediaExcursionesPorOrganizador(Double mediaExcursionesPorOrganizador) {
		this.mediaExcursionesPorOrganizador = mediaExcursionesPorOrganizador;
	}
	public Long getPeticionesExcursion() {
		return peticionesExcursion;
	}
	public void setPeticionesExcursion(Long peticionesExcursion) {
		this.peticionesExcursion = peticionesExcursion;
	}
	public Double getMediaPeticionesPorExcursion() {
		return mediaPeticionesPorExcursion;
	}
	public void setMediaPeticionesPorExcursion(Double mediaPeticionesPorExcursion) {
		this.mediaPeticionesPorExcursion = mediaPeticionesPorExcursion;
	}
	public Long getPeticionesAceptadas() {
		return peticionesAceptadas;
	}
	public void setPeticionesAceptadas(Long peticionesAceptadas) {
		this.peticionesAceptadas = peticionesAceptadas;
	}
	public Double getRatioPeticionesAceptadas() {
		return ratioPeticionesAceptadas;
	}
	public void setRatioPeticionesAceptadas(Double ratioPeticionesAceptadas) {
		this.ratioPeticionesAceptadas = ratioPeticionesAceptadas;
	}
	public Long getPeticionesRechazadas() {
		return peticionesRechazadas;
	}
	public void setPeticionesRechazadas(Long peticionesRechazadas) {
		this.peticionesRechazadas = peticionesRechazadas;
	}
	public Double getRatioPeticionesRechazadas() {
		return ratioPeticionesRechazadas;
	}
	public void setRatioPeticionesRechazadas(Double ratioPeticionesRechazadas) {
		this.ratioPeticionesRechazadas = ratioPeticionesRechazadas;
	}
	public Long getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Long feedbacks) {
		this.feedbacks = feedbacks;
	}
	public Double getMediaFeedbacksPorExcursion() {
		return mediaFeedbacksPorExcursion;
	}
	public void setMediaFeedbacksPorExcursion(Double mediaFeedbacksPorExcursion) {
		this.mediaFeedbacksPorExcursion = mediaFeedbacksPorExcursion;
	}
	

}
