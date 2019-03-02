import time

EXACT = 0
LOWERB = -1
UPPERB = 1


class Counter:
    def __init__(self): self.count = 0
    def inc(self): self.count += 1
    def reset(self): self.count = 0
    def __int__(self): return self.count
    def __str__(self): return str(self.count)

class Timer:
    def __init__(self): self.t = 0
    def start(self): self.t = time.time()
    def stop(self): self.t = time.time() - self.t
    def time(self): return self.t
    def __str__(self): return str(self.t)


def TTEntry(bestVal, d, aOrig, b):
    if bestVal <= aOrig: flag = UPPERB
    elif bestVal >= b: flag = UPPERB
    else: flag = EXACT
    return (bestVal, d, flag)

def serialize(key):
    t, gr = key
    return str(t)+str(gr.flatten())

class TTable:

    def __init__(self):
        self.tab = {}

    def __getitem__(self, key):
        try:
            return self.tab[serialize(key)]
        except Exception:
            return None

    def __setitem__(self, key, value):
        (val, d, aOrig, b) = value
        self.tab[serialize(key)] = TTEntry(val,d,aOrig,b)

    def __delitem__(self, key):
        del self.tab[serialize(key)]

    def clear(self):
        self.tab.clear()
