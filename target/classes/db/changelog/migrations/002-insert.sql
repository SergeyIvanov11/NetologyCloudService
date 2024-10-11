INSERT INTO public.Users(username, password, email, phone_number, birthdate, registration_date, role) VALUES
('AlexeyPetrov', 'petrov', 'petrov@mail.ru','89771234567', '1998-12-31', '2024-08-30 22:12:12', 'ROLE_USER'),
('PetrAlexeev', 'alexeev', 'alexeev@mail.ru','89771234568', '1990-03-11', '2024-08-31 09:12:12', 'ROLE_USER'),
('ElenaKotova', 'kotova', 'kotova@mail.ru','89771234570', '1970-06-06', '2024-08-29 09:12:12', 'ROLE_USER');

INSERT INTO public.Files(filename, owner, filepath, date_of_upload) VALUES
('1.txt', 1, 'c:\Users\user\Downloads\1.txt', '2024-09-02 11:01:12'),
('2.txt', 1, 'c:\Users\user\Downloads\2.txt', '2024-09-02 07:16:15'),
('3.txt', 2, 'c:\Users\user\Downloads\3.txt', '2024-09-03 11:18:12'),
('4.txt', 2, 'c:\Users\user\Downloads\4.txt', '2024-09-03 09:12:12'),
('5.txt', 3, 'c:\Users\user\Downloads\5.txt', '2024-09-01 12:14:15'),
('6.txt', 3, 'c:\Users\user\Downloads\6.txt', '2024-09-04 08:00:01');