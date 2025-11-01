package entidades;

import java.util.HashMap;
import java.util.Map;

public class HomeSolution implements IHomeSolution{
	private Map<Integer, Proyecto> proyectos;
	private Map<Integer, Empleado> empleados;
	private int contadorProyectos;
	
	public HomeSolution() {
		this.proyectos = new HashMap<>();
		this.empleados = new HashMap<>();
	}
	
}
