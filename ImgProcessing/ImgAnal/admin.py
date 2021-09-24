from django.contrib import admin

# Register your models here.
from ImgAnal.models import MyImage, Addresses

admin.site.register(MyImage)
admin.site.register(Addresses)
