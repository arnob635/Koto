from rest_framework import serializers
from django.contrib import admin
from.models import googleCredentials
from.models import kotoUser
from.models import transaction
from.models import receipt
from.models import income
from.models import expences

# Serialize json Data
class kotoUserserializer(serializers.ModelSerializer):

    class Meta:
        model = kotoUser
        #fields = ('monthlyExpencess','profession')
        fields = '__all__'
        depth = 1

class transactionserializer(serializers.ModelSerializer):

    #expences = serializers.PrimaryKeyRelatedField(many =True, read_only=True)

    class Meta:
        model = transaction

        fields = ('Details','isExpence','date','amount')
        #fields = '__all__'
        depth = 2


