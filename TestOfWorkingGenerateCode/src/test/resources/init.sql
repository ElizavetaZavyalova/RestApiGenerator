INSERT INTO Table0
(fieldStr,fieldInt,fieldBool,fieldFloat,fieldName)
VALUES ('String1',101,true,1.101,'Name1'),
       ('String2',102,false,1.102,'Name2'),
       ('String3',103,false,1.103,'Name3'),
       ('String4',101,true,1.101,'Name1'),
       ('String5',103,true,1.103,'Name1'),
       ('String6',102,false,1.102,'Name2'),
       ('String7',104,false,1.104,'Name3'),
       ('String8',108,true,1.08,'Name4');
INSERT INTO Table1
(fieldStr1)
VALUES ('String1'),
       ('String2'),
       ('String3'),
       ('String4'),
       ('String5'),
       ('String6'),
       ('String7'),
       ('String8'),
       ('String9'),
       ('String10');
INSERT INTO Table2
(fieldStr2)
VALUES ('String1'),
       ('String2'),
       ('String3'),
       ('String4'),
       ('String5'),
       ('String6'),
       ('String7'),
       ('String8'),
       ('String9'),
       ('String10');
INSERT INTO table12_ref
( Table1_id, Table2_id)
VALUES (1,2),
       (1,3),
       (8,1),
       (3,4),
       (3,5),
       (5,1),
       (7,1),
       (2,9),
       (1,5),
       (6,8);
INSERT INTO Table4
(fieldStr4)
VALUES ('String1'),
       ('String2'),
       ('String3'),
       ('String4'),
       ('String5'),
       ('String6'),
       ('String7'),
       ('String8'),
       ('String9'),
       ('String10');
INSERT INTO Table3
(fieldStr3, Table4_id)
VALUES ('String1',1),
       ('String2',3),
       ('String3',2),
       ('String4',4),
       ('String5',6),
       ('String6',5),
       ('String7',8),
       ('String8',7),
       ('String9',10),
       ('String10',9);
INSERT INTO Table5
(fieldStr5)
VALUES ('String1'),
       ('String2'),
       ('String3'),
       ('String4'),
       ('String5'),
       ('String6'),
       ('String7'),
       ('String8'),
       ('String9'),
       ('String10');
INSERT INTO table5_ref
( Table5R_id, Table5L_id)
VALUES (1,2),
       (1,3),
       (8,1),
       (3,4),
       (3,5),
       (5,1),
       (7,1),
       (2,9),
       (1,5),
       (6,9);
INSERT INTO Table6
(fieldStr6, Table6_id)
VALUES ('String1',null),
       ('String2',1),
       ('String3',2),
       ('String4',2),
       ('String5',3),
       ('String6',5),
       ('String7',4),
       ('String8',1),
       ('String9',6),
       ('String10',8);
