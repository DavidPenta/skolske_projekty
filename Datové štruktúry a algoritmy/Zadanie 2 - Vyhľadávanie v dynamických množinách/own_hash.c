#include<stdio.h>
#include <string.h>
#include <time.h>

#define n 999983

struct bucket{  // vytvorenie bucketu
    char free;
    char data[15];
};

struct bucket table[n];  // vyrobenie tabulky bucketov

void init() {  // inicializacia a vycistenie tabulky
    for (int i = 0; i < n; i++) {
        table[i].free = 1;
        memset(table[i].data,0,sizeof(table[i].data));
    }
}

unsigned long hash(char *str) // hashovacia funkcia
{
    int i, len = strlen(str);
    unsigned long hash = 0;
    for (i = 0; i < len; i++)
        hash = 53 * hash + str[i];
    return hash;
}

int insert(char* str){ // vlozenie stringu do hashovacej tabulky
    int number_of_collisions = 0;
    unsigned long i = hash(str) % n;
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
unsigned long search(char* str){ // vyhladavacia funkcia
    unsigned long i = hash(str) % n;
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

void print(){
    for (unsigned long i = 0; i < n; i++) {
        printf("%ld: %s,%ld\n",i,table[i].data, table[i].free);
    }
}

void test(FILE* filePointer, unsigned long m){ // testovanie vstupov zo subora

    char buffer[16];

    long double search_count = 0;
    unsigned long number_of_collisions = 0;

    double time_spent = 0.0;
    clock_t begin = clock();
    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        number_of_collisions += insert(buffer);
    }
    rewind(filePointer);

    for (int i = 0; i < m; i++){
        fgets(buffer, 16, filePointer);
        buffer[strcspn(buffer, "\n")] = 0;
        search_count += search(buffer);
    }
    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;

    printf("Hashing %lu words into table of size %d: %lf%%\n",m,n,  (double) (search_count / m)*100 );
    printf("Number of collisions: %ld.\n", number_of_collisions);
    printf("Time elpased is %f seconds.\n", time_spent);
    printf("\n");
    rewind(filePointer);

}

int main (void) {

    FILE* f;
    f = fopen("C:\\Users\\david\\Desktop\\Best own hash\\cmake-build-debug\\random_strings", "r");

    init();
    test(f, 10000);

    init();
    test(f, 500000);

    init();
    test(f, 999983);

    fclose(f);
    return 0;
}
