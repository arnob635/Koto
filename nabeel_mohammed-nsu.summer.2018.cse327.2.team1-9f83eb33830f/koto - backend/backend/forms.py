from django.contrib.auth.models import User
from django import forms

class UserForm(forms.ModelForm):
    # hides the password characters so hackers can't see or change the password
    password = forms.CharField(widget = forms.PasswordInput)

    # class Meta contains information about the UserForm class
    class Meta:
        model = User
        fields = ['username', 'email', 'password'] # not interested in any other field such as first name etc.
