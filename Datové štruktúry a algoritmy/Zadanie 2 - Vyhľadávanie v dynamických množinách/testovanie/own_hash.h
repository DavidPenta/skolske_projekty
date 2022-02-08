#include<stdio.h>
#include <string.h>
#include <time.h>

#define n 999983

struct bucket{  // vytvorenie bucketu
    char free;
    char data[15];
};

struct bucket table[n];  // vyrobenie tabulky bucketov

void init3() {  // inicializacia a vycistenie tabulky
    for (int i = 0; i < n; i++) {
        table[i].free = 1;
        memset(table[i].data,0,sizeof(table[i].data));
    }
}

unsigned long hash3(char *str) // hashovacia funkcia
{
    int i, len = strlen(str);
    unsigned long hash = 0;
    for (i = 0; i < len; i++)
        hash = 53 * hash + str[i];
    return hash;
}

int insert3(char* str){ // vlozenie stringu do hashovacej tabulky
    int number_of_collisions = 0;
    unsigned long i = hash3(str) % n;
    while (table[i].free == 0){ // riesenie kolizii linearnim skusanim
        if (i < n){
            i++;
            number_of_collisions++;
        }
        else {
            //printf("Not enough space in the hash table.\n");
            break;
        }
    }
    if (i < n){
        strcpy(table[i].data, str);
        table[i].free = 0;
    }
    return number_of_collisions;

}
unsigned long search3(char* str){ // vyhladavacia funkcia
    unsigned long i = hash3(str) % n;
    while (table[i].free == 0){
        if (strcmp(table[i].data, str) == 0)
            return 1;
        if (i < n)
            i++;
        else {
            return 0;
        }
    }
}