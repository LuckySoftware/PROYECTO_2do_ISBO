-- Creacion de la base de datos
CREATE DATABASE proyecto_db;
-- DROP DATABASE proyecto_db;
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

-- DESCRIBE jugador;
-- DROP TABLE jugador;
SELECT * FROM jugador;
INSERT INTO jugador (nombreCompleto, correoElectronico, userPassword) VALUES ("admin", "admin", "admin");

-- TABLA DE PARTIDA
CREATE TABLE partida (
    idPartida INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    horaComienza TIME,
    horaFinalizacion TIME,
    estado ENUM('En curso', 'Finalizada')
);

SELECT * FROM partida;

-- TABLA DE JUEGO (PARTIDA - JUGADOR)
CREATE TABLE juega (
    idPartida INT,
    idJugador INT,
    rol ENUM('Activo', 'Esperando'),
    PRIMARY KEY(idPartida, idJugador),
    FOREIGN KEY(idPartida) REFERENCES partida(idPartida),
    FOREIGN KEY(idJugador) REFERENCES jugador(idJugador)
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

-- TABLA DE CATEGORIAS
CREATE TABLE categoria (
    idCategoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20),
    descripcion VARCHAR(100)
);


-- drop table categoria;
-- TABLA DE PREGUNTAS



CREATE TABLE pregunta (
    idPregunta int AUTO_INCREMENT ,
    idCategoria INT,
    respuestaCorrecta VARCHAR(30),
    nivelDificultad VARCHAR(25),
    textoPregunta VARCHAR(255),
    PRIMARY KEY(idPregunta),
    FOREIGN KEY(idCategoria) REFERENCES categoria(idCategoria)
);
SELECT * FROM pregunta;


-- TABLA DE OPCION DE RESPUESTAS
CREATE TABLE opcionRespuesta (
    idOpcion INT AUTO_INCREMENT PRIMARY KEY,
    idPregunta INT,
    textoOpcion VARCHAR(30),
    esCorrecta BOOLEAN DEFAULT FALSE,
    FOREIGN KEY(idPregunta) REFERENCES pregunta(idPregunta)
);


-- TABLA DE RANKINGS
CREATE TABLE ranking (
    idRanking INT AUTO_INCREMENT PRIMARY KEY,
    idJugador INT,
    idCategoria INT,
    posicion INT,
    puntosObtenidos INT,
	FOREIGN KEY(idJugador) REFERENCES jugador(idJugador),
    FOREIGN KEY(idCategoria) REFERENCES categoria(idCategoria)
);
SELECT * FROM ranking;

-- UPDATE ranking SET puntosObtenidos = puntosObtenidos + 10 WHERE idJugador = 2;
-- INSERT INTO ranking (idJugador, puntosObtenidos) VALUES (2, 0);



-- Insertar categorías
INSERT INTO categoria (nombre, descripcion) VALUES 
('Geografía', 'Preguntas relacionadas con lugares y mapas'),
('Matemáticas', 'Preguntas de operaciones matemáticas básicas'),
('Ciencia', 'Preguntas de ciencias naturales'),
('Tecnología', 'Preguntas sobre programación y tecnología');

-- Insertar preguntas con respuestas correctas en la columna 'respuestaCorrecta'
INSERT INTO pregunta (idCategoria, textoPregunta, respuestaCorrecta, nivelDificultad) VALUES 
(1,  '¿Cuál es el continente más grande?' ,'Asia', 'Medio'),    
(2, '¿Cuánto es 10 + 10?' ,'20', 'Fácil'),      
(3, '¿Cuál es la unidad básica de la vida?','Célula', 'Fácil'),  
(4, '¿Qué lenguaje de programación se utiliza principalmente para aplicaciones Android?' ,'Java', 'Medio');

-- Insertar opciones de respuesta para cada pregunta (asumiendo que los IDs son conocidos)
-- Pregunta: ¿Cuál es el continente más grande?
INSERT INTO opcionRespuesta (idPregunta, textoOpcion) VALUES 
(1, 'África'),
(1, 'Europa'),
(1, 'América'),
(1, 'Asia');  -- Correcta

-- Pregunta: ¿Cuánto es 10 + 10?
INSERT INTO opcionRespuesta (idPregunta, textoOpcion) VALUES 
(2, '15'),
(2, '30'),
(2, '25'),
(2, '20');  -- Correcta

-- Pregunta: ¿Cuál es la unidad básica de la vida?
INSERT INTO opcionRespuesta (idPregunta, textoOpcion) VALUES 
(3, 'Órgano'),
(3, 'Tejido'),
(3, 'Átomo'),
(3, 'Célula');  -- Correcta

-- Pregunta: ¿Qué lenguaje de programación se utiliza principalmente para aplicaciones Android?
INSERT INTO opcionRespuesta (idPregunta, textoOpcion) VALUES 
(4, 'Python'),
(4, 'JavaScript'),
(4, 'Kotlin'),
(4, 'Java');  -- Correcta

SELECT * FROM pregunta;
SELECT * FROM opcionRespuesta;

UPDATE opcionRespuesta SET esCorrecta = TRUE WHERE idOpcion = 4;
UPDATE opcionRespuesta SET esCorrecta = TRUE WHERE idOpcion = 8;
UPDATE opcionRespuesta SET esCorrecta = TRUE WHERE idOpcion = 12;
UPDATE opcionRespuesta SET esCorrecta = TRUE WHERE idOpcion = 16;

-- SELECT r.*, j.nombreCompleto FROM ranking r JOIN jugador j ON r.idJugador = j.idJugador ORDER BY r.puntosObtenidos DESC;
