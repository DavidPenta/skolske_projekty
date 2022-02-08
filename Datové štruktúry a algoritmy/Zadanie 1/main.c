#include<stdio.h>
#include<stddef.h>
#include <string.h>
#include <stdlib.h>

char* first = NULL; // jedina globalna premenna, ktora bude oznacovat zaciatok vyhradenej pamate

struct block_head{ // hlavicka kazdeho bloku v pamati
    int size;
    struct block_head *next_explicit;
    struct block_head *next_implicit;
};

struct start_of_memory{ // pointrere na zaciatky spajanich zoznamov
    struct block_head *head; // bude oznacovat pionter na volny blok velkosti 1-8
    struct block_head *head8; // 9-64
    struct block_head *head64; // 65 - 256
    struct block_head *head256; // 257+
};

void memory_init(void *ptr, unsigned int size){
    size = size - sizeof(struct start_of_memory);
    first = ptr;
    struct start_of_memory *free_block = (struct start_of_memory *) (char *) first;
    first = first + sizeof(struct start_of_memory);
    struct block_head *freeList = (struct block_head *) (char *) first;


// vytvorenie prveho bloku ktory obsahuje celu priradenu pamat
    freeList->size = size - sizeof(struct block_head);
    freeList->next_explicit = NULL;
    freeList->next_implicit = NULL;

// pridelenie prveho bloku do spajaneho zoznamu spravnej velkostnej triedy
    if (freeList->size > 256) {
        free_block->head = NULL;
        free_block->head8 = NULL;
        free_block->head64 = NULL;
        free_block->head256 = freeList;
    }

    else if (freeList->size > 64) {
        free_block->head = NULL;
        free_block->head8 = NULL;
        free_block->head64 = freeList;
        free_block->head256 = NULL;
    }

    else if (freeList->size > 8) {
        free_block->head = NULL;
        free_block->head8 = freeList;
        free_block->head64 = NULL;
        free_block->head256 = NULL;
    }

    else {
        free_block->head = freeList;
        free_block->head8 = NULL;
        free_block->head64 = NULL;
        free_block->head256 = NULL;
    }
}

void split(struct block_head *original_block, struct block_head *prev_slot ,unsigned int size){

    struct start_of_memory *top = (struct start_of_memory *) (first - sizeof(struct start_of_memory));
    struct block_head *block, *block8, *block64, *block256;
    struct block_head *new = (struct block_head *) (char *) ((char *) original_block + size + sizeof(struct block_head));

    // vytvorenie noveho bloku v pameti rozdelenim
    new->size=(original_block->size)-size-sizeof(struct block_head);
    new->next_explicit = NULL;
    new->next_implicit = original_block->next_implicit;

    if (prev_slot != NULL)  // ak nebol prvy v zozname, tak blok ktory kazoval na tento blok bude ukazovat na blok za tymto
        prev_slot->next_explicit = original_block->next_explicit; // musi byt stale v tej istej velkostnej skupine

    else{  // ak bol prvy v zozname, tak sa na neho nastavy zaciatok spajaneho zoznamu
        if(top->head256 == original_block)
            top->head256 = original_block->next_explicit;
        else if(top->head64 == original_block)
            top->head64 = original_block->next_explicit;
        else if(top->head8 == original_block)
            top->head8 = original_block->next_explicit;
        else if(top->head == original_block)
            top->head = original_block->next_explicit;

    }
    block = top->head;
    block8 = top->head8;
    block64 = top->head64;
    block256 = top->head256;

// priradenie noveho bloku do spravneho zoznamu
    if (new->size > 256){
        if (block256 == NULL) // ak je zoznam prazny
            top->head256 = new;
        else{
            while (block256->next_explicit != NULL)
                block256 = block256->next_explicit;
            block256->next_explicit = new;
        }
    }
    else if (new->size > 64){
        if (block64 == NULL)
            top->head64 = new;
        else{
            while (block64->next_explicit != NULL)
                block64 = block64->next_explicit;
            block64->next_explicit = new;
        }
    }
    else if (new->size > 8){
        if (block8 == NULL)
            top->head8 = new;
        else{
            while (block8->next_explicit != NULL)
                block8 = block8->next_explicit;
            block8->next_explicit = new;
        }
    }
    else{
        if (block == NULL)
            top->head = new;
        else{
            while (block->next_explicit != NULL)
                block = block8->next_explicit;
            block->next_explicit = new;
        }
    }

    original_block->size = -size;
    original_block->next_implicit = new;
    original_block->next_explicit = NULL; // blok je pouzivany, tak explicitne nebude ukazovat na ziadny blok
}

void *memory_alloc(unsigned int size){
    struct start_of_memory *top;
    struct block_head *block, *block8, *block64, *block256, *selected_block = NULL, *prev = NULL;
    top = first - sizeof(struct start_of_memory) ;
    block = top -> head;
    block8 = top -> head8;
    block64 = top -> head64;
    block256 = top -> head256;

    if (block == NULL && block8 == NULL && block64 == NULL && block256 == NULL) // ak nikde nie je miesto tak vrati NULL
        return NULL;

    //hadanie volneho bloku rovnakej velkosti

    if (size > 256){
        while(block256 != NULL){
            if((block256->size) == size){
                block256->size = -block256->size;
                if (prev == NULL)
                    top -> head8 = block256 -> next_explicit;
                else
                    prev -> next_explicit = block256 -> next_explicit;
                block256->next_explicit=NULL;
                return (char*)(++block256);

            }
            prev = block256;
            block256 = block256 -> next_explicit;
        }
    }
    else if (size > 64){
        while(block64 != NULL){
            if((block64->size) == size){
                block64->size = -block64->size;
                if (prev == NULL)
                    top -> head64 = block64 -> next_explicit;
                else
                    prev -> next_explicit = block64 -> next_explicit;
                block64->next_explicit=NULL;
                return (char*)(++block64);

            }
            prev = block64;
            block64 = block64 -> next_explicit;
        }
    }

    else if (size > 8){
        while(block8 != NULL){
            if((block8->size) == size){
                block8->size = -block8->size;
                if (prev == NULL)
                    top -> head8 = block8 -> next_explicit;
                else
                    prev -> next_explicit = block8 -> next_explicit;
                block8->next_explicit=NULL;
                return (char*)(++block8);

            }
            prev = block8;
            block8 = block8 -> next_explicit;
        }
    }

    else {
        while (block != NULL) {
            if ((block->size) == size) {
                block->size = -block->size;
                if (prev == NULL)
                    top->head = block->next_explicit;
                else
                    prev->next_explicit = block->next_explicit;
                block->next_explicit=NULL;
                return (char *) (++block);

            }
            prev = block;
            block = block->next_explicit;
        }
    }

    //ak sa nenasiel perfect fit

    block = top -> head;
    block8 = top -> head8;
    block64 = top -> head64;
    block256 = top -> head256;
    prev = NULL;

    // hladanie najmenieho pouzitelneho bloku
    if (size <= 8 && block != NULL)
        while(((block->size) < size) && (block -> next_explicit != NULL))
            block=block->next_explicit;
    selected_block = block;

    if ((size <= 64) && ((selected_block == NULL) || (selected_block->size) < size)) {
        while ((block8 != NULL) && ((block8->size) < size) && (block8->next_explicit != NULL))
            block8 = block8->next_explicit;
        selected_block = block8;
    }
    if ((size <= 256) && ((selected_block == NULL) || (selected_block->size) < size)) {
        while ((block64 != NULL) && ((block64->size) < size) && (block64->next_explicit != NULL))
            block64 = block64->next_explicit;
        selected_block = block64;
    }
    if (((selected_block == NULL) || (selected_block->size) < size)) {
        while ((block256 != NULL) && ((block256->size) < size) && (block256->next_explicit != NULL))
            block256 = block256->next_explicit;
        selected_block = block256;
    }

    //zapisanie do najdeneho bloku

    if((selected_block != NULL) && (selected_block->size) > (size+sizeof(struct block_head))){ // ak je najdeni blok dostatocne velky tak sa rozdeli na dva
        split(selected_block, prev, size);
        return (char*)(++selected_block);
    }

    if((selected_block != NULL) && (selected_block->size) > size){ // ak nie je dostatocne velky tak sa pouzije cely


        if (selected_block->size > 256) {
            if (prev == NULL) // ak bol prvy v zozname
                top->head256 = block256->next_explicit;
            else
                prev->next_explicit = block256->next_explicit;
            block256->next_explicit = NULL;
            selected_block->size = - selected_block->size;
            return (char *) (++selected_block);
        }
        if (selected_block->size > 64) {
            if (prev == NULL)
                top->head64 = block64->next_explicit;
            else
                prev->next_explicit = block64->next_explicit;
            block64->next_explicit = NULL;
            selected_block->size = - selected_block->size;
            return (char *) (++selected_block);
        }
        if (selected_block->size > 8) {
            if (prev == NULL)
                top->head8 = block8->next_explicit;
            else
                prev->next_explicit = block8->next_explicit;
            block8->next_explicit = NULL;
            selected_block->size = - selected_block->size;
            return (char *) (++selected_block);
        }

        if (selected_block->size > 0) {
            if (prev == NULL)
                top->head = selected_block->next_explicit;
            else
                prev->next_explicit = selected_block->next_explicit;
            selected_block->next_explicit = NULL;
            selected_block->size = - selected_block->size;
            return (char *) (++selected_block);
        }
    }
    else // ak nebol najdeny vyhovujuci blok
        return NULL;
}
void connect2(){  // ide cez celu vyhradenu pamat a usporiadava do zoznamou
    struct start_of_memory *top;
    struct block_head *block, *prev = NULL,*prev8 = NULL,*prev64 = NULL,*prev256 = NULL;
    block = (struct block_head *) first;
    top = (struct start_of_memory *) (first - sizeof(struct start_of_memory));

    top->head = NULL;
    top->head8 = NULL;
    top->head64 = NULL;
    top->head256 = NULL;

    while(block != NULL){
        if (block->size > 0) { // ak je blok volny
            if (block->size > 256) {  // pridelenie do spravneho zoznamu
                if (top->head256 == NULL) { // ak je zoznam prazdny
                    top->head256 = block;
                    prev256 = block;
                } else
                    prev256->next_explicit = block;
            }
            else if (block->size > 64) {
                if (top->head64 == NULL) {
                    top->head64 = block;
                    prev64 = block;
                } else
                    prev64->next_explicit = block;
            }
            else if (block->size > 8) {
                if (top->head8 == NULL) {
                    top->head8 = block;
                    prev8 = block;
                } else
                    prev8->next_explicit = block;
            }
            else{
                if (top->head == NULL) {
                    top->head = block;
                    prev = block;
                } else
                    prev->next_explicit = block;
            }
        }
        block = block->next_implicit;
    }
}

void connect(){
    struct block_head *block;
    block = (struct block_head *) first;

    while((block!=NULL) && (block->next_implicit)!=NULL){
        if((block->size > 0) && (block->next_implicit->size > 0)){ //ak su dva za sebou iduce bloky volne
            if ((block->next_implicit->next_implicit)!=NULL){
                if (block->next_implicit->next_implicit->size > 0){ //a ak je aj trety nasledujuci blok volny, tak ich vsetky tri spoji
                    block->size += block->next_implicit->size + sizeof(struct block_head) + block->next_implicit->next_implicit->size + sizeof(struct block_head);
                    block->next_implicit = block->next_implicit->next_implicit->next_implicit;
                    block->next_explicit = NULL;
                    connect2();
                    return;
                }
            }
            block->size += block->next_implicit->size + sizeof(struct block_head); // spojenie dvoch blokou
            block->next_implicit = block->next_implicit->next_implicit;
            block->next_explicit = NULL;
            connect2();
            return;

        }
        block=block->next_implicit;
    }
}

int memory_free(void *ptr){

    struct block_head *block = ptr;
    --block; //ziskanie bloku na ktory ukazuje pointer
    if (block == NULL)
        return 0;

    if (block->size < 0){ //kontrola ci je blok prazdny
        block->size = -(block->size);
        connect();
        return 1;
    }
    else
        return 0;
}

int memory_check(void *ptr) {

    struct block_head *block = ptr;
    --block; //ziskanie bloku na ktory ukazuje pointer
    if (block == NULL)
        return 0;
    if (block->size < 0) // overenie ci je blok volny
        return 1;
    if (block->size > 0)
        return 0;

}

void test_random_block(char *region, char **pointer, int block_min, int block_max, int memory) {
    int  i = 0, j, allocated = 0, possible_to_alloc = 0, allocated_count = 0, possible_to_alloc_count = 0, random;

    memset(region, 0, 100000); // vycistenie celej pamate
    memory_init(region, memory);

    while (allocated <= memory - block_min) {  // alokovanie blokov nahodnej velkosti do pamate dokym sa zmestia
        random = (rand() % (block_max - block_min + 1)) + block_min;
        if (allocated + random > memory)
            continue;
        allocated += random;
        allocated_count++;
        pointer[i] = memory_alloc(random);
        if (pointer[i]) {
            i+=1;
            possible_to_alloc_count++;
            possible_to_alloc += random;
        }
    }
    for (j = 0; j < i; j++) {   // uvolnenie vsetkych poinerov
        if (memory_check(pointer[j])) {
            memory_free(pointer[j]);
        }
        else {
            printf("Wrong memory check.\n");
        }

    }
    printf("Block of random size in memory size of %d bytes: allocated %.2f%% blocks (%.2f%% bytes).\n",memory, ((double)possible_to_alloc_count / allocated_count) * 100, ((double)possible_to_alloc / allocated) * 100);
}

void test_constant_block(char *region, char **pointer, int block, int memory) {
    int  i = 0, j, allocated = 0, possible_to_alloc = 0, allocated_count = 0, possible_to_alloc_count = 0;

    memset(region, 0, 100000);  // vycistenie celej pamate
    memory_init(region, memory);

    while (allocated <= memory - block) { // alokovanie blokov konstantnej velkosti do pamate dokym sa zmestia
        if (allocated + block > memory)
            continue;
        allocated += block;
        allocated_count++;
        pointer[i] = memory_alloc(block);
        if (pointer[i]) {
            i+=1;
            possible_to_alloc_count++;
            possible_to_alloc += block;
        }
    }
    for (j = 0; j < i; j++) { // uvolnenie vsetkych poinerov
        if (memory_check(pointer[j])) {
            memory_free(pointer[j]);
        }
        else {
            printf("Wrong memory check.\n");
        }
    }
    if (allocated_count > 0)
        printf("Block of size %d in memory size of %d bytes: allocated %.2f%% blocks (%.2f%% bytes).\n",block, memory, ((double)possible_to_alloc_count / allocated_count) * 100, ((double)possible_to_alloc / allocated) * 100);
    else
        printf("Block of size %d cannot fit in memory size of %d.",block, memory);
}
int main() {
    char region[100000];
    char* pointer[100000];

    printf("Scenario 1\n"); // testovanie
    test_constant_block(region, pointer, 8, 50);
    test_constant_block(region, pointer, 8, 100);
    test_constant_block(region, pointer, 8, 200);
    printf("\n");
    test_constant_block(region, pointer, 15, 50);
    test_constant_block(region, pointer, 15, 100);
    test_constant_block(region, pointer, 15, 200);
    printf("\n");
    test_constant_block(region, pointer, 24, 50);
    test_constant_block(region, pointer, 24, 100);
    test_constant_block(region, pointer, 24, 200);
    printf("\n");

    printf("Scenario 2\n");
    test_random_block(region, pointer, 8, 24, 50);
    test_random_block(region, pointer, 8, 24, 100);
    test_random_block(region, pointer, 8, 24, 200);
    printf("\n");

    printf("Scenario 3\n");
    for (int i = 0; i < 10; i++)
        test_random_block(region, pointer, 500, 5000, 10000);
    printf("\n");
    printf("Scenario 4\n");
    for (int i = 0; i < 10; i++)
        test_random_block(region, pointer, 8, 50000, 100000);
    printf("\n");
    printf("Scenario 5\n");
    test_constant_block(region, pointer, 1000000, 1000);
    return 0;
}


