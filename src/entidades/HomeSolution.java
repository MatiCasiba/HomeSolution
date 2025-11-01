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
	
}
