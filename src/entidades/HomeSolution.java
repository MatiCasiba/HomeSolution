package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

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

		// creo cliente
		Cliente clienteObj = new Cliente(cliente[0], cliente[2], cliente[1]);

		// creo tareas
		Map<String, Tarea> tareasMap = new HashMap<>();
		for (int i = 0; i < titulos.length; i++) {
			Tarea tarea = new Tarea(titulos[i], descripcion[i], (int) dias[i]);
			tareasMap.put(tarea.getClave(), tarea);
		}

		// parseo fechas
		LocalDate fechaInicio = parsearFecha(inicio);
		LocalDate fechaFin = parsearFecha(fin);

		// creao proyecto
		Proyecto proyecto = new Proyecto(contadorProyectos, clienteObj, domicilio, fechaInicio, tareasMap);
		proyectos.put(contadorProyectos, proyecto);
		contadorProyectos++;
	}
	
	private void validarDatosProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio,
			String[] cliente, String inicio, String fin) {
		if (titulos == null || descripcion == null || dias == null || titulos.length == 0
				|| titulos.length != descripcion.length || titulos.length != dias.length) {
			throw new IllegalArgumentException("Los arrays de tareas deben tener la misma longitud y no ser vacíos");
		}
		if (domicilio == null || domicilio.isBlank()) {
			throw new IllegalArgumentException("El domicilio no puede estar vacío");
		}
		if (cliente == null || cliente.length < 3) {
			throw new IllegalArgumentException("El cliente debe tener nombre, email y teléfono");
		}
		if (inicio == null || fin == null) {
			throw new IllegalArgumentException("Las fechas no pueden ser nulas");
		}
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
		validarProyectoNoFinalizado(proyecto);

		Tarea tarea = obtenerTarea(proyecto, titulo);
		validarTareaNoAsignada(tarea);

		Empleado empleado = buscarEmpleadoDisponible();
		if (empleado == null) {
			throw new IllegalStateException("No hay empleados disponibles para asignar");
		}
		tarea.asignarEmpleado(empleado);
		proyecto.marcarComoEnCurso();
	}
	
	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoNoFinalizado(proyecto);

		Tarea tarea = obtenerTarea(proyecto, titulo);
		validarTareaNoAsignada(tarea);

		Empleado empleado = buscarEmpleadoMenosRetrasos();
		if (empleado == null) {
			throw new IllegalStateException("No hay empleados disponibles para asignar");
		}

		tarea.asignarEmpleado(empleado);
		proyecto.marcarComoEnCurso();
	}
	
	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoNoFinalizado(proyecto);

		Tarea tarea = obtenerTarea(proyecto, titulo);

		if (cantidadDias < 0) {
			throw new IllegalArgumentException("Los días de retraso no pueden ser negativos");
		}

		tarea.setDiasRetraso((int) cantidadDias);

		//registra retraso en el empleado si está asignado
		if (tarea.getEmpleadoAsignado() != null) {
			tarea.getEmpleadoAsignado().registrarRetrasos(numero, (int) cantidadDias);
		}
	}
	
	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoNoFinalizado(proyecto);
		if (dias <= 0) {
			throw new IllegalArgumentException("Los días deben ser mayores a 0");
		}

		Tarea nuevaTarea = new Tarea(titulo, descripcion, (int) dias);
		proyecto.agregarTarea(nuevaTarea);
	}
	
	@Override
	public void finalizarTarea(Integer numero, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoNoFinalizado(proyecto);
		Tarea tarea = obtenerTarea(proyecto, titulo);
		if (tarea.estaTerminada()) {
			throw new IllegalStateException("La tarea ya está finalizada");
		}
		proyecto.marcarTareaTerminada(titulo);
	}
	
	@Override
	public void finalizarProyecto(Integer numero, String fin) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		if (proyecto.getEstado().equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto ya está finalizado");
		}

		LocalDate fechaFin = parsearFecha(fin);
		if (fechaFin.isBefore(proyecto.getFechaInicio())) {
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
		}
		proyecto.marcarComoFinalizado();
	}
	
	// REASIGNACIÓN DE EMPLEADOS
	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) {
		Proyecto proyecto = obtenerProyectoValido(numero);
		validarProyectoNoFinalizado(proyecto);

		Tarea tarea = obtenerTarea(proyecto, titulo);

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
}
