#include<stdio.h>
#include <string.h>
#include <time.h>
#include "own_avl_tree.h"
#include "prevzaty_tree.h"
#include "own_hash.h"
#include "prevzaty_hash.h"


void test1(FILE* f, int m){
    struct Block* root = NULL;
    unsigned long long b;
    rotations1 = 0;

    double time_spent = 0.0;
    clock_t begin = clock();

    for (int i = 0; i < m; i++){
        fscanf(f, "%15llu", &b);
        root = insert1(root, b);
    }


    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Inserting %d numbers into AVL Tree\n",m);
    printf("Total rotations: %lu.\n", rotations1);
    printf("Time elpased: %f seconds.\n", time_spent);
}

void test2(FILE* f, int m){
    struct node* root = NULL;
    unsigned long long b;
    rotations2 = 0;

    double time_spent = 0.0;
    clock_t begin = clock();

    for (int i = 0; i < m; i++){
        fscanf(f, "%15llu", &b);
        insert2(&root, b);
    }

    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Inserting %d numbers into Black and Red Tree\n",m);
    printf("Total rotations: %lu.\n", rotations2);
    printf("Time elpased: %f seconds.\n", time_spent);
}

void test3(FILE* filePointer, int m){ // testovanie vstupov zo subora

    char buffer[16];

    long double search_count = 0;
    unsigned long long number_of_collisions = 0;

    double time_spent = 0.0;
    clock_t begin = clock();
    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        number_of_collisions += insert3(buffer);
    }
    rewind(filePointer);

    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        search_count += search3(buffer);
    }
    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Linear probing:\n");
    printf("Hashing %lu words into table of size %d: %lf%%\n",m,n,  (double) (search_count / m)*100 );
    printf("Number of collisions: %lld.\n", number_of_collisions);
    printf("Time elpased is %f seconds.\n", time_spent);
    printf("\n");

}

void test4(FILE* filePointer, int m){

    char buffer[16];

    long double search_count = 0;
    unsigned long number_of_collisions = 0;

    double time_spent = 0.0;
    clock_t begin = clock();
    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        number_of_collisions += insert4(buffer);
    }
    rewind(filePointer);

    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        search_count += search4(buffer);
    }
    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;

    printf("Chaining:\n");
    printf("Hashing %lu words into table of size %d: %lf%%\n",m,size,  (double) (search_count / m)*100 );
    printf("Number of collisions: %ld.\n", number_of_collisions);
    printf("Time elpased is %f seconds.\n", time_spent);
    printf("\n");

}

int main(){
    FILE* f_numbers;
    FILE* f_strings;
    f_numbers = fopen("C:\\Users\\david\\Desktop\\random_numbers.txt", "r");
    int m = 100000;

    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");
    m = 250000;

    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");

    m = 500000;
    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");

    m = 750000;
    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");

    m = 900000;
    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");

    m = 1000000;
    test1(f_numbers,m);
    printf("\n");
    test2(f_numbers,m);
    printf("--------------------------------------------------------------------------------------\n");


    printf("\n\n");
    fclose(f_numbers);

    f_strings = fopen("C:\\Users\\david\\Desktop\\random_strings.txt", "r");
    printf("--------------------------------------------------------------------------------------\n");



    printf("--------------------------------------------------------------------------------------\n");
    m = 100000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    printf("--------------------------------------------------------------------------------------\n");
    m = 250000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    printf("--------------------------------------------------------------------------------------\n");
    m = 500000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    printf("--------------------------------------------------------------------------------------\n");
    m = 600000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    printf("--------------------------------------------------------------------------------------\n");
    m = 750000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    printf("--------------------------------------------------------------------------------------\n");
    m = 900000;
    init3();
    test3(f_strings, m);
    rewind(f_strings);
    init4();
    test4(f_strings, m);
    rewind(f_strings);

    fclose(f_strings);

    return 0;

}
