package entidades;

import java.time.LocalDate;
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
	
}
