package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	
//	public double calcularCostoTaeas() {
//		double total = 0;
//		boolean hayRetraso = false;
//		for(Tarea t: tareas.values()) {
//			total += t.calcularCosto();
//			if(t.getDiasRetraso() > 0) hayRetraso = true;
//		}
//		if(hayRetraso) {
//			double recargo = (tieneRetrasosGraves() ? 0.35 : 0.25);
//			total *= (1 + recargo);
//		}
//		return total;
//	}
	
	public double calcularCostoTaeas() {
	    double total = 0;
	    boolean hayRetraso = false;
	    
	    //si el proyecto está FINALIZADO, calcular basado en historial
	    if (estado.equals(Estado.finalizado)) {
	        for (Empleado emp : historialEmpleados) {
	            //calcular costo aproximado por empleado
	            total += emp.calcularSueldo() * (calcularDiasEstimados() / 30.0);
	        }
	    } else {
	        //proyecto ACTIVO/PENDIENTE - calcula normalmente
	        for(Tarea t: tareas.values()) {
	            total += t.calcularCosto();
	            if(t.getDiasRetraso() > 0) hayRetraso = true;
	        }
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
