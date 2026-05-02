package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller principal — recibe todas las peticiones HTTP y las gestiona.
// @RestController indica a Spring que esta clase maneja endpoints REST.
// @CrossOrigin permite peticiones desde el cliente web aunque esté en otro puerto.
// JdbcTemplate es inyectado por Spring automáticamente desde application.properties.

@RestController
@CrossOrigin
public class RentingController {

	private final JdbcTemplate jdbcTemplate;

	public RentingController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/crear")
	public String crearTablas() {

		// Tabla de vehiculos.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS vehiculos ("
				+ "idVehiculo INT AUTO_INCREMENT PRIMARY KEY,"
				+ "marca VARCHAR(255) NOT NULL,"
				+ "modelo VARCHAR(255) NOT NULL,"
				+ "matricula VARCHAR(7),"
				+ "tipoVehiculo ENUM('Pequeño', 'Mediano', 'Grande', 'Todo-terreno', 'Lujo', 'Mono-volumen', 'Furgoneta'),"
				+ "precioDia DECIMAL(7,2) NOT NULL,"
				+ "disponible BOOLEAN DEFAULT TRUE)");

		// Tabla clientes.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clientes ("
				+ "idCliente INT AUTO_INCREMENT PRIMARY KEY,"
				+ "nombre VARCHAR(50),"
				+ "apellido VARCHAR(50),"
				+ "email VARCHAR(50) UNIQUE NOT NULL,"
				+ "telefono VARCHAR(9) UNIQUE NOT NULL,"
				+ "DNI VARCHAR(9) UNIQUE NOT NULL)");

		// Tabla alquileres.
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS alquileres ("
				+ "idAlquiler INT AUTO_INCREMENT PRIMARY KEY,"
				+ "idCliente INT NOT NULL,"
				+ "idVehiculo INT NOT NULL,"
				+ "fechaInicio VARCHAR(10) NOT NULL,"
				+ "fechaDevolucion VARCHAR(10) NOT NULL,"
				+ "estado BOOLEAN DEFAULT TRUE,"
				+ "costeTotal DOUBLE,"
				+ "FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),"
				+ "FOREIGN KEY (idVehiculo) REFERENCES vehiculos(idVehiculo))");

		return "TABLAS CREADAS";
	}

	// VEHICULOS

	// Listar todos los vehiculos.
	@GetMapping("/vehiculos")
	public List<Vehiculo> listarVehiculos() {
		return jdbcTemplate.query("SELECT * FROM vehiculos", new RowMapperVehiculo());
	}

	// Listar vehiculos disponibles.
	@GetMapping("/vehiculos/disponibles")
	public List<Vehiculo> listarVehiculosDisponibles() {
		return jdbcTemplate.query("SELECT * FROM vehiculos WHERE disponible = TRUE", new RowMapperVehiculo());
	}

	// Buscar vehiculo por ID.
	@GetMapping("/vehiculos/{id}")
	public Vehiculo buscarVehiculo(@PathVariable long id) {
		List<Vehiculo> lista = jdbcTemplate.query("SELECT * FROM vehiculos WHERE idVehiculo = ?",
				new RowMapperVehiculo(), id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Añadir vehiculo.
	@GetMapping("/vehiculos/aniadir")
	public String aniadirVehiculo(@RequestParam String marca, @RequestParam String modelo,
			@RequestParam String matricula, @RequestParam String tipo, @RequestParam double precio) {
		int filas = jdbcTemplate.update(
				"INSERT INTO vehiculos (marca, modelo, matricula, tipoVehiculo, precioDia, disponible) VALUES (?,?,?,?,?,TRUE)",
				marca, modelo, matricula, tipo, precio);
		return filas > 0 ? "Vehiculo añadido" : "Error al añadir el vehiculo";
	}

	// Borrar vehiculo.
	@GetMapping("/vehiculos/delete/{id}")
	public String deleteVehiculo(@PathVariable long id) {
		List<AlquilerVehiculo> activos = jdbcTemplate.query(
				"SELECT * FROM alquileres WHERE idVehiculo = ? AND estado = TRUE", new RowMapperRentig(), id);
		if (!activos.isEmpty()) {
			return "No se puede eliminar: el vehiculo tiene un alquiler activo";
		}
		int filas = jdbcTemplate.update("DELETE FROM vehiculos WHERE idVehiculo = ?", id);
		return filas > 0 ? "Vehiculo eliminado" : "Vehiculo no encontrado";
	}

	// CLIENTES

	// Listar clientes.
	@GetMapping("/clientes")
	public List<Cliente> listarClientes() {
		return jdbcTemplate.query("SELECT * FROM clientes", new RowMapperCliente());
	}

	// Buscar cliente por ID.
	@GetMapping("/clientes/{id}")
	public Cliente buscarCliente(@PathVariable long id) {
		List<Cliente> lista = jdbcTemplate.query("SELECT * FROM clientes WHERE idCliente = ?",
				new RowMapperCliente(), id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Buscar cliente por DNI — usado por la web para que el usuario se identifique.
	@GetMapping("/clientes/dni/{dni}")
	public Cliente buscarClientePorDni(@PathVariable String dni) {
		List<Cliente> lista = jdbcTemplate.query("SELECT * FROM clientes WHERE DNI = ?",
				new RowMapperCliente(), dni);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Añadir cliente.
	@GetMapping("/clientes/anhadir")
	public String insertarCliente(@RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String email, @RequestParam String telefono, @RequestParam String DNI) {
		int filas = jdbcTemplate.update(
				"INSERT INTO clientes (nombre, apellido, email, telefono, DNI) VALUES (?,?,?,?,?)",
				nombre, apellido, email, telefono, DNI);
		return filas > 0 ? "Cliente añadido" : "Error al añadir cliente";
	}

	// Borrar cliente.
	@GetMapping("/clientes/delete/{id}")
	public String deleteCliente(@PathVariable long id) {
		List<AlquilerVehiculo> activos = jdbcTemplate.query(
				"SELECT * FROM alquileres WHERE idCliente = ? AND estado = TRUE", new RowMapperRentig(), id);
		if (!activos.isEmpty()) {
			return "No se puede eliminar: el cliente tiene un alquiler activo";
		}
		int filas = jdbcTemplate.update("DELETE FROM clientes WHERE idCliente = ?", id);
		return filas > 0 ? "Cliente eliminado" : "Cliente no encontrado";
	}

	// ALQUILERES

	// Listar todos los alquileres.
	@GetMapping("/alquileres")
	public List<AlquilerVehiculo> listarAlquileres() {
		return jdbcTemplate.query("SELECT * FROM alquileres", new RowMapperRentig());
	}

	// Listar solo los alquileres activos.
	@GetMapping("/alquileres/activos")
	public List<AlquilerVehiculo> listarAlquileresActivos() {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE estado = TRUE", new RowMapperRentig());
	}

	// Buscar alquiler por ID.
	@GetMapping("/alquileres/{id}")
	public AlquilerVehiculo buscarAlquiler(@PathVariable long id) {
		List<AlquilerVehiculo> lista = jdbcTemplate.query("SELECT * FROM alquileres WHERE idAlquiler = ?",
				new RowMapperRentig(), id);
		return lista.isEmpty() ? null : lista.get(0);
	}

	// Crear un alquiler.
	@GetMapping("/alquileres/crear")
	public String crearAlquiler(@RequestParam long idCliente, @RequestParam long idVehiculo,
			@RequestParam String fechaInicio, @RequestParam String fechaDevolucion) {

		// 1. Comprobamos si existe el cliente.
		List<Cliente> clientes = jdbcTemplate.query("SELECT * FROM clientes WHERE idCliente = ?",
				new RowMapperCliente(), idCliente);
		if (clientes.isEmpty()) {
			return "No existe el cliente";
		}

		// 2. Comprobamos si existe el vehiculo.
		List<Vehiculo> vehiculos = jdbcTemplate.query("SELECT * FROM vehiculos WHERE idVehiculo = ?",
				new RowMapperVehiculo(), idVehiculo);
		if (vehiculos.isEmpty()) {
			return "No existe el vehiculo";
		}

		// 3. Comprobamos si el vehiculo esta disponible.
		Vehiculo vehiculo = vehiculos.get(0);
		if (!vehiculo.isDisponible()) {
			return "El vehiculo no esta disponible";
		}

		// 4. Insertamos el alquiler.
		int filasAlquiler = jdbcTemplate.update(
				"INSERT INTO alquileres (idCliente, idVehiculo, fechaInicio, fechaDevolucion, estado, costeTotal) VALUES (?,?,?,?,TRUE,0)",
				idCliente, idVehiculo, fechaInicio, fechaDevolucion);

		// 5. Marcamos el vehiculo como no disponible.
		int filasVehiculo = jdbcTemplate.update(
				"UPDATE vehiculos SET disponible = FALSE WHERE idVehiculo = ?", idVehiculo);

		if (filasAlquiler > 0 && filasVehiculo > 0) {
			return "Alquiler creado correctamente";
		} else {
			return "Error al crear el alquiler";
		}
	}

	// Devolver un vehiculo.
	@GetMapping("/alquileres/devolver/{id}")
	public String devolverVehiculo(@PathVariable long id, @RequestParam double costeTotal) {

		// 1. Buscamos el alquiler.
		List<AlquilerVehiculo> alquileres = jdbcTemplate.query("SELECT * FROM alquileres WHERE idAlquiler = ?",
				new RowMapperRentig(), id);
		if (alquileres.isEmpty()) {
			return "No existe el alquiler";
		}

		AlquilerVehiculo alquiler = alquileres.get(0);

		// 2. Comprobamos que siga activo.
		if (!alquiler.isEstado()) {
			return "El alquiler ya estaba cerrado";
		}

		// 3. Cerramos el alquiler y guardamos el coste total.
		int filasAlquiler = jdbcTemplate.update(
				"UPDATE alquileres SET estado = FALSE, costeTotal = ? WHERE idAlquiler = ?", costeTotal, id);

		// 4. Volvemos a poner el vehiculo como disponible.
		int filasVehiculo = jdbcTemplate.update(
				"UPDATE vehiculos SET disponible = TRUE WHERE idVehiculo = ?", alquiler.getIdVehiculo());

		if (filasAlquiler > 0 && filasVehiculo > 0) {
			return "Vehiculo devuelto correctamente";
		} else {
			return "Error al devolver el vehiculo";
		}
	}

	// Ver alquileres de un cliente.
	@GetMapping("/clientes/{id}/alquileres")
	public List<AlquilerVehiculo> alquileresPorCliente(@PathVariable long id) {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE idCliente = ?", new RowMapperRentig(), id);
	}

	// Ver alquileres de un vehiculo.
	@GetMapping("/vehiculos/{id}/alquileres")
	public List<AlquilerVehiculo> alquileresPorVehiculo(@PathVariable long id) {
		return jdbcTemplate.query("SELECT * FROM alquileres WHERE idVehiculo = ?", new RowMapperRentig(), id);
	}
}