INSERT INTO subscription (name, max_loans)
VALUES ('enfant', 3),
       ('etudiant', 5),
       ('adulte', 7),
       ('senior', 10);


-- Insérer des livres dans la table Book
INSERT INTO Book (id, title, author)
VALUES (1, 'Le Petit Prince', 'Antoine de Saint-Exupéry'),
       (2, '1984', 'George Orwell'),
       (3, 'L Etranger', 'Albert Camus');

-- Insérer des copies dans la table Copy
INSERT INTO Copy (id, book_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 3),
       (5, 3);

INSERT INTO Loan_Type (max_loan_days, name)
VALUES (7, 'sur place'),
       (14, 'emporte')