CREATE TABLE DEPARTMENT (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255));
CREATE TABLE EMPLOYEE (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), department_id INTEGER);
ALTER TABLE EMPLOYEE ADD CONSTRAINT FKbejtwvg9bxus2mffsm3swj3u9 FOREIGN KEY (department_id) REFERENCES department;


INSERT INTO DEPARTMENT (name) VALUES ('dep1');
INSERT INTO DEPARTMENT (name) VALUES ('dep2');
INSERT INTO DEPARTMENT (name) VALUES ('dep3');
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Bob', 1);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Dan', 1);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Foo', 2);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Baz', 3);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Bar', 3);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Tik', 1);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Tak', 2);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Tok', 3);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Man', 2);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Nac', 1);
INSERT INTO EMPLOYEE (name, department_id) VALUES ('Lot', 1);