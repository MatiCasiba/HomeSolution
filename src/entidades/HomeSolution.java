package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeSolution implements IHomeSolution{
	private Map<Integer, Proyecto> proyectos;
	private Map<Integer, Empleado> empleados;
	private int contadorProyectos;
	
	public HomeSolution() {
		this.proyectos = new HashMap<>();
		this.empleados = new HashMap<>();
		this.contadorProyectos = 1;
	}
	
	// REGISTRO DE EMPLEADOS
	@Override
	public void registrarEmpleado(String nombre, double valor) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre no puede ser vacío");
		}
		if (valor <= 0) {
			throw new IllegalArgumentException("El valor debe ser mayor a 0");
		}

		int legajo = generarLegajo();
		EmpleadoContratado empleado = new EmpleadoContratado(legajo, nombre, valor);
		empleados.put(legajo, empleado);
	}
	
	@Override
    public void registrarEmpleado(String nombre, double valor, String categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor a 0");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoría no puede ser vacía");
        }

        int legajo = generarLegajo();
        EmpleadoPlanta empleado = new EmpleadoPlanta(legajo, nombre, valor, categoria);
        empleados.put(legajo, empleado);
    }

	private int generarLegajo() {
		return empleados.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
	}
	
	// REGISTRO Y GESTION DE PROYECTOS
	@Override
	public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio,
	        String[] cliente, String inicio, String fin) {
	    validarDatosProyecto(titulos, descripcion, dias, domicilio, cliente, inicio, fin);

	    // parseo fechas antes de validar
	    LocalDate fechaInicio = parsearFecha(inicio);
	    LocalDate fechaFin = parsearFecha(fin);
	    
	    // valido que la fecha fin no sea anteriror a la inicio
	    if (fechaFin.isBefore(fechaInicio)) {
	        throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
	    }

	    Cliente clienteObj = new Cliente(cliente[0], cliente[2], cliente[1]);

	    Map<String, Tarea> tareasMap = new HashMap<>();
	    for (int i = 0; i < titulos.length; i++) {
	        Tarea tarea = new Tarea(titulos[i], descripcion[i], (int) dias[i]);
	        tareasMap.put(tarea.getClave(), tarea);
	    }

	    Proyecto proyecto = new Proyecto(contadorProyectos, clienteObj, domicilio, fechaInicio, tareasMap);
	    proyectos.put(contadorProyectos, proyecto);
	    contadorProyectos++;
	}
	
	private void validarDatosProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio,
	        String[] cliente, String inicio, String fin) {
	    Proyecto.validarArraysTareas(titulos, descripcion, dias);
	    Proyecto.validarDomicilio(domicilio);
	    Cliente.validarDatosCliente(cliente);
	    
	    if(inicio == null || fin == null) {
	    	throw new IllegalArgumentException("Las fechas no puden ser nulas");
	    }
	    LocalDate fechaInicio = parsearFecha(inicio);
	    LocalDate fechaFin = parsearFecha(fin);
	    Proyecto.validarFechas(fechaInicio, fechaFin);
	}
	
	private LocalDate parsearFecha(String fecha) {
		try {
			return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD");
		}
	}
	
	// ASIGNACIÓN Y GESTIÓN DE TAREAS
	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		proyecto.validarQuePuedeModificarse();

		Tarea tarea = proyecto.obtenerTarea(titulo);
		validarTareaNoAsignada(tarea);

		Empleado empleado = buscarEmpleadoDisponible();
		if (empleado == null) {
			throw new IllegalStateException("No hay empleados disponibles para asignar");
		}
		tarea.asignarEmpleado(empleado);
		empleado.asignar();
		proyecto.marcarComoEnCurso();
	}
	
	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) {
	    Proyecto proyecto = obtenerProyectoValido(numero);
	    validarProyectoParaModificacion(numero);

	    Tarea tarea = proyecto.obtenerTarea(titulo);
	    validarTareaNoAsignada(tarea);

	    Empleado empleado = buscarEmpleadoMenosRetrasos();
	    if (empleado == null) {
	        throw new IllegalStateException("No hay empleados disponibles para asignar");
	    }

	    if (!empleado.estaDisponible()) {
	        empleado.liberar();
	    }

	    tarea.asignarEmpleado(empleado);
	    empleado.asignar();
	    proyecto.marcarComoEnCurso();
	}
	
	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) {
	    Proyecto proyecto = obtenerProyectoValido(numero);
	    validarProyectoParaModificacion(numero);

	    Tarea tarea = proyecto.obtenerTarea(titulo);

	    if (cantidadDias < 0) {
	        throw new IllegalArgumentException("Los días de retraso no pueden ser negativos");
	    }

	    tarea.acumularRetraso((int) cantidadDias);

	    //registrar retrasos en el empleado asignado
	    if (tarea.getEmpleadoAsignado() != null && cantidadDias > 0) {
	        tarea.getEmpleadoAsignado().registrarRetrasos(numero, (int) cantidadDias);
	    }
	}
	
	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoParaModificacion(numero);
		if (dias <= 0) {
			throw new IllegalArgumentException("Los días deben ser mayores a 0");
		}

		Tarea nuevaTarea = new Tarea(titulo, descripcion, (int) dias);
		proyecto.agregarTarea(nuevaTarea);
	}
	
	@Override
	public void finalizarTarea(Integer numero, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoParaModificacion(numero);
		
		proyecto.marcarTareaTerminada(titulo);
	}
	
	@Override
	public void finalizarProyecto(Integer numero, String fin) {
	    Proyecto proyecto = obtenerProyectoValido(numero);
	    if (proyecto.getEstado().equals(Estado.finalizado)) {
	        throw new IllegalArgumentException("El proyecto ya está finalizado");
	    }

	    LocalDate fechaFin = parsearFecha(fin);
	    
	    // valido que la fecha fin no sea anteriror a la inicio
	    if (fechaFin.isBefore(proyecto.getFechaInicio())) {
	        throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
	    }
	    
	    proyecto.marcarComoFinalizado();
	}
	
	// REASIGNACIÓN DE EMPLEADOS
	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoParaModificacion(numero);

		Tarea tarea = proyecto.obtenerTarea(titulo);

		if (tarea.getEmpleadoAsignado() == null) {
			throw new IllegalStateException("La tarea no tiene empleado asignado previamente");
		}

		Empleado nuevoEmpleado = obtenerEmpleado(legajo);
		if (nuevoEmpleado == null || !nuevoEmpleado.estaDisponible()) {
			throw new IllegalStateException("El empleado no existe o no está disponible");
		}

		tarea.liberarEmpleado();
		tarea.asignarEmpleado(nuevoEmpleado);
	}
	
	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoParaModificacion(numero);

		Tarea tarea = proyecto.obtenerTarea(titulo);

		if (tarea.getEmpleadoAsignado() == null) {
			throw new IllegalStateException("La tarea no tiene empleado asignado previamente");
		}

		Empleado nuevoEmpleado = buscarEmpleadoMenosRetrasos();
		if (nuevoEmpleado == null) {
			throw new IllegalStateException("No hay empleados disponibles");
		}
		if (!nuevoEmpleado.estaDisponible()) {
	        nuevoEmpleado.liberar();
	    }

		tarea.liberarEmpleado();
		tarea.asignarEmpleado(nuevoEmpleado);
	}
	
	// CONSULTAS
	@Override
	public double costoProyecto(Integer numero) {
		Proyecto proyecto = proyectos.get(numero);
		return proyecto != null ? proyecto.getCostoProyecto() : 0.0;
	}
	
	@Override
    public List<Tupla<Integer, String>> proyectosFinalizados() {
        return proyectos.values().stream()
                .filter(p -> p.getEstado().equals(Estado.finalizado))
                .map(p -> new Tupla<>(p.getNumeroProyecto(), p.getDireccion())).collect(Collectors.toList());
    }
	
	@Override
    public List<Tupla<Integer, String>> proyectosPendientes() {
        return proyectos.values().stream()
                .filter(p -> p.getEstado().equals(Estado.pendiente))
                .map(p -> new Tupla<>(p.getNumeroProyecto(), p.getDireccion()))
                .collect(Collectors.toList());
    }
	
	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {
		return proyectos.values().stream().filter(p -> p.getEstado().equals(Estado.activo))
				.map(p -> new Tupla<>(p.getNumeroProyecto(), p.getDireccion())).collect(Collectors.toList());
	}
	
	@Override
	public Object[] empleadosNoAsignados() {
	    return empleados.values().stream()
	            .filter(Empleado::estaDisponible)
	            .map(Empleado::getLegajo)  //solamente devuelve los legajos
	            .toArray();
	}
	
	@Override
    public boolean estaFinalizado(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        return proyecto != null && proyecto.estaFinalizado();
    }
	
	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {
		Empleado empleado = empleados.get(legajo);
		return empleado != null ? empleado.getRetrasosTotales() : 0;
	}
	
	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        return Collections.emptyList();
	    }
	    
	    return proyecto.obtenerEmpleadosAsignados().stream()
	    		.map(e -> new Tupla<>(e.getLegajo(), e.getNombre()))
	    		.collect(Collectors.toList());
	}
	
	// REQUERIMIENTOS NUEVOS
	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("Proyecto inexistente");
	    }
	    if (proyecto.estaFinalizado()) {
	        throw new IllegalArgumentException("No se pueden consultar tareas de un proyecto finalizado");
	    }

	    return proyecto.obtenerTareasNoAsignadas().toArray();
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		Proyecto proyecto = proyectos.get(numero);
		if (proyecto == null) {
			return new Object[0];
		}
		return proyecto.obtenerTodasLasTareas().toArray();
	}
	
	@Override
	public String consultarDomicilioProyecto(Integer numero) {
		Proyecto proyecto = proyectos.get(numero);
		return proyecto != null ? proyecto.getDireccion() : null;
	}
	
	@Override
	public boolean tieneRestrasos(Integer legajo) {
		Empleado empleado = empleados.get(legajo);
		return empleado != null && empleado.getRetrasosTotales() > 0;
	}
	
	@Override
    public List<Tupla<Integer, String>> empleados() {
        return empleados.values().stream()
                .map(e -> new Tupla<>(e.getLegajo(), e.getNombre()))
                .collect(Collectors.toList());
    }
	
	@Override
    public String consultarProyecto(Integer numero) {
        Proyecto proyecto = proyectos.get(numero);
        return proyecto != null ? proyecto.toString() : "Proyecto no encontrado";
    }
	
	// METODOS AUXILIARES
	
	private Proyecto obtenerProyectoValido(Integer numero) {
		if (numero == null || numero <= 0) {
			throw new IllegalArgumentException("Número de proyecto inválido");
		}
		Proyecto proyecto = proyectos.get(numero);
		if (proyecto == null) {
			throw new IllegalArgumentException("Proyecto no encontrado");
		}
		return proyecto;
	}
	
//	private void validarProyectoNoFinalizado(Proyecto proyecto) {
//		if (proyecto.getEstado().equals(Estado.finalizado)) {
//			throw new IllegalArgumentException("El proyecto está finalizado");
//		}
//	}
	private void validarProyectoParaModificacion(Integer numero) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		proyecto.validarQuePuedeModificarse();
	}

    private Tarea obtenerTarea(Proyecto proyecto, String titulo) {
        return proyecto.obtenerTarea(titulo);
    }
    
	private void validarTareaNoAsignada(Tarea tarea) {
		if (tarea.getEmpleadoAsignado() != null) {
			throw new IllegalArgumentException("La tarea ya tiene un empleado asignado");
		}
	}
	
	private Empleado obtenerEmpleado(Integer legajo) {
		return empleados.get(legajo);
	}
	
	private Empleado buscarEmpleadoDisponible() {
		return empleados.values().stream().filter(Empleado::estaDisponible).findFirst().orElse(null);
	}
	
	private Empleado buscarEmpleadoMenosRetrasos() {
	    return empleados.values().stream()
	            .filter(Empleado::estaDisponible)
	            .min(Comparator.comparingInt(Empleado::getRetrasosTotales))
	            .orElse(null);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Home Solution ---\n");
		if(proyectos.isEmpty()) {
			sb.append("No hay proyectos registrados.\n");
		} else {
			for(Proyecto p : proyectos.values()) {
				sb.append("\n----------------------------\n");
				sb.append(p.toString()).append("\n");
			}
		}
		
		sb.append("\n--- Empleados Registrados ---\n ");
		if(empleados.isEmpty()) {
			sb.append("No hay empleados registrados.\n");
		} else {
			empleados.values().forEach(e -> sb.append(e.toString()).append("\n"));
		}
		
		return sb.toString();
		
	}
}
