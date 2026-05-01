package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

public class ClienteConsola {
	// Constantes para la clase ClienteConsola necesaria.
	static final String BASE = "http://localhost:8080";
	static HttpClient cliente = HttpClient.newHttpClient();
	static Scanner in = new Scanner(System.in);

	static String get(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
			HttpResponse<String> response = cliente.send(request, BodyHandlers.ofString());

			return response.body();
		} catch (Exception e) {
			return "ERROR DE CONEXION " + e.getMessage();
		}
	}

	public static void main(String[] args) {
		System.out.println("==== RENTING DE VEHICULOS ====");

		int opcion = -1;

		do {
			System.out.println("---- MENU ----");
			System.out.println("1. Gestión de vehiculos.");
			System.out.println("2. Gestión de clientes.");
			System.out.println("3. Gestión de alquileres.");
			System.out.println("0. Salir.");
			System.out.print("Elegir opcion: ");
			opcion = in.nextInt();
			in.nextLine();

			System.out.println();

			switch (opcion) {
			case 1:

				menuvehiculos();
				break;

			case 2:

				menuClientes();
				break;

			case 3:

				menuAlquileres();
				break;

			case 0:
				System.out.println("Saliendo...");
				break;

			default:
				System.err.println("Opcion no valida.");
				break;

			}
		} while (opcion != 0);
	}

	private static void menuvehiculos() {

		int opcion = 0;
		String regexTipos = "^(pequeño|mediano|grande|todo-terreno|lujo|mono-volumen|furgoneta)$";

		System.out.println("==== MENU VEHICULOS ====");
		System.out.println("1. Listar vehiculos.");
		System.out.println("2. Listar vehiculos disponibles.");
		System.out.println("3. Buscar vehiculos por ID.");
		System.out.println("4. Añadir vehiculo.");
		System.out.println("5. Borrar vehiculo.");
		System.out.print("Elige una opcion:");
		opcion = in.nextInt();
		in.nextLine();

		switch (opcion) {
		case 1:
			System.out.println(get(BASE + "/vehiculos"));
			break;

		case 2:
			System.out.println(get(BASE + "/vehiculos/disponibles"));
			break;

		case 3:
			long idBuscar = 0;
			boolean esValidoBuscar = false;

			System.out.print("ID del vehiculo a buscar: ");
			do {
				if (in.hasNextLong()) {
					idBuscar = in.nextLong();
					in.nextLine();
					if (idBuscar < 0) {
						System.err.println("ERROR INTRODUZCA UN ID MAYOR A 0.");
					} else {
						esValidoBuscar = true;
					}
				} else {
					System.err.println("INTRODUZCA UN VALOR VALIDO");
					in.nextLine();
				}
			} while (!esValidoBuscar);

			String resultado = get(BASE + "/vehiculos/" + idBuscar);

			if (resultado.isEmpty() || resultado.contains("404")) {
				System.err.println("VEHICULO NO ENCONTRADO");
			} else {
				System.out.println(resultado);
			}

			break;

		case 4:
			String marca, modelo, matricula, tipo;
			double precio = 0;
			boolean esValido = false;

			marca = leerCadena(in, "Marca: ").toLowerCase().trim();

			modelo = leerCadena(in, "Modelo: ").toLowerCase().trim();

			matricula = leerCadena(in, "Matricula: ").toLowerCase().trim();

			System.out.print("Tipo: ");
			do {

				tipo = in.nextLine().trim().toLowerCase();

				if (tipo.isEmpty()) {
					System.err.println("ERROR NO PUEDE ESTAR VACIO");
				} else if (!tipo.matches(regexTipos)) {
					System.err.println("ERROR: Tipo no válido. (Ej: pequeño, lujo, furgoneta...");
				}

			} while (!tipo.matches(regexTipos));

			System.out.print("Precio por dia: ");

			do {
				if (in.hasNextDouble()) {
					precio = in.nextDouble();
					in.nextLine();
					if (precio <= 0) {
						System.err.println("ERROR: EL PRECIO DEBE SER MAYOR A 0.");
					} else {
						esValido = true;
					}
				} else {
					System.err.println("ERROR: INTRODUZCA UN VALOR NUMÉRICO VÁLIDO.");
					in.nextLine();
				}
			} while (!esValido);
			
			String url = BASE + "/vehiculos/aniadir"
			        + "?marca=" + marca.trim()
			        + "&modelo=" + modelo.trim()
			        + "&matricula=" + matricula.trim()
			        + "&tipo=" + tipo.trim()
			        + "&precio=" + precio;

			System.out.println(get(url));
			break;
			
		case 5:
			long idBorrar = 0;
			boolean esValidoBorrar = false;

			System.out.print("ID del vehiculo a borrar: ");
			do {
				if (in.hasNextLong()) {
					idBorrar = in.nextLong();
					in.nextLine();
					if (idBorrar < 0) {
						System.err.println("ERROR INTRODUZCA UN ID MAYOR A 0.");
					} else {
						esValidoBorrar = true;
					}
				} else {
					System.err.println("INTRODUZCA UN VALOR VALIDO");
					in.nextLine();
				}
			} while (!esValidoBorrar);
			
			String resultadoBorrar = get(BASE + "/vehiculos/delete/" + idBorrar);

			if (resultadoBorrar.isEmpty() || resultadoBorrar.contains("404")) {
				System.err.println("VEHICULO NO ENCONTRADO");
			} else {
				System.out.println(resultadoBorrar);
			}
			break;
			
			default:
				System.err.println("Opcion no valida...");
		}
	}

	private static void menuClientes() {

	}

	private static void menuAlquileres() {

	}
	
	private static String leerCadena(Scanner in, String mensaje) {
		String entrada;

		do {

			System.out.print(mensaje);
			entrada = in.nextLine().trim();

			if (entrada.isEmpty()) {
				System.err.println("ERROR NO PUEDES TENER ESTE CAMPO VACIO");
			}

		} while (entrada.isEmpty());
		
		return entrada;
	}

}
