import cv2
import os

# 이미지 찾기

# 이미지 불러오기
img = cv2.imread("../img/suwon.jpg")



# gray함수로 변환
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# blur 처리
gray = cv2.GaussianBlur(gray,(5,5),0)

mser = cv2.MSER_create()
regions,_ = mser.detectRegions(gray)

clone = img.copy()

hulls = [cv2.convexHull(p.reshape(-1, 1, 2)) for p in regions]

# 사각형안에 다른사각형 지워주기
remove1 = []
for i,c1 in enumerate(hulls):

    x, y, w, h = cv2.boundingRect(c1)
    r1_start = (x,y)
    r1_end = (x+w, y+h)

    for j,c2 in enumerate(hulls):

        if i == j:
            continue

        x, y, w, h = cv2.boundingRect(c2)
        r2_start = (x,y)
        r2_end = (x+w, y+h)

        # 큰사각형안에 작은사각형이 있는지
        if r1_start[0] >= r2_start[0] and r1_start[1] >= r2_start[1] and r1_end[0] <= r2_end[0] and r1_end[1] <= r2_end[1]:
            remove1.append(i)

#count = 0
for j,cnt in enumerate(hulls):
    if j in remove1: continue
    x, y, w, h = cv2.boundingRect(cnt)
    margin = 10
    cv2.rectangle(clone, (x-margin, y-margin), (x + w + margin, y + h + margin), (0, 255, 0), 1)
#    count = count+1

#print(count)


cv2.imshow('mser', clone)
cv2.waitKey(0)