"""
Mario Luis Aguayo, Jr.
(2015) mlaguayojr@gmail.com
Python 2.7
https://github.com/mlaguayojr/pdfutil
"""

from pyPdf import PdfFileWriter, PdfFileReader #load the pyPdf library
import sys, os

def main(files):
	fs = str(files).replace("[","").replace("]","").replace(",","").replace("\'","").split(" ")
	if(fs[0] in ("-merge")): #allowed commands, incase if i want to add a splitter or something
		if str(fs[0]) == "-merge":
			merge(fs[1:])
	else:
		print "Syntax error:\n"
		print "pdf.exe <-merge> -o <output file> -i <pdf files to merge>"
		print "! only outputs to one file (output is sequential of input)"

def merge(params):

	fout = str(params[(params.index("-o")+1)])	#output file
	fin = params[(params.index("-i"))+1:]	#input files
	# print "File output:",fout #debug
	# print "File inputs:",fin #debug

	output = PdfFileWriter()

	for i in fin:
		fixed = ""
		if "*" in i:
			fixed = os.path.join(''.join(str(i).replace("*"," ")))
		else:
			fixed = os.path.join(''.join(str(i)))

		# print "reading",fixed #debug
		appendPages(PdfFileReader(file(fixed,"rb")),output)

	output.write(file(fout,"wb"))

def appendPages(inFile,output):
	for page_num in range(inFile.numPages):
		output.addPage(inFile.getPage(page_num))

if __name__ == '__main__':
	main(sys.argv[1:])
