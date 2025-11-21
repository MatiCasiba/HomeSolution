package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	public Proyecto(int numeroProyecto, Cliente cliente, String direccion, LocalDate fechaInicio, Map<String, Tarea> tareas) {
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
			throw new IllegalArgumentException("El proyecto debe tener al menos una tarea");
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
	    
	    for(Tarea t : tareas.values()) {
	    	total += t.calcularCosto();
	    	if(t.getDiasRetraso() > 0) hayRetraso = true;
	    }
	    
	    double recargo = hayRetraso ? 0.25 : 0.35;
	    return total * (1 + recargo);
	}
	
//	private boolean tieneRetrasosGraves() {
//		for(Tarea t : tareas.values()) {
//			if(t.getDiasRetraso() > 5) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public void actualizarFechaFinReal() {
		this.fechaFinReal = LocalDate.now();
	}
	
	public void marcarComoEnCurso() {
		// cambia de pendiente a activo (en curso)
		if (estado.equals(Estado.pendiente)) {
			boolean tieneAsignadas = tareas.values().stream().anyMatch(t -> t.getEmpleadoAsignado() != null);
			//si ya hay alguna tarea asignada, lo ponemos como ACTIVO
			if (tieneAsignadas) {
				estado = Estado.activo;
			}
		}
	}

	
	public void marcarComoFinalizado() {
	    if(estado.equals(Estado.finalizado)) {
	        throw new IllegalStateException("El proyecto ya está finalizado");
	    }
	    
	    // autotermina todas las tareas antes de finalizar el proyecto
	    for (Tarea tarea : tareas.values()) {
	        if (!tarea.estaTerminada()) {
	            tarea.marcarComoTerminada();
	            Empleado emp = tarea.getEmpleadoAsignado();
	            if(emp != null && !historialEmpleados.contains(emp)) {
	                historialEmpleados.add(emp);
	            }
	        }
	    }
	    
	    actualizarFechaFinReal();
	    estado = Estado.finalizado;
	}
	
	public void marcarTareaTerminada(String tituloTarea) {
		if(tituloTarea == null || tituloTarea.isBlank()) {
			throw new IllegalArgumentException("El título de la tarea no puede ser vacío");
		}
		
		Tarea tarea = tareas.get(tituloTarea);
		if(tarea == null) {
			throw new IllegalArgumentException("No existe una tarea con el título: " + tituloTarea);
		}
		
		if(!tarea.estaTerminada()) {
			tarea.marcarComoTerminada();
			Empleado emp = tarea.getEmpleadoAsignado();
			if(emp != null && !historialEmpleados.contains(emp)) {
				historialEmpleados.add(emp);
			}
		}
	}
	
	private int calcularDiasEstimados() {
		int total = 0;
		for(Tarea t : tareas.values()) {
			total += t.getDiasNecesarios();
		}
		return total;
	}
	
	public boolean estaFinalizado() {
		return estado.equals(Estado.finalizado);
	}
	
	public boolean tieneTareaConTitulo(String titulo) {
		return tareas.containsKey(titulo.toLowerCase().trim());
	}
	
	public Tarea obtenerTarea(String titulo) {
		Tarea tarea = tareas.get(titulo.toLowerCase().trim());
		if(tarea == null) {
			throw new IllegalArgumentException("Tarea no encontrada: " + titulo);
		}
		return tarea;
	}
	
	public boolean puedeSerModificado() {
		return !estaFinalizado();
	}
	
	public void validarQuePuedeModificarse() {
		if(!puedeSerModificado()) {
			throw new IllegalStateException("El proyecto está finalizado y no puede modificarse");
		}
	}
	
	//obtengo todos los empleados asignados al proyecto (historial+actuales)
	public List<Empleado> obtenerEmpleadosAsignados(){
		Set<Empleado> empleados = new HashSet<>();
		empleados.addAll(historialEmpleados); // empleados del historial (proyectos finalizados)
		
		//empleados actualmente asignados a tareas
		tareas.values().stream()
			.filter(t -> t.getEmpleadoAsignado() != null)
			.map(Tarea::getEmpleadoAsignado)
			.forEach(empleados::add);
		
		return new ArrayList<>(empleados);
	}
	
	// obtengo las tareas no asignadas del proyecto
	public List<Tarea> obtenerTareasNoAsignadas(){
		return tareas.values().stream()
				.filter(t -> t.getEmpleadoAsignado() == null)
				.collect(Collectors.toList());
	}
	
	// obtengo todas las tareas del proyecto
	public List<Tarea> obtenerTodasLasTareas(){
		return new ArrayList<>(tareas.values());
	}
	
	@Override
	public String toString() {
		return String.format(
				"Proyecto #%d [%s]\nCliente: %s\nDirección: %s\bEstado: %s\nInicio: %s\nFin estimado: %s\n",
				numeroProyecto,
				cliente.getNombre(),
				cliente.getContacto(),
				direccion,
				estado,
				fechaInicio,
				(fechaFinEstimado != null ? fechaFinEstimado : "-")
		);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		Proyecto other = (Proyecto) obj;
		return this.numeroProyecto == other.numeroProyecto;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(numeroProyecto);
	}
}
