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
    lat = models.FloatField()
    lng = models.FloatField()
    english_name = models.CharField(default = '',max_length=255)


class Hotels(models.Model):
    rating = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    lat = models.FloatField()
    lng = models.FloatField()
    english_rating = models.CharField(default = '',max_length=255)
    english_name = models.CharField(default = '',max_length=255)
    english_address = models.CharField(default = '',max_length=255)


class Restaurants(models.Model):
    name = models.CharField(max_length=255)
    represent = models.CharField(max_length=255)
    address = models.CharField(max_length=255)
    lat = models.FloatField()
    lng = models.FloatField()
    english_name = models.CharField(default = '',max_length=255)
    english_represent = models.CharField(default = '',max_length=255)
    english_address = models.CharField(default = '',max_length=255)

#
#
class Addresses(models.Model):
    objects = None
    name = models.CharField(max_length=10)
    phone_number = models.CharField(max_length=13)
    address = models.TextField()
    created = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['created']