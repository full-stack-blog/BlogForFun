USE BlogForFun_db;

-- Will not seed if no user exists in user table!!!!!

insert into user (user_role, email, username, password, first_name, last_name, profile_image, cover_img) values
('admin','Ptyus31@gmail.com','Ptyus','P123','Paris','Tyus', 'https://images.pexels.com/photos/1034675/pexels-photo-1034675.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260', 'https://images.pexels.com/photos/1031955/pexels-photo-1031955.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260');

insert into categories(name) values
('Sports'),
('Technology'),
('Beauty'),
('Travel'),
('Personal'),
('Politics'),
('Food'),
('TV'),
('Automobiles'),
('Home Improvement'),
('Animals'),
('Shopping'),
('Gaming'),
('Shows/Concerts');
