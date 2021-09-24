
from rest_framework import serializers
from .models import Addresses, MyImage

class AddressesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Addresses
        fields = ['name', 'phone_number', 'address', 'created']


class ImageSerializer(serializers.ModelSerializer):

    class Meta:
        model = MyImage
        fields = ['model_pic']
