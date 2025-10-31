package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proyecto {
	private int numeroProyecto;
	private Cliente cliente;
	private String direccion;
	private Map<String, Tarea> tareas;
	private LocalDate fechaInicio;
	private LocalDate fechaFinEstimado;
	private LocalDate fechaFinReal;
	private String estado;
	private List<Empleado> historialEmpleados;
	
	public Proyecto(int numeroProyeccto, Cliente cliente, String direccion, LocalDate fehcaInicio, Map<String, Tarea> tarteas) {
		if(numeroProyecto <= 0) {
			throw new IllegalArgumentException("El número de proyecto debe ser mayor a 0");
		}
		if(cliente == null) {
			throw new IllegalArgumentException("El cliente no puede ser nulo");
		}
		if(direccion == null || direccion.isBlank()) {
			throw new IllegalArgumentException("La dirección no puede estar vacia");
		}
		if(fechaInicio == null) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
		}
		if(tareas == null || tareas.isEmpty()) {
			throw new IllegalArgumentException("El poryecto debe tener al menos una tarea");
		}
		this.numeroProyecto = numeroProyecto;
		this.cliente = cliente;
		this.direccion = direccion;
		this.tareas = new HashMap<>(tareas);
		this.fechaInicio = fechaInicio;
		this.fechaFinEstimado = fechaInicio.plusDays(calcularDiasEstimados());
		this.fechaFinReal = null;
		this.estado = Estado.pendiente;
		this.historialEmpleados = new ArrayList<>();
	}
	
	public int getNumeroProyecto() {
		return numeroProyecto;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public String getDireccion() {
		return direccion;
	}
	public String getEstado() {
		return estado;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public LocalDate getFechaFinEstimado() {
		return fechaFinEstimado;
	}
	public LocalDate getFechaFinReal() {
		return fechaFinReal;
	}
	public Map<String, Tarea> getTareas(){
		return Collections.unmodifiableMap(tareas);
	}
	public List<Empleado> obtenerHistorialEmpleados(){
		return Collections.unmodifiableList(historialEmpleados);
	}
	
	public void agregarTarea(Tarea tarea) {
		if(tarea == null) {
			throw new IllegalArgumentException("La tarea no puede ser nula");
		}
		if(tareas.containsKey(tarea.getClave())) {
			throw new IllegalArgumentException("Ya existe una tarea con ese título en el proyecto");
		}
		tareas.put(tarea.getClave(), tarea);
		fechaFinEstimado = fechaInicio.plusDays(calcularDiasEstimados());
	}
	
	public double calcularCostoTaeas() {
		double total = 0;
		boolean hayRetraso = false;
		for(Tarea t: tareas.values()) {
			total += t.calcularCosto();
			if(t.getDiasNecesarios() > 0) hayRetraso = true;
		}
		if(hayRetraso) {
			double recargo = (tieneRetrasosGraves() ? 0.35 : 0.25);
			total *= (1 + recargo);
		}
		return total;
	}
	
	private boolean tieneRetrasosGraves() {
		for(Tarea t : tareas.values()) {
			if(t.getDiasRetraso() > 5) {
				return true;
			}
		}
		return false;
	}
	
	public void actualizarFechaFinReal() {
		this.fechaFinReal = LocalDate.now();
	}
	
	public void marcarComoEnCurso() {
		if(!estado.equals(Estado.pendiente)) {
			throw new IllegalStateException("Solo se puede pasar a ACTIVO desde PENDIENTE");
		}
		boolean tieneAsignadas = tareas.values().stream().anyMatch(t -> t.getEmpleadoAsignado() != null);
		if(tieneAsignadas) {
			throw new IllegalStateException("No se puede iniciar un proyecto con tareas ya asignadas");
		}
		estado = Estado.activo;
	}
	
	public void marcarComoFinalizado() {
		if(estado.equals(Estado.finalizado)) {
			throw new IllegalStateException("El proyecto ya está finalizado");
		}
		boolean hayIncompletas = tareas.values().stream().anyMatch(t -> t.estaTerminada());
		if(hayIncompletas) {
			throw new IllegalStateException("No se pude finalizar el proyecto: hay tareas sin terminar");
		}
		actualizarFechaFinReal();
		estado = Estado.finalizado;
	}
	
	private int calcularDiasEstimados() {
		int total = 0;
		for(Tarea t : tareas.values()) {
			total += t.getDiasNecesarios();
		}
		return total;
	}
}
