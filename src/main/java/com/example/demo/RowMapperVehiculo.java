package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

//Convierte cada fila de la tabla vehiculos en un objeto Vehiculo.
//mapRow() se llama una vez por cada fila que devuelve la query.
//Los nombres entre comillas deben coincidir EXACTAMENTE con las columnas SQL.

public class RowMapperVehiculo implements RowMapper<Vehiculo> {

	public Vehiculo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return new Vehiculo(rs.getLong("idVehiculo"), rs.getString("marca"), rs.getString("modelo"),
				rs.getString("matricula"), rs.getString("tipoVehiculo"), rs.getDouble("precioDia"),
				rs.getBoolean("disponible"));
	}
}
