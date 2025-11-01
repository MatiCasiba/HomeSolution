package entidades;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
	
	public void asignarEmpleadoATarea(int numProyecto, String tituloTarea) {
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Proyecto inexistente");
		}
		if(p.getEstado().equals(Estado.finalizado)) {
			throw new IllegalStateException("No se puede asignar empleados a proyectos finalizados");
		}
		
		Tarea t = p.getTareas().get(tituloTarea);
		if(t == null) {
			throw new IllegalArgumentException("Tarea no encontrada");
		}
		
		Optional<Empleado> disponible = empleados.values().stream().filter(Empleado::estaDisponible).findFirst();
		if(disponible.isEmpty()) {
			throw new IllegalStateException("No hay empleados disponibles");
		}
		t.asignarEmpleado(disponible.get());
		disponible.get().asignar();
	}
	
	public void registrarRetrasoEnTarea(int numProyecto, String tituloTarea, int diasRetraso) {
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Poryecto no encontrado");
		}
		if(p.getEstado().equals(Estado.finalizado)) {
			throw new IllegalStateException("Proyecto ya finalizado");
		}
		
		Tarea t = p.getTareas().get(tituloTarea);
		if(t == null) {
			throw new IllegalArgumentException("Tarea inexistente");
		}
		t.setDiasRetraso(diasRetraso);
	}
}
