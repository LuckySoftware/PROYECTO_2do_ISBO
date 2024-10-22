-- Creacion de la base de datos
CREATE DATABASE proyecto_db;

-- Uso de la base de datos
USE proyecto_db;


-- TABLA DE JUGADORES
CREATE TABLE jugador (
    idJugador INT AUTO_INCREMENT PRIMARY KEY,
    nombreCompleto VARCHAR(30),
    sexo VARCHAR(15),
    fechaNacimiento DATE,
    paisNacimiento VARCHAR(25),
    paisResidencia VARCHAR(25),
    correoElectronico VARCHAR(30),
    userPassword VARCHAR(30)
);

DESCRIBE jugador;
DROP TABLE jugador;
SELECT * FROM jugador;
INSERT INTO jugador (paisResidencia, paisNacimiento) VALUES ("Uruguay", "Uruguay");





-- TABLA DE PARTIDA
CREATE TABLE partida (
    idPartida INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    horaComienza TIME,
    horaFinalizacion TIME,
    estado VARCHAR(25)
);



-- TABLA DE JUEGO (PARTIDA - JUGADOR)
CREATE TABLE juega (
    idPartida INT,
    idJugador INT,
    rol ENUM('Activo', 'Esperando'),
    PRIMARY KEY(idPartida, idJugador),
    FOREIGN KEY(idPartida) REFERENCES partida(idPartida),
    FOREIGN KEY(idJugador) REFERENCES jugadores(idJugador)
);


-- TABLA DE RONDAS
CREATE TABLE ronda (
    idRonda INT AUTO_INCREMENT PRIMARY KEY,
    idPartida INT,
    respuestaUsuario VARCHAR(30),
    respuestaCorrecta VARCHAR(30),
    numeroRonda INT,
    puntosObtenidos INT,
    FOREIGN KEY(idPartida) REFERENCES partida(idPartida)
);



-- TABLA DE PREGUNTAS
CREATE TABLE pregunta (
    idPregunta INT,
    idCategoria INT,
    respuestaCorrecta VARCHAR(30),
    nivelDificultad VARCHAR(25),
    PRIMARY KEY(idPregunta),
    FOREIGN KEY(idCategoria) REFERENCES categoria(idCategoria)
);



-- TABLA DE OPCION DE RESPUESTAS
CREATE TABLE opcionRespuesta (
    idOpcion INT AUTO_INCREMENT PRIMARY KEY,
    idPregunta INT,
    textoOpcion VARCHAR(30),
    FOREIGN KEY(idPregunta) REFERENCES pregunta(idPregunta)
);



-- TABLA DE CATEGORIAS
CREATE TABLE categoria (
    idCategoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20),
    descripcion VARCHAR(40)
);



-- TABLA DE RANKINGS
CREATE TABLE ranking (
    idRanking INT AUTO_INCREMENT PRIMARY KEY,
    idJugador INT,
    idCategoria INT,
    posicion INT,
    puntosObtenidos INT,
	FOREIGN KEY(idJugador) REFERENCES jugadores(idJugador),
    FOREIGN KEY(idCategoria) REFERENCES categoria(idCategoria)
);