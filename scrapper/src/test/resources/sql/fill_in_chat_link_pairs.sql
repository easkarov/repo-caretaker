INSERT INTO chat(id)
VALUES (1),
       (2),
       (3),
       (4);

INSERT INTO link(id, url)
VALUES (2222, 'http://stackoverflow.com/12312313'),
       (2223, 'https://github.com/gram3r/tinkoff-java'),
       (2224, 'https://stackoverflow.com/7777777');

INSERT INTO chat_link
VALUES (1, 2222),
       (1, 2223),
       (2, 2222),
       (3, 2222);