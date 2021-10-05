
from rest_framework import serializers
from .models import Addresses, MyImage, Landmarks, Restaurants, Hotels


class AddressesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Addresses
        fields = ['name', 'phone_number', 'address', 'created']


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = MyImage
        fields = ['model_pic']


class LandmarksSerializer(serializers.ModelSerializer):
    class Meta:
        model = Landmarks
        fields = ['name', 'desc', 'address', 'lat', 'lng']


class HotelsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Hotels
        fields = ['rating','name', 'address', 'lat', 'lng']


class RestaurantsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Restaurants
        fields = ['name', 'address', 'lat', 'lng']
