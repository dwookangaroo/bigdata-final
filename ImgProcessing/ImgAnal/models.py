from django.db import models

# Create your models here.

class Image(models.Model):
    id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    image = models.ImageField(upload_to='media/images')

class MyImage(models.Model):
    model_pic = models.ImageField(upload_to='media/images')

# class Photo(models.Model):
#     id = models.AutoField(primary_key=True)
#     title = models.CharField(max_length=255, blank=True)
#     photo = models.FileField(upload_to='photos')



class Addresses(models.Model):
    objects = None
    name = models.CharField(max_length=10)
    phone_number = models.CharField(max_length=13)
    address = models.TextField()
    created = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['created']