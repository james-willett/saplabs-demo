INSERT INTO admin (id, username, password)
VALUES (1,'admin','admin');

INSERT INTO videogames (id, name, release_date, review_score, category, rating)
VALUES (1, 'Resident Evil 4', '2005-10-01 23:59:59', 85, 'Shooter', 'Universal'),
       (2, 'Gran Turismo 3', '2001-03-10 23:59:59', 91, 'Driving', 'Universal'),
       (3, 'Tetris', '1984-06-25 23:59:59', 88, 'Puzzle', 'Universal'),
       (4, 'Super Mario 64', '1996-10-20 23:59:59', 90, 'Platform', 'Universal'),
       (5, 'The Legend of Zelda: Ocarina of Time', '1998-12-10 23:59:59', 93, 'Adventure', 'PG-13'),
       (6, 'Doom', '1993-02-18 23:59:59', 81, 'Shooter', 'Mature'),
       (7, 'Minecraft', '2011-12-05 23:59:59', 77, 'Puzzle', 'Universal'),
       (8, 'SimCity 2000', '1994-09-11 23:59:59', 88, 'Strategy', 'Universal'),
       (9, 'Final Fantasy VII', '1997-08-20 23:59:59', 97, 'RPG', 'PG-13'),
       (10, 'Grand Theft Auto III', '2001-04-23 23:59:59', 90, 'Driving', 'Mature');

INSERT INTO users (id, username, password, email, phone_number)
VALUES (1,'john','pass','john@gmail.com','11111111'),
       (2,'user1','pass','user1@email.com','11111111'),
       (3,'user2','pass','user2@email.com','11111111'),
       (4,'user3','pass','user3@email.com','11111111');
