package entidades;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Empleado {
	private int legajo;
	private String nombre;
	private boolean disponible;
	private int retrasosTotales;
	private Map<Integer, Integer> retrasosPorProyecto;
	
	public Empleado(int legajo, String nombre) {
		if(legajo<=0) throw new IllegalArgumentException("El legajo debe ser mayor que 0");
		if(nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede ser vacío");
		this.legajo = legajo;
		this.nombre = nombre;
		this.disponible = true;
		this.retrasosTotales = 0;
		this.retrasosPorProyecto = new HashMap<>();
	}
	
	public boolean estaDisponible() {
		return disponible;
	}
	
	public void liberar() {
		this.disponible = true;
	}
	
	public void asignar() {
		this.disponible = false;
	}
	
	public void registrarRetrasos(int numProyecto, int dias) {
		if(numProyecto <= 0) throw new IllegalArgumentException("Número de poryecto inválido");
		if(dias < 0) throw new IllegalArgumentException("Los días de retraso no pueden ser negativos");
		retrasosTotales += dias;
		retrasosPorProyecto.merge(numProyecto, dias, Integer::sum);
	}
	
	public int getLegajo() {
		return legajo;
	}
	public String getNombre() {
		return nombre;
	}
	public int getRetrasosTotales() {
		return retrasosTotales;
	}
	public Map<Integer, Integer> getRetrasosPorProyecto(){
		return new HashMap<>(retrasosPorProyecto);
	}
	
	public abstract double calcularSueldo();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Empleado [Legajo=").append(legajo)
			.append(", Nombre=").append(nombre)
			.append(", Disponibles=").append(disponible)
			.append(", Retrasos Totales=").append(retrasosTotales)
			.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Empleado)) return false;
		Empleado other = (Empleado) obj;
		return legajo == other.legajo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(legajo);
	}
	
}
