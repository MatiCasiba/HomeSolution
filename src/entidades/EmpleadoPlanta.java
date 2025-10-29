package entidades;

public class EmpleadoPlanta extends Empleado {
	private double valorDia;
	private String categoria;
	
	public EmpleadoPlanta(int legajo, String nombre, double valorDia, String categoria) {
		super(legajo, nombre);
		
		if(valorDia <= 0) throw new IllegalArgumentException("El valor del día es mayor a 0");
		
		this.valorDia = valorDia;
		this.categoria = categoria.toUpperCase();
	}
}
