-- phpMyAdmin SQL Dump
-- version 2.10.3
-- http://www.phpmyadmin.net
-- 
-- Servidor: localhost
-- Tiempo de generación: 02-06-2016 a las 21:15:47
-- Versión del servidor: 5.1.73
-- Versión de PHP: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


CREATE DATABASE  IF NOT EXISTS DB_MIO;
USE DB_MIO;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Base de datos: `DB_mio`
-- 

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `abordaje`
-- 

CREATE TABLE `abordaje` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `origen` varchar(30) NOT NULL,
  `es_estacion` int(1) NOT NULL,
  `fecha` varchar(6) NOT NULL,
  `franja` varchar(10) NOT NULL,
  `numero_pasajeros` int(11) NOT NULL,
  `destino` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fecha` (`fecha`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- 
-- Volcar la base de datos para la tabla `abordaje`
-- 

