from django.contrib import admin

# Register your models here.
from ImgAnal.models import Addresses, Landmarks, Hotels, Restaurants, Image, MyImage

admin.site.register(Image)
admin.site.register(MyImage)
admin.site.register(Addresses)
admin.site.register(Landmarks)
admin.site.register(Hotels)
admin.site.register(Restaurants)
