import math
from random import gauss, random
import matplotlib.pyplot as pyp

A = []
B = []
C = []
zbrojPogra = []
zbrojPogrb = []
zbrojPogrc = []
zbrojPogrd = []
zbrojPogrp = []
zbrojPogrq = []
zbrojPogrr = []
def glorot(fan_in, fan_out):
    var = 2/(fan_in + fan_out)
    std = math.sqrt(var)
    return gauss(0.0, std)

def pravila(brPrav):

    for i in range(brPrav):
        tezineab = [glorot(4,5), glorot(4,5)]
        tezinecd = [glorot(4,5), glorot(4,5)]
        tezinepqr = [glorot(4,5), glorot(4,5), glorot(4,5)]
        A.append(tezineab)
        B.append(tezinecd)
        C.append(tezinepqr)
        zbrojPogra.append(0)
        zbrojPogrb.append(0)
        zbrojPogrc.append(0)
        zbrojPogrd.append(0)
        zbrojPogrp.append(0)
        zbrojPogrq.append(0)
        zbrojPogrr.append(0)

def sigm(tezine, x):
    ex = tezine[1]*(x-tezine[0])
    y = 1/(1 + math.exp(ex))
    return y

def propagacija_unaprijed(ulazi):
    w = []
    for i in range(len(A)):
        wi = sigm(A[i], ulazi[0])*sigm(B[i], ulazi[1])
        w.append(wi)
    w_crta = []
    sumw = 0
    for i in w:
        sumw += i
    for i in w:
        wi = i/sumw
        w_crta.append(wi)
    wf = 0
    for i in range(len(w_crta)):
        y = w_crta[i]*(C[i][0]*ulazi[0]+C[i][1]*ulazi[1]+C[i][2])
        wf += y

    return wf

def propagacija_unatrag(eta, ulaz, dobiveni_izlaz, stoh):

    sumApB = 1
    alf = []
    bet = []
    pogreske_a = []
    pogreske_b = []
    pogreske_c = []
    pogreske_d = []
    pogreske_p = []
    pogreske_q = []
    pogreske_r = []
    for i in range(len(A)):
        alf.append(sigm(A[i], ulaz[0]))
        bet.append(sigm(B[i], ulaz[1]))
        sumApB += alf[i]*bet[i]

    for i in range(len(C)):
        pogreske_p.append((ulaz[2] - dobiveni_izlaz) * (alf[i] * bet[i]) / sumApB * ulaz[0])
        pogreske_q.append((ulaz[2] - dobiveni_izlaz) * (alf[i] * bet[i]) / sumApB * ulaz[1])
        pogreske_r.append((ulaz[2] - dobiveni_izlaz) * (alf[i] * bet[i]) / sumApB)
    #pogreske_q

    for i in range(len(A)):
        fi = C[i][0]*ulaz[0]+C[i][1]*ulaz[1]+C[i][2]
        sumFi = 0
        for j in range(len(A)):
            if i != j:
                fj = C[j][0]*ulaz[0]+C[j][1]*ulaz[1]+C[j][2]
                sumFi+= alf[j]*bet[j]*(fi - fj)
        sumFi/=pow(sumApB,2)
        pogreske_a.append((ulaz[2]-dobiveni_izlaz)*sumFi*bet[i]*alf[i]*(1-alf[i])*A[i][1])
        pogreske_b.append((ulaz[2]-dobiveni_izlaz)*sumFi*bet[i]*(ulaz[0] - A[i][0])*alf[i]*(1-alf[i]))
        # eta* (pravi_izlaz - dobiveni)* suma(alfa*beta*(fi -fj)/suma(alfa*beta)^2 * alfai * betai*(1-betai)* di
        pogreske_c.append((ulaz[2] - dobiveni_izlaz) * sumFi * alf[i] * bet[i]* (1 - bet[i])*B[i][1])
        pogreske_d.append((ulaz[2]-dobiveni_izlaz)*sumFi*alf[i]*(ulaz[1] - B[i][0])*bet[i]*(1-bet[i]))
    if stoh:
        for i in range(len(pogreske_d)):
            A[i][0] += eta*pogreske_a[i]
            A[i][1] -= eta*pogreske_b[i]
            B[i][0] += eta*pogreske_c[i]
            B[i][1] -= eta*pogreske_d[i]
            C[i][0] += eta*pogreske_p[i]
            C[i][1] += eta*pogreske_q[i]
            C[i][2] += eta*pogreske_r[i]
    else:
        for i in range(len(A)):
            zbrojPogra[i]+=pogreske_a[i]
            zbrojPogrb[i]+=pogreske_b[i]
            zbrojPogrc[i]+=pogreske_c[i]
            zbrojPogrd[i]+=pogreske_d[i]
            zbrojPogrp[i]+=pogreske_p[i]
            zbrojPogrq[i]+=pogreske_q[i]
            zbrojPogrr[i]+=pogreske_r[i]





def ANFISmreza(brPravila, eta, ulazi, epohe, stoh):
    pravila(brPravila)
   # if stoh:
     #   f = open("zad7stoh.txt", 'w')
    #else:
     #   f = open("zad7stan.txt", 'w')
    for epoha in range(epohe):
        uk_greska = 0
        izlazi = []
        for ulaz in ulazi:
            izlaz = propagacija_unaprijed(ulaz)
            izlazi.append(izlaz)
            propagacija_unatrag(eta, ulaz, izlaz, stoh)
            uk_greska += (izlaz - ulaz[2])**2

        if not stoh:
            for i in range(len(zbrojPogrp)):
                A[i][0] += eta*zbrojPogra[i]
                A[i][1] -= eta*zbrojPogrb[i]
                B[i][0] += eta*zbrojPogrc[i]
                B[i][1] -= eta*zbrojPogrd[i]
                C[i][0] += eta*zbrojPogrp[i]
                C[i][1] += eta*zbrojPogrq[i]
                C[i][2] += eta*zbrojPogrr[i]


        print('Epoha: '+ str(epoha)+ ' ukupna greska = '+str(uk_greska/(len(ulazi))))
       # f.write(str(uk_greska/len(ulazi))+'\n')
        if stoh and (uk_greska/len(ulazi)) <= 0.001:
            break
        if not stoh and (uk_greska/len(ulazi)) <= 20:
            break
    #f.close()
    return izlazi


uzorci_za_ucenje = []
for i in range(-4, 5):
    for j in range(-4, 5):
        f = ((i-1)**2 + (j + 2)**2 - 5*i*j + 3) * math.pow(math.cos(i/5), 2)
        uzorci_za_ucenje.append([i, j, f])
#Stohasticki
#najIzlazi = ANFISmreza(1, 0.0001, uzorci_za_ucenje, 10000, True)
#najIzlazi = ANFISmreza(2, 0.01, uzorci_za_ucenje, 10000, True)
najIzlazi = ANFISmreza(13, 0.01, uzorci_za_ucenje, 10000, True)
#Standardni
#najIzlazi = ANFISmreza(1, 0.0000001, uzorci_za_ucenje, 10000, False)
#najIzlazi = ANFISmreza(2, 0.00000001, uzorci_za_ucenje, 10000, False)
#najIzlazi = ANFISmreza(13, 0.0000001, uzorci_za_ucenje, 10000, False)

X = [uzorak[0] for uzorak in uzorci_za_ucenje]
Y = [uzorak[1] for uzorak in uzorci_za_ucenje]
F = [uzorak[2] for uzorak in uzorci_za_ucenje]
#fig = pyp.figure()
#ax = fig.gca(projection = '3d')
#ax.plot_trisurf(X,Y,F, color='maroon')
#pyp.show()
pogreska_uzorka = []

for i in range(len(F)):
    pogreska_uzorka.append(najIzlazi[i] - F[i])
'''
fig2 = pyp.figure()
ax = fig2.gca(projection = '3d')
ax.plot_trisurf(X,Y,najIzlazi, color= 'blueviolet')
pyp.show()
fig2 = pyp.figure()
ax = fig2.gca(projection = '3d')
ax.plot_trisurf(X,Y,pogreska_uzorka, color= 'blueviolet')
pyp.show()
ax = fig2.gca()
ax.set_ylim([0,1])
'''
izlaziA = []
izlaziB = []
for i in range(len(A)):
    pravIzlazA = []
    pravIzlazB = []
    for j in range(-4,5):
        pravIzlazA.append(sigm(A[i],j))
        pravIzlazB.append(sigm(B[i],j))
    izlaziA.append(pravIzlazA)
    izlaziB.append(pravIzlazB)
'''
for i in range(len(A)):
    #pyp.subplot(5,3,i+1)
    fig2 = pyp.figure()
    ax = fig2.gca()
    ax.set_ylim([0, 1])
    ax.plot(range(-4,5), izlaziA[i], label= 'Funkcija pripadnosti A'+str(i+1), color = 'forestgreen')
    ax.plot(range(-4, 5), izlaziB[i], label='Funkcija pripadnosti B' + str(i + 1), color='maroon')
    pyp.title('Funkcije pripadnosti za pravilo  '+str(i+1))
    pyp.legend()
    pyp.show()
#pyp.title('Funkcije pripadnosti za neizrazite skupove A')
'''
#pyp.show()
#fig3 = pyp.figure()
#ax = fig3.gca()
#ax.set_ylim([0,1])
#for i in range(len(B)):
    #pyp.subplot(5, 3, i+1)
    #fig3 = pyp.figure()
    #ax = fig3.gca()
    #ax.set_ylim([0, 1])
    #ax.plot(range(-4,5), izlaziB[i], label= 'Pravilo'+str(i+1))
    #pyp.title('Funkcija pripadnosti B za pravilo ' +str(i+1))
    #pyp.show()
#pyp.title('Funkcije pripadnosti za neizrazite skupove B')
#pyp.legend()
#pyp.show()