package entidades;

public class EmpleadoPlanta extends Empleado {
	private double valorDia;
	private String categoria;
	
	public EmpleadoPlanta(int legajo, String nombre, double valorDia, String categoria) {
		super(legajo, nombre);
		
		if(valorDia <= 0) throw new IllegalArgumentException("El valor del día es mayor a 0");
		if(!categoriaValida(categoria)) throw new IllegalArgumentException("Categoría inválida, debe ser INICIAL, TECNICO o EXPERTO");
		this.valorDia = valorDia;
		this.categoria = categoria.toUpperCase();
	}
	
	private boolean categoriaValida(String c) {
		if(c == null) return false;
		String cate = c.toUpperCase();
		return cate.equals("INICIAL") || cate.equals("TECNICO") || cate.equals("EXPERTO");
	}
	
	public double getValorDia() {
		return valorDia;
	}
	public String getCategoria() {
		return categoria;
	}
	
	@Override
	public double calcularSueldo() {
		int diasTrabajados;
		switch(categoria) {
			case "INICIAL": diasTrabajados = 20; break;
			case "TECNICO": diasTrabajados = 25; break;
			case "EXPERTO": diasTrabajados = 30; break;
			default: diasTrabajados = 20;
		}
		return valorDia * diasTrabajados;
	}
	
	@Override
	public String toString() {
		return super.toString() + " [Planta - Valor día="+ valorDia + ", Categoría=" + categoria + "]";
	}
}
