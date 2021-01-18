INSERT INTO hiking.hiker
  (hiker_id, age, mail, name, surname)
VALUES ('9899a076-9d93-471e-b06e-3447bcaa5200',
        15,
        'test1@mail.com',
        'name1',
        'surname1')
ON CONFLICT (hiker_id) DO NOTHING;

INSERT INTO hiking.hiker
  (hiker_id, age, mail, name, surname)
VALUES ('3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e',
        20,
        'test2@mail.com',
        'name2',
        'surname2')
ON CONFLICT (hiker_id) DO NOTHING;

INSERT INTO hiking.hiker
  (hiker_id, age, mail, name, surname)
VALUES ('d7be7688-01c4-4713-b273-3749ccd2a1ab',
        40,
        'test3@mail.com',
        'name3',
        'surname3')
ON CONFLICT (hiker_id) DO NOTHING;

INSERT INTO hiking.hiker
  (hiker_id, age, mail, name, surname)
VALUES ('a3e69f82-0645-4ae4-9f44-a46de25dbea6',
        100,
        'test4@mail.com',
        'name4',
        'surname4')
ON CONFLICT (hiker_id) DO NOTHING;
		