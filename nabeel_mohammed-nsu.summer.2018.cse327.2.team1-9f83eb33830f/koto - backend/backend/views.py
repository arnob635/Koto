from django.shortcuts import render
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework import status
from rest_framework.response import Response
from rest_framework import generics
from .models import kotoUser
from .models import transaction
from .models import income
from .serializers import kotoUserserializer, transactionserializer
from django.shortcuts import render, redirect # redirects users to page we (Koto team) wants
from django.contrib.auth import authenticate, login # authenticates whether the user exists in the database (via email and username), login makes sure that our users dont have to input password on every page they browse
from django.views.generic import View
from .forms import UserForm
from django.contrib.auth import logout
from django.http import JsonResponse
from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse


IMAGE_FILE_TYPES = ['png', 'jpg', 'jpeg']

# Create your views here.

#rest api requests
class userList(APIView):
    def get(self, request):
        users = kotoUser.objects.all()
        serializers = kotoUserserializer(users, many = True)
        return Response(serializers.data)

    def pos(self):
        pass


class userTransactions(APIView):

   def get(self, request):
        transactionsList = transaction.objects.filter()
        serializers = transactionserializer(transactionsList,many=True)
        return Response(serializers.data)


class TransactionList(APIView):
    t_list = transactionserializer

    def get_queryset(self):
        """
        This view should return a list of all the purchases for
        the user as determined by the username portion of the URL.
        """
        tid = self.kwargs['tid']
        return transaction.objects.filter(transId=tid)



class UserFormView(View):
    form_class = UserForm # blueprint for the form
    template_name = 'HTML/registration_form.html' # template made to include the form in the html

    # (User) getting the form (displays the blank form)
    def get(self, request):
        form = self.form_class(None)
        return render(request, self.template_name, {'form': form})

    # (User) posting the form and process the form data (input) by the user
    # done to keep track of registered users for later authentication
    def post(self, request):
        form = self.form_class(request.POST)

        if form.is_valid():

            user = form.save(commit = False) # creates a user object but still doesnt save it to the DB

            #cleaned (normalized) data so that the users input using the same format
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            user.set_password(password) # allows to modify password
            user.save() # this line's execution saves the user info to the DB

            # returns User objects if credentials are correct
            user = authenticate(username = username, password = password)

            # gives us the power to ban or deactivate user accounts for whatever reason(s)
            if user is not None:

                if user.is_active:
                    login(request, user)
                    return redirect('user:index')

        return render(request, self.template_name, {'form': form})


#def logout_user(request):
    #logout(request)
    #form = UserForm(request.POST or None)
    #context = {
    #    "form": form,
    #}
    #return render(request, 'HTML/user_login.html', context)

#class User_Login(login):

    #login_class = UserLogin # blueprint for the form
    #template_name = 'HTML/user_login.html' # template made to include the form in the html

    #def get(self, request):
    #    login = self.login_class(None)
    #    return render(request, self.template_name, {'login': login})

    def user_login(request):
        if request.method == "POST":
            username = request.POST['username']
            password = request.POST['password']
            user = authenticate(username = username, password = password)

        if user is not None:
            if user.is_active:
                login(request, user)
                kotoUser = kotoUser.objects.filter(user = request.user)
                return render(request, 'HTML/index.html', {'login': 'Home Page'})
            else:
                return render(request, 'HTML/user_login.html', {'error_message': 'Your account has been disabled'})
        else:
            return render(request, 'HTML/user_login.html', {'error_message': 'Invalid login'})

            return render(request, 'HTML/user_login.html')
