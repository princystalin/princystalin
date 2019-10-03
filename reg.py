from flask import Flask, request ,send_from_directory,jsonify
import random
import socket
import json
import sqlite3
import hashlib
import re
import os
import base64

ip = "http://192.168.43.8:8080"
#veg_list,nonveg_list = create_list()
description = "Lorem ipsum dolor sit amet, proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
reg = Flask(__name__, static_url_path='' )
conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists Registration(name TEXT,email TEXT,phone INTEGER(11),password TEXT,street INTEGER,address TEXT,city TEXT,pincode INTEGER,PRIMARY KEY (phone))')
	conn.close()
except :
	print("Error ikde")
	conn.close()

conn = sqlite3.connect('database.db')
try:
	conn.execute('CREATE TABLE if not exists images(phone TEXT, images TEXT, product TEXT, quantity TEXT)')
	conn.close()
except:
	print("Error")
	conn.close()
	
@reg.route('/login', methods = ['POST', 'GET'])
def login():
	if request.method == 'POST':
		print("aapan loginvar aahe")
		content = request.json
		phone = content['phone']
		password = content['pass']
		print(phone,password)
		result = (hashlib.md5(password.encode())).hexdigest()
		with sqlite3.connect("database.db") as conn:
			cur = conn.cursor()
			print("Sql query aapan execute karnar aahe")
			cur.execute("SELECT * from Registration where phone = ? and password = ?", (phone, result))
			rows = cur.fetchall();
			if(rows):
				print(rows)
				print("Login Success")
				return "1"
			else:
				print("unsuccessful login sad :(")
				return "0"
	


@reg.route('/registration', methods = ['POST', 'GET'])
def registration():
	print("call hua bhava")
	if request.method == 'POST':
		content = request.json
		print(content)
		email= content['email']
		password= content['pass']
		name = content['name']
		phone = content['phone']
		phone=int(phone)
		city = content['city']
		pincode = content['pin']
		pincode = int(pincode)
		address = content['address']
		street = content['street']
		#check whether parameters are blank
		if(email=="" or password=="" or name=="" or city=="" or phone=="" or pincode=="" or address=="" or street==""):
			return "One of the parameters is blank."
		else:
			if not re.match(r"[^@]+@[^@]+\.[^@]+", email): #check format of email
				return "Invalid Email"
			else:
				result = (hashlib.md5(password.encode())).hexdigest()
				with sqlite3.connect("database.db") as conn:
					cur = conn.cursor()
					cur.execute("SELECT * FROM registration where phone = ?", (phone,)) #check whether user is present or not
					rows = cur.fetchall();
					if(rows):
						return "User already exists."
					else:
						cur = conn.cursor()
						cur.execute("INSERT INTO registration VALUES (?, ?, ?, ?, ?, ?, ?, ?)",(name,email,phone,result,street,address,city,pincode)) #insert
						conn.commit()
						print("Successful")
						return "1" #registration success

@reg.route('/upload',methods=['GET','POST'])
def upload():
	if request.method == 'POST':
		content = request.json
		images= content['images']
		product= content['product']	
		phone = content['phone']
		phone = str(phone)
		quantity = content['quantity']
		quantity = str(quantity)
		if (images=="" or product==""):
			return "parameter is blank"
		else:
			print(images)
			print(product)
			print(quantity)
			with sqlite3.connect("database.db") as conn:
				cur = conn.cursor()
				cur.execute("INSERT INTO images VALUES (?, ?, ? , ?)",(phone,images,product,quantity)) #insert
				conn.commit()
				#return "1"				#registration success
			encoded = images.encode()
			if(not os.path.exists(phone)):
				os.mkdir(phone)
			with open(phone+"/"+product+".png", "wb") as fh:
				fh.write(base64.decodebytes(encoded))
				return "1"

	else:
		return "Y U NO USE POST?"


if __name__ == '__main__':
   reg.run('0.0.0.0',8080,True)

