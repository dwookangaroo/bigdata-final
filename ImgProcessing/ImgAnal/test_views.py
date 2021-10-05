import json
import time
import os

import pymysql
import simplejson
from django.contrib.auth.decorators import login_required
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.generics import CreateAPIView
from rest_framework.response import Response
from rest_framework.views import APIView

from .forms import ImageForm
from .models import Addresses, MyImage, Landmarks
from .serializers import AddressesSerializer, ImageSerializer
from rest_framework.parsers import JSONParser
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
import re
# from .forms import ImageForm
from tensorflow.keras.models import Sequential, load_model
from PIL import Image
import numpy as np
from haversine import haversine


def hotel_view(request):
    awb_dict = {'롯데호텔롯데월드': (37.511534, 127.099981),
 '라자호텔': (37.515367, 127.108224),
 'CF호텔': (37.5153507, 127.1092966),
 '톰지모텔': (37.515546, 127.10954),
 '레이크호텔': (37.50683, 127.100985)}


    return HttpResponse(simplejson.dumps(awb_dict))
