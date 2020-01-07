from django.contrib import admin
from.models import googleCredentials
from.models import kotoUser
from.models import transaction
from.models import receipt
from.models import income
from.models import expences



admin.site.register(googleCredentials)
admin.site.register(kotoUser)
admin.site.register(transaction)
admin.site.register(receipt)
admin.site.register(income)
admin.site.register(expences)
