INSERT INTO admin (admin_username, admin_password, login_status) VALUES ('admin11','admin@11','inactive');
INSERT INTO USER_TABLE(USER_NAME,EMAIL,FULL_NAME,PASSWORD,STATUS)
VALUES ('user11','user11@c.c','Ramesh','user@11','inactive'),
('user12','user12@k.k','suresh','user@12','inactive'),
('user13','user13@c.c','Rakesh','user@13','inactive'),
('user14','user14@k.k','Lokesh','user@14','inactive');
INSERT INTO Genre (ID,GENRE_NAME) VALUES (1,'Action'),(2, 'Comedy'),(3,'Thriller'),(4,'Suspense'),(5,'Drama');

INSERT INTO SHOW_TIMES(ID,SHOW_NAME,START_TIME,END_TIME) VALUES
(1,'Morning Show','10:30','13:00'),
(2,'Matinee show','14:00','17:00'),
(3,'First show','18:30','21:00'),
(4,'Second show','22:00','1:00');

INSERT INTO ADDRESS(BUILDING,STREET,AREA,CITY,STATE,COUNTRY,PINCODE)
VALUES ('Sap70','Near RTC X Roads Metro','RTC X Roads','Hyderabad','Telangana','INDIA',567882),
('SU35','Near RTC X Roads Metro','RTC X Roads','Hyderabad','Telangana','INDIA',567882),
('San75','Near RTC X Roads Metro','RTC X Roads','Hyderabad','Telangana','INDIA',567882),
('Sarath City Capital Mall','Near botanical garden','Kondapur','Hyderabad','Telangana','INDIA',668224),
('Galleria Mall','Near punjagutta metro','Punjagutta','Hyderabad','Telangana','INDIA',687554),
('Inox','Governorpeta','Governorpeta','Vijayawada','Andhra Pradesh','INDIA',521001),
('CMR Mall','Near beach road','Jagadamba Center','Visakhapatnam','Andhra Pradesh','INDIA',521098);

INSERT INTO THEATRE(ID,THEATRE_NAME,NUMBER_OF_SCREENS,ADDRESS_BUILDING)
VALUES (1,'Sapthagiri 70mm',1,'Sap70'),
(2,'Sudarshan 35mm',1,'SU35'),
(3,'Sandhya 75mm',1,'San75'),
(4,'AMB Cinemas',18,'Sarath City Capital Mall'),
(5,'PVR Screens',12,'Galleria Mall'),
(6,'Inox',4,'Inox'),
(7,'PVR Cinemas',8,'CMR Mall');

INSERT INTO MOVIE(ID,TITLE,LANGUAGE,DESCRIPTION,FILENAME,TICKET_PRICE,RELEASE_DATE,STATUS)
VALUES ('Baahubali_Telugu_2015-04-17','Baahubali','Telugu','An Epic Historical Action Movie','bahubali.jpg',120,'2015-04-17','enabled')
,('Baahubali_2_Telugu_2017-04-28','Baahubali 2','Telugu','A sequel to an epic historical action movie','baahubali2.webp',160,'2017-04-28','enabled')
,('Gemini_Telugu_2002-10-11','Gemini','Telugu','A Gangster movie','gemini.jpg',100,'2002-10-11','enabled')
,('Julayi_Telugu_2012-08-09','Julayi','Telugu','A family friendly action,comedy,thriller movie.','Julayi.jpg',100,'2012-08-09','enabled');


INSERT INTO MOVIE_GENRES(MOVIES_ID,GENRES_ID) VALUES
('Baahubali_Telugu_2015-04-17',1),
('Baahubali_2_Telugu_2017-04-28',1),
('Gemini_Telugu_2002-10-11',1),
('Gemini_Telugu_2002-10-11',2),
('Julayi_Telugu_2012-08-09',1),
('Julayi_Telugu_2012-08-09',2),
('Julayi_Telugu_2012-08-09',3);




INSERT INTO SCREENING(ID,DATE,STATUS,MOVIE_ID,SHOW_TIME_ID,THEATRE_ID)
VALUES (1,'2023-09-09','Not Started','Gemini_Telugu_2002-10-11',1,1),
(2,'2023-09-09','Not Started','Gemini_Telugu_2002-10-11',1,2),
(3,'2023-09-10','Not Started','Gemini_Telugu_2002-10-11',1,3),
(4,'2023-09-09','Not Started','Baahubali_Telugu_2015-04-17',3,4),
(5,'2023-09-10','Not Started','Baahubali_Telugu_2015-04-17',3,5),
(6,'2023-09-09','Not Started','Baahubali_2_Telugu_2017-04-28',4,4),
(7,'2023-09-10','Not Started','Baahubali_2_Telugu_2017-04-28',4,5),
(8,'2023-09-09','Not Started','Gemini_Telugu_2002-10-11',3,6),
(9,'2023-09-09','Not Started','Julayi_Telugu_2012-08-09',3,7),
(10,'2023-09-09','Not Started''Gemini_Telugu_2002-10-11',4,6),
(11,'2023-09-10','Not Started','Julayi_Telugu_2012-08-09',4,7);