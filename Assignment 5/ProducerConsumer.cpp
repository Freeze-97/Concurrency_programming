//
// Tommy Yasi
// toya1800
#include "ProducerConsumer.h"

ProducerConsumer::ProducerConsumer(int pNUM_OF_PRODUCERS, int pNUM_OF_CONSUMERS, int pNUM_OF_ELEMENTS, int pQ_SIZE)
        : NUM_OF_ELEMENTS(pNUM_OF_ELEMENTS), NUM_OF_CONSUMERS(pNUM_OF_CONSUMERS),
        NUM_OF_PRODUCERS(pNUM_OF_PRODUCERS), Q_SIZE(pQ_SIZE) {}

void ProducerConsumer::run() {
    // Elements per producer but not the last one
    int numsPerThread = NUM_OF_ELEMENTS / NUM_OF_PRODUCERS;
    int lastElement = numsPerThread * NUM_OF_PRODUCERS - 1;
    int lastBitSize = NUM_OF_ELEMENTS - lastElement;

    // Information
    cout << "Number of elements: " << NUM_OF_ELEMENTS << endl;
    cout << "Producers: " << NUM_OF_PRODUCERS << endl;
    cout << "Consumers: " << NUM_OF_CONSUMERS << endl;
    cout << "Queue size: 10" << endl << endl;

    // Create queue
    auto *blockingQueue = new BlockingQueue<int>(Q_SIZE);

    // Thread vector
    vector<thread> producerThreads;

    // Creating Producer threads
    for (int i = 0; i < NUM_OF_PRODUCERS -1; i++) {
        // The first int and the last int of the interval
        int firstInt = i * numsPerThread;
        int lastInt = firstInt + numsPerThread;

        // Info
        cout << "Starting a producer for " << numsPerThread << " values. Interval [" << firstInt
             << "..." << lastInt << "]" << endl;

        // Create Producer-threads
        producerThreads.emplace_back(std::thread(Producer(blockingQueue, firstInt, lastInt)));
    }

    // Info on last Producer thread
    cout << "Starting a producer for " << lastBitSize << " values. Interval [" << lastElement
         << "..." << NUM_OF_ELEMENTS << "]" << endl;

    // The last Producer thread
    producerThreads.emplace_back(std::thread(Producer(blockingQueue, lastElement, NUM_OF_ELEMENTS)));

    // See how many values every Consumer thread needs
    numsPerThread = NUM_OF_ELEMENTS / NUM_OF_CONSUMERS;
    lastBitSize = NUM_OF_ELEMENTS - (numsPerThread*(NUM_OF_CONSUMERS - 1));

    // Consumer vector
    auto consumerFutures = vector<future<shared_ptr<deque<int>>>>();

    // Create Consumer futures
    for (int i = 0; i < NUM_OF_CONSUMERS - 1; i++) {
        future<shared_ptr<deque<int>>> fu = async(std::launch::async, Consumer(blockingQueue, numsPerThread));
        consumerFutures.emplace_back(move(fu));
    }

    // Last one done separated because the amount of elements usually won't be the same
    future<shared_ptr<deque<int>>> fu = async(std::launch::async, Consumer(blockingQueue, lastBitSize));
    consumerFutures.emplace_back(move(fu));

    // Now get the results from the consumer vector
    vector<deque<int>> pDeq;
    int nElem = 0; // Show how many elements we got
    for (auto &con : consumerFutures) {
        cout << "Waiting... ";
        pDeq.push_back(*con.get());
        cout << "got a result" << endl;
        nElem++;
    }
    cout << "Got " << nElem << " values" << endl << endl;

    // Join all producer threads when it is all done
    for (auto &pt : producerThreads) {
        if (pt.joinable()) {
            pt.join();
            cout << "Producer thread joined" << endl;
        }
    }

    // The program is finished
    cout << "----- FINISHED -----" << endl;
}
