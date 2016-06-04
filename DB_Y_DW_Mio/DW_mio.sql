-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Servidor: localhost
-- Tiempo de generación: 02-06-2016 a las 21:15:47
-- Versión del servidor: 5.1.73
-- Versión de PHP: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


CREATE DATABASE  IF NOT EXISTS DW_MIO;
USE DW_MIO;

-- 
-- Base de datos: `DW_mio`
-- 

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `dim_fecha`
-- 

CREATE TABLE `dim_fecha` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` varchar(10) NOT NULL,
  `anio` varchar(4) NOT NULL,
  `mes` varchar(2) NOT NULL,
  `mes_nombre` varchar(10) NOT NULL,
  `dia` varchar(2) NOT NULL,
  `dia_del_anio` varchar(3) NOT NULL,
  `nombre_dia` varchar(10) NOT NULL,
  `fin_de_semana` varchar(10) DEFAULT NULL,
  `numero_de_semana` int(11) NOT NULL,
  `trimestre` int(11) NOT NULL,
  `festivo` tinyint(10) DEFAULT NULL,
  `nombre_festivo` varchar(50) DEFAULT NULL,
  `semana_mes` varchar(1) NOT NULL,
  `numero_dia_semana` varchar(1) NOT NULL,
  `feriado` varchar(10) DEFAULT NULL,
  `nombre_feria` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fecha` (`fecha`),
  UNIQUE KEY `fecha_2` (`fecha`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `dim_fecha`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `dim_ruta_estacion`
-- 

CREATE TABLE `dim_ruta_estacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) NOT NULL,
  `es_ruta` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`),
  UNIQUE KEY `nombre_2` (`nombre`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `dim_ruta_estacion`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `dim_tiempo`
-- 

CREATE TABLE `dim_tiempo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `franja_horaria` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `franja_horaria` (`franja_horaria`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `dim_tiempo`
-- 


-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `frecuencia`
-- 

CREATE TABLE `frecuencia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_dim_fecha` varchar(6) NOT NULL,
  `fk_dim_tiempo` varchar(5) NOT NULL,
  `fk_dim_ruta_estacion_origen` varchar(30) NOT NULL,
  `ruta_estacion_destino` varchar(30) DEFAULT NULL,
  `numero_pasajeros` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_dim_fecha` (`fk_dim_fecha`,`fk_dim_tiempo`,`fk_dim_ruta_estacion_origen`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `frecuencia`
-- 

