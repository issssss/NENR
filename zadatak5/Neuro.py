import math
from tkinter import *

canvas_width= 500
canvas_height = 500
tocke = []
listaUzoraka = []
izlaz = []

def paint(event):
    color = 'red'
    x1, y1 = (event.x - 1), (event.y - 1)
    x2, y2 = (event.x + 1), (event.y + 1)
    c.create_oval(x1, y1, x2, y2, fill = color, outline = color)
    tocke.append([event.x, event.y])

def delete(event):
    print(len(tocke))
    pripremiPodatke()
    tocke.clear()
    izlaz.clear()
    #tocke = []
    c.delete('all')

def izlazAlfa():
    izlaz.append(1)
    for i in range(4):
        izlaz.append(0)

def izlazBeta():
    izlaz.append(0)
    izlaz.append(1)
    for i in range(3):
        izlaz.append(0)

def izlazGamma():
    for i in range(2):
        izlaz.append(0)
    izlaz.append(1)
    for i in range(2):
        izlaz.append(0)

def izlazDelta():
    for i in range(3):
        izlaz.append(0)
    izlaz.append(1)
    izlaz.append(0)

def izlazEpsi():
    for i in range(4):
        izlaz.append(0)
    izlaz.append(1)

def pripremiPodatke():
    #print(len(tocke))
    xc = 0
    yc = 0
    for i in tocke:
        xc += i[0]
        yc += i[1]
    Tc = []
    Tc.append(xc/len(tocke))
    Tc.append(yc/len(tocke))

    print(Tc)

    korTocke = tocke.copy()

    for i in korTocke:
        i[0] = i[0] - Tc[0]
        i[1] = i[1] - Tc[1]

    print(korTocke)

    xmax = korTocke[0][0]
    ymax = korTocke[0][1]

    for i in korTocke:
        if xmax < i[0]:
            xmax = i[0]
        if ymax < i[1]:
            ymax = i[1]

    m = max(xmax, ymax)

    print(xmax)
    print(ymax)
    print(m)

    for i in korTocke:
        i[0] = i[0]/m
        i[1] = i[1]/m

    print(korTocke)

    D = 0
    for i in range(len(korTocke)-1):
        D += math.sqrt((korTocke[i][0]-korTocke[i+1][0])**2 + (korTocke[i][1]-korTocke[i+1][1])**2)

    print(D)
    M = 50 #broj reprezenativnih tocki
    K = range(0, M)
    reprTocke = []
   # for k in K:
   #     udalj = k*D/(M-1)
   #     for i in range(len(korTocke)):
    #        udljodO = math.sqrt((korTocke[0][0]-korTocke[i][0])**2 + (korTocke[0][1]-korTocke[i][1])**2)
    #        if abs(udljodO - udalj) <= 0.0001:
   #             reprTocke.append(korTocke[i])
   #         else:
    #            interX = korTocke[0][0] +

    for k in K:
        ind = int(k*len(korTocke)/(M-1))
        if ind > len(korTocke) -1:
            ind = len(korTocke) - 1
        reprTocke.append(korTocke[ind])
    #reprTocke.append(izlaz)
    novired = ''
    for i in reprTocke:
        novi = str(i[0]) + ' ' +str(i[1])
        novired += novi + '\t'
    novired += 'i: '
    for i in izlaz:
        novired+= str(i) + ' '
    f = open("uzorciZaUcenje2.txt", "a")
    stringRep = novired + "\n"
    f.write(stringRep)
    f.close()
    listaUzoraka.append(reprTocke)
    print(len(listaUzoraka))
    print(listaUzoraka)


master = Tk()
master.title('Geste za treniranje')
c = Canvas(master, width = canvas_width, height = canvas_height, bg = 'black')
f1 = Frame(master)
c.grid(row = 0, column = 0)
f1.grid(row = 1, column = 0, sticky = 'nsew')
Tekst = Label(f1, text = 'Prvo odaberite grcko slovo koje crtate!').pack(side = TOP)
A = Button(f1, text = 'alfa', command = izlazAlfa).pack(side = LEFT)
B = Button(f1, text = 'beta', command = izlazBeta).pack(side = LEFT)
C = Button(f1, text = 'gamma', command = izlazGamma).pack(side = LEFT)
De = Button(f1, text = 'delta', command = izlazDelta).pack(side = LEFT)
E = Button(f1, text = 'epsilon', command = izlazEpsi).pack(side = LEFT)
#A.pack(side = "top") #grid(row = 1, column = 0)
#B.grid(row = 1, column = 0)
#C.grid(row = 1, column = 0)
#De.grid(row = 1, column = 0)
#E.grid(row = 1, column = 0)
#c.pack(expand = YES, fill = BOTH)
c.bind('<B1-Motion>', paint)
c.bind('<ButtonPress-3>', delete)

message = Label(master, text = 'Press and Drag to draw')
#message.pack(side = BOTTOM)

master.mainloop()
