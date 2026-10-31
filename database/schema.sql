
-- ISY2202 - Semana 2
-- Estructura de la base de datos

CREATE DATABASE IF NOT EXISTS recetas_db
CHARACTER SET utf8mb4;

USE recetas_db;

CREATE TABLE `recetas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `tipo_cocina` varchar(100) DEFAULT NULL,
  `pais_origen` varchar(100) DEFAULT NULL,
  `ingredientes` text NOT NULL,
  `instrucciones` text NOT NULL,
  `tiempo_coccion_min` int NOT NULL,
  `dificultad` varchar(30) NOT NULL,
  `fotografia` varchar(500) DEFAULT NULL,
  `visitas` int NOT NULL DEFAULT '0',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- recetas_db.usuarios definition

CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `rol` varchar(30) NOT NULL DEFAULT 'ROLE_USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;