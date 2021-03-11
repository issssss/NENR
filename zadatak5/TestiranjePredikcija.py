import math
from tkinter import *

from Backpropagation import *

canvas_width= 500
canvas_height = 500
tocke = []
listaUzoraka = []

def paint(event):
    color = 'red'
    x1, y1 = (event.x - 1), (event.y - 1)
    x2, y2 = (event.x + 1), (event.y + 1)
    c.create_oval(x1, y1, x2, y2, fill = color, outline = color)
    tocke.append([event.x, event.y])

def delete(event):
    #print(len(tocke))
    tocke.clear()
    c.delete('all')

def predikcija():
    pripremiPodatke()
    f = open('zaPredikciju.txt', 'r' )
    dataset = []
    for i in f.readlines():
        rastavUlaza = i.strip().split('\t')
        ulazniPodaci = []
        for j in rastavUlaza:
            if j == '':
                continue
            dva = j.split(' ')
            ulazniPodaci.append(float(dva[0]))
            ulazniPodaci.append(float(dva[1]))
       # print(ulazniPodaci)
        dataset.append(ulazniPodaci)
    #print(dataset)
    global tekst
    te = "Mislim da je ovo... "
    pred = propagacija_unaprijed(network, dataset[0])
    max = 0
    for i in range(len(pred)):
        if pred[i] > pred[max]:
            max = i
            #print(max)
    if max == 0:
        te += 'alfa'
    elif max == 1:
        te += 'beta'
    elif max == 2:
        te += 'gamma'
    elif max == 3:
        te += 'delta'
    else:
        te += 'epsilon'
    tekst.set(te)
    print(pred)
    print(te)
    #Rjesenje.(text = tekst)
    tocke.clear()




def pripremiPodatke():
    xc = 0
    yc = 0
    for i in tocke:
        xc += i[0]
        yc += i[1]
    Tc = []
    Tc.append(xc/len(tocke))
    Tc.append(yc/len(tocke))

    #print(Tc)

    korTocke = tocke.copy()

    for i in korTocke:
        i[0] = i[0] - Tc[0]
        i[1] = i[1] - Tc[1]

    #print(korTocke)

    xmax = korTocke[0][0]
    ymax = korTocke[0][1]

    for i in korTocke:
        if xmax < i[0]:
            xmax = i[0]
        if ymax < i[1]:
            ymax = i[1]

    m = max(xmax, ymax)

    #print(xmax)
    #print(ymax)
    #print(m)

    for i in korTocke:
        i[0] = i[0]/m
        i[1] = i[1]/m

    #print(korTocke)

    D = 0
    for i in range(len(korTocke)-1):
        D += math.sqrt((korTocke[i][0]-korTocke[i+1][0])**2 + (korTocke[i][1]-korTocke[i+1][1])**2)

    #print(D)
    M = 50 #broj reprezenativnih tocki
    K = range(0, M)
    reprTocke = []

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
    f = open("zaPredikciju.txt", "w")
    stringRep = novired + "\n"
    f.write(stringRep)
    f.close()
    listaUzoraka.append(reprTocke)


master = Tk()
master.title('Geste za treniranje')
c = Canvas(master, width = canvas_width, height = canvas_height, bg = 'black')
f1 = Frame(master)
tekst = StringVar(f1, value= 'Nacrtajte slovo za predikciju i stisnite Predikcija')
c.grid(row = 0, column = 0)
f1.grid(row = 1, column = 0, sticky = 'nsew')
Tekst = Label(f1, text = 'Nacrtajte grcko slovo!').pack(side = TOP)
Pred = Button(f1, text = 'Napravi Predikciju', command = predikcija).pack(side = TOP)
Rjesenje = Label(f1, textvariable = tekst).pack(side = BOTTOM)
c.bind('<B1-Motion>', paint)
c.bind('<ButtonPress-3>', delete)



master.mainloop()
