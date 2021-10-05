from django.contrib import admin

# Register your models here.
from ImgAnal.models import MyImage, Addresses, Landmarks, Restaurants, Hotels

admin.site.register(MyImage)
admin.site.register(Addresses)
admin.site.register(Landmarks)
admin.site.register(Restaurants)
admin.site.register(Hotels)
