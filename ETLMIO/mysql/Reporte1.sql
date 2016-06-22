SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion
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











SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '140128') AND (dim_tiempo.tiempo_bruto = '4-5') AND (dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN');

SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion 
	FROM frecuencias 
		INNER JOIN dim_fecha 
			ON frecuencias.fk_fecha = dim_fecha.id_fecha 
		INNER JOIN dim_tiempo 
			ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo 
		INNER JOIN dim_ruta_estacion 
			ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion 
	WHERE 
		(dim_fecha.fecha_bruta >= '140616' AND dim_fecha.fecha_bruta <= '140626') 
        AND 
        (dim_tiempo.tiempo_bruto = '4-5') 
        AND 
        (dim_ruta_estacion.nombre_ruta_estacion = 'ATANASIO' OR dim_ruta_estacion.nombre_ruta_estacion = 'P74_1' OR dim_ruta_estacion.nombre_ruta_estacion = 'T50_2' OR dim_ruta_estacion.nombre_ruta_estacion = 'A14B_1' OR dim_ruta_estacion.nombre_ruta_estacion = 'T40_2' OR dim_ruta_estacion.nombre_ruta_estacion = 'A17C_1');


SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '140128') AND (dim_tiempo.tiempo_bruto = '13-14') AND (dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN'OR dim_ruta_estacion.nombre_ruta_estacion = 'A42A_2'OR dim_ruta_estacion.nombre_ruta_estacion = 'A70'OR dim_ruta_estacion.nombre_ruta_estacion = 'P12A_1'OR dim_ruta_estacion.nombre_ruta_estacion = 'P30A_2');


SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '140128') AND (dim_tiempo.tiempo_bruto = '13-14') AND (dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN'OR dim_ruta_estacion.nombre_ruta_estacion = 'A01A');


SELECT cant_pasajeros, dim_fecha.fecha_bruta, dim_fecha.es_festivo, dim_fecha.es_feriado, dim_tiempo.tiempo_bruto, dim_ruta_estacion.nombre_ruta_estacion FROM frecuencias INNER JOIN dim_fecha ON frecuencias.fk_fecha = dim_fecha.id_fecha INNER JOIN dim_tiempo ON frecuencias.fk_tiempo = dim_tiempo.id_tiempo INNER JOIN dim_ruta_estacion ON frecuencias.fk_ruta_estacion = dim_ruta_estacion.id_ruta_estacion WHERE (dim_fecha.fecha_bruta >= '140114' AND dim_fecha.fecha_bruta <= '140128') AND (dim_tiempo.tiempo_bruto = 'N-N') AND (dim_ruta_estacion.nombre_ruta_estacion = '7 AGOSTO' OR dim_ruta_estacion.nombre_ruta_estacion = 'A.SANIN'OR dim_ruta_estacion.nombre_ruta_estacion = 'A01A');