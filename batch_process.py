import os
files_easy=next(os.walk('Images/Easy'))[2]
files_medium=next(os.walk('Images/Medium'))[2]
files_difficult=next(os.walk('Images/Difficult'))[2]

len_easy=len(files_easy)
len_medium=len(files_medium)
len_difficult=len(files_difficult)

for i in range(len_easy):
	cmd='./code Images/Easy/'+files_easy[i]+' '+files_easy[i]
	os.system(cmd)

for i in range(len_medium):
	cmd='./code Images/Medium/'+files_medium[i]+' '+files_medium[i]
	os.system(cmd)

for i in range(len_difficult):
	cmd='./code Images/Difficult/'+files_difficult[i]+' '+files_difficult[i]
	os.system(cmd)
