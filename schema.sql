--1. Роль
CREATE TABLE C_Role
(
	ID_Role SERIAL PRIMARY KEY,
	Name_Role VARCHAR (50) NOT NULL
)
INSERT INTO C_Role (Name_Role)
VALUES ('Администратор'),
('Супер-администратор')



--2. Кладбища
CREATE TABLE Cemeteries
(
	ID_Cemetery SERIAL PRIMARY KEY,
	Name_Cemetery VARCHAR (100) NOT NULL,
	Address VARCHAR (150) NOT NULL
)
INSERT INTO Cemeteries (Name_Cemetery, Address)
VALUES ('Северное кладбище', 'ул.Лесная, 15, г.Москва'),
('Восточное кладбище', 'ул.Садовая, 28, г.Москва'),
('Западное кладбище', 'ул.Парковая, 7, г.Москва'),
('Центральное кладбище', 'ул.Центральная, 1, г.Москва'),
('Южное мемориальное кладбище', 'пр-т Памяти, 25, г.Н')



--3. Секции кладбищ
CREATE TABLE Cemeteries_Sections
(
	ID_Section SERIAL PRIMARY KEY,
	ID_Cemetery INT NOT NULL REFERENCES Cemeteries(ID_Cemetery),
	Number_Section INT NOT NULL
)
INSERT INTO Cemeteries_Sections (ID_Cemetery, Number_Section)
VALUES (1, 1), (1, 2),
(2, 1), (2, 2),
(3, 1), (3, 2),
(4, 1), (4, 2),
(5, 1), (5, 2)



--4. Участки секций
CREATE TABLE Plots
(
	ID_Plot SERIAL PRIMARY KEY,
	ID_Section INT NOT NULL REFERENCES Cemeteries_Sections (ID_Section),
	Number_Plot INT NOT NULL
)
INSERT INTO Plots (ID_Section, Number_Plot)
VALUES (1, 1), (1, 2), (1, 3),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1)



--5. Захоронения
CREATE TABLE Graves
(
	ID_Grave SERIAL PRIMARY KEY,
	ID_Plot INT NOT NULL REFERENCES Plots (ID_Plot),
	Number_Grave INT NOT NULL
)
INSERT INTO Graves (ID_Plot, Number_Grave)
VALUES (1, 1),
(2, 2),
(3, 3),
(5, 1),
(4, 1)



--6. Фото захоронения
CREATE TABLE Photo_Burial
(
	ID_Photo_Burial SERIAL PRIMARY KEY,
	ID_Grave INT NOT NULL REFERENCES Graves (ID_Grave) UNIQUE,
	Path_File TEXT NOT NULL
)
INSERT INTO Photo_Burial (ID_Grave, Path_File)
VALUES (1, 'https://storage.yandexcloud.net/electroniccemetery/%D0%BC%D0%BE%D0%B3%D0%B8%D0%BB%D0%B01.jpg'),
(2, 'https://storage.yandexcloud.net/electroniccemetery/%D0%BC%D0%BE%D0%B3%D0%B8%D0%BB%D0%B02.JPG'),
(3, 'https://storage.yandexcloud.net/electroniccemetery/%D0%BC%D0%BE%D0%B3%D0%B8%D0%BB%D0%B03.jpg'),
(4, 'https://storage.yandexcloud.net/electroniccemetery/%D0%BC%D0%BE%D0%B3%D0%B8%D0%BB%D0%B04.jpg'),
(5, 'https://storage.yandexcloud.net/electroniccemetery/%D0%BC%D0%BE%D0%B3%D0%B8%D0%BB%D0%B05.jpg')



--7. Усопшие
CREATE TABLE Deceased 
(
	ID_Deceased SERIAL PRIMARY KEY,
	ID_Grave INT NOT NULL REFERENCES Graves (ID_Grave),
	LastName VARCHAR (50) NOT NULL,
	FirstName VARCHAR (50) NOT NULL,
	Othestvo VARCHAR (50),
	Birth_Date DATE,
	Death_Date DATE,
	Description TEXT 
)
INSERT INTO Deceased (ID_Grave, LastName, FirstName, Othestvo, Birth_Date, Death_Date, Description)
VALUES (1, 'Иванов', 'Иван', 'Иванович', '1950-03-12', '2020-01-23', 'Ветеран труда'), 
(2, 'Обрученко', 'Клавдия', 'Никифоровна', '1975-01-01', '2021-12-30', 'Любимая мать и бабушка'),
(3, 'Русенко', 'Матвей', 'Анатольевич', '1992-08-13', '2012-11-28', 'Любимый сын'),
(4, 'Соколова', 'Ирина', 'Олеговна', '1958-04-18', '2018-06-27', 'Инженер-конструктор'),
(5, 'Кушоренко', 'Николай', 'Матвеевич', '1970-09-30', '2021-08-19', 'Учитель начальных классов')



--8. Фото усопшего
CREATE TABLE Photo_Deceased
(
	ID_Photo_Deceased SERIAL PRIMARY KEY,
	ID_Deceased INT NOT NULL REFERENCES Deceased (ID_Deceased) UNIQUE,
	Path_File TEXT NOT NULL
)
INSERT INTO Photo_Deceased (ID_Deceased, Path_File)
VALUES (1, 'https://storage.yandexcloud.net/electroniccemetery/%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA1.jpg'),
(2, 'https://storage.yandexcloud.net/electroniccemetery/%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA2.jpg'),
(3, 'https://storage.yandexcloud.net/electroniccemetery/%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA3.jpg'),
(4, 'https://storage.yandexcloud.net/electroniccemetery/%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA4.jpg'),
(5, 'https://storage.yandexcloud.net/electroniccemetery/%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA5.jpg')



--9. Пользователи(Администраторы)
CREATE TABLE Users
(
	ID_User SERIAL PRIMARY KEY,
	ID_Role INT NOT NULL REFERENCES C_Role(ID_Role),
	ID_Cemetery INT NULL REFERENCES Cemeteries(ID_Cemetery),
	LastName VARCHAR (50) NOT NULL,
	FirstName VARCHAR (50) NOT NULL,
	Othestvo VARCHAR (50),
	Login VARCHAR (50) UNIQUE NOT NULL,
	U_Password VARCHAR (255) NOT NULL 
)
-- Администраторы:
INSERT INTO Users (ID_Role, ID_Cemetery, LastName, FirstName, Othestvo, Login, U_Password)
VALUES (1, 1, 'Павлеченко', 'Ирина', 'Ивановна', 'admin1', '12345'),
(1, 2, 'Ковалев', 'Николай', 'Николаевич', 'admin2', 'admin123'),
(1, 3, 'Львицин', 'Никита', 'Александрович', 'admin3', 'addmin') 

-- Супер-администраторы:
INSERT INTO Users (ID_Role, ID_Cemetery, LastName, FirstName, Othestvo, Login, U_Password)
VALUES (2, NULL, 'Нинченко', 'Анатолий', 'Викторович', 'superADMIN', 'superpuper')



