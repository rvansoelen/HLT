import re
import time
start = time.clock()
#pattern = re.compile('<\s*[Pp]\s*ID\s*=\s*(\d*)\s*>(.*?)')
docPat = re.compile('<\s*[Pp]\s*ID\s*=\s*(\d*)\s*>(.*?)</\s*[Pp]\s*>', re.DOTALL)
fid = open('/Users/rvansoelen/Downloads/lesmis.txt')
allText = fid.read()
docs = re.findall(docPat,allText)
wordPat = re.compile('\w+')
invFile = {}
for doc in docs:
	sDict = {}
	docID = int(doc[0])
	words = re.findall(wordPat, doc[1])
	#if(docID == 487):
	#	print doc
	#	print words
	for word in words:
		word = word.lower()
		entry = invFile.get(word)
		if entry == None:
			#add new word entry
			invFile[word] = [(docID, 1)]
		elif docID != entry[-1][0]: #assumes documents are processed in increasing order
			#add new document to word entry
			entry.append((docID, 1))
		else:
			#update entry
			entry[-1] = (docID, 1+entry[-1][1])

stop = time.clock()
print 'Parsing took '+str(stop-start)+' seconds'
trms = raw_input('Please enter search terms, separated by spaces:\n')
terms = re.findall('\w+', trms)
search = []
ctr = []
merging = True
for term in terms:
	term = term.lower()
	entry = invFile.get(term)
	if entry != None:
		search.append(entry)
		ctr.append(0)
	else:
		merging = False
results = []
smallestMatching = 0;
#print search
while merging:
	hasMatch = True;
	for i, entry in enumerate(search):
		if not merging:
			break
		#increment the list to catch up
		length = len(entry)
		while ctr[i]<length and entry[ctr[i]][0] < smallestMatching:
			ctr[i] = ctr[i]+1
			
		if(ctr[i]>=length): #can't match anymore
			merging = False
			hasMatch = False
			break
		docID = entry[ctr[i]][0] 
		if docID != smallestMatching: #overshot current ID, no match
			hasMatch = False
			#update smallestMatching
			smallestMatching = docID
			break
	if hasMatch: #found a match
		results.append(smallestMatching)
		smallestMatching = smallestMatching +1

print 'Document ID Results:'
print results

	

