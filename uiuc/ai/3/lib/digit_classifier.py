import numpy as np
import matplotlib.pyplot as plt
import re
from math import log, inf

# Reads the image and label file and returns an array of label-img pairs
def init_data(image_f, labels_f):
    with open(image_f, 'r') as test_set:
        lines = [re.sub('\+', '#', line.strip('\r\n')) for line in test_set.readlines()]
    with open(labels_f, 'r') as test_set:
        labels = [int(line.strip('\r\n')) for line in test_set.readlines()]
    images = [(lab,
               np.array([list(row) for row in lines[28*i : 28*(i+1)]], dtype=str))
              for i, lab in enumerate(labels)]
    return images

# Trains a distribution and prior based on the training and label files
def train(image_f, labels_f, k=1):
    imgs = init_data(image_f, labels_f)
    counts = np.array([0]*10, dtype=int)
    freqs = [{' ':np.zeros((28,28), dtype=int), '#':np.zeros((28,28), dtype=int)} for i in range(10)]

    for lab, img in imgs:
        counts[lab] += 1
        # freqs[lab][v] is the NumPy matrix holding the counts of the character v for the class lab
        for (r, c), v in np.ndenumerate(img): freqs[lab][v][r,c] += 1

    for i in range(10):
        freqs[i][' '] = (freqs[i][' ']+k) / (counts[i] + k*2)
        freqs[i]['#'] = (freqs[i]['#']+k) / (counts[i] + k*2)

    return (counts / np.sum(counts)), freqs

# Classifies an image based on MAP
def classifyMap(img, priors, freqs):
    scores = [log(priors[i]) for i in range(10)]
    for i in range(10):
        lkhd = freqs[i]
        for (r, c), v in np.ndenumerate(img):
            scores[i] += log(lkhd[v][r,c])
    return max(enumerate(scores), key=(lambda t: t[1]))

# Classifies the images in the test file using the given distribution
def classify(image_f, labels_f, dists):
    priors, freqs = dists
    counts = np.zeros((10,10), dtype=int)
    maxs = [(-inf, -1)]*10
    mins = [(inf, -1)]*10

    imgs = init_data(image_f, labels_f)
    for i, (lab, img) in enumerate(imgs):
        pred, score = classifyMap(img, priors, freqs)
        counts[lab, pred] += 1
        scr_i = (score, i)
        maxs[pred] = max(maxs[pred], scr_i)
        mins[pred] = min(mins[pred], scr_i)

    conf_mat = np.zeros((10,10))
    for i in range(10):
        row = counts[i,:]
        conf_mat[i,:] = row / np.sum(row)
    return counts, conf_mat, maxs, mins, imgs

# Prints the label-img pair
def print_img(i_img):
    digit, img = i_img
    print(digit, '\n')
    print('\n'.join(''.join(str(cell) for cell in row)
                    for row in img))

# Prints out the images for each class with the highest and lowest MAP value
def show_minmax_posteriors(imgs, maxs, mins):
    for i in range(10):
        print(i, '=========================================')
        _, max_i = maxs[i]
        _, min_i = mins[i]
        print_img(imgs[max_i])
        print_img(imgs[min_i])

# Displays a graphic showing the likelihood distribution of each class, and then their log odds ratio
def show_odds(freqs, c1, c2):
    logfunc = np.vectorize((lambda t: log(t)))
    x = freqs[c1]['#']
    y = freqs[c2]['#']
    z = logfunc(x) - logfunc(y)
    show_array(x)
    show_array(y)
    show_array(z)

# Helper function for displaying an array of values as a heat map
def show_array(z):
    plt.imshow(z ,cmap=plt.get_cmap('jet'), vmin = np.amin(z),vmax = np.amax(z))
    plt.colorbar()
    plt.show()



train_imgs = '../data/digitdata/trainingimages'
train_labs = '../data/digitdata/traininglabels'
test_imgs = '../data/digitdata/testimages'
test_labs = '../data/digitdata/testlabels'

dists = train(train_imgs, train_labs, 0.3)

# show_odds(dists[1], 4, 9)
# show_odds(dists[1], 7, 9)
# show_odds(dists[1], 8, 3)
# show_odds(dists[1], 5, 6)
show_odds(dists[1], 1, 8)

# c,m, maxs, mins, imgs = classify(test_imgs, test_labs, dists)
# confusion_mat = np.round(c, decimals=3)
# print(confusion_mat)
# show_minmax_posteriors(imgs, maxs, mins)