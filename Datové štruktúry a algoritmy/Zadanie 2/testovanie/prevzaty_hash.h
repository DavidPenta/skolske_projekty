//https://www.log2base2.com/algorithms/searching/open-hashing.html
#include<stdio.h>
#include<stdlib.h>
#include <string.h>
#include <time.h>

#define size 999983

struct node4
{
    char data[15];
    struct node4 *next;
};

struct node4 *chain[size];

void init4()
{
    int i;
    for(i = 0; i < size; i++)
        chain[i] = NULL;
}

unsigned long hash4(char *str)
{
    int i, len = strlen(str);
    unsigned long hash = 0;
    for (i = 0; i < len; i++)
        hash = 53 * hash + str[i];
    return hash;
}

int insert4(char* str)
{
    int number_of_collisions = 0;
    //create a newnode with value
    struct node4 *newNode = malloc(sizeof(struct node));
    strcpy(newNode->data, str);
    newNode->next = NULL;

    //calculate hash key
    int key = hash4(str) % size;

    //check if chain[key] is empty
    if(chain[key] == NULL)
        chain[key] = newNode;
        //collision
    else
    {
        //add the node at the end of chain[key].
        struct node4 *temp = chain[key];
        while(temp->next)
        {
            number_of_collisions++;
            temp = temp->next;
        }

        temp->next = newNode;
    }
    return number_of_collisions;
}

int search4(char* str){

    int key = hash4(str) % size;
    if(chain[key] == NULL)
        return 0;

    struct node4 *Node = chain[key];

    while (Node != NULL)
        if (strcmp(Node->data, str) == 0)
            return 1;
        else
            Node = Node->next;

}
