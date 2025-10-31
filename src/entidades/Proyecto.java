package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
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
		this.fechaFinReal = null;
		this.estado = Estado.pendiente;
		this.historialEmpleados = new ArrayList<>();
	}
}
