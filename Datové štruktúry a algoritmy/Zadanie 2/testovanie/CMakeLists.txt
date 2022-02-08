cmake_minimum_required(VERSION 3.17)
project(testovanie C)

set(CMAKE_C_STANDARD 11)

add_executable(testovanie main.c own_hash.h prevzaty_hash.h own_avl_tree.h prevzaty_tree.h)