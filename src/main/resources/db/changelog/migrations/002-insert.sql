INSERT INTO public.Users(username, password, email, phone_number, birthdate, registration_date, role) VALUES
('AlexeyPetrov', 'petrov', 'petrov@mail.ru','89771234567', '1998-12-31', '2024-08-30 22:12:12', 'ROLE_USER'),
('PetrAlexeev', 'alexeev', 'alexeev@mail.ru','89771234568', '1990-03-11', '2024-08-31 09:12:12', 'ROLE_USER'),
('ElenaKotova', 'kotova', 'kotova@mail.ru','89771234570', '1970-06-06', '2024-08-29 09:12:12', 'ROLE_USER');

INSERT INTO public.Files(filename, owner, filepath, content, date_of_upload) VALUES
('1.txt', 1, '/files/1.txt', decode('d0bfd0b5d180d0b2d18bd0b920d184d0b0d0b9d0bb20d0b220d185d180d0b0d0bdd0b8d0bbd0b8d189d0b5', 'hex'), NOW()),
('2.txt', 1, '/files/2.txt', decode('d0b2d182d0bed180d0bed0b920d184d0b0d0b9d0bb', 'hex'), NOW()),
('3.txt', 2, '/files/3.txt', decode('31323334353621', 'hex'), NOW()),
('4.txt', 2, '/files/4.txt', decode('d187d0b5d182d0b2d0b5d180d182d18bd0b9', 'hex'), NOW()),
('5.txt', 3, '/files/5.txt', decode('d0b820d18dd182d0be20d0bfd18fd182d18bd0b920d184d0b0d0b9d0bb21', 'hex'), NOW()),
('6.txt', 3, '/files/6.txt', decode('d0b2d181d0b52c20d185d0b2d0b0d182d0b8d182', 'hex'), NOW());