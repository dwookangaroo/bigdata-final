from django.db import models

# Create your models here.

class Image(models.Model):
    id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=100)
    image = models.ImageField(upload_to='media/images')

class MyImage(models.Model):
    model_pic = models.ImageField(upload_to='media/images')

class Landmarks(models.Model):
    name = models.CharField(max_length=255)
    #desc = models.TextField()
    #address = models.CharField(max_length=10, default='')
    lat = models.FloatField()
    lng = models.FloatField()


class Hotels(models.Model):
    rating = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    lat = models.FloatField()
    lng = models.FloatField()


class Restaurants(models.Model):
    name = models.CharField(max_length=255)
    represent = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    lat = models.FloatField()
    lng = models.FloatField()



class Addresses(models.Model):
    objects = None
    name = models.CharField(max_length=10)
    phone_number = models.CharField(max_length=13)
    address = models.TextField()
    created = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['created']