cmake_minimum_required(VERSION 3.15)
project(BlockingQueue)

set(CMAKE_CXX_STANDARD 11)

add_executable(BlockingQueue main.cpp BlockingQueue.h BlockingQueue.cpp
        Producer.h Producer.cpp Consumer.h Consumer.cpp ProducerConsumer.h ProducerConsumer.cpp)