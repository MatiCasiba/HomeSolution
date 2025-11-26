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
		if(!email.contains("@") || !email.contains(".")){
			throw new IllegalArgumentException("El formato del mail no es válido");
		}
		
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
	}
	
	public static void validarDatosCliente(String[] cliente) {
		if(cliente == null || cliente.length < 3 ||
				cliente[0] == null || cliente[0].isBlank() ||
				cliente[1] == null || cliente[1].isBlank() ||
				cliente[2] == null || cliente[2].isBlank()) {
				throw new IllegalArgumentException("El cliente debe tener nombre, email y teléfono válidos");
		}
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
	public String getContacto() {
		return String.format("Cliente: %s | Tel: %s | Email: %s", nombre, telefono, email);
	}
}
