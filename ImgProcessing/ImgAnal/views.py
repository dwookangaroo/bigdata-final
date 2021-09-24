import json
import time
import os
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
from .models import Addresses, MyImage
from .serializers import AddressesSerializer, ImageSerializer
from rest_framework.parsers import JSONParser
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
import re
# from .forms import ImageForm
from tensorflow.keras.models import Sequential, load_model
from PIL import Image
import numpy as np

# caltech_dir = "D:/bigdata_project/ImgProcessing/static/final_test"
pre_ans_str = ''
new_model = load_model('model/trained_model.h5')


class ImageCreateAPIView(CreateAPIView):
    serializer_class = ImageSerializer
    queryset = MyImage.objects.all()


def image_upload_view(request):
    """Process images uploaded by users"""
    # if request.method == 'POST':
    #     form = ImageForm(request.POST, request.FILES)
    #
    #     if form.is_valid():
    #         form.save()
    #         # Get the current instance object to display in the template
    #         img_obj = form.instance

    time.sleep(1)
    file = r'D:\bigdata_project\ImgProcessing\media\media\images\testFile.jpg'

    X = []
    img = Image.open(file)
    img = img.resize((224, 224))
    img = img.convert("RGB")
    data = np.asarray(img)
    X.append(data)

    X = np.array(X)
    new_prediction = new_model.predict(X)
    pre_ans = new_prediction[0].argmax()  # 예측 레이블

    if pre_ans == 0:
        pre_ans_str = "63buliding"
    elif pre_ans == 1:
        pre_ans_str = "castle"
    elif pre_ans == 2:
        pre_ans_str = "coex"
    elif pre_ans == 3:
        pre_ans_str = "general"
    elif pre_ans == 4:
        pre_ans_str = "indep_door"
    elif pre_ans == 5:
        pre_ans_str = "judgement_castle"
    elif pre_ans == 6:
        pre_ans_str = "lotte_tower"
    elif pre_ans == 7:
        pre_ans_str = "moonlight"
    elif pre_ans == 8:
        pre_ans_str = "namsan"
    elif pre_ans == 9:
        pre_ans_str = "seoulstation"
    elif pre_ans == 10:
        pre_ans_str = "tapgol_park"
    else:
        pre_ans_str = "what the!!"

    if os.path.exists(file):
        os.remove(r'D:\bigdata_project\ImgProcessing\media\media\images\testFile.jpg')

    else:
        pass

    return render(request, 'index.html', {'pre_ans_str': pre_ans_str})
    # else:
    #     form =ImageForm()
    # return render(request, 'index.html', {'form': form,})


# Create your views here.
@csrf_exempt
def address_list(request):
    if request.method == 'GET':
        query_set = Addresses.objects.all()
        serializer = AddressesSerializer(query_set, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = AddressesSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


@csrf_exempt
def address(request, pk):
    obj = Addresses.objects.get(pk=pk)

    if request.method == 'GET':
        serializer = AddressesSerializer(obj)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = AddressesSerializer(obj, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        obj.delete()
        return HttpResponse(status=204)


@csrf_exempt
def login(request):
    if request.method == 'POST':
        print("리퀘스트 로그" + str(request.body))
        id = request.POST.get('userid', '')
        pw = request.POST.get('userpw', '')
        print("id = " + id + "pw = " + pw)

        result = authenticate(username=id, password=pw)

        if result:
            print("로그인 성공!")
            return HttpResponse(status=200)
        else:
            print("실패")
            return HttpResponse(status=401)

    return render(request, 'addresses/login.html')


@csrf_exempt
def app_login(request):
    if request.method == 'POST':
        print("리퀘스트 로그" + str(request.body))
        id = request.POST.get('userid', '')
        pw = request.POST.get('userpw', '')
        print("id = " + id)
        print("pw = " + pw)

        result = authenticate(username=id, password=pw)
        id_manuel = re.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$")  # id는 이메일 형식
        pw_manuel = re.compile("^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$")

        if not id:
            print("아이디 입력 안함")
            return JsonResponse({'code': '0000', 'msg': '아이디를 입력해주세요!'}, status=400)
        elif id_manuel.match(id) is None:
            print("올바르지 않은 이메일 형식")
            return JsonResponse({'code': '1002', 'msg': '아이디 또는 비밀번호가 일치하는 정보가 없습니다!'}, status=402)
            print("비밀번호 입력 안함")
            return JsonResponse({'code': '2000', 'msg': '비밀번호를 입력해주세요!'}, status=403)
        elif pw_manuel.match(pw) is None:
            print("비밀번호 정규식 오류")
            return JsonResponse({'code': '2001', 'msg': '아이디 또는 비밀번호가 일치하는 정보가 없습니다!'}, status=404)
        elif result:
            print("로그인 성공!")
            return JsonResponse({'code': '0000', 'msg': '로그인 성공입니다!'}, status=200)
        else:
            print("로그인 실패!")
            return JsonResponse({'code': '0000', 'msg': '로그인 실패입니다!'}, status=201)


@csrf_exempt
def app_register(request):
    if request.method == 'POST':
        print("리퀘스트 로그" + str(request.body))
        id = request.POST.get('userid', '')
        pw = request.POST.get('userpw', '')
        pw2 = request.POST.get('userpw2', '')
        print("id = " + id)
        print("pw = " + pw)
        print("pw2 = " + pw2)

        id_manuel = re.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$")  # id는 이메일 형식
        pw_manuel = re.compile("^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$")

        if not id:
            print("아이디 입력 안함")
            return JsonResponse({'code': '1000', 'msg': '아이디를 입력해주세요!'}, status=400)
        elif User.objects.filter(username=id).exists():
            print("아이디 중복")
            return JsonResponse({'code': '1001', 'msg': '아이디 중복!!!!!'}, status=401)
        elif id_manuel.match(id) is None:
            print("올바르지 않은 이메일 형식")
            return JsonResponse({'code': '1002', 'msg': '아이디를 올바른 이메일 형식으로 써주세요!'}, status=402)
        elif not pw:
            print("패스워드 입력 안함")
            return JsonResponse({'code': '2000', 'msg': '패스워드를 입력해주세요!'}, status=403)
        elif pw_manuel.match(pw) is None:
            print("비밀번호 정규식 오류")
            return JsonResponse({'code': '2001', 'msg': '비밀번호를 최소 8자리 이상, 하나의 문자, 숫자/특수 문자를 넣어 주세요!'}, status=404)
        elif not pw2:
            print("패스워드2 확인 안함")
            return JsonResponse({'code': '3000', 'msg': '비밀번호 확인을 해주세요!'}, status=405)
        elif pw2 != pw:
            print("비밀번호 같지 않음")
            return JsonResponse({'code': '3001', 'msg': '비밀번호가 일치하지가 않습니다!'}, status=406)
        else:
            print("올바른 계정입력 형식")
            member = User(
                username=id
            )
            member.set_password(pw)
            member.save()
            return JsonResponse({'code': '0000', 'msg': '계정 사용 가능!'}, status=200)
