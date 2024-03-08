import csv
import random

def gen_rating():
    return 5.0 * random.random()

def gen_bool():
    return random.randint(0, 1)

companies = {}
class Info:
    def __init__(self, cid, uid):
        self.cid = cid
        self.uid = uid
        self.position = repr(random.choice(["Intern", "Co-op Student"]))
        self.rating = gen_rating()

    def to_sql(self):
        s = []
        s.append(f"INSERT INTO Info (cid, uid, position, rating) VALUES ({self.cid}, {self.uid}, {self.position}, {self.rating});")
        s.append("\n")
        return s

class Benefits:
    def __init__(self, cid, uid):
        self.cid = cid
        self.uid = uid
        self.insurance = gen_bool()
        self.housing = 10 * random.randint(0, 250)
        self.transportation = 10 * random.randint(0, 50)

    def to_sql(self):
        s = []
        s.append(f"INSERT INTO Benefits (cid, uid, insurance, housing, transportation) VALUES ({self.cid}, {self.uid}, {self.insurance}, {self.housing}, {self.transportation});")
        s.append("\n")
        return s

class Interview:
    def __init__(self, cid, uid):
        self.cid = cid
        self.uid = uid
        self.difficulty = gen_rating()
        self.experience_rating = gen_rating()

    def to_sql(self):
        s = []
        s.append(f"INSERT INTO Interview (cid, uid, difficulty, experience_rating) VALUES ({self.cid}, {self.uid}, {self.difficulty}, {self.experience_rating});")
        s.append("\n")
        return s

class Requirements:
    def __init__(self, cid, uid):
        self.cid = cid
        self.uid = uid
        self.security_clearance = gen_bool()
        self.SWPP = gen_bool()
        self.transportation = repr(random.choice(["None", "G2", "G"]))

    def to_sql(self):
        s = []
        s.append(f"INSERT INTO Requirements (cid, uid, security_clearance, SWPP, transportation) VALUES ({self.cid}, {self.uid}, {self.security_clearance}, {self.SWPP}, {self.transportation});")
        s.append("\n")
        return s

class Data:
    def __init__(self, uid):
        self.company = random.choice(list(companies.keys()))
        self.cid = companies[self.company]
        self.uid = uid
        
        self.info = Info(self.cid, self.uid)
        self.benefits = Benefits(self.cid, self.uid)
        self.interview = Interview(self.cid, self.uid)
        self.requirements = Requirements(self.cid, self.uid)

    def to_sql(self):
        s = []
        s += self.info.to_sql()
        s += self.benefits.to_sql()
        s += self.interview.to_sql()
        s += self.requirements.to_sql()
        s.append("\n")
        return s

class Company:
    def __init__(self, cid, name):
        self.cid = cid
        self.name = repr(name)

    def to_sql(self):
        s = []
        s.append(f"INSERT INTO Companies (cid, name) VALUES ({self.cid}, {self.name});")
        s.append("\n")
        return s


# Assuming the CSV file has headers
i = 0
with open('data.csv', 'r', encoding='utf-8') as file:
    # Create a CSV reader object
    csv_reader = csv.reader(file)

    # Read and print each row
    for row in csv_reader:
        i += 1
        print(i)
        if i == 1:
            headers = row
        else:
            companies[row[0]] = i

        i += 1

reviews: Data = []
for j in range(1000):
    reviews.append(Data(j))

comp_data = []
for cname in companies:
    comp_data.append(Company(companies[cname], cname))

with open("sql_tables.txt", 'w') as f:
    for c in comp_data:
        f.writelines(c.to_sql())

with open("sql_data.txt", 'w') as f:
    for r in reviews:
        f.writelines(r.to_sql())

