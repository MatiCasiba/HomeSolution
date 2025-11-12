# Gestión de empleados

## registrarEmpleado(String nombre, double valor)
Este método registra un empleado contratado por horas
* EmpleadoContratado: Crea una instancia de esta clase
* Map<Integer, Empleado> empleados: Almacena el em,pleado en el mapa usando el legajo como clave
* generarLegajo(): Método auxiliar que generaa el legajo automáticamente

### Flujo:
```java
HomeSolution.registrarEmpleado()
  generarLegajo()
  new EmpleadoContratado()
  empleado.put(legajo, empleado)
```

## registrarEmpleado(String nombre, double valor, String nombre, double valor, String categoria)
Registrar un empleado de planta con categoría, se relaciona con laas clases EmpleadoPlanta y Empleado.
* EmpleadoPlanta: Crea una instancia de esta clase
* Empleado: Clase abstracta padre de ambos tipos de empleado
* Validación de categoría: Solo permite "INICIAL", "TECNICO" o "EXPERTO"

### Flujo:
```java
HomeSolution.RegistrarEmpleado()
  generarLegajo()
  new EmpleadoPlanta()
  empleados.put(legajo, empleado)
```

## generarLegajo() --> Método privado
Genera legajos autoincrementales.
* empleados.keySet(): obtiene los legajos existentes
* Stream API: Encuentra el máximo legajo existente

## empleados()
Lista todods los empleados registrados
* Tupla<Integer, String>: retorna una lista de tuplas (legajo, nombre)
* Stream API: Transforma el mapa de empleados a la lista de tuplas

## consultarCantidadRetrasosEmpleado(Integer legajo)
Este método va a consultar retrasos totales de un empleado
* Empleado.getRetrasosTotales(): llama al método del empleado
* Map<Integer, Empleado> empleados: buscca el empleado por legajo

## tieneRetrasos(Integer legajo)
Va a verificar si un empleado tiene retrasos
* Empleado.getRetrasosTotales(): consulta los retrasos acumulados

## empleadosNoAsignados()
Este obtiene empleados disponibles (los no asignados a tareas)
* Empleado.estaDisponible(): filtra por estado de disponibilidada
* Retorna solo los legajos (no los objetos completos)
