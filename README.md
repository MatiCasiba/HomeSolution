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

# Gestión de tareas

## asignarResponsableEnTarea(Integer numero, String titulo)
Asigna un empleado disponible a una tarea
* Proyecto: obtiene el proyecto específico
* Tarea: Obtiene la tarea específica dentro del proyecto
* Empleado: Busca un empleado disponible y lo asigna
* Estado: Cambia el estado del proyecto a ACTIVO si es necesario

### Flujo
```java
HomeSolution.asignarResponsableEnTarea()
  obtenerProyectoValido()
  validarProyectoNoFinalizado()
  obtenerTarea(proyecto, titulo)
  validarTareaNoAsignada()
  buscarEmpleoDisponible()
  Tarea.asignaarEmpleado(empleado)
  Empleado.asignar() --> cambia estado a no disponioble
  Proyecto.marcarComoEnCurso()
```

## asignarResponsableMenosRetraso(Integer numero, Stgring titulo)
Esto asigna el empleado con menos retrasos a una tarea
* Empleado.getRetrasosTotales(): para comparar retrasos entre empleados
* Comparator.comparingInt(): ordena empleados por menor retraso
* Empleado.liberar(): libera al empleado si no está disponible

## registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias)
Este método va a registrar días de retraso en una tarea
* Tarea.setDiasRetraso(): Establece los días de retraso en la tarea
* Empleado.registrarRetrasos(): registra los restrasos del empleado asignado
* Proyecto.numeroProyecto: pasa el número de proyecto para registro

## agregarTareaEnProyecto(Integer numero, String titulo, String descripción, double dias)
Agrega una nueva tarea a un proyecto existente
* Tarea: Crea una nueva instancia de tarea
* Proyecto.agregarTarea(): delega la adición al proyceto
* Proyecto.getTareas(): El proyecto maneja su propio mapa de tareas

## finalizarTarea(Integer numero, String titulo)
Marca una tarea específica como terminada
* Tarea.estaTermninada(): Vertifica si ya está finaliada
* Proyecto.marcarTareaTerminada(): delega la finalización al proyecto
* Tarea.marcarComoTerminada(): La tarea maneja su propia lógica de finalización

## tareasDeUnProyecto(Integer numero)
Obtiene todas las tareas de un proyecto
* Poryecto.getTareas().values(): obtiene la colección de tareas
* toArray(): convierte a array para retornar

## tareasProyectoNoAsignadas(Integer numero)
Obtiene tareas sin empelado asignado
* Tarea.getEmpleadosAsignado(): Filtra tareas sin asignar (null)
* Proyecto.getEstado(): valida que el proyecto no esté finalizado

# Consultas y reportes

## costoProyecto(Integer numero)
Esto calcula el costo total de un proyecto
* Proyecto.calcularCostoTareas(): delega el cálculo al proyecto
* Lógica diferenciada: Maneja proyectos finalizados vs activos/pendientes

Su comportamiento según estado:
* Proyectos ACTIVOS/PENDIENTES: calcula costo basado en tareas actuales
* Proyectos FINALIZADOS: calcula costo basado en historial de empleados

## proyectosFinalizados():
lista todos los proyectos finalizados
* Proyecto.getEstado(): filtra por Estado.finalizado
* Tupla<Integer, String>: retorna (numero_proyecto, direccion)
* Stream API: Filtra y transforma la colección

## proyectosPendientes()
Esto lista proyectos en estado pendiente
* Estado.pendiente: Constante para filtrar
* Proyecto.getDireccion(): Obtiene la dirección para la tupla

## proyectosActivos()
Este método lista proyectos en ejecución
* Estado.activo: constante para filtrar
* Mismo patrón: similar a proyectosFinalizados() pero con diferente filtro

## empleadosAsignadosAProyecto(Integer numero)
Este obtiene todos los empleados que han trabajado en un proyecto
* Proyecto.obtenerHistorialEmpleados(): empleados de proyectos finalizados
* Tarea.getEmpleadoAsignado(): empleados actualmente asignados
* Set<Empleado>: combian y elimina duplicados
* Empleado.getLegajo(), Empleado.getNombre(): datos para la tupla

### Fujo
```java
empleados_historial(proyectos finalizados)
  + empleados_ actuales (tarea asignadas)
  Set<Empleado> (sin duplicados)
  List<Tupla<legajo, nombre>>
```

## empleadosNoAsignados()
Me da los empleados disponibles (sin tareas asignadas)
* Empleados.estaDisponible(): filtra por estado del empleado
* Retorna solo legajos: Object[] con números de legajo

## consultarCantidadRetrasosEmpleado(Integer legajo)
Consulta retrasos totales de un empleado
* Empleado.getRetrasosTotales(): Obtiene el contador acumulado
* Map<Integer, Empleado> empleados: busca por legajo

## tieneRetrasos(Integer legajo)
Esto verifica si un empleado tiene retrasos registrados
* Empleado-getRetrasosTotales() > 0: condición booleana simple
