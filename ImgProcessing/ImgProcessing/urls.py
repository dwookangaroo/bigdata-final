"""ImgProcessing URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
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

from django.conf.urls import include, url
from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static
from rest_framework import routers
from ImgAnal import views

urlpatterns = [
    path('img_processing/', views.image_upload_view, name='img_process'),
    path('addresses/', views.address_list),
    path('addresses/<int:pk>/', views.address),
    path('login/', views.login),
    path('app_login/', views.app_login),
    path('app_register/', views.app_register),
    path('admin/', admin.site.urls),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    path('upload/', views.ImageCreateAPIView.as_view(), name='upload'),
    path('hotel/', views.hotel_recommendation, name='hotel'),
    path('restaurant/', views.restaurant_recommendation, name='restaurant'),
    path('landmark/', views.landmark_information, name='landmark'),
    path('seoul_landmarks/', views.landmark_seoul, name='seoul_landmarks')
]


if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL,
                          document_root=settings.MEDIA_ROOT)



