INSERT INTO Members (first_name, last_name, email, password, weight_goal, time_goal, blood_pressure, physical_ability) VALUES 
 ('Ema', 'Ila', 'emaila@fauxemail.net', 'Password43', 72.0, '2024-08-06 13:05:40', '120/80', 'poor'),
 ('Dus', 'Lin', 'duslin@fauxemail.net', 'pA123', 84.5, '2024-07-12 06:05:40', '120/80', 'poor'),
 ('Sen', 'Mea', 'senmea@fauxemail.net', 'Las21', 60.8, '2025-04-06 20:05:40', '120/80', 'poor');

INSERT INTO Trainers (first_name, last_name, email, password) VALUES
 ('Cal', 'Mine', 'calmine@fauxemail.net', 'Tra123'),
 ('Kat', 'Nine', 'katnine@fauxemail.net', '89Trs');

INSERT INTO Staff (first_name, last_name, email, password) VALUES
 ('Kig', 'Liea', 'kigliea@fauxemail.net', 'Linw21'),
 ('Lia', 'Buf', 'liabuf@fauxemail.net', 'hkgrcvdx1368');

INSERT INTO ExerciseEquipment (equipment_name, working) VALUES
 ('Bike', TRUE),
 ('Treadmill', TRUE);
 
INSERT INTO Avaiblitiy (avaiblitiy_date_start, avaiblitiy_date_end, trainer_id) VALUES
 ('2024-03-07 15:00:00', '2024-03-07 16:00:00', 2),
 ('2024-04-01 14:00:00', '2024-04-01 15:00:00', 1),
 ('2024-04-01 12:00:00', '2024-04-01 13:00:00', 1),
 ('2024-04-01 15:00:00', '2024-04-01 16:00:00', 1),
 ('2024-04-01 14:00:00', '2024-04-01 15:00:00', 2),
 ('2024-04-01 14:00:00', '2024-04-01 15:00:00', 2),
 ('2024-04-01 15:00:00', '2024-04-01 16:00:00', 2);
 
INSERT INTO Exercise (trainer_id, member_id, exercise_date, exercise_type) VALUES
 (1, 2, '2024-03-04 12:00:00', 'Personal'),
 (2, 1, '2024-04-01 15:00:00', 'Group'),
 (2, 3, '2024-04-01 15:00:00', 'Group');

INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES
 (1, 120.00, 'Personal Workout', TRUE),
 (2, 50.00, 'Group Workout', FALSE),
 (3, 50.00, 'Group Workout', FALSE);

INSERT INTO Classes (name_of_class, location, date_start, date_end) VALUES
 ('Swimming lessons', 'Swimming pool', '2024-04-08 08:00:00', '2024-04-08 10:00:00'),
 ('Crossfit class', 'Crossfit room', '2024-04-17 10:00:00', '2024-04-17 12:00:00');

INSERT INTO BookedRooms (name_of_room, class_id, date_start, date_end) VALUES
 ('Swimming room', 1, '2024-04-08 08:00:00', '2024-04-08 10:00:00'),
 ('Crossfit room', 2, '2024-04-17 10:00:00', '2024-04-17 12:00:00');
 