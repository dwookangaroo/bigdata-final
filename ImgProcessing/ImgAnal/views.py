from django.http import JsonResponse
from django.shortcuts import render
from django.views import View
from rest_framework import viewsets
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.renderers import JSONRenderer
from rest_framework.views import APIView

from .forms import ImageForm
from tensorflow.keras.models import Sequential, load_model
from PIL import Image
import numpy as np

# from .models import Post
# from .serializers import PostSerializer

new_model = load_model('model/trained_model.h5')

caltech_dir = "D:/bigdata_project/ImgProcessing/static/final_test"
pre_ans_str = ''

def image_upload_view(request):
    """Process images uploaded by users"""
    if request.method == 'POST':
        form = ImageForm(request.POST, request.FILES)

        if form.is_valid():
            form.save()
            # Get the current instance object to display in the template
            img_obj = form.instance

            X = []
            file = "D:/bigdata_project/ImgProcessing" + img_obj.image.url
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

            return render(request, 'index.html', {'form': form, 'img_obj': img_obj, 'pre_ans_str': pre_ans_str})
    else:
        form = ImageForm()
    return render(request, 'index.html', {'form': form,})

# class RestTestView(View):
#     def __init__(self):
#         pass
#
#     def get(self, request):
#         a = request.GET.get('a')
#         b = request.GET.get('b')
#
#         return JsonResponse({'a' : a, 'b' : b })
#
#     def post(self, request):
#         return JsonResponse({'result' : 'bad request' })
#
#
# class DRFView(APIView):
#
#     permission_classes = (AllowAny)
#     renderer_classes = (JSONRenderer)
#     authentication_classes = (JSONWebTokenAuthentication)


# def image_anal(request):
#     X = []
#     filenames = []
#     file="D:/bigdata_project/ImgProcessing/static/final_test/63buliding1.jpg"
#     img = Image.open(file)
#     img = img.resize((224, 224))
#     img = img.convert("RGB")
#     data = np.asarray(img)
#     filenames.append(file)
#     X.append(data)
#
#     X = np.array(X)
#     np.set_printoptions(formatter={'float': lambda x: "{0:0.3f}".format(x)})
#     cnt = 0
#
#     new_prediction = new_model.predict(X)
#
#     pre_ans = new_prediction[0].argmax()  # 예측 레이블
#     pre_ans_str = ''
#
#     if pre_ans == 0:
#         pre_ans_str = "63buliding"
#     elif pre_ans == 1:
#         pre_ans_str = "castle"
#     elif pre_ans == 2:
#         pre_ans_str = "coex"
#     elif pre_ans == 3:
#         pre_ans_str = "general"
#     elif pre_ans == 4:
#         pre_ans_str = "indep_door"
#     elif pre_ans == 5:
#         pre_ans_str = "judgement_castle"
#     elif pre_ans == 6:
#         pre_ans_str = "lotte_tower"
#     elif pre_ans == 7:
#         pre_ans_str = "moonlight"
#     elif pre_ans == 8:
#         pre_ans_str = "namsan"
#     elif pre_ans == 9:
#         pre_ans_str = "seoulstation"
#     elif pre_ans == 10:
#         pre_ans_str = "tapgol_park"
#     else:
#         pre_ans_str = "what the!!"
#
#     print(pre_ans_str)
#
#     return render(request, 'data.html', {'pre_ans_str':pre_ans_str})

