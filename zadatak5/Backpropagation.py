import math
import re
from random import random, seed, gauss


def glorot(fan_in, fan_out):
    var = 2/(fan_in + fan_out)
    std = math.sqrt(var)
    return gauss(0.0, std)

def init_network(ulsloj, sksloj, izsloj, brsksl):
    mreza = []
    for i in range(brsksl):
        if i == 0:
            skriven_sloj = [{'tezine':[glorot(ulsloj, sksloj[i]) for j in range(ulsloj+1)]} for j in range(sksloj[i])]
        else:
            skriven_sloj = [{'tezine': [glorot(sksloj[i-1], sksloj[i]) for j in range(sksloj[i-1] + 1)]} for j in range(sksloj[i])]
        mreza.append(skriven_sloj)
    izlazni_sloj = [{'tezine':[glorot(sksloj[-1], izsloj) for i in range(sksloj[-1]+1)]} for i in range(izsloj)]
    mreza.append(izlazni_sloj)
    return mreza

def aktivacijaNeur(tezine, ulazi):
    aktivacija = tezine[-1]
    for i in range(len(tezine) - 1):
        aktivacija += tezine[i]*ulazi[i]
    return aktivacija

def izlazNeurona(aktivacija):
    izl = 1/(1 + math.exp(-aktivacija))
    return izl

def propagacija_unaprijed(mreza, red_ulazi):
    ulazi = red_ulazi.copy()
    for sloj in mreza:
        noviUl = []
        for neuron in sloj:
            neuron['ulaz'] = ulazi[0]
            aktivacija = aktivacijaNeur(neuron['tezine'], ulazi)
            neuron['izlaz'] = izlazNeurona(aktivacija)
            noviUl.append(neuron['izlaz'])
        ulazi = noviUl.copy()
    return ulazi

def derivacijaSigm(izlaz):
    izl = izlaz*(1-izlaz)
    return izl

def backpropagation(mreza, ocekivan_izlaz, ulaz, st):
    for i in reversed(range(len(mreza))):
        sloj = mreza[i]
        pogreske = []
        if i == len(mreza) - 1:
            for j in range(len(sloj)):
                neuron = sloj[j]
                greska = ocekivan_izlaz[j] - neuron['izlaz']
                pogreske.append(greska)
        else:
            for j in range(len(sloj)):
                greska = 0
                for neuron in mreza[i+1]:
                    greska += (neuron['tezine'][j] * neuron['grad'])
                pogreske.append(greska)
        for j in range(len(sloj)):
            neuron = sloj[j]
            #if st == -1:
            neuron['grad'] = pogreske[j] * derivacijaSigm(neuron['izlaz'])



def podesiTezineStoh(mreza, red, eta):
    for i in range(len(mreza)):
        ulazi = red[:-1]
        if i != 0:
            ulazi = [neuron['izlaz'] for neuron in mreza[i-1]]
        for neuron in mreza[i]:
            for j in range(len(ulazi)):
                neuron['tezine'][j] += eta*neuron['grad']*ulazi[j]
            neuron['tezine'][-1] += eta*neuron['grad']

def podesiTezineStan(mreza, eta):
    o = 0
    for sloj in mreza:
        for neuron in sloj:
            for i in range(len(neuron['tezine'])-1):
                neuron['tezine'][i] = eta*neuron['zbrojGrad'][i]
                if abs(neuron['tezine'][i]) > 10:
                    o = 1
            neuron['tezine'][-1] = eta*neuron['biasGrad']

    return o

def zbrojiGradijente(mreza, red):
    for i in range(len(mreza)):
        ulazi = red[:-1]
        if i != 0:
            ulazi = [neuron['izlaz'] for neuron in mreza[i - 1]]
        for neuron in mreza[i]:
            for j in range(len(ulazi)):
                if not neuron.__contains__('zbrojGrad'):
                    neuron['zbrojGrad'] = []
                    neuron['biasGrad'] = 0
                    for k in range(len(ulazi)):
                        neuron['zbrojGrad'].append(0)
                neuron['zbrojGrad'][j] += neuron['grad'] * ulazi[j]
            neuron['biasGrad'] += neuron['grad']
            #neuron['tezine'][-1] += neuron['grad']

def trenirajMrezu(mreza, ulazi, eta, epohe, br_izlaza, st):
    noviUlazi = []
    brojac = 0
    if st != -1 and st != 2:
        l1, l2, l3, l4, l5, l6, l7, l8, l9, l10 = [ulazi[i * st: (i + 1) * st] for i in range(10)]
    for epoha in range(epohe):
        uk_greska = 0
        if st != -1 and st != -2:
            brojac +=1
            if brojac == 1:
                noviUlazi = l1
            elif brojac == 2:
                noviUlazi = l2
            elif brojac == 3:
                noviUlazi = l3
            elif brojac == 4:
                noviUlazi = l4
            elif brojac == 5:
                noviUlazi = l5
            elif brojac == 6:
                noviUlazi = l6
            elif brojac == 7:
                noviUlazi = l7
            elif brojac == 8:
                noviUlazi = l8
            elif brojac == 9:
                noviUlazi = l9
            elif brojac == 10:
                noviUlazi = l10
                brojac = 0
        else:
            noviUlazi = ulazi
        for red in noviUlazi:
            izlazi = propagacija_unaprijed(mreza, red)
            ocekivano = [0 for i in range(br_izlaza)]
            for i in range(br_izlaza):
                if len(red[-1]) == 1:
                    ocekivano[i] = red[-1][0]
                else:
                    ocekivano[i] = red[-1][i]
            uk_greska += sum([(ocekivano[i] - izlazi[i])**2 for i in range(len(ocekivano))])
            backpropagation(mreza, ocekivano, red, st)
            if st == -1:
                podesiTezineStoh(mreza, red, eta)
                #print('Ulaz: ' + str(red) + ' izlaz = '+ str(mreza[-1][-1]['izlaz'])+ ' greska = ' + str(uk_greska))
            else:
                #mreza2 = mreza.copy()
                zbrojiGradijente(mreza, red)
        if st != -1:
            o = podesiTezineStan(mreza, eta)
            print('Ulaz: ' + str(len(red)) + ' izlaz = ' + str(mreza[-1][-1]['izlaz']) + ' greska = ' + str(uk_greska))
            if o == 1:
                print('Velike tezine za epohu: ' +str(epoha))
        print('Epoha = ' + str(epoha) + ' Eta = '+ str(eta) + ' Greska = '+str(uk_greska/(2*len(ulazi))))

seed(1)
#mreza = init_network(2,1,2,1)
#for sloj in mreza:
   # print(sloj)
#row = [1, 0, None]

#izlaz = propagacija_unaprijed(mreza, row)
#print(izlaz)
#ocekivano = [0,1]
#backpropagation(mreza, ocekivano)
#print(mreza)

#dataset = [[2.7810836,2.550537003,0],
	#[1.465489372,2.362125076,0],
	#[3.396561688,4.400293529,0],
	#[1.38807019,1.850220317,0],
	#[3.06407232,3.005305973,0],
	#[7.627531214,2.759262235,1],
	#[5.332441248,2.088626775,1],
	#[6.922596716,1.77106367,1],
	#[8.675418651,-0.242068655,1],
	#[7.673756466,3.508563011,1]]

#dataset = [[-1, [1]], [-0.8,[0.64]], [-0.6,[0.36]],[-0.4,[0.16]], [0,[0]], [0.2,[0.04]], [0.4,[0.16]], [0.6,[0.36]], [0.8, [0.64]], [1,[1]]]
dataset = []

f = open('uzorciZaUcenje.txt', 'r')

for i in f.readlines():
    set = (i.split('i:'))
    sviulazi = set[0]
    izlazi = set[1].strip()
    rastavUlaza = sviulazi.split('\t')
    ulazniPodaci = []
    for j in rastavUlaza:
        if j == '':
            continue
        dva = j.split(' ')
        ulazniPodaci.append(float(dva[0]))
        ulazniPodaci.append(float(dva[1]))

    splitizl = izlazi.split(' ')
    izl = []
    for j in splitizl:
        izl.append(float(j))
    ulazniPodaci.append(izl)
    #print(ulazniPodaci)
    dataset.append(ulazniPodaci)
#print(dataset)

n_inputs = (len(dataset[0])-1)
#print(n_inputs)
n_outputs = len(dataset[0][-1])
#print(n_outputs)
#zaPred = [-0.16720668485675302, 0.25656548431105053, -0.26807639836289215, 0.24215552523874495, -0.4986357435197817, 0.24215552523874495, -0.873294679399727, 0.3430252387448841, -1.1038540245566166, 0.41507503410641206, -1.19031377899045, 0.45830491132332885, -1.19031377899045, 0.5015347885402456, -1.161493860845839, 0.6024045020463847, -1.118263983628922, 0.6600443383356072, -1.046214188267394, 0.7465040927694407, -0.9885743519781718, 0.804143929058663, -0.9021145975443383, 0.8473738062755799, -0.7291950886766712, 0.8906036834924966, -0.556275579809004, 0.8906036834924966, -0.2824863574351978, 0.8473738062755799, -0.05192701227830828, 0.8185538881309686, 0.12099249658935883, 0.7609140518417463, 0.1930422919508868, 0.7320941336971352, 0.29391200545702595, 0.6888642564802183, 0.3515518417462483, 0.6168144611186904, 0.4380115961800819, 0.559174624829468, 0.5244713506139155, 0.4727148703956344, 0.5677012278308322, 0.3718451568894953, 0.5677012278308322, 0.27097544338335616, 0.5532912687585266, 0.21333560709413377, 0.5100613915416098, 0.12687585266030021, 0.48124147339699863, 0.08364597544338344, 0.42360163710777626, -0.0028137789904501198, 0.3947817189631651, -0.06045361527967249, 0.3515518417462483, -0.11809345156889486, 0.27950204638472037, -0.17573328785811723, 0.25068212824010916, -0.21896316507503402, 0.1930422919508868, -0.2621930422919508, 0.13540245566166442, -0.3054229195088676, 0.06335266030013646, -0.3774727148703955, 0.00571282401091409, -0.4495225102319235, -0.03751705320600269, -0.5215723055934515, -0.03751705320600269, -0.5792121418826738, -0.03751705320600269, -0.6944918144611186, 0.00571282401091409, -0.7809515688949521, 0.06335266030013646, -0.8530013642564801, 0.13540245566166442, -0.9250511596180081, 0.22186221009549797, -0.9682810368349248, 0.3947817189631651, -1.0259208731241471, 0.5821111869031378, -1.1123806275579808, 0.7838506139154161, -1.1123806275579808, 0.8559004092769441, -1.1123806275579808, 0.9423601637107776, -1.1123806275579808, 0.9855900409276944, -1.0835607094133697, 1.0, -1.0403308321964528]
#print(zaPred)
'''ZA OBICNI'''
network = init_network(n_inputs, [20], n_outputs, 1)
trenirajMrezu(network, dataset, 0.1, 1000, n_outputs, -2)
'''ZA STOHAISTICKI'''
#network = init_network(n_inputs, [20], n_outputs, 1)
#trenirajMrezu(network, dataset, 2, 100, n_outputs, -1)
'''MINI-BATCH'''
#network = init_network(n_inputs, [20], n_outputs, 1)
#trenirajMrezu(network, dataset, 1, 500, n_outputs, 10) #za kvadratnu fju eta = 3, iter = 100000, mreza 10x20x5x1
print('Gotovo treniranje :)')
#for sloj in network:
    #print(sloj)

#predikcija = propagacija_unaprijed(network, [0.8])

#print(predikcija)
#print('Greska = ' + str((0.49 - predikcija[0])))