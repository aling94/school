import numpy as np
import random

# Converts a line from the file into an array of ones and zeros
def convertSyms(str):
    s = str.strip('\r\n')
    return [1 if (c == '#' or c == '+') else 0 for c in s]

# Creates a feature vector for the ith image in the data array
def featVect(data, i, bias):
    vect = np.array(data[28*i : 28*(i+1)]).flatten()
    return np.append(vect, 1) if bias else vect


# Reads the image and label file and returns an array of label-img pairs
def init_data(image_f, labels_f, bias=False):

    with open(image_f, 'r') as test_set:
        lines = [convertSyms(line) for line in test_set.readlines()]
    with open(labels_f, 'r') as test_set:
        labels = [int(line.strip('\r\n')) for line in test_set.readlines()]
    images = [(lab, featVect(lines, i, bias))
              for i, lab in enumerate(labels)]
    return images

# Trains a weight matrix based on the training and label files
def train(image_f, labels_f, bias=False, randOrd=False, initRand=False, maxEpoch=1, k=50):
    imgs = init_data(image_f, labels_f, bias)
    if randOrd: random.shuffle(imgs)
    w_len = 28**2 + 1 if bias else 28**2
    weights = np.random.rand(10, w_len) if initRand else np.zeros((10, w_len))

    sample_n = len(imgs)
    for t in range(maxEpoch):
        if randOrd: random.shuffle(imgs)
        a = k / (k+t)
        correct_n = 0
        for c, x in imgs:
            res = np.argmax(np.dot(weights,x))
            if c == res:
                correct_n += 1
                continue
            weights[c] += a*x
            weights[res] -= a*x
        # print(t+1, correct_n/sample_n)
        if correct_n == sample_n: break
    return weights

# Classifies the images in the test file using the given weights
def classify(image_f, labels_f, bias, weights):
    imgs = init_data(image_f, labels_f, bias)
    counts = np.zeros((10,10), dtype=int)
    for c, x in imgs:
        res = np.argmax(np.dot(weights,x))
        counts[c, res] += 1

    conf_mat = np.array([row/np.sum(row) for row in counts])
    ovr_acc = np.sum(np.diag(counts)) / len(imgs)
    return ovr_acc, conf_mat, counts

# Prints the label-img pair
def print_img(i_img):
    digit, img = i_img
    print(digit, '\n')
    print(''.join(str(img)))

# Prints a matrix of numbers
def print_mat(m):
    m = np.round(m, decimals=3)
    print('\n'.join(' '.join(str(cell) for cell in row)
                    for row in m))


train_imgs = '../data/digitdata/trainingimages'
train_labs = '../data/digitdata/traininglabels'
test_imgs = '../data/digitdata/testimages'
test_labs = '../data/digitdata/testlabels'

bias = True
randOrd = True
randInit = True
maxEpoch = 150
trials = 10

for t in range(trials):
    w = train(train_imgs, train_labs, bias, randOrd, randInit, maxEpoch, 2000)
    acc, conf, cts = classify(test_imgs, test_labs, bias, w)
    print(acc)
    print_mat(conf)
    print('')

# Parameter Tuning

# for decay in range(2000, 2110, 10):
#     print(decay, end=' ')
#     counts = np.zeros((10,10), dtype=int)
#     for t in range(trials):
#         w = train(train_imgs, train_labs, bias, randOrd, randInit, maxEpoch, decay)
#         _, _, cts = classify(test_imgs, test_labs, bias, w)
#         counts += cts
#     ovr_acc = np.sum(np.diag(counts)) / np.sum(counts)
#     print(ovr_acc)

# bools = [True, False]
# for bias in bools:
#     for randOrd in bools:
#         for randInit in bools:
#             print('Bias?','Yes' if bias else 'No')
#             print('Weights init:', 'Random' if randInit else 'Zero')
#             print('Training order', 'Random' if randOrd else 'Fixed')
#             for i in range(trials):
#                 w = train(train_imgs, train_labs, bias, randOrd, randInit, 100)
#                 classify(test_imgs, test_labs, w)
#             print('===============================================================================')
