# Loading the pyPdf Library
from pyPdf import PdfFileWriter, PdfFileReader
import sys

def main(files):
	fs = str(files).replace("[","").replace("]","").replace(",","").replace("\'","").split(" ")

	#this.py <filename> <pdf1> <pdf2> 
	mergeName = str(fs[0])
	pdf1 = fs[1]
	pdf2 = fs[2]
	output = PdfFileWriter()
	append_pdf(PdfFileReader(file(str(pdf1),"rb")),output)
	append_pdf(PdfFileReader(file(str(pdf2),"rb")),output)

	output.write(file(mergeName,"wb"))

def append_pdf(input,output):
	[output.addPage(input.getPage(page_num)) for page_num in range(input.numPages)] #this is a routine, fancy stuff

if __name__ == '__main__':
	main(sys.argv[1:])
