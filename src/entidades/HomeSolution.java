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
	
	public void registrarEmpleado(Empleado e) {
		if(e == null || empleados.containsKey(e.getLegajo())) {
			throw new IllegalArgumentException("Empleado nulo o legajo duplicado");
		}
		empleados.put(e.getLegajo(), e);
	}
	
	public void registrarEmpleadoPlanta(EmpleadoPlanta e) {
		registrarEmpleado(e);
	}
	public void registrarEmpleadoContratado(EmpleadoContratado e) {
		registrarEmpleado(e);
	}
	
	public void registrarProyecto(Proyecto p) {
		if(p == null || proyectos.containsKey(p.getNumeroProyecto())) {
			throw new IllegalArgumentException("Proyecto nulo o número duplicado");
		}
		proyectos.put(p.getNumeroProyecto(), p);
	}
}
