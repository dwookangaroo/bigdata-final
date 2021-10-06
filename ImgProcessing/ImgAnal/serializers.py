
from rest_framework import serializers
from .models import Addresses, Landmarks, Hotels, Restaurants, MyImage


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
        fields = ['name', 'lat', 'lng', 'english_name']


class HotelsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Hotels
        fields = ['rating','name', 'address', 'lat', 'lng','english_rating','english_name','english_address']


class RestaurantsSerializer(serializers.ModelSerializer):
    class Meta:
        model = Restaurants
        fields = ['name','representative', 'address', 'lat', 'lng','english_name','english_representative','english_address']
