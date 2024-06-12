INSERT INTO TOPICS (id, title, description)
SELECT * FROM (
    SELECT 1 AS id, 'Visuels' AS title, 'Thème lié aux librairies associées aux visuels' AS description UNION ALL
    SELECT 2, 'Architectures', 'Thème lié aux librairies associées aux architectures' UNION ALL
    SELECT 3, 'Tests', 'Thème lié aux librairies associées aux tests' UNION ALL
    SELECT 4, 'Paradigmes', 'Thème lié aux librairies associées aux paradigmes de programmation'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM TOPICS);