import re
from math import log

# Returns a list of the unique words from the training set sorted
def init_vocab(train_f):
    vocab = set([])
    with open(train_f, 'r') as trn_set:
        for d in trn_set.readlines():
            line = re.sub(':[\d]+', '', d.strip('\r\n').lower()[2:])
            vocab.update(line.split())
    return sorted(vocab)

# Training function. Returns priors, distributions, and the vocabulary
def train(train_f, k_mult=1, k_bern=0.3):
    voc = init_vocab(train_f)
    v = len(voc)
    # Init book-keeping structs
    d_count = {1: 0, -1: 0}
    w_count = {1: 0, -1: 0}
    bern = {1: dict.fromkeys(voc, 0), -1: dict.fromkeys(voc, 0)}
    multi = {1: dict.fromkeys(voc, 0), -1: dict.fromkeys(voc, 0)}

    # Open the training file and count the words
    with open(train_f, 'r') as trn_set:
        for doc in trn_set.readlines():
            words = doc.strip('\r\n').lower().split()
            label = int(words[0])
            d_count[label] += 1
            for word in words[1:]:
                w,c = word.split(':')[0:2]
                bern[label][w] += 1
                multi[label][w] += int(c)
                w_count[label] += int(c)

    # Normalize counts to obtain distributions
    for w in voc:
        multi[1][w] = (multi[1][w] + k_mult) / (w_count[1] + v * k_mult)
        multi[-1][w] = (multi[-1][w] + k_mult) / (w_count[-1] + v * k_mult)
        bern[1][w] = (bern[1][w] + k_bern) / (d_count[1] + 2 * k_bern)
        bern[-1][w] = (bern[-1][w] + k_bern) / (d_count[-1] + 2 * k_bern)

    d = d_count[1] + d_count[-1]
    return {1: d_count[1]/d , -1: d_count[-1]/d}, bern, multi, voc


# Classifies a document based on a multinomial distribution
def classifyMult(doc, dist, priors):
    expected = int(doc[0])
    # Dict for summing up scores
    scores = {1: log(priors[1]), -1: log(priors[-1])}
    for word in doc[1:]:
        w,c = word.split(':')[0:2]
        if w not in dist[1]: continue
        c = int(c)
        scores[1] += c * log(dist[1][w])
        scores[-1] += c * log(dist[-1][w])
    predicted = max(scores.keys(), key=(lambda k: scores[k]))
    return (expected == predicted), expected, predicted

# Classifies based on Bernoulli
def classifyBern(doc, dist, priors):
    words_in_d = set([])
    true_label = int(doc[0])
    # Dict for summing up scores
    scores = {1: log(priors[1]), -1: log(priors[-1])}
    # Sum up the words that are in the document
    for word in doc[1:]:
        w,c = word.split(':')[0:2]
        if w not in dist[1]: continue
        words_in_d.add(w)
        scores[1] += log(dist[1][w])
        scores[-1] += log(dist[-1][w])
    predict = max(scores.keys(), key=(lambda k: scores[k]))
    return (true_label == predict), true_label, predict

# Classfies a test set, using the provided training set
def classify(test_f, train_f):
    with open(test_f, 'r') as test_set:
        tests = [d.strip('\r\n').lower() for d in test_set.readlines()]
    priors, mult, bern, voc = train(train_f)
    mult_results = [classifyMult(d.split(), mult, priors) for d in tests]
    bern_results = [classifyBern(d.split(), mult, priors) for d in tests]

    print_results(mult_results, mult, voc)
    print_results(bern_results, bern, voc)

# Prints the results of the classifier
def print_results(results, dist, voc, labels=''):
    success_counts = {1: 0, -1: 0}
    doc_counts = {1: 0, -1: 0}
    for succ, exp, pred in results:
        if succ: success_counts[exp] += 1
        doc_counts[exp] += 1

    print('Overall:', (success_counts[1]+success_counts[-1]) / (doc_counts[1]+doc_counts[-1]))
    print('Class 1: ', success_counts[1] / doc_counts[1])
    print('Class -1: ', success_counts[-1] / doc_counts[-1])

    likelihoods1 = sorted(voc, key=(lambda k: dist[1][k]), reverse=True)[0:10]
    likelihoods2 = sorted(voc, key=(lambda k: dist[-1][k]), reverse=True)[0:10]
    odds1 = sorted(voc, key=(lambda k: log(dist[1][k]) - log(dist[-1][k])), reverse=True)[0:10]
    odds2 = sorted(voc, key=(lambda k: log(dist[1][k]) - log(dist[-1][k])))[0:10]
    for l in [likelihoods1, likelihoods2]:
        for w in l: print(w)
        print('')
    for l in [odds1, odds2]:
        for w in l: print(w)
        print('')


rt_train = '../data/movie_review/rt-train.txt'
rt_test = '../data/movie_review/rt-test.txt'
fisher_train = '../data/fisher_2topic/fisher_train_2topic.txt'
fisher_test = '../data/fisher_2topic/fisher_test_2topic.txt'

classify(rt_test, rt_train)
classify(fisher_test, fisher_train)