package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

// RowMapper que convierte cada fila de la tabla "vehiculos" en un objeto Vehiculo.
// Cuando se hace un jdbcTemplate.query(), MariaDB devuelve un ResultSet
// que es basicamente una tabla de filas y columnas con los resultados.
// Spring llama a mapRow() una vez por cada fila que devuelve la query.

public class RowMapperVehiculo implements RowMapper<Vehiculo> {

	// mapRow recibe la fila actual (rs) y el numero de fila (rowNum).
	// rs.getLong("idVehiculo") coge el valor de la columna "idVehiculo" de esa
	// fila.
	// Los nombres entre comillas tienen que coincidir EXACTAMENTE con las columnas
	// de la BD.
	// Devuelve un objeto Vehiculo construido con todos los datos de esa fila.

	public Vehiculo mapRow(ResultSet rs, int rowNum) throws SQLException {

		return new Vehiculo(rs.getLong("idVehiculo"), rs.getString("marca"), rs.getString("modelo"),
				rs.getString("matricula"), rs.getString("tipoVehiculo"), rs.getDouble("precioDia"),
				rs.getBoolean("disponible"));
	}
}