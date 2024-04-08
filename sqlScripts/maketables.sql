/****** Object:  Table [Benefit]    Script Date: 2024-04-07 12:12:24 PM ******/
GO
CREATE TABLE Benefit(
	b_id int NOT NULL,
	b_name varchar(255) NOT NULL,
    PRIMARY KEY (b_id));

/****** Object:  Table [Company]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE Company(
	c_id int NOT NULL,
	c_name varchar(255) NOT NULL,
	PRIMARY KEY (c_id));

/****** Object:  Table [Import_Sheet]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE Import_Sheet(
	student_name text NULL,
	workterm_number int NULL,
	Position text NULL,
	Company text NULL,
	salary real NULL,
	Benefits text NULL,
	Report_Year int NULL
);

/****** Object:  Table [Position]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE Position(
	p_id [int] NOT NULL,
	position_name varchar(255) NOT NULL,
	c_id int NOT NULL,
PRIMARY KEY (p_id));

/****** Object:  Table [Program]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE Program(
	p_id int NOT NULL,
	program_name varchar(255) NOT NULL,
PRIMARY KEY (p_id));

/****** Object:  Table [Report]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE [Report](
	r_id int NOT NULL,
	s_id int NOT NULL,
	p_id int NOT NULL,
PRIMARY KEY (r_id));

/****** Object:  Table [ReportBenefit]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE ReportBenefit(
	r_id int NOT NULL,
	b_id int NOT NULL,
	quantity float NULL,
	comment text NULL
);

/****** Object:  Table [ReportInfo]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE ReportInfo(
	r_id int NOT NULL,
	p_id int NOT NULL,
	rating float NOT NULL,
	report_date date NOT NULL,
	comment text NULL,
	student_year int NULL,
	student_semester int NULL,
	student_workterm_number int NULL
);

/****** Object:  Table [Student]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE Student(
	s_id int NOT NULL,
	username varchar(63) NOT NULL,
PRIMARY KEY (s_id));

/****** Object:  Table [StudentPersonalInfo]    Script Date: 2024-04-07 12:12:24 PM ******/
CREATE TABLE StudentPersonalInfo(
	s_id int NOT NULL,
	student_name varchar(255) NULL,
	birthday date NULL,
	gender varchar(255) NULL,
	Email varchar(255) NULL,
	pwd varchar(255) NULL
);

ALTER TABLE Position  WITH CHECK ADD FOREIGN KEY([c_id])
REFERENCES Company([c_id])
GO
ALTER TABLE [Report]  WITH CHECK ADD FOREIGN KEY([p_id])
REFERENCES [Position] ([p_id])
GO
ALTER TABLE [Report]  WITH CHECK ADD FOREIGN KEY([s_id])
REFERENCES [Student] ([s_id])
GO
ALTER TABLE [ReportBenefit]  WITH CHECK ADD FOREIGN KEY([b_id])
REFERENCES [Benefit] ([b_id])
GO
ALTER TABLE [ReportBenefit]  WITH CHECK ADD FOREIGN KEY([r_id])
REFERENCES [Report] ([r_id])
GO
ALTER TABLE [ReportInfo]  WITH CHECK ADD FOREIGN KEY([p_id])
REFERENCES [Program] ([p_id])
GO
ALTER TABLE [ReportInfo]  WITH CHECK ADD FOREIGN KEY([r_id])
REFERENCES [Report] ([r_id])
GO
ALTER TABLE [StudentPersonalInfo]  WITH CHECK ADD FOREIGN KEY([s_id])
REFERENCES [Student] ([s_id])
GO
