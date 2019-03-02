import random
from math import floor
from numpy import sign

S0 = (0.5, 0.5, 0.03, 0.01, 0.4)
Actions = [0.04, 0, -0.04]
State_Space = [((x, y, vx, vy, pad_y), a)
               for x in range(12) for y in range(12)
               for vx in [1,-1] for vy in [1,0,-1]
               for pad_y in range(12)
               for a in Actions]

def update_ball(state):
    if not state: return None
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
    if x >= 1:
        if y < pad_y or y > pad_y + 0.2: return None
        x = 2 - x_old
        vx = -vx + random.uniform(-0.015, 0.015)
        vy = -vy + random.uniform(-0.03, 0.03)
        if abs(vx) < 0.03: vx = sign(vx) * 0.03
    return (x,y,vx,vy, pad_y)


def act(s,a):
    x,y,vx,vy, pad_y = s
    pad_y += a
    pad_y = max(0, pad_y)
    pad_y = min(0.8, pad_y)
    return (x,y,vx,vy, pad_y)

def reward(state):
    x,y,vx,vy, pad_y = state
    x += vx
    y += vy

    # Bounce off walls
    if y < 0: y = -y
    if y > 1: y = 2 - y
    if x < 0: x = -x
    # Bounce off paddle
    if x >= 1:
        if y < pad_y or y > pad_y + 0.2: return -1
        return 1
    return 0


def discrete(state):
    x,y,vx,vy, pad_y = state
    x = min(11, floor(12*x))
    y = min(11, floor(12*y))
    if abs(vy) < 0.015: vy = 0

    vx = sign(vx)
    vy = sign(vy)
    pad_y = min(11, floor(12 * pad_y / 0.8))
    return (x,y,vx,vy, pad_y)


Disc = 0.3
K = 1000
Trial_Max = 500000
Ne = 100
R_exp = 0.35

def train():
    N = dict.fromkeys(State_Space, 0)
    Q = dict.fromkeys(State_Space, 0)
    explore = lambda u,n : R_exp if n < Ne else u
    bounces = 0
    fails = 0
    for t in range(Trial_Max):
        if t % 10000 == 0: print(t)
        s = S0
        while s:
            rs = reward(s)
            sd = discrete(s)
            sp = s
            if rs == -1:
                fails += 1
                for a in Actions: Q[(sd,a)] = -1
            else:
                a = max([(sd,a) for a in Actions], key=lambda sa: explore(Q[sa], N[sa]))[1]
                decay = K / (K + N[(sd,a)])
                N[(sd,a)] += 1
                sp = act(s,a)
                max_Qsp = max([Q[sa] for sa in [(discrete(sp),a) for a in Actions]])
                Q[(sd,a)] += decay * (rs + Disc*max_Qsp - Q[(sd,a)])
            s = update_ball(sp)
            if rs == 1: bounces += 1
    return Q,N, bounces, fails
#
# q,n,b,f = train()
#
# print(b, f)
#
# qp = [(k,v) for k,v in q.items() if v != 0]
# np = [(k,v) for k,v in n.items() if v != 0]
#
# print('Q vals filled', len(qp)/len(q))
# print('N vals filled', len(np)/len(n))
#
# for k,v in qp:
#     print(k, v)
#
# for k,v in np:
#     print(k, v)

def foo(x):
    x = 5


a = 40
print(a)
foo(a)
print(a)
