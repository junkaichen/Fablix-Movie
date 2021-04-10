DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;
USE moviedb;

CREATE TABLE movies (
	id			VARCHAR(10) NOT NULL,
	title		VARCHAR(100) NOT NULL,
    year		INTEGER NOT NULL,
    director	VARCHAR(100) NOT NULL,
    PRIMARY KEY (id) 
    );
    
CREATE TABLE stars (
    id			VARCHAR(10) NOT NULL,
    name		VARCHAR(100) NOT NULL,
    birthYear	INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies (
	starId		VARCHAR(10),
    movieId		VARCHAR(10),
    FOREIGN KEY (starId) REFERENCES stars(id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE genres (
	id			INTEGER AUTO_INCREMENT,
    name		VARCHAR(32),
    PRIMARY KEY (id)
);


CREATE TABLE genres_in_movies (
	genreId		INTEGER,
    movieId		VARCHAR(10),
    FOREIGN KEY (genreId) REFERENCES genres(id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE
    
);

CREATE TABLE creditcards (
	id			VARCHAR(20) NOT NULL,
    firstName	VARCHAR(50) NOT NULL,
    lastName	VARCHAR(50) NOT NULL,
    expiration	DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE customers (
	id			INTEGER NOT NULL AUTO_INCREMENT,
    firstName	VARCHAR(50) NOT NULL,
    lastName	VARCHAR(50) NOT NULL,
    ccId		VARCHAR(20),
    address		VARCHAR(200) NOT NULL,
    email		VARCHAR(50) NOT NULL,
    password	VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (ccId) REFERENCES creditcards(id) ON UPDATE CASCADE
);

CREATE TABLE sales (
	id			INTEGER NOT NULL AUTO_INCREMENT,
    customerId 	INTEGER,
    movieId		VARCHAR(10),
    saleDate	DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customerId) REFERENCES customers (id) ON DELETE CASCADE,
    FOREIGN KEY (movieId) REFERENCES movies (id) ON DELETE CASCADE
);


CREATE TABLE ratings (
	movieId		VARCHAR(10),
    rating		FLOAT NOT NULL,
    numVotes	INTEGER NOT NULL,
    FOREIGN KEY (movieId) REFERENCES movies (id) ON DELETE CASCADE
);
