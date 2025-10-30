package entidades;

public class Tarea {
	private String titulo;
	private String descripcion;
	private int diasNecesarios;
	private int diasRetraso;
	private Empleado empleadoAsignado;
	private boolean terminada;
	
	public Tarea(String titulo, String descripcion, int diasNecesarios) {
		if(titulo == null || titulo.isBlank()) {
			throw new IllegalArgumentException("El titulo de la tarea no puede estar vacio");
		}
		if(descripcion == null || descripcion.isBlank()) {
			throw new IllegalArgumentException("La descripción no puede ser vacía");
		}
		if(diasNecesarios <= 0) {
			throw new IllegalArgumentException("Los días necesarios deben ser mayor a 0");
		}
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.diasNecesarios = diasNecesarios;
		this.diasRetraso = 0;
		this.empleadoAsignado = null;
		this.terminada = false;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public int getDiasNecesarios() {
		return diasNecesarios;
	}
	public int getDiasRetraso() {
		return diasRetraso;
	}
	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	public boolean estaTerminada() {
		return terminada;
	}
	
	public void asignarEmpleado(Empleado empleado) {
		if(empleado == null) {
			throw new IllegalArgumentException("El empleado asigndo no puede ser nulo");
		}
		if(!empleado.estaDisponible()) {
			throw new IllegalArgumentException("El empleado no está disponible para asignar");
		}
		if(terminada) {
			throw new IllegalStateException("No se puede asignar un empleado a una tarea finalizada"); 
		}
		this.empleadoAsignado = empleado;
		empleado.asignar();
	}
	
	public void liberarEmpleado() {
		if(empleadoAsignado != null) {
			empleadoAsignado.liberar();
			empleadoAsignado = null;
		}
	}
	
	public void setDiasRetraso(int deasRetraso) {
		if(diasRetraso < 0) {
			throw new IllegalArgumentException("Los días de retrasso no pueden ser negativos");
		}
		this.diasRetraso = diasRetraso;
		if(empleadoAsignado != null && diasRetraso > 0) {
			empleadoAsignado.registrarRetrasos(0, deasRetraso); // el proyecto se asigna mass adelante en HomeSolution
		}
	}
}
