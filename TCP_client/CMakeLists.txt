cmake_minimum_required(VERSION 3.16)
project(pcom_hw2_udp_client C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(pcom_hw2_udp_client
        helpers.h
        server.c
        subscriber.c)
