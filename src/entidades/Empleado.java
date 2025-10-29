package entidades;

import java.util.HashMap;
import java.util.Map;

public class Empleado {
	protected int legajo;
	protected String nombre;
	protected boolean disponible;
	protected int retrasosTotales;
	protected Map<Integer, Integer> retrasosPorProyecto;
	
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
}
