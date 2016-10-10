import numpy as np
import cv2
import random
import math
import serial
import picamera
import picamera.array

port = serial.Serial('/dev/ttyS0',baudrate=38400,bytesize=EIGHTBITS,
	PARITY_NONE,STOPBITS_ONE,None,
	True,False,False,None,None)

def serialConect():

    port.open()
    port.flushInput()
    port.flushOutput()
    
	while(True)
		if(port.in_waiting==4)
			if(port.read(4)=="ping".encode('ascii'))
				port.write("here".encode('ascii'))
				break

def imageShoot():

    #img = cv2.imread('/Users/Nolan/Documents/theGreenGoal.png')
    #img2 = cv2.imread('/Users/Nolan/Documents/theGreenGoal.png')
    with picamera.PiCamera() as camera:
        time.sleep(2)
    with picamera.array.PiRGBArray(camera) as stream:
	camera.resolution = (768, 576)
        camera.capture(stream, format='bgr')
        # At this point the image is available as stream.array
        img = stream.array
    imgToMatch = cv2.imread('/Users/Nolan/Documents/frcGOAL.png')

    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

    lower_green = np.array([50, 50, 120])
    upper_green = np.array([70, 255, 255])

    mask = cv2.inRange(hsv, lower_green, upper_green)

    res = cv2.bitwise_and(img,img, mask= mask)

    edges = cv2.Canny(res,50,150,apertureSize = 3)
    edges1 = cv2.Canny(imgToMatch,50,150,apertureSize = 3)

    contours, hierarchy = cv2.findContours(edges, cv2.RETR_LIST, cv2.CHAIN_APPROX_TC89_KCOS)
    contours1, hierarchy1 = cv2.findContours(edges1, cv2.RETR_LIST, cv2.CHAIN_APPROX_TC89_KCOS)

    goalNum = 0
    contour1 = contours1[1]

    potGoalSpot = []
    potGoalNum = []

    for i in range(0, len(contours)):
        contour = contours[i]
        M = cv2.moments(contour)
        if M['m00'] <= 50:
            continue
        cx = int(M['m10']/M['m00'])
        cy = int(M['m01']/M['m00'])
        area = str(M['m00'])
        b = random.randint(128, 255)
        g = random.randint(128, 255)
        r = random.randint(128, 255)
        cv2.drawContours(img, contours, i, [b, g, r], 5)
        cv2.putText(img, area,(cx, cy), cv2.FONT_HERSHEY_SIMPLEX, .5,(r, g, b),2)
        matching = cv2.matchShapes(contour1,contour,1,0.0)
        potGoalSpot.append(i)
        potGoalNum.append(matching)
        print(matching, i)

    whitePixels = 0

    contour = contours[potGoalSpot[min(xrange(len(potGoalNum)),key=potGoalNum.__getitem__)]]
    #cv2.drawContours(img2, contours, potGoalSpot[min(xrange(len(potGoalNum)),key=potGoalNum.__getitem__)], [0, 0, 255], 1)

    #img2[contour[0][0][1], contour[0][0][0]] = [255, 0, 0]

    # Finds the highest x value in the contour array
    newHigh = 0
    newHighSpot = 0
    isHigh = False
    for j in range(0, len(contour)):
        newHigh = contour[j][0][0]
        newHighSpot = j
        for k in range(0, len(contour)):
            if newHigh >= contour[k][0][0]:
                isHigh = True
            else:
                isHigh = False
                break
        if isHigh == True:
            break

    # Finds the lowest x value in the countour array
    newLow = 0
    newLowSpot = 0
    isLow = False
    for l in range(0, len(contour)):
        newLow = contour[l][0][0]
        newLowSpot = l
        for m in range(0, len(contour)):
            if newLow <= contour[m][0][0]:
                isLow = True
            else:
                isLow = False
                break
        if isLow == True:
            break

    pixelD = newHigh - newLow

    #cv2.imwrite("/Users/Nolan/Documents/theContors.png", img)
    #cv2.imwrite("/Users/Nolan/Documents/theContorsWithOne.png", img2)






    # Distance to an object, everythin in mm or pixels
    focalLength = 3.6 # Will change for every camera, 1.0 is not correct, in mm
    goalWidthReal = 508.1016 # real width of goal in mm. 1.667 ft
    imageWidth = 2592 # Will change for every camera, 1000 is not correct, in pixels
    goalWidthPixels = pixelD # Determined above
    sensorWidth = 3.67 # Will change for every camera, 1.0 is not correct, in mm

    distanceToObject = (focalLength * goalWidthReal * imageWidth) / (goalWidthPixels * sensorWidth)

    height, width, channels = img.shape




    #This finds the lowest y value in the contour array
    newLowHeight = 0
    newLowSpotHeight = 0
    isLowHeight = False
    for l in range(0, len(contour)):
        newLowHeight = contour[l][0][1]
        newLowSpotHeight = l
        for m in range(0, len(contour)):
            if newLowHeight >= contour[m][0][1]:
                isLowHeight = True
            else:
                isLowHeight = False
                break
        if isLowHeight == True:
            break



    # Finds distance in pixels bewteen center of screen and newLowSpotHeight... found above
    toHeight = contour[newLowSpotHeight][0][1]

    if toHeight < (height / 2):
        yFromCenter = (height / 2) - (toHeight)
    else:
        yFromCenter = (toHeight) - (height / 2)




    # Finds the the y angle the goal is at above or below the aready pre-existing set angle
    yCorrectReal = (goalWidthReal * yFromCenter) / goalWidthPixels
    yToCorrectAngle = (np.arcsin((1930.4 + yCorrectReal) / distanceToObject))





    # Finds the distance in pixels between the center of the screen and newHighSpot... found in the first set of for loops
    toWidth = ((contour[newHighSpot][0][0] + contour[newLowSpot][0][0]) / 2)

    if toWidth < width / 2:
        xFromCenter = ((width / 2) - ((contour[newHighSpot][0][0] + contour[newLowSpot][0][0]) / 2)) * -1
    elif toWidth > width / 2:
        xFromCenter = ((contour[newHighSpot][0][0] + contour[newLowSpot][0][0]) / 2) - (width / 2)
    else:
        xFromCenter = 0




    # Finds the x angle the goal is at relative to where the robot is pointing
    xToCorrectReal = (goalWidthReal * xFromCenter) / goalWidthPixels
    xToCorrectAngle = np.arcsin(xToCorrectReal / distanceToObject)

    groundDistance = math.sqrt((distanceToObject * distanceToObject) - ((1930.4 + yCorrectReal) * (1930.4 + yCorrectReal)))

    # groundDistance line 167
    # xToCorrectAngle line 165
    # yToCorrectAngle line 145
    print("groundDistance " + groundDistance)
    print("xToCorrectAngle " + xToCorrectAngle)
    print("yToCorrectAngle " + yToCorrectAngle)
    
	port.write((str(groundDistance) + " " + str(xToCorrectAngle)).encode('ascii'))

def takePicture():
	picamera.PiCamera().capture(port,'jpeg')
	port.flushOutput()
	
if __name__ == '__main__':
    serialConect()
	
	while(True)
		input=port.read(4)
		if(input=="gett".encode('ascii'))
			imageShoot()
		else if(input=="pict".encode('ascii'))
			takePicture()
