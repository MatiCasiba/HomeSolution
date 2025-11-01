package entidades;

import java.util.HashMap;
import java.util.Map;

public class HomeSolution {
	private Map<Integer, Proyecto> proyectos;
	private Map<Integer, Empleado> empleados;
	
	public HomeSolution() {
		this.proyectos = new HashMap<>();
		this.empleados = new HashMap<>();
	}
}
