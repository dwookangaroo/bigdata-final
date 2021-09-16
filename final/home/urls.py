from django.urls import path
from . import views

app_name = 'final'
urlpatterns = [
    path('', views.home, name='home'),
]