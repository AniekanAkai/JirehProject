
create table "Users" (
	id SERIAL,
	username varchar(250), 
	fullName varchar(250),
	dateOfBirth DATE,
	currentLocation varchar(250),
	phoneNumber integer,
	email varchar(250), 
	averageRating float,
        PRIMARY KEY (id)
);



create table "ServiceProviders" (
	id SERIAL,
	user_id integer NOT NULL  REFERENCES public.Users(id),
	availabilityRadius float NOT NULL,
	numberOfCancellations integer default 0,
	serviceOffered TEXT NOT NULL,
	PRIMARY KEY (id)
);

create table "Services" (
	id SERIAL,
	FOREIGN KEY (user_id) REFERENCES Users(id),
	FOREIGN KEY (serviceprovider_id) REFERENCES ServiceProviders(id),
	PRIMARY KEY (id)
);