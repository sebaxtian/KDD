-- DIAS LABORALES
SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion
	FROM frecuencias
		INNER JOIN dim_fecha
			ON frecuencias.fk_fecha = dim_fecha.id_fecha
		INNER JOIN dim_tiempo
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo
		INNER JOIN dim_ruta_estacion
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion
	WHERE
		(dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '150901')
		AND
        (dim_fecha.nombre_dia != 'sábado' AND dim_fecha.nombre_dia != 'domingo' AND dim_fecha.es_festivo = false)
        AND
		(dim_tiempo.tiempo_bruto = '13-14')
		AND
		(dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN')

SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '150901') AND (dim_fecha.nombre_dia != 'sábado' AND dim_fecha.nombre_dia != 'domingo' AND dim_fecha.es_festivo = false) AND (dim_tiempo.tiempo_bruto = '13-14') AND (dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN');


-- FIN DE SEMANA
SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion
	FROM frecuencias
		INNER JOIN dim_fecha
			ON frecuencias.fk_fecha = dim_fecha.id_fecha
		INNER JOIN dim_tiempo
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo
		INNER JOIN dim_ruta_estacion
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion
	WHERE
		(dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '150901')
		AND
        (dim_fecha.nombre_dia = 'sábado' OR dim_fecha.nombre_dia = 'domingo')
        AND
		(dim_tiempo.tiempo_bruto = '8-9')
		AND
		(dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN')




-- DIA FESTIVO
SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion
	FROM frecuencias
		INNER JOIN dim_fecha
			ON frecuencias.fk_fecha = dim_fecha.id_fecha
		INNER JOIN dim_tiempo
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo
		INNER JOIN dim_ruta_estacion
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion
	WHERE
		(dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '150901')
		AND
		(dim_fecha.es_festivo = true)
        AND
		(dim_tiempo.tiempo_bruto = '8-9')
		AND
		(dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN')




-- DIA FERIADO
SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion
	FROM frecuencias
		INNER JOIN dim_fecha
			ON frecuencias.fk_fecha = dim_fecha.id_fecha
		INNER JOIN dim_tiempo
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo
		INNER JOIN dim_ruta_estacion
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion
	WHERE
		(dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '150901')
		AND
		(dim_fecha.es_feriado = true)
        AND
		(dim_tiempo.tiempo_bruto = '8-9')
		AND
		(dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN')


SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.nombre_dia, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140101' AND dim_fecha.fecha_bruta <= '151231') AND (dim_fecha.nombre_dia != 'sábado' AND dim_fecha.nombre_dia != 'domingo' AND dim_fecha.es_festivo = false) AND (dim_tiempo.tiempo_bruto = '7-8') AND (dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO');