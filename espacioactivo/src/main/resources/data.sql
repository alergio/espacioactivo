-- roles
INSERT INTO role (id, name)
VALUES
    (1, 'ROLE_CUSTOMER'),
    (2, 'ROLE_SERVICE_PROVIDER'),
    (3, 'ROLE_ADMIN') AS new_values(id, name)
ON DUPLICATE KEY UPDATE
    name = new_values.name;



-- users
INSERT INTO user (id, email, firstname, lastname, is_enabled, password, registration_date)
VALUES
    (1, 'customer_raul@test.com', 'Raul', 'Pereira', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (2, 'customer_martin@test.com', 'Martin', 'Perez', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (3, 'customer_romina@test.com', 'Romina', 'Segovia', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (4, 'customer_sofia@test.com', 'Sofia', 'Martinez', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (5, 'rent_provider_marcela@test.com', 'Marcela', 'Munis', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (6, 'yogui_svelsen@test.com', 'Maui', 'Svelsen', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (7, 'box_teacher@test.com', 'Roberto', 'Rodriguez', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20'),
    (8, 'admin@test.com', 'Alejo', 'Maya', 1, '$2a$10$.F4/kvwGaLtB9FI8sLk0meC5gvb4GTRV.IUco5sEwZo.A3/cejoNu', '2024-04-20')
    AS new_values(id, email, firstname, lastname, is_enabled, password, registration_date)
ON DUPLICATE KEY UPDATE
    email = new_values.email,
    firstname = new_values.firstname,
    lastname = new_values.lastname,
    is_enabled = new_values.is_enabled,
    password = new_values.password,
    registration_date = new_values.registration_date
    ;



-- user roles
INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1),
    (6, 1),
    (7, 1),
    (8, 1),
    (5, 2),
    (6, 2),
    (7, 2),
    (8, 3)
    AS new_values(user_id, role_id)
ON DUPLICATE KEY UPDATE
    user_id = new_values.user_id,
    role_id = new_values.role_id
    ;


-- disciplinas
INSERT INTO discipline (id, name, type)
VALUES
    (1, 'Futbol', 'SPACE_RENTAL'),
    (2, 'Yoga', 'GROUP_CLASS'),
    (3, 'Entrenamiento personalizado', 'PERSONALIZED_CLASS'),
    (4, 'Sala de musculacion', 'GROUP_CLASS'),
    (5, 'Tennis', 'SPACE_RENTAL'),
    (6, 'Boxeo', 'GROUP_CLASS')
    AS new_values(id, name, type)
ON DUPLICATE KEY UPDATE
    name = new_values.name,
    type = new_values.type
    ;


-- addresses
INSERT INTO address(id, number, state, street)
VALUES
    (1, '3413', 'Montevideo', 'Av Comercio'),
    (2, '2121', 'Montevideo', 'Blvar Artigas'),
    (3, '2532', 'Montevideo', 'Justo Maeso'),
    (4, '2169', 'Montevideo', 'Av Siempreviva'),
    (5, '3762', 'Montevideo', 'Av Italia'),
    (6, '1697', 'Montevideo', '18 de julio')
    AS new_values(id, number, state, street)
ON DUPLICATE KEY UPDATE
    number = new_values.number,
    state = new_values.state,
    street = new_values.street
    ;



-- actividades
INSERT INTO activity (id, price, address_id, discipline_id, user_id)
VALUES
	(1, 2000, 1, 1, 5),
	(2, 300, 2, 2, 6),
	(3, 500, 3, 3, 6),
	(4, 100, 4, 4, 7),
    (5, 300, 5, 5, 7),
    (6, 200, 6, 6, 7)
	AS new_values(id, price, address_id, discipline_id, user_id)
 ON DUPLICATE KEY UPDATE
     price = new_values.price,
     address_id = new_values.address_id,
     discipline_id = new_values.discipline_id,
     user_id = new_values.user_id
     ;


-- estados de turno
INSERT INTO appointment_state (id, name)
VALUES
    (1, 'AVAILABLE'),
    (2, 'UNAVAILABLE'),
    (3, 'EXPIRED')
    AS new_values(id, name)
ON DUPLICATE KEY UPDATE
    name = new_values.name
    ;




-- turnos
INSERT INTO appointment (id, date, time, max_people, total_reserves, is_full, activity_id, state_id)
VALUES

    -- turnos para alquiler de cancha de futbol para maniana y pasado
	(1, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '17:00:00', 1, 0, false, 1, 1),
	(2, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '18:00:00', 1, 0, false, 1, 1),
	(3, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '19:00:00', 1, 1, false, 1, 1),
	(4, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '20:00:00', 1, 1, false, 1, 1),
    (5, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '21:00:00', 1, 0, false, 1, 1),
    (6, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '22:00:00', 1, 0, false, 1, 1),
	(8, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '18:00:00', 1, 0, false, 1, 1),
    (7, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '17:00:00', 1, 0, false, 1, 1),
	(9, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '19:00:00', 1, 0, false, 1, 1),
	(10, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '20:00:00', 1, 0, false, 1, 1),
    (11, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '21:00:00', 1, 0, false, 1, 1),
    (12, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '22:00:00', 1, 0, false, 1, 1),


    -- turnos para inscribirse a clases grupales de yoga toda la semana
    (13, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '18:00:00', 5, 2, false, 2, 1),
	(14, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 2, 1),
	(15, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 2, 1),
	(16, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 4 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 2, 1),
    (17, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 5 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 2, 1),
    (18, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 6 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 2, 1),


    -- turnos para entrenamiento personalizado, varios horarios para varios dias
    (19, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '18:00:00', 1, 0, false, 3, 1),
	(20, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '19:00:00', 1, 0, false, 3, 1),
	(21, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '17:00:00', 1, 0, false, 3, 1),
	(22, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '18:00:00', 1, 0, false, 3, 1),
    (23, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '19:00:00', 1, 0, false, 3, 1),
    (24, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '20:00:00', 1, 0, false, 3, 1),


    -- turnos para clases grupales de tennis
    (25, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '18:00:00', 5, 1, false, 5, 1),
	(26, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '21:00:00', 3, 0, false, 5, 1),
	(27, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 5, 1),
	(28, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '21:00:00', 3, 0, false, 5, 1),
    (29, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '18:00:00', 5, 0, false, 5, 1),
    (30, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '21:00:00', 3, 0, false, 5, 1),


    -- turnos para clases grupales de boxeo
    (31, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 6, 1),
	(32, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 2 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 6, 1),
	(33, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 6, 1),
	(34, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 4 DAY), '%Y-%m-%d'), '21:00:00', 5, 1, false, 6, 1),
    (35, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 5 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 6, 1),
    (36, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 6 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 6, 1),
    (37, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 6 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 4, 3),
    (38, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 6 DAY), '%Y-%m-%d'), '21:00:00', 5, 0, false, 4, 2),
    (39, DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 6 DAY), '%Y-%m-%d'), '21:00:00', 5, 1, false, 4, 1)

	AS new_values(id, date, time, max_people, total_reserves, is_full, activity_id, state_id)
    ON DUPLICATE KEY UPDATE
     date = new_values.date,
     time = new_values.time,
     max_people = new_values.max_people,
     total_Reserves = new_values.total_reserves,
     is_full = new_values.is_full,
     activity_id = new_values.activity_id,
     state_id = new_values.state_id
     ;


-- reservas
INSERT INTO reservation (id, is_cancelled, appointment_id, user_id, cancelled_by)
VALUES

	(1, 0, 3, 1, "NONE"),
	(2, 0, 4, 2, "NONE"),
	(3, 0, 13, 3, "NONE"),
	(4, 0, 13, 4, "NONE"),
    (5, 0, 34, 1, "NONE"),
    (6, 0, 25, 2, "NONE"),
    (7, 0, 39, 2, "NONE")

	AS new_values(id, is_cancelled, appointment_id, user_id, cancelled_by)
 ON DUPLICATE KEY UPDATE
     is_cancelled = new_values.is_cancelled,
     appointment_id = new_values.appointment_id,
     user_id = new_values.user_id,
     cancelled_by = new_values.cancelled_by
     ;


--    select * from role;
--	select * from user;
--    select * from user_roles;
--    select * from discipline;
--    select * from activity;
--    select * from appointment_state;
--    select * from appointment;
--    select * from reservation;
