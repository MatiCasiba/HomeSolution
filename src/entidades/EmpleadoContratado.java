package entidades;

public class EmpleadoContratado extends Empleado {
	private double valorHora;
	
	public EmpleadoContratado(int legajo, String nombre, double valorHora) {
		super(legajo, nombre);
		if(valorHora <= 0) throw new IllegalArgumentException("El valor por hora debe ser mayor a 0");
		this.valorHora = valorHora;
	}
}
