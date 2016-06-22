SELECT SUM(cant_pasajeros) as total_pasajeros, dim_ruta_estacion.nombre_ruta_estacion
	FROM frecuencias
		INNER JOIN dim_fecha
			ON frecuencias.fk_fecha = dim_fecha.id_fecha
		INNER JOIN dim_tiempo
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo
		INNER JOIN dim_ruta_estacion
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion
	WHERE
		(dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '140128')
		AND
		(dim_tiempo.tiempo_bruto = '4-5')
		AND
		(dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN')
	GROUP BY dim_ruta_estacion.nombre_ruta_estacion
    ORDER BY total_pasajeros DESC