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

from .models import Addresses, Landmarks, Hotels, Restaurants, MyImage
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

# caltech_dir = "D:/bigdata_project/ImgProcessing/static/final_test"
pre_ans_str=''
pre_number = 0
landmark_value = 0

# load deeplearning model
new_model = load_model('model/trained_model.h5')

conn = pymysql.connect(host="15.165.104.248",  # ex) '127.0.0.1'
                       port=3306,
                       user="hjs942",  # ex) root
                       password="1234",
                       database="aws_db",
                       charset='utf8')


# 안드로이드에 이미지를 받아올 때 필요한 Class
class ImageCreateAPIView(CreateAPIView):
    serializer_class = ImageSerializer
    queryset = MyImage.objects.all()


# 특정 경로에 있는 이미지 파일을 불러와 Predict후 결과값을 Json 형태로 웹 서버에 띄워준다.
def image_upload_view(request):
    global pre_ans_str
    global landmark_value
    landmark_value = 0
    """Process images uploaded by users"""
    # if request.method == 'POST':
    #     form = ImageForm(request.POST, request.FILES)
    #
    #     if form.is_valid():
    #         form.save()
    #         # Get the current instance object to display in the template
    #         img_obj = form.instance

    file = r'/home/ubuntu/bigdata-final/ImgProcessing/media/media/images/testFile.jpg'

    X = []
    img = Image.open(file)
    img = img.resize((224, 224))
    img = img.convert("RGB")
    data = np.asarray(img)
    X.append(data)

    X = np.array(X)
    new_prediction = new_model.predict(X)
    pre_ans = new_prediction[0].argmax()  # 예측  레이블

    # 전이학습 predict value 기준 값은 아직 정하기 너무 애매함
    if(new_prediction[0][pre_ans])>=5:
        if pre_ans == 0:
            pre_ans_str = "63빌딩"
        elif pre_ans == 1:
            pre_ans_str = "명동성당"
        elif pre_ans == 2:
            pre_ans_str = "코엑스"
        elif pre_ans == 3:
            pre_ans_str = "이순신동상"
        elif pre_ans == 4:
            pre_ans_str = "독립문"
        elif pre_ans == 5:
            pre_ans_str = "서대문형무소"
        elif pre_ans == 6:
            pre_ans_str = "롯데타워"
        elif pre_ans == 7:
            pre_ans_str = "경복궁"
        elif pre_ans == 8:
            pre_ans_str = "남산타워"
        elif pre_ans == 9:
            pre_ans_str = "구서울역"
        elif pre_ans == 10:
            pre_ans_str = "탑골공원팔각정"
    else:
        pre_ans_str = "what the!!"
        landmark_value = new_prediction[0][pre_ans]


    # 어디까지나 3개월 프로젝트이고 귀찮아서 쓴것 뿐, 현업에서는 절대 쓰지 말아야 하는 코드
    if os.path.exists(file):
        os.remove(r'/home/ubuntu/bigdata-final/ImgProcessing/media/media/images/testFile.jpg')

    else:
        pass

    awb_dict = {'pre_ans_str':pre_ans_str}

    return HttpResponse(simplejson.dumps(awb_dict))
    # return render(request, 'index.html', {'pre_ans_str': pre_ans_str})
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


# 호텔 추천 알고리즘 함수
def hotel_recommendation(request):
    global pre_number
    global pre_ans_str


   # objects.values를 써서 데이터를 가져왔지만 pymsql를 사용해 쿼리를 짠다면
    # 훨씬 더 쉽게 코드를 짤 수 있다.
    if pre_ans_str == '경복궁':
        pre_number = 0
    elif pre_ans_str == '명동성당':
        pre_number = 1
    elif pre_ans_str == '이순신동상':
        pre_number = 2
    elif pre_ans_str == '63빌딩':
        pre_number = 3
    elif pre_ans_str == '탑골공원팔각정':
        pre_number = 4
    elif pre_ans_str == '독립문':
        pre_number = 5
    elif pre_ans_str == '남산타워':
        pre_number = 6
    elif pre_ans_str == '롯데타워':
        pre_number = 7
    elif pre_ans_str == '코엑스':
        pre_number = 8
    elif pre_ans_str == '서대문형무소':
        pre_number = 9
    elif pre_ans_str == '구서울역':
        pre_number = 10

    landmark = Landmarks.objects.values('name', 'lat', 'lng','english_name')
    landmark_lat = landmark[pre_number]['lat']
    landmark_lng = landmark[pre_number]['lng']
    landmark_latlng = landmark_lat, landmark_lng
######################################################################

    hotel = Hotels.objects.values('rating', 'name', 'address', 'lat', 'lng',\
                                  'english_rating','english_name','english_address','picture_name',
                                  'star_rate','telephone_number')

    distance_hot = []
    # haversine 기법을 써서 완성된 데이터를 하나씩 추가해준다.
    for count, value in enumerate(hotel):
        hotel_lat = hotel[count]['lat']
        hotel_lng = hotel[count]['lng']
        hotel_latlng = hotel_lat, hotel_lng
        d = haversine(landmark_latlng, hotel_latlng, unit='km')
        distance_hot.append([d, hotel[count]['name'], hotel[count]['lat'],
                            hotel[count]['lng'], hotel[count]['address'], hotel[count]['rating'],
                             hotel[count]['english_name'], hotel[count]['english_address'],
                             hotel[count]['english_rating'],hotel[count]['picture_name'],
                             hotel[count]['star_rate'],hotel[count]['telephone_number']])

    # haversine 기준으로 가장 가까운 곳 5곳만 추려주는 코드
    distance_hot = sorted(distance_hot, key=lambda x:x[0])
    n = 5
    distance_hot_final = distance_hot[:n]

    new_dict = {}


    # 필요한 데이터들을 Json형태로 웹 서버에 띄운다.
    for i in range(len(distance_hot_final)):
        new_english_address = distance_hot_final[i][7]
        new_english_address = new_english_address.replace(",", "#")

        new_dict[i] =  [distance_hot_final[i][1], distance_hot_final[i][2],\
                        distance_hot_final[i][3], distance_hot_final[i][4],\
                        distance_hot_final[i][5], distance_hot_final[i][6],\
                        new_english_address, distance_hot_final[i][8],\
                        distance_hot_final[i][9], distance_hot_final[i][10],\
                        distance_hot_final[i][11], distance_hot_final[i][0]]



    return HttpResponse(simplejson.dumps(new_dict))


# 맛집 추천 알고리즘 구현 기능
def restaurant_recommendation(request):
    global pre_number
    global pre_ans_str

    if pre_ans_str == '경복궁':
        pre_number = 0
    elif pre_ans_str == '명동성당':
        pre_number = 1
    elif pre_ans_str == '이순신동상':
        pre_number = 2
    elif pre_ans_str == '63빌딩':
        pre_number = 3
    elif pre_ans_str == '탑골공원팔각정':
        pre_number = 4
    elif pre_ans_str == '독립문':
        pre_number = 5
    elif pre_ans_str == '남산타워':
        pre_number = 6
    elif pre_ans_str == '롯데타워':
        pre_number = 7
    elif pre_ans_str == '코엑스':
        pre_number = 8
    elif pre_ans_str == '서대문형무소':
        pre_number = 9
    elif pre_ans_str == '구서울역':
        pre_number = 10


    landmark = Landmarks.objects.values('name', 'lat', 'lng')
    landmark_lat = landmark[pre_number]['lat']
    landmark_lng = landmark[pre_number]['lng']
    landmark_latlng = landmark_lat, landmark_lng
    ###########################################################

    restaurant = Restaurants.objects.values('name', 'represent', 'address', 'lat', 'lng',\
                                            'english_name','english_represent','english_address','picture_name',\
                                            'operation_time','service_option','star_rate','telephone_number',\
                                            'english_operation_time','english_service_option')

    distance_res = []
    for count, value in enumerate(restaurant):
        restaurant_lat = restaurant[count]['lat']
        restaurant_lng = restaurant[count]['lng']
        restaurant_latlng = restaurant_lat, restaurant_lng
        d = haversine(landmark_latlng, restaurant_latlng, unit='km')
        distance_res.append([d, restaurant[count]['name'], restaurant[count]['lat'],\
                            restaurant[count]['lng'], restaurant[count]['address'], restaurant[count]['represent'],
                             restaurant[count]['english_name'], restaurant[count]['english_address'],
                             restaurant[count]['english_represent'],restaurant[count]['picture_name'],
                             restaurant[count]['operation_time'],restaurant[count]['service_option'],
                             restaurant[count]['star_rate'],restaurant[count]['telephone_number'],
                             restaurant[count]['english_operation_time'],restaurant[count]['english_service_option']])

    distance_res = sorted(distance_res, key=lambda x: x[0])
    n = 10
    distance_res_final = distance_res[:n]


    new_dict = {}

    for i in range(len(distance_res_final)):
        new_english_address = distance_res_final[i][7]
        new_english_address = new_english_address.replace(",", "#")

        new_operation = distance_res_final[i][10]
        new_operation = new_operation.replace(",", "#")

        new_option = distance_res_final[i][11]
        new_option = new_option.replace(",", "#")

        new_english_operation = distance_res_final[i][14]
        new_english_operation = new_english_operation.replace(",", "#")

        new_english_option = distance_res_final[i][15]
        new_english_option = new_english_option.replace(",", "#")



        new_dict[i] =  [distance_res_final[i][1], distance_res_final[i][2],\
                        distance_res_final[i][3], distance_res_final[i][4],\
                        distance_res_final[i][5], distance_res_final[i][6],\
                        new_english_address, distance_res_final[i][8],\
                        distance_res_final[i][9], new_operation,\
                        new_option, distance_res_final[i][12],\
                        distance_res_final[i][13], new_english_operation,\
                        new_english_option, distance_res_final[i][0]]

    return HttpResponse(simplejson.dumps(new_dict))


# 랜드마크 정보 제공
def landmark_information(request):
    global pre_number
    global pre_ans_str
    global landmark_value

    if landmark_value==0:
        if pre_ans_str == '경복궁':
            pre_number = 0
        elif pre_ans_str == '명동성당':
            pre_number = 1
        elif pre_ans_str == '이순신동상':
            pre_number = 2
        elif pre_ans_str == '63빌딩':
            pre_number = 3
        elif pre_ans_str == '탑골공원팔각정':
            pre_number = 4
        elif pre_ans_str == '독립문':
            pre_number = 5
        elif pre_ans_str == '남산타워':
            pre_number = 6
        elif pre_ans_str == '롯데타워':
            pre_number = 7
        elif pre_ans_str == '코엑스':
            pre_number = 8
        elif pre_ans_str == '서대문형무소':
            pre_number = 9
        elif pre_ans_str == '구서울역':
            pre_number = 10

        landmark = Landmarks.objects.values('name', 'lat', 'lng', 'english_name','eng_desc','kor_desc')

        landmark_name = landmark[pre_number]['name']
        landmark_lat = landmark[pre_number]['lat']
        landmark_lng = landmark[pre_number]['lng']
        landmark_english_name = landmark[pre_number]['english_name']
        landmark_english_desc = landmark[pre_number]['eng_desc']
        landmark_desc = landmark[pre_number]['kor_desc']


       # 중간에 전처리가 필요할 경우 간단하게 해준다.
        landmark_english_desc = landmark_english_desc.replace(",","#")
        landmark_desc = landmark_desc.replace(",", "#")

        result_dict = {}
        result_dict['landmark'] = [landmark_name,landmark_lat,landmark_lng,landmark_english_name,\
                                   landmark_desc,landmark_english_desc]


    else:
        result_dict = {}
        result_dict['landmark'] = ["what???",1.0,1.0,"what???",\
                                   "picture error!!!","picture error!!!"]

    return HttpResponse(simplejson.dumps(result_dict))


# def landmark_seoul(request):
#     landmarks = Landmarks.objects.values('name', 'lat', 'lng','english_name')
#     landmark_dict = {}
#     for i in range(11):
#         name = landmarks[i]['name']
#         lat = landmarks[i]['lat']
#         lng = landmarks[i]['lng']
#         english_name = landmarks[i]['english_name']
#         landmark_dict[i]=[name,lat,lng,english_name]
#
#
#     return HttpResponse(simplejson.dumps(landmark_dict))









