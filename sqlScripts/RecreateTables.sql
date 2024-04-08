DROP TABLE Import_Sheet;
DROP TABLE StudentPersonalInfo;
DROP TABLE ReportInfo;
DROP TABLE ReportBenefit;
DROP TABLE Report;
DROP TABLE Position;
DROP TABLE Student;
DROP TABLE Prog;
DROP TABLE Company;
DROP TABLE Benefit;

--------------------------------------------------------------------

CREATE TABLE Benefit(
    b_id int NOT NULL,
    b_name varchar(255),

    PRIMARY KEY (b_id)
);

CREATE TABLE Company(
    c_id int NOT NULL,
    c_name varchar(255),

    PRIMARY KEY (c_id)
);

CREATE TABLE Prog(
    p_id int NOT NULL,
    prog_name varchar(255),

    PRIMARY KEY (p_id)
);

CREATE TABLE Student(
    s_id int NOT NULL,
    username varchar(63),

    PRIMARY KEY (s_id)
);

--------------------------------------------------------------------

CREATE TABLE Position(
    p_id int NOT NULL,
    position_name varchar(255),
    salary float,

    c_id int,
    
    PRIMARY KEY (p_id),
    FOREIGN KEY (c_id) REFERENCES Company(c_id)
);

CREATE TABLE Report(
    r_id int NOT NULL,

    s_id int NOT NULL,
    p_id int NOT NULL,

    PRIMARY KEY (r_id),
    FOREIGN KEY (s_id) REFERENCES Student(s_id),
    FOREIGN KEY (p_id) REFERENCES Position(p_id)
);

--------------------------------------------------------------------

CREATE TABLE ReportBenefit(
    quantity float,
    comment text,

    b_id int not NULL,
    r_id int not NULL,

    FOREIGN KEY (b_id) REFERENCES Benefit(b_id),
    FOREIGN KEY (r_id) REFERENCES Report(r_id)
);

CREATE TABLE ReportInfo(
    rating float,
    report_date date,
    comment text,
    student_year int,
    student_semester int,
    student_workterm_number int,

    p_id int NOT NULL,
    r_id int NOT NULL,

    FOREIGN KEY (p_id) REFERENCES Prog(p_id),
    FOREIGN KEY (r_id) REFERENCES Report(r_id)
);

CREATE TABLE StudentPersonalInfo(
    student_name varchar(63),
    birthday date,
    gender varchar(255),
    email varchar(255) NOT NULL,
    pwd int NOT NULL,

    s_id int NOT NULL
    
    FOREIGN KEY (s_id) REFERENCES Student(s_id)
);

--------------------------------------------------------------------

CREATE TABLE Import_Sheet(
    student_name varchar(63),
    workterm_number int,
    Position varchar(63),
    Company varchar(63),
    salary float,
    Benefits text,
    Report_Year int
);

--------------------------------------------------------------------
