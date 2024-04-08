import csv

def convert(tablename):
    csvfilepath = "datain\\" + tablename + ".csv"
    sqlfilepath = "dataout\\" + tablename + ".sql"

    i = 0
    headers = []
    tabledefstr = ""
    with open(csvfilepath, 'r') as csvfile:
        csv_reader = csv.reader(csvfile)
        
        with open(sqlfilepath, 'w') as sqlfile:
            # Read and print each row
            for row in csv_reader:
                i += 1
                print(i)
                if i == 1:
                    headers = tuple(row)
                    tabledefstr += f"INSERT INTO {tablename}("
                    tabledefstr += str(headers[0])
                    
                    for i in range(1, len(headers)):
                        tabledefstr += ", "
                        tabledefstr += str(headers[i])
                    
                    tabledefstr += f") VALUES "
                else:
                    sqlfile.write(tabledefstr)
                    sqlfile.write('(')
                    
                    try:
                        float(row[0])
                        sqlfile.write(f"{row[0]}")
                    except:
                        sqlfile.write(f"\'{row[0]}\'")

                    for j in range(1, len(row)):
                        try:
                            if row[j] != "NULL":
                                float(row[j])
                            sqlfile.write(f", {row[j]}")
                        except:
                            sqlfile.write(f", \'{row[j]}\'")
                            
                    sqlfile.write(");\n")
                
    print("Done writing", tablename)

convert("Company")
convert("Import_Sheet")
convert("Position")
convert("Program")
convert("Report")
convert("ReportInfo")
convert("Student")
convert("StudentPersonalInfo")
