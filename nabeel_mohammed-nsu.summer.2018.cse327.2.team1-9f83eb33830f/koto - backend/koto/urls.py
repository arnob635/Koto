"""koto URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.conf import settings
from django.conf.urls.static import static
from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from backend import views


urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^users/', views.userList.as_view()),

    url(r'^transactions/',views.userTransactions.as_view()),

    url(r'^transactions/(?P<pk>[0-9]+)/$',views.userTransactions.as_view()),
    url(r'^register/$', views.UserFormView.as_view(), name = 'register'), # form register
    url("^purchases/(?P<tid>.+)/$", views.TransactionList.as_view()),

    url(r'^register/', views.UserFormView.as_view(), name = 'register'), # form register

#    url(r'^api-auth/', include('rest_framework.urls')),
#    url(r'^user_login/', views.User_Login, name='user_login'),
#    url(r'^user_logout/', views.user_logout, name='logout_user'),

    #url(r'^transactions/(?P<request>[0-9]+)/$',views.userTransactions),
    #url(r'^register/$', views.UserFormView.as_view(), name = 'register'),# form register

    #url(r'^register/', views.UserFormView.as_view(), name = 'register'),# form register

    #url(r'^api-auth/', include('rest_framework.urls')),

    #url(r'^user_login/', views.user_login, name='user_login'),
   # url(r'^user_logout/', views.user_logout, name='logout_user'),

    #url(r'^user_login/', views.User_Login, name='user_login'),
#    url(r'^user_logout/', views.user_logout, name='logout_user'),

    #url(r'^create_receipt/$', views.create_receipt, name='create_receipt'),
]

urlpatterns = format_suffix_patterns(urlpatterns)

if settings.DEBUG:
    urlpatterns += static(settings.STATIC_URL,document_root=settings.STATIC_ROOT)
    urlpatterns += static(settings.MEDIA_URL,document_root=settings.MEDIA_URL)
