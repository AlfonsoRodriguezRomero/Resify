-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');

INSERT INTO users(username,password,enabled) VALUES ('organizador1','0rg4n1z4d0r',TRUE);
INSERT INTO authorities VALUES ('organizador1','organizador');

INSERT INTO users(username,password,enabled) VALUES ('organizador2','0rg4n1z4d0r',TRUE);
INSERT INTO authorities VALUES ('organizador2','organizador');

INSERT INTO users(username,password,enabled) VALUES ('organizador3','organizador3',TRUE);
INSERT INTO authorities VALUES ('organizador3','organizador');

INSERT INTO organizadores VALUES (1, 'Fernandez', 'Juan', 'Prueba1', 'Prueba1', 'organizador1');
INSERT INTO organizadores VALUES (2,  'Limon', 'Maria', 'Prueba2', 'Prueba2', 'organizador2');
INSERT INTO organizadores VALUES (3,  'Naranja', 'Juana', 'Prueba3', 'Prueba3', 'organizador3');
	
-- Managers

INSERT INTO users(username,password,enabled) VALUES ('manager1','manager1',TRUE);
INSERT INTO authorities VALUES ('manager1','manager');

INSERT INTO users(username,password,enabled) VALUES ('manager2','manager2',TRUE);
INSERT INTO authorities VALUES ('manager2','manager');

INSERT INTO users(username,password,enabled) VALUES ('manager3','manager3',TRUE);
INSERT INTO authorities VALUES ('manager3','manager');

INSERT INTO users(username,password,enabled) VALUES ('manager4','manager4',TRUE);
INSERT INTO authorities VALUES ('manager4','manager');

INSERT INTO users(username,password,enabled) VALUES ('manager5','manager5',TRUE);
INSERT INTO authorities VALUES ('manager5','manager');

INSERT INTO managers VALUES (3, 'Rodriguez', 'Rosa', 'Prueba1', 'Prueba1', 'manager1');
INSERT INTO managers VALUES (4, 'Dominguez', 'Carlos', 'Prueba2', 'Prueba2', 'manager2');
INSERT INTO managers VALUES (8, 'Jimenez', 'Laura', 'Prueba4', 'Prueba4', 'manager4');
INSERT INTO managers VALUES (9, 'MVisitaSanitaria', 'MVisitaSanitaria', 'MVisitaSanitaria', 'MVisitaSanitaria', 'manager5');

-- Manager sin residencia
INSERT INTO managers VALUES (7, 'Sanchez', 'Roberto', 'Pureba3', 'Prueba3', 'manager3');

-- Residencias

INSERT INTO residencias VALUES (1, false, 100, 'residencia1@mail.es', 'Descripcion de prueba', 'Direccion', '70', '07:00', '21:00', 
	'http://www.resi1.com', 'Residencia 1', '987654321', 3);
  
INSERT INTO residencias VALUES (2, false, 100, 'residencia2@mail.es', 'Descripcion de prueba 2', 'Direccion 2', '70', '07:00', '21:00', 
	'http://www.resi2.com', 'Residencia 2', '987654321', 4);
	
INSERT INTO residencias VALUES (3, false, 10, 'residenciaSinPlazas@mail.es', 'Descripcion sin plazas', 'Direccion sin plazas', '70', '07:00', '21:00', 
	'http://www.resisinplazas.com', 'Residencia Sin Plazas', '987654322', 8);
	
INSERT INTO residencias VALUES (4, true, 10, 'residenciaVisitaSanitaria@mail.es', 'Descripcion visita sanitaria', 'Direccion visita sanitaria', '70', '07:00', '21:00', 
	'http://www.resivisitasanitaria.com', 'Residencia visita sanitaria', '987654322', 9);

-- Actividades

INSERT INTO actividades VALUES (1, 'Descripcion de prueba1', '2010-09-07',  '22:30', '17:00', 
	'Prueba1', 1);
INSERT INTO actividades VALUES (2, 'Descripcion de prueba2', '2010-09-07',  '22:30', '17:00', 
	'Prueba2', 1);
INSERT INTO actividades VALUES (3, 'Descripcion de prueba3', '2010-09-07',  '22:30', '17:00', 
	'Prueba3', 1);
INSERT INTO actividades VALUES (4, 'Descripcion de prueba4', '2010-09-07',  '22:30', '17:00', 
	'Prueba4', 2);	
INSERT INTO actividades VALUES (5, 'Descripcion de prueba5', '2020-09-07',  '22:30', '17:00', 
	'Prueba5', 1);
	
-- Ancianos

INSERT INTO users(username,password,enabled) VALUES ('anciano1','anciano1',TRUE);
INSERT INTO authorities VALUES ('anciano1','anciano');

INSERT INTO users(username,password,enabled) VALUES ('anciano2','anciano2',TRUE);
INSERT INTO authorities VALUES ('anciano2','anciano');

INSERT INTO ancianos VALUES (5, 'Garcia', 'Anacleto', 'Presentacion1', 70, false, 'anciano1');
INSERT INTO ancianos VALUES (6, 'Prieto', 'Dolores', 'Presentacion2', 80, true, 'anciano2');

INSERT INTO users(username,password,enabled) VALUES ('anciano3','anciano3',TRUE);
INSERT INTO authorities VALUES ('anciano3','anciano');

INSERT INTO ancianos VALUES (7, 'Gonzalez', 'Rosa', 'Presentacion3', 66, false, 'anciano3');

	-- Anciano para residencia de visita sanitaria --

INSERT INTO users(username,password,enabled) VALUES ('ancianovs','ancianovs',TRUE);
INSERT INTO authorities VALUES ('ancianovs','anciano');

INSERT INTO ancianos VALUES (23, 'Anciano', 'Residencia', 'TestVS', 66, false, 'ancianovs');

	-- Ancianos para Residencia sin plazas --

INSERT INTO users(username,password,enabled) VALUES ('ancianors1','ancianors1',TRUE);
INSERT INTO authorities VALUES ('ancianors1','anciano');

INSERT INTO ancianos VALUES (11, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianors1');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS2','ancianoRS2',TRUE);
INSERT INTO authorities VALUES ('ancianoRS2','anciano');

INSERT INTO ancianos VALUES (12, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS2');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS3','ancianoRS3',TRUE);
INSERT INTO authorities VALUES ('ancianoRS3','anciano');

INSERT INTO ancianos VALUES (13, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS3');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS4','ancianoRS4',TRUE);
INSERT INTO authorities VALUES ('ancianoRS4','anciano');

INSERT INTO ancianos VALUES (14, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS4');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS5','ancianoRS5',TRUE);
INSERT INTO authorities VALUES ('ancianoRS5','anciano');

INSERT INTO ancianos VALUES (15, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS5');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS6','ancianoRS6',TRUE);
INSERT INTO authorities VALUES ('ancianoRS6','anciano');

INSERT INTO ancianos VALUES (16, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS6');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS7','ancianoRS7',TRUE);
INSERT INTO authorities VALUES ('ancianoRS7','anciano');

INSERT INTO ancianos VALUES (17, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS7');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS8','ancianoRS8',TRUE);
INSERT INTO authorities VALUES ('ancianoRS8','anciano');

INSERT INTO ancianos VALUES (18, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS8');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS9','ancianoRS9',TRUE);
INSERT INTO authorities VALUES ('ancianoRS9','anciano');

INSERT INTO ancianos VALUES (19, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS9');

INSERT INTO users(username,password,enabled) VALUES ('ancianoRS10','ancianoRS10',TRUE);
INSERT INTO authorities VALUES ('ancianoRS10','anciano');

INSERT INTO ancianos VALUES (20, 'Plaza', 'Sin', 'PresentacionRS', 69, false, 'ancianoRS10');

INSERT INTO users(username,password,enabled) VALUES ('ancianoDependencia','ancianoDependencia',TRUE);
INSERT INTO authorities VALUES ('ancianoDependencia','anciano');

INSERT INTO ancianos VALUES (22, 'Con Dependencia', 'Anciano', 'PresentacionDependencia', 69, true, 'ancianoDependencia');


-- Anciano sin inscripcion
INSERT INTO users(username,password,enabled) VALUES ('joselitoanca','joselitoanca',TRUE);
INSERT INTO authorities VALUES ('joselitoanca','anciano');

INSERT INTO ancianos VALUES (21, 'Joselito', 'Angelo', 'PresentacionRes', 69, false, 'joselitoanca');

-- Incidencias

INSERT INTO incidencias VALUES (1, 'Descripcion de incidencia1', '2010-09-07', 'titulo1', 1);
INSERT INTO incidencias VALUES (2, 'Descripcion de incidencia2', '2010-09-07', 'titulo2', 2);
INSERT INTO incidencias VALUES (3, 'Descripcion de incidencia3', '2010-09-07', 'titulo3', 2);
INSERT INTO incidencias VALUES (4, 'Descripcion de incidencia4', '2010-09-07', 'titulo4', 2);

-- Buenas acciones

INSERT INTO buenas_acciones VALUES (1, 'Descripcion de buena accion1', '2010-09-07', 'titulo1', 1);
INSERT INTO buenas_acciones VALUES (2, 'Descripcion de buena accion2', '2010-09-07', 'titulo2', 1);
INSERT INTO buenas_acciones VALUES (3, 'Descripcion de buena accion3', '2010-09-07', 'titulo3', 2);
INSERT INTO buenas_acciones VALUES (4, 'Descripcion de buena accion4', '2010-09-07', 'titulo4', 2);

-- Inscripciones

INSERT INTO inscripciones VALUES (1, 'Declaracion1', 'pendiente', '2020-02-01', null , 5, 1);
INSERT INTO inscripciones VALUES (2, 'Declaracion2', 'pendiente', '2020-02-01', null , 6, 1);
INSERT INTO inscripciones VALUES (3, 'Declaracion4', 'rechazada', '2020-02-01', 'justificacion de prueba', 6, 2);
INSERT INTO inscripciones VALUES (4, 'Declaracion5', 'aceptada', '2020-02-01', null , 7, 1);

	-- Inscripciones Residencia sin plazas --

INSERT INTO inscripciones VALUES (11, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 11, 3);
INSERT INTO inscripciones VALUES (12, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 12, 3);
INSERT INTO inscripciones VALUES (13, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 13, 3);
INSERT INTO inscripciones VALUES (14, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 14, 3);
INSERT INTO inscripciones VALUES (15, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 15, 3);
INSERT INTO inscripciones VALUES (16, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 16, 3);
INSERT INTO inscripciones VALUES (17, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 17, 3);
INSERT INTO inscripciones VALUES (18, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 18, 3);
INSERT INTO inscripciones VALUES (19, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 19, 3);
INSERT INTO inscripciones VALUES (20, 'DeclaracionRS', 'aceptada', '2020-02-01', null , 20, 3);

	-- Inscripciones residencia de visita sanitaria

INSERT INTO inscripciones VALUES (21, 'DeclaracionDependencia', 'aceptada', '2020-02-01', null , 22, 4);
INSERT INTO inscripciones VALUES (22, 'Declaracion', 'aceptada', '2020-02-01', null , 23, 4);

-- Visitas sanitarias

INSERT INTO visitas_sanitarias VALUES (1, 'visita sanitaria', '2020-04-20', '22:00', '17:00', 'Ejemplo', 'Sanitario prueba', 22, 4);

-- Excursiones

INSERT INTO excursiones VALUES (1, 'Descripcion de prueba1', '2020-01-01',  '22:30', '17:00', 
	'Prueba1', '2020-09-07', TRUE, '4', '2.0', 1);
INSERT INTO excursiones VALUES (2, 'Descripcion de prueba2', '2020-09-07',  '22:30', '17:00', 
	'Prueba2', '2020-09-07', FALSE, '7', '1.0', 1);
INSERT INTO excursiones VALUES (3, 'Descripcion de prueba3', '2020-09-07',  '22:30', '17:00', 
	'Prueba3', '2020-09-07', TRUE, '2', '1.0', 1);
INSERT INTO excursiones VALUES (4, 'Descripcion de prueba4', '2020-09-07',  '22:30', '17:00', 
	'Excursion exigente', '2020-09-07', TRUE, '2', '5.0', 1);
INSERT INTO excursiones VALUES (5, 'Descripcion de prueba5', '2020-09-07',  '22:30', '17:00', 
	'Excursion prueba UI', '2020-09-07', TRUE, '2', '1.0', 1);
	
	
INSERT INTO excursiones VALUES (6, 'Excursion mia terminada 1', '2020-05-02',  '23:30', '17:00', 
	'Excursion acabada 1 res1', '2020-05-03', TRUE, '2', '1.0', 1);
INSERT INTO excursiones VALUES (7, 'Excursion mia terminada 2', '2020-05-01',  '09:58', '17:00', 
	'Excursion acabada 2 res1', '2020-05-02', TRUE, '2', '1.0', 1);
INSERT INTO excursiones VALUES (8, 'Descripcion de prueba6', '2020-05-07',  '22:30', '17:00', 
	'Excursion acabada 3 res1', '2020-05-07', TRUE, '2', '1.0', 1);

-- Excursion NO FINAL MODE	
INSERT INTO excursiones VALUES (9, 'Descripcion de prueba7', '2020-05-07',  '22:30', '17:00', 
	'Excursion acabada 3 res1', '2020-05-07', FALSE, '2', '1.0', 1);
	
	-- Excursion NO CADUCADA	
INSERT INTO excursiones VALUES (10, 'Descripcion de prueba7', '2023-05-07',  '22:30', '17:00', 
	'Excursion acabada 3 res1', '2023-05-07', FALSE, '2', '1.0', 1);	
	
-- PeticionesExcursion

INSERT INTO peticiones_excursion VALUES (1, 'Declaracion1', 'aceptada', '2020-02-01', null , 3, 1);
INSERT INTO peticiones_excursion VALUES (2, 'Declaracion2', 'aceptada', '2020-02-01', null , 3, 2);
INSERT INTO peticiones_excursion VALUES (3, 'Declaracion3', 'pendiente', '2020-02-01', null , 1, 1);

INSERT INTO peticiones_excursion VALUES (4, 'Declaracion4', 'aceptada', '2020-02-01', null , 6, 1);
INSERT INTO peticiones_excursion VALUES (5, 'Declaracion5', 'aceptada', '2020-02-01', null , 7, 1);
INSERT INTO peticiones_excursion VALUES (6, 'Declaracion6', 'aceptada', '2020-02-01', null , 8, 1);

-- Quejas

INSERT INTO quejas VALUES (1, 'Descripcion Prueba 1', '2020-03-02', FALSE, 'Titulo Prueba 1', 7);
INSERT INTO quejas VALUES (2, 'Descripcion Prueba 2', '2020-03-02', FALSE, 'Titulo Prueba 2', 7);

-- Feedbacks
INSERT INTO feedback VALUES (1, false, 'desc1', 2, 6, 1);
INSERT INTO feedback VALUES (2, false, 'desc2', 5, 7, 1);
