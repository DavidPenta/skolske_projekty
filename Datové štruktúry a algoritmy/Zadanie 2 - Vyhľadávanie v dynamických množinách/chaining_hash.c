//https://www.log2base2.com/algorithms/searching/open-hashing.html
#include<stdio.h>
#include<stdlib.h>
#include <string.h>
#include <time.h>

#define size 999983

struct node
{
    char data[15];
    struct node *next;
};

struct node *chain[size];

void init()
{
    int i;
    for(i = 0; i < size; i++)
        chain[i] = NULL;
}

unsigned long hash(char *str)
{
    unsigned long hash = 69109;
    int c;

    while (c = *str++)
        hash = ((hash << 5) + hash) + c;
    return hash;
}

int insert(char* str)
{
    int number_of_collisions = 0;
    //create a newnode with value
    struct node *newNode = malloc(sizeof(struct node));
    strcpy(newNode->data, str);
    newNode->next = NULL;

    //calculate hash key
    int key = hash(str) % size;

    //check if chain[key] is empty
    if(chain[key] == NULL)
        chain[key] = newNode;
    //collision
    else
    {
        //add the node at the end of chain[key].
        struct node *temp = chain[key];
        while(temp->next)
        {
            number_of_collisions++;
            temp = temp->next;
        }

        temp->next = newNode;
    }
    return number_of_collisions;
}

int search(char* str){

    int key = hash(str) % size;
    if(chain[key] == NULL)
        return 0;

    struct node *Node = chain[key];

    while (Node != NULL)
        if (strcmp(Node->data, str) == 0)
            return 1;
        else
            Node = Node->next;

}

void print()
{
    int i;

    for(i = 0; i < size; i++)
    {
        struct node *temp = chain[i];
        printf("chain[%d]-->",i);
        while(temp)
        {
            printf("%s -->",temp->data);
            temp = temp->next;
        }
        printf("NULL\n");
    }
}

void test(FILE* filePointer, unsigned long m){

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

    printf("Hashing %lu words into table of size %d: %lf%%\n",m,size,  (double) (search_count / m)*100 );
    printf("Number of collisions: %ld.\n", number_of_collisions);
    printf("Time elpased is %f seconds.\n", time_spent);
    printf("\n");
    rewind(filePointer);

}


int main()
{
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