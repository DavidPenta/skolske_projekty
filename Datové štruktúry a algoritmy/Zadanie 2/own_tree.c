#include<stdio.h>
#include<stdlib.h>
#include <time.h>

unsigned long rotations = 0;

struct Block{ // prvok binarneho stromu
    int height;
    unsigned long long value;
    struct Block *left, *right;
};

struct Block *rotation_to_right(struct Block *block1){
    int a = 0, b = 0, c = 0, d = 0;
    struct Block *block2 = block1->left, *block3 = block2->right;
    rotations++;

    block2->right = block1;
    block1->left = block3;

    if (block1->left != NULL)  //zistenie spravnej vysky
        a = block1->left->height;
    if (block1->right != NULL)
        b = block1->right->height;

    if (a > b)  // ulozenie spravnej vysky
        block1->height = a+1;
    else
        block1->height = b+1;

    if (block2->left != NULL)  //zistenie spravnej vysky
        c = block2->left->height;
    if (block2->right != NULL)
        d = block2->right->height;

    if (c > d)  // ulozenie spravnej vysky
        block2->height = c+1;
    else
        block2->height = d+1;

    return block2;
}

struct Block*rotation_to_left(struct Block *block1){
    int a = 0, b = 0, c = 0, d = 0;
    struct Block *block2 = block1->right, *block3 = block2->left;
    rotations++;

    block2->left = block1;
    block1->right = block3;

    if (block1->left != NULL)  //zistenie spravnej vysky
        a = block1->left->height;
    if (block1->right != NULL)
        b = block1->right->height;

    if (a > b)   // ulozenie spravnej vysky
        block1->height = a+1;
    else
        block1->height = b+1;

    if (block2->left != NULL)  //zistenie spravnej vysky
        c = block2->left->height;
    if (block2->right != NULL)
        d = block2->right->height;

    if (c > d)  // ulozenie spravnej vysky
        block2->height = c+1;
    else
        block2->height = d+1;

    return block2;
}

struct Block* insert(struct Block* Block, unsigned long long value) {
    int balance, a = 0, b = 0;

    if (Block == NULL){ // vlozenie noveho bloku do binarneho stromu
        Block = (struct Block*)malloc(sizeof(struct Block));
        Block->value   = value;
        Block->left   = NULL;
        Block->right  = NULL;
        Block->height = 1;
    }
    // najdenie spravneho miesta v strome
    if (value < Block->value)
        Block->left = insert(Block->left, value);

    else if (value > Block->value)
        Block->right = insert(Block->right, value);

    else // ak tam uz taky je, tak sa nic neudeje
        return Block;


    // upravenie vysky
    if (Block->left != NULL) //zistenie spravnej vysky
        a = Block->left->height;
    if (Block->right != NULL)
        b = Block->right->height;

    if (a > b)  // ulozenie spravnej vysky
        Block->height = a + 1;
    else
        Block->height = b + 1;


    // zistenie ci treba strom vybalancovat
    if (Block != NULL) {
        if (Block->left != NULL)
            a = Block->left->height;
        else
            a = 0;

        if (Block->right != NULL)
            b = Block->right->height;
        else
            b = 0;

        balance = a - b;
    }
    else
        balance = 0;

    // styri moznosti balancovania

    // Left Left Case
    if (balance > 1 && value < Block->left->value)
        return rotation_to_right(Block);

    // Left Right Case
    if (balance > 1 && value > Block->left->value){
        Block->left =  rotation_to_left(Block->left); // musi sa 2 krat rotovat
        return rotation_to_right(Block);
    }

    // Right Right Case
    if (balance < -1 && value > Block->right->value)
        return rotation_to_left(Block);

    // Right Left Case
    if (balance < -1 && value < Block->right->value){
        Block->right = rotation_to_right(Block->right);
        return rotation_to_left(Block);
    }

    return Block;
}

int search(struct Block *root, unsigned long long value){
    if(root != NULL){
        if (root->value == value)
            return 1;
        if (root->value > value)
            search(root->left, value);
        if (root->value < value)
            search(root->right, value);
        return 0;
    }
    else
        return 0;
}

void print(struct Block*root){
    if(root != NULL)
    {
        printf("%llu ", root->value);
        print(root->left);
        print(root->right);
    }
}

int main(){
    FILE* f;
    f = fopen("C:\\Users\\david\\Desktop\\random_numbers.txt", "r");
    struct Block* root = NULL;
    int m = 1000000;
    unsigned long long b;

    double time_spent = 0.0;
    clock_t begin = clock();
    for (int i = 0; i < m; i++){
        fscanf(f, "%15llu", &b);

        root = insert(root, b);;
    }
    rewind(f);

    for (int i = 0; i < m; i++){
        fscanf(f, "%15llu", &b);
        search(root, b);
    }
    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Total rotations: %lu.\n", rotations);
    printf("Time elpased: %f seconds.\n", time_spent);
    fclose(f);
    return 0;
}
