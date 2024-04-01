CREATE DATABASE "Health_and_fitness"
	WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE members(
	member_id SERIAL PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	weight_goal FLOAT NOT NULL,
	time_goal TIMESTAMP,
	blood_pressure VARCHAR(255) NOT NULL,
	physical_ability VARCHAR(255) NOT NULL
);

CREATE TABLE trainers(
	trainer_id SERIAL PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE staff(
	staff_id SERIAL PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE avaiblitiy(
	avaiblitiy_id SERIAL PRIMARY KEY,
	avaiblitiy_date_start TIMESTAMP,
	avaiblitiy_date_end TIMESTAMP,
	trainer_id INT REFERENCES Trainers(trainer_id)
);

CREATE TABLE classes(
	class_id SERIAL PRIMARY KEY,
	name_of_class VARCHAR(255) NOT NULL,
	location VARCHAR(255) NOT NULL,
	date_start TIMESTAMP,
	date_end TIMESTAMP
);

CREATE TABLE bookedrooms (
	room_id SERIAL PRIMARY KEY,
	name_of_room VARCHAR(255) NOT NULL,
	class_id INT REFERENCES Classes(class_id),
	date_start TIMESTAMP,
	date_end TIMESTAMP
);

CREATE TABLE exercise(
	exercise_id SERIAL PRIMARY KEY,
	trainer_id INT REFERENCES Trainers(trainer_id),
	member_id INT REFERENCES Members(member_id),
	exercise_date TIMESTAMP,
	exercise_type VARCHAR(255) NOT NULL
);

CREATE TABLE bills(
	bill_id SERIAL PRIMARY KEY,
	member_id INT REFERENCES Members(member_id),
	price FLOAT NOT NULL,
	name_of_bill VARCHAR(255) NOT NULL,
	is_paid BOOLEAN NOT NULL
);

CREATE TABLE exerciseequipment(
	equipment_id SERIAL PRIMARY KEY,
	equipment_name VARCHAR(255) NOT NULL,
	working BOOLEAN NOT NULL
);
