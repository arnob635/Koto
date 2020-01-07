from django.db import models


#user information
class kotoUser(models.Model):
    id = models.AutoField(primary_key=True)
    monthlyExpencess = models.IntegerField(default=0)
    profession = models.CharField(max_length=250)

    def __int__(self):
        return self.id

    

# Additional user information from google login
class googleCredentials(models.Model):
    kotoUser=models.ForeignKey(kotoUser,on_delete=models.CASCADE)
    email = models.EmailField(max_length = 500)
    dob = models.DateTimeField ('birth date')
    name = models.CharField (max_length = 250)

    def __str__(self):
        return self.name




#User transactions
class transaction(models.Model):
    kotoUser=models.ForeignKey(kotoUser,on_delete=models.CASCADE)
    transId = models.AutoField(primary_key=True)
    date = models.DateField(auto_now_add = True)
    isExpence = models.BooleanField(default = True)
    amount = models.FloatField()
    Details = models.CharField(max_length=1000)



#For storing images of reciepts and json data extracted from the image
class receipt(models.Model):
    transaction = models.ForeignKey(transaction,on_delete=models.CASCADE)
    jsonString = models.CharField(max_length=1000)
    image = models.FileField()

class income(models.Model):
    transaction = models.ForeignKey(transaction,on_delete=models.CASCADE)
    amount = models.IntegerField()
    comments = models.CharField(max_length=250)
    type = models.CharField(max_length = 1000)

class expences(models.Model):
    transaction = models.ForeignKey(transaction,on_delete=models.CASCADE)
    amount = models.IntegerField()
    comments = models.CharField(max_length=250)
    type = models.CharField(max_length = 1000)
