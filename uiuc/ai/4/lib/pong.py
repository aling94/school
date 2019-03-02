from random import uniform as rand_fl
from math import floor
from numpy import sign
import pickle, pygame, sys


def save_obj(obj, name ):
    with open(name + '.pkl', 'wb') as f:
        pickle.dump(obj, f, pickle.HIGHEST_PROTOCOL)

def load_obj(name ):
    with open(name + '.pkl', 'rb') as f:
        return pickle.load(f)


pygame.init()
scn = pygame.display.set_mode((480,480))


def draw(state):
    x,y,vx,vy, pad_y = state

    x = int(x*480)
    y = int(y*480)
    pad_y = int(pad_y*480)

    white = (255,255,255)
    black = (0,0,0)
    scn.fill(white)
    pygame.draw.lines(scn, black, True, [(478, pad_y), (478, pad_y + 96)], 2)
    pygame.draw.circle(scn, black, (x, y), 2, 0)
    pygame.display.update()

S0 = (0.5, 0.5, 0.03, 0.01, 0.4)
FAIL = (10, -1, -1, -1, -1)
Actions = [0.04, 0, -0.04]
State_Space = [((x, y, vx, vy, pad_y))
               for x in range(12)
               for y in range(12)
               for vx in [1,-1]
               for vy in [1,0,-1]
               for pad_y in range(12) ] + [FAIL]

All_SA = [(s,a) for s in State_Space for a in Actions]


def update_ball(state):
    if not state: return None
    if state == FAIL: return FAIL

    x,y,vx,vy, pad_y = state
    x_old = x
    x += vx
    y += vy

    # Bounce off walls
    if y < 0:
        y = -y
        vy = -vy
    if y > 1:
        y = 2 - y
        vy = -vy
    if x < 0:
        x = -x
        vx = -vx

    # Bounce off paddle
    if x > 1:
        if y < pad_y or y > pad_y + 0.2: return -1, FAIL
        x = 2 - x_old
        vx = -vx + rand_fl(-0.015, 0.015)
        vy = -vy + rand_fl(-0.03, 0.03)
        if abs(vx) < 0.03: vx = sign(vx) * 0.03
        return 1, (x,y,vx,vy, pad_y)
    return 0, (x,y,vx,vy, pad_y)


def act(s,a):
    if s == FAIL: return FAIL
    x,y,vx,vy, pad_y = s
    pad_y += a
    pad_y = min(max(0, pad_y), 0.8)
    return update_ball((x,y,vx,vy, pad_y))


def discrete(state):
    if state == FAIL: return FAIL
    x,y,vx,vy, pad_y = state
    x = min(11, floor(12*x))
    y = min(11, floor(12*y))
    if abs(vy) < 0.015: vy = 0

    vx = sign(vx)
    vy = sign(vy)
    pad_y = min(11, floor(12 * pad_y / 0.8))
    return (x,y,vx,vy, pad_y)


Disc = 0.8
K = 50
Trial_Max = 100000
Ne = 100
R_exp = 2

def q_learn():
    N = dict.fromkeys(All_SA, 0)
    Q = dict.fromkeys(All_SA, 0)
    explore = lambda u,n : R_exp if n < Ne else u
    bounces = 0
    b = 0
    for t in range(Trial_Max):
        if t % 10000 == 0:
            print(t, bounces, '(+ %d)' % b)
            print('Q vals filled', len([k for k,v in Q.items() if v != 0])/len(Q))
            print('N vals filled', len([k for k,v in N.items() if v != 0])/len(N))
        s = S0
        b = 0
        while s != FAIL:
            sd = discrete(s)
            a = max([(sd, a) for a in Actions], key=lambda sa: explore(Q[sa], N[sa]))[1]
            sda = (sd,a)
            r, ss = act(s,a)
            decay = K / (K + N[sda])
            N[sda] += 1
            max_Qsp = 0 if ss == FAIL else max([Q[sa] for sa in [(discrete(ss), a) for a in Actions]])
            Q[sda] += decay * (r + Disc * max_Qsp - Q[sda])
            if r == 1:
                bounces += 1
                b += 1
            s = ss

    print(100000, bounces, '(+ %d)' % b)
    print('Q vals filled', len([k for k,v in Q.items() if v != 0])/len(Q))
    print('N vals filled', len([k for k,v in N.items() if v != 0])/len(N))

    save_obj(Q, 'qvals')
    save_obj(N, 'nvals')


    return Q,N, bounces

def test(Q):
    total = 0
    maxb = 0
    minb = 1000
    for t in range(1000):
        s = S0
        bounces = 0
        while s != FAIL and bounces < 50:
            sd = discrete(s)
            a = max([(sd, a) for a in Actions], key=lambda sa: Q[sa])[1]
            r, ss = act(s,a)
            if r == 1: bounces += 1
            s = ss
        total += bounces
        minb = min(minb, bounces)
        maxb = max(maxb, bounces)
    print('AVG', total/1000)
    print('MAX', maxb)
    print('MIN', minb)


# q,n,b = q_learn()
# test(q)
while 1:
    draw(S0)

