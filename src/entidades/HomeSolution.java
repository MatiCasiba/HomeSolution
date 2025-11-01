package entidades;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	public void finalzarProyecto(int numProyecto) {
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Proyecto no encontrado"); 
		}
		p.marcarComoFinalizado();
	}
	
	public void reasignarEmpleado(int numProyecto, String tituloTarea, int nuevoLegajo) {
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Proyecto inexistente");
		}
		if(p.getEstado().equals(Estado.finalizado)) {
			throw new IllegalStateException("Proyecto ya finalizado");
		}
		
		Empleado nuevo = empleados.get(nuevoLegajo);
		if(nuevo == null) {
			throw new IllegalArgumentException("Empleado no encontrado");
		}
		if(!nuevo.estaDisponible()) {
			throw new IllegalStateException("El empleado no está disponible");
		}
		
		Tarea t = p.getTareas().get(tituloTarea);
		if(t == null) {
			throw new IllegalArgumentException("Tarea inexistente");
		}
		if(t.getEmpleadoAsignado() != null) {
			t.getEmpleadoAsignado().liberar();
		}
		t.asignarEmpleado(nuevo);
		nuevo.asignar();
	}
	
	public double calcularCostoFinal(int numProyecto) {
		Proyecto p = proyectos.get(numProyecto);
		if(p==null) {
			throw new IllegalArgumentException("Poryecto inexistente");
		}
		return p.calcularCostoTaeas();
	}
	
	public List<Proyecto> obtenerProyectosNoFinalizados(){
		return proyectos.values().stream().filter(p -> !p.getEstado().equals(Estado.finalizado)).collect(Collectors.toList());
	}
	
	public List<Proyecto> obtnerProyectosPendientes(){
		return proyectos.values().stream().filter(p -> p.getEstado().equals(Estado.pendiente)).collect(Collectors.toList());
	}
	
	public List<Proyecto> obtnerProyectosActivos(){
		return proyectos.values().stream().filter(p -> p.getEstado().equals(Estado.activo)).collect(Collectors.toList());
	}
	
	public boolean consultarFinalizado(int numProyecto) {
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Poryecto no encontrado");
		}
		return p.getEstado().equals(Estado.finalizado);
	}
	
	public List<Empleado> obtenerEmpleadosNoAsignados(){
		return empleados.values().stream().filter(Empleado::estaDisponible).collect(Collectors.toList());
	}
	
	public boolean empleadoTuvoRetrasos(int legajo) {
		Empleado e = empleados.get(legajo);
		if(e == null) {
			throw new IllegalArgumentException("Empleado no encontrado");
		}
		return e.getRetrasosTotales() > 0;
	}
	
	public List<Empleado> consultarEmpleadosAsignadosAProyecto(int numProyecto){
		Proyecto p = proyectos.get(numProyecto);
		if(p == null) {
			throw new IllegalArgumentException("Poryecto no encontrado");
		}
		return p.getTareas().values().stream().map(Tarea::getEmpleadoAsignado).filter(Objects::nonNull).distinct().collect(Collectors.toList());
	}
	
	public void reasignarEmpleadoMenosRetrasos(int numProyecto, String tituloTarea) {
		Proyecto p = proyectos.get(numProyecto);
		if(p==null) {
			throw new IllegalArgumentException("Proyecto inexistente");
		}
		Optional<Empleado> menorRetraso = empleados.values().stream().filter(Empleado::estaDisponible).min(Comparator.comparingInt(Empleado::getRetrasosTotales));
		if(menorRetraso.isEmpty()) {
			throw new IllegalStateException("No hay emplados disponibles");
		}
		
		Tarea t = p.getTareas().get(tituloTarea);
		if(t==null) {
			throw new IllegalArgumentException("Tarea no encontrada");
		}
		
		if(t.getEmpleadoAsignado() != null) {
			t.getEmpleadoAsignado().liberar();
		}
		t.asignarEmpleado(menorRetraso.get());
		menorRetraso.get().asignar();
	}
	
	private boolean irepValido() {
		boolean empleadosExisten = proyectos.values().stream()
                .flatMap(p -> p.getTareas().values().stream())
                .map(Tarea::getEmpleadoAsignado)
                .filter(Objects::nonNull)
                .allMatch(empleados::containsValue);
		
		boolean pendientesOk = proyectos.values().stream()
				.filter(p -> p.getEstado().equals(Estado.pendiente))
				.flatMap(p -> p.getTareas().values().stream())
				.allMatch(t -> t.getEmpleadoAsignado() == null);
		
		boolean finalizadosOk = proyectos.values().stream()
				.filter( p -> p.getEstado().equals(Estado.finalizado))
				.allMatch(p -> p.getTareas().values().stream().allMatch(Tarea::estaTerminada));
		
		return empleadosExisten && pendientesOk && finalizadosOk; 
	}
}
