package entidades;

import java.util.Objects;

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
		if(descripcion == null) {
			throw new IllegalArgumentException("La descripción no puede ser vacía");
		}
		if(diasNecesarios <= 0) {
			diasNecesarios = 1;
		}
		this.titulo = titulo;
		this.descripcion = descripcion.isBlank() ? "Sin descripción" : descripcion;
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
	
	public void setDiasRetraso(int diasRetraso) {
	    if(diasRetraso < 0) {
	        throw new IllegalArgumentException("Los días de retraso no pueden ser negativos");
	    }
	    this.diasRetraso = diasRetraso;
	}
	
	public void marcarComoTerminada() {
	    if(terminada) {
	        throw new IllegalStateException("La tarea ya está finalizada");
	    }
	    terminada = true;
	    liberarEmpleado(); // esto libera al empleado automáticamente
	}
	
	public double calcularCosto() {
	    if(empleadoAsignado == null) {
	        return 0;
	    }
	    int diasRealesTrabajados = diasNecesarios + diasRetraso;
	    //para los cotnratados: costo por hora * horas trabajadas
	    if(empleadoAsignado instanceof EmpleadoContratado) {
	    	EmpleadoContratado ec = (EmpleadoContratado) empleadoAsignado;
	    	return ec.getValorHora() * 8 * diasRealesTrabajados;
	    }
	    //para los de planta: costo por día * días trabajados
	    else if(empleadoAsignado instanceof EmpleadoPlanta) {
	    	EmpleadoPlanta ep = (EmpleadoPlanta) empleadoAsignado;
	    	return ep.getValorDia() * diasRealesTrabajados;
	    }
	    return 0;
	}
	
	public String getClave() {
		return titulo.toLowerCase().trim();
	}
	
	@Override
	public String toString() {
		return titulo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		Tarea other = (Tarea) obj;
		return titulo.equalsIgnoreCase(other.titulo);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(titulo.toLowerCase());
	}
}
