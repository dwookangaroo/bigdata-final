from django.db import models

# Create your models here.

class Image(models.Model):
    title = models.CharField(max_length=200)
    image = models.ImageField(upload_to='media/images')

    def __str__(self):
        return self.title

# class Post(models.Model):
#     title = models.CharField(max_length=200)
#     text = models.TextField()
