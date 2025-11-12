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

# Gestión de proyectos

## registrarProyecto()
Este método crea un nuevo proyecto con todas sus tareas y datos
* Proyecto: Crea una nueva instancia
* Cliente: Crea un objeto Cliente con los datos proporcionados
* Tarea: Crea múltiples tareas a partir de los arrays
* LocalDate: Parsea las fechas de inicio y fin
* Map<String, Tarea> tareasMap: Crea un mapa interno de tareas

### Flujo:
```java
HomeSolution.registrarProyecto()
  validarDatosProyecto()
  parsearFecha(inicio), parsearFecha(fin)
  new Cliente(cliente[0], cliente[2], cliente[1])
  for --> new Tarea() para cada tarea
  new Proyecto(contadorProyectos, clienteObj, direccion, fecchaIniciao, tareasMap)
  proyectos.put(contadorProyectos, proyecto)
  contadorProyectos++
```

## validarDatosProyecto() --> Método privado
Esto va a validar los datos del proyecto antes de crearlo
* Validaciones de arrays: verifica que títulos, descripción y dias tengan misma longitud
* Validaciones de negocio: dias > 0, domicilio no vació, cliente completo

## parsearFecha(String feccha) --> Método privado
Este convierte strings a objetos LocalDate
* LocalDate: Clase de Java Time API
* DateTimeFormatter: es para el formato "yyy-MM-dd"

## finalizarProyecto(Integer numero, String fin)
Va a marcar un proyecto como finalizado
* Proyecto.marcarComoFinalizado(): delega la lógica al proyecto
* Proyecto.getEstado(): verifica si ya está finalizado
* parsearFecha(fin): convierte la fecha de finalización

### Flujo:
```java
HomeSolution.finalizarProyecto()
  obtenerProyectoValido()
  Proyecto.getEstado() --> veriica si no está finalizado
  parsearFecha(fin)
  Proyecto.marcarComoFinalizado()
```

## estaFinalizado(Integer numero)
Esto va a consultar si un proyecto está finalizado
* Proyecto.getEstado(): obtine el estado del proyecto
* Estado.finalizado: Constante que define el estado "FINALIZADO"

## consultarProyecto(Integer numero)
Este método va aobtener la representación en string del proyecto
* Proyecto.toString(): Delega la representación al proyecto

## consultarDomicilioProyecto(Integer numero)
Obtiene la dirección de un proyecto
* Proyecto.getDireccion(): método getter del proyecto
