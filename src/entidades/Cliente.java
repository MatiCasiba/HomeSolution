package entidades;

public class Cliente {
	private String nombre;
	private String telefono;
	private String email;
	
	public Cliente(String nombre, String telefono, String email) {
		
		if(nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if(telefono == null || telefono.isBlank()) {
			throw new IllegalArgumentException("El teléfono no puede estar vacío");
		}
		if(email == null || email.isBlank()) {
			throw new IllegalArgumentException("El email no puede estar vacío");
		}
		if(!email.contains("@") || !email.contains(".")) {
			throw new IllegalArgumentException("El email debe tener '@' y '.' ppara ser válido ");
		}
		
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
	}
	
	public String getNombre() {
		return nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public String getEmail() {
		return email;
	}
}
