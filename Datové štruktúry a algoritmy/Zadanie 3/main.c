#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

typedef struct { // premenna
    char name; // nazov pemennej
    char val;  // hodnota premennej
} var;

typedef struct bdd{   // uzol stromu
    char content[20000];
    int height;
    struct bdd* parent;
    struct bdd* left;
    struct bdd* right;
} BDD;

struct bdd_free{ // prvok spajaneho zoznammu pomocou ktoreho sa odstrni zredukovany strom
    struct bdd* this;
    struct bdd_free* next;
};

typedef struct bf{
    char content[20000];
} BF;

var vars[20];  // zoznam premennych
char s[100000];

char str1[20000];
char str2[20000];

char sTest[100000];
char varsTest[20] = "00000000000000000000";
int testCount = 0;


char* changeVarsToBool(char* expr, int vars_len){ // vymeni premenne vo funkcii za ich aktualne hodnoty, napr. ab+c!d --> 00+01
    char str[100000];
    int i;

    for(i = 0; i < strlen(expr); i++) {
        if (expr[i] == '+')
            str[i] = '+';
        if (expr[i] == '*')
            str[i] = '*';
        if (expr[i] == '!')
            str[i] = '!';
        else {
            for (int j = 0; j < vars_len; ++j) {
                if (vars[j].name == expr[i])
                    str[i] = vars[j].val;
            }
        }
    }
    str[strlen(expr)] = '\0';
    return str;
}

char getFunctionValue(char* str){ // ziska vyslednu hodnotu funkcie,  napr. 00+01 --> 0
    char s[100000];
    char c = str[0];
    char c1 = 'x';
    char v = 'x';
    int i = 0, j = 0;

    while (c != '\0'){
        if (c == '+'){
            s[i] = c1;
            i++;
            s[i] = c;
            i++;
            c1 = 'x';
        }
        if (c == '!'){
            c = str[++j];
            if(c == '0' && c1 != '0')
                c1 = '1';
            if(c == '1')
                c1 = '0';
        }
        else{
            if(c == '0')
                c1 = '0';
            if(c == '1' && c1 != '0')
                c1 = '1';
        }
        c = str[++j];
    }
    s[i] = c1;
    i++;
    s[i] = '\0';
    j = 0;
    c = s[0];
    while (c != '\0'){
        if (c != '+'){
            if(c == '1')
                v = '1';
            if(c == '0' && v != '1')
                v = '0';
        }
        c = s[++j];
    }
    return v;
}

void set_vars(int pos,char* expr, int vars_len) {// na vytvorenie vektora, ziska vstky kombinacie premennych
    char str[20000];
    char c;
    if (pos == vars_len){
        strcpy(str, changeVarsToBool(expr, vars_len));
        c = getFunctionValue(str);
        strncat(s, &c, 1);
    }
    else if (pos < vars_len){
        vars[pos].val = '0';
        set_vars(pos + 1, expr,vars_len);
        vars[pos].val = '1';
        set_vars(pos + 1, expr,vars_len);
    }

    else{
        printf("Chyba\n");
        exit(1);}
}

void init(char* expr) { // ziska mena premennych bez toho aby sa opakovali
    int i, t = 0;
    char e;

    int vars_len = 0;

    for (i = 0; i < strlen(expr); ++i) {
        e = expr[i];
        if (e != '+' && e != '*' && e != '!' && e != '\n') {
            for (int j = 0; j < vars_len; j++){ // kontrola opakovania
                if (e == vars[j].name){
                    t = 1;
                    break;
                }
            }
            if (t == 0){
                vars[vars_len].name = e;
                vars[vars_len].val = '0';
                vars_len++;
            }
            else
                t = 0;
        }
    }
    set_vars(0, expr, vars_len);
}



void divide(BDD* node){ // z vektoru vytvori binarny strom
    int i,k;
    if(strlen(node->content) > 1){ // ak to nema byt list, tak ho rozdeli
        BDD* node1 = (BDD *) malloc(sizeof(BDD));
        BDD* node2 = (BDD *) malloc(sizeof(BDD));
        if (node1 == NULL || node2 == NULL){
            printf("Malloc failed\n");
            exit(1);
        }

        for(i = 0; i < strlen(node->content)/2; i++) {
            str1[i]= node->content[i];
        }
        str1[i] = '\0';


        for(i = strlen(node->content)/2, k = 0; i <= strlen(node->content); i++, k++) {
            str2[k]= node->content[i];
        }
        str2[k] = '\0';
        strcpy(node1->content,str1);
        node1->parent = (struct bdd*) node;
        node1->height = node->height+1;
        node1->right = NULL;
        node1->left = NULL;

        strcpy(node2->content,str2);
        node2->parent = (struct bdd*) node;
        node2->height = node->height+1;
        node2->right = NULL;
        node2->left = NULL;

        node->left = (struct bdd*) node1;
        node->right =  (struct bdd*) node2;

        str1[0] = '\0';
        str2[0] = '\0';

        divide(node1);
        divide(node2);

    }
}

BDD *BDD_create(BF bfunkcia){ // vytvori binarny strom
    char* b = bfunkcia.content;
    BDD* node = (BDD *) malloc(sizeof(BDD));
    char str[100000];
    if (b[0] != '0' && b[0] != '1'){ // ak je zadana funkcia a nie vektor
        init(b);
        strcpy(str, s);
        strcpy(sTest, s);
        s[0] = '\0';
    }
    else
        strcpy(str, b);

    // vytvorenie korena
    strcpy(node->content, str);
    node->height = 0;
    node->parent = NULL;
    node->right = NULL;
    node->left = NULL;
    divide(node);

    return node;
}

char BDD_use(BDD *bdd, char *vstupy){ //zisti vystup pre kombinaciu premennych
    BDD* node = bdd;
    char c;

    for(int i = 0; i < strlen(vstupy); i++) {
        c = vstupy[i];
        if(c == '0'){
            if (node->left == NULL){
                printf("\nToo many variables\n",vstupy);
                return '-';
            }
            else
                node = node->left;
        }
        if(c == '1'){
            if (node->right == NULL){
                printf("\nToo many variables\n",vstupy);
                return '-';
            }
            else
                node = node->right;
        }
    }

    if (strlen(node->content) == 1){
        if (node->content[0] == '0')
            return '0';
        if (node->content[0] == '1')
            return '1';
    }
    else{
        printf("\nNeed more variables\n",vstupy);
        return '-';
    }

}
/*
void BDD_print(BDD* root){ // vypise cely strom
    if(root != NULL){
        printf("%s ", root->content);
        BDD_print(root->left);
        BDD_print(root->right);
    }
}
*/

int BDD_free(BDD* bddo){ // uvolnenie casti binarneho stromu
    int a = 0;

    if(bddo != NULL){
        a += BDD_free(bddo->left);
        a += BDD_free(bddo->right);
        free(bddo);
        a++;
    }
    return a;
}


int BDD_free2_1(BDD* bddo, struct bdd_free *root){// pridavanie uzlov do spajaneho zoznamu bez opakovania
    int a = 0;
    char b = 0;
    if(bddo != NULL){
        a += BDD_free2_1(bddo->left,root);
        a += BDD_free2_1(bddo->right,root);
        while (b == 0){
            if(bddo == root->this)
                b = 1;
            else{
                if(root->next != NULL)
                    root = root->next;
                else{
                    struct bdd_free* r = (struct bdd_free *) malloc(sizeof(struct bdd_free));
                    r->this = bddo;
                    r->next = NULL;
                    root->next = r;
                    b = 1;
                }
            }
        }
    }
    return a;
}

int BDD_free2(BDD* bddo){  //uvlonenie celeho zredukovaneho stromu pred vytvorenim noveho
    struct bdd_free* root = (struct bdd_free *) malloc(sizeof(struct bdd_free));
    struct bdd_free* next;
    root->this = bddo;
    root->next = NULL;
    BDD_free2_1(bddo,root);
    while(root->next != NULL){ // vymazanie celeho spajaneho zoznamu
        free(root->this);
        root = root->next;

    }
    free(root->this);

    while(root != NULL){
        next = root->next;
        free(root);
        root = next;
    }
}

int search(BDD* bddo, BDD* b){ // porovnavanie uzla s ostatnymi do rovnakej hlbky  (2**(n+1))
    int a = 0;
    if(bddo != NULL){
        if (bddo->height == b->height) {
            if (bddo != b && strcmp(b->content, bddo->content) == 0) {
                BDD *parent = bddo->parent;
                if (bddo == parent->left)
                    parent->left = b;
                else if (bddo == parent->right)
                    parent->right = b;
                a += BDD_free(bddo);
            }
        }

        else if (bddo->height < b->height){
            a += search(bddo->left,b);
            a += search(bddo->right,b);
        }
    }
    return a;
}

int getLevel(BDD* root, BDD* b, int level) // postupne podla hlbky uzlov- od najmensej
{

    int a = 0;
    if (b == NULL){ // 2**(n+1)
        return a;}
    if (level == 0){ // 2**(n+1) - 1
        a += search(root,b);}
    else if (level > 0){ // 2**(n+2)
        a += getLevel(root, b->left, level - 1);
        a += getLevel(root, b->right, level - 1);
    }
    return a;
}

int level(BDD* root, int height) // postupne podla hlbky uzlov- od najmensej
{
    int a = 0;
    for (int i = 0; i <= height; i++)
        a += getLevel(root, root, i);
    return a;
}

int BDD_reduce(BDD *bdd){
    BDD* root = bdd;
    BDD* t = bdd;
    int i = 0;
    while (t != NULL){ // ziskanie hlbky stromu
        t = t->left;
        i++;
    }

    return level(root,i);
}



void testVars(BDD *bdd, int pos, int vars_len) {// testovanie vsetkych moznosti hodnot premennych
    if (pos == vars_len){
        varsTest[vars_len] = '\0';
        if (BDD_use(bdd,varsTest) != sTest[testCount])
            printf("Error.\n");
        testCount++;

    }
    else if (pos < vars_len){
        varsTest[pos] = '0';
        testVars(bdd,pos + 1, vars_len);
        varsTest[pos] = '1';
        testVars(bdd,pos + 1, vars_len);
    }
    else
        exit(1);

}


void randomize(char *list, int n){ // poprehadzovanie prvkov pola
    if (n > 1)
    {
        for (int i = 0; i < n - 1; i++)
        {
            int j = i + rand() / (RAND_MAX / (n - i) + 1);
            char c = list[j];
            list[j] = list[i];
            list[i] = c;
        }
    }
}

char* testCreateRandomFunction(int i) { // vytvorenie nahodnej booleovskej funkcie
    char str[1000] = "";
    int size = i;
    char c;
    char char1[14] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n'};
    randomize(char1,size);
    randomize(char1,size);
    randomize(char1,size);
    int r;

    r = rand() % 2;
    if(r == 0){
        c ='!';
        strncat(str, &c, 1);
    }
    r = rand() % i;
    if(r == 0){
        c ='a';
        strncat(str, &c, 1);
    }
    if(r == 1){
        c ='b';
        strncat(str, &c, 1);
    }
    if(r == 2){
        c ='c';
        strncat(str, &c, 1);
    }
    if(r == 3){
        c ='d';
        strncat(str, &c, 1);
    }
    if(r == 4){
        c ='e';
        strncat(str, &c, 1);
    }
    if(r == 5){
        c ='f';
        strncat(str, &c, 1);
    }
    if(r == 6){
        c ='g';
        strncat(str, &c, 1);
    }
    if(r == 7){
        c ='h';
        strncat(str, &c, 1);
    }
    if(r == 8){
        c ='i';
        strncat(str, &c, 1);
    }
    if(r == 9){
        c ='j';
        strncat(str, &c, 1);
    }
    if(r == 10){
        c ='k';
        strncat(str, &c, 1);
    }
    if(r == 11){
        c ='l';
        strncat(str, &c, 1);
    }
    if(r == 12){
        c ='m';
        strncat(str, &c, 1);
    }
    if(r == 13){
        c ='n';
        strncat(str, &c, 1);
    }

    while (size > 0){
        r = rand() % 6;
        if(r == 0){
            c ='+';
            strncat(str, &c, 1);
        }
        r = rand() % 2;
        if(r == 0){
            c ='!';
            strncat(str, &c, 1);
        }
        randomize(char1,size);
        randomize(char1,size);
        randomize(char1,size);

        c = char1[size-1];
        strncat(str, &c, 1);

        r = rand() % 2;
        if(r == 0) {
            char1[size-1] = NULL;
            size--;
        }
    }
    c ='\0';
    strncat(str, &c, 1);
    //printf("%s\n",str);
    return str;

}

char* testCreateRandomVector(int size) { // vytvorenie nahodneho vektora
    char str[100000] = "";
    char c;
    int r;
    int k = 1;
    for(int i = 0; i < size; i++){
        k *= 2;
    }

    for(int i = 0; i < k; i++){
        r = rand() % 2;
        if(r == 0)
            c ='0';

        if(r == 1)
            c ='1';
        strncat(str, &c, 1);

    }
    c ='\0';
    strncat(str, &c, 1);
    strcpy(s,str);
    //printf("%s\n",str);
    return str;

}
int o = 0; // scitavanie uzlov stromu
void BDD_count(BDD* root){
    if(root != NULL){
        o++;
        BDD_count(root->left);
        BDD_count(root->right);
    }
}

unsigned long long int testNotReducedCount = 0;
unsigned long long int testReducedCount = 0;

unsigned long long int testNotReducedCountTotal = 0;
unsigned long long int testReducedCountTotal = 0;

int main(){
    BDD* bdd = NULL;
    BF bf;
    strcpy(bf.content, testCreateRandomFunction(10));
    bdd = BDD_create(bf);
    BDD_reduce(bdd);

    long double percentage;
    clock_t begin = clock();
    clock_t beginTotal = clock();
    for(int i = 0; i < 500; i++){
        strcpy(bf.content, testCreateRandomFunction(13));
        bdd = BDD_create(bf);
        BDD_count(bdd);
        testNotReducedCount += o;
        o = 0;
        testReducedCount += BDD_reduce(bdd);
        testCount = 0;
        strcpy(varsTest,"0000000000000000000");
        testVars(bdd,0, 13);
        BDD_free2(bdd);
        s[0] = '\0';
        sTest[0] = '\0';

    }
    percentage = (long double) testReducedCount / testNotReducedCount * 100.0;
    printf("%lld %lld %lf%%\n",testNotReducedCount, testReducedCount, percentage );
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Time: %lf seconds\n", time_spent);
    begin = clock();

    testNotReducedCountTotal += testNotReducedCount;
    testReducedCountTotal += testReducedCount;
    testNotReducedCount = 0;
    testReducedCount = 0;

    for(int i = 0; i < 500; i++){
        strcpy(bf.content, testCreateRandomFunction(14));
        bdd = BDD_create(bf);
        BDD_count(bdd);
        testNotReducedCount += o;
        o = 0;
        testReducedCount += BDD_reduce(bdd);
        testCount = 0;
        strcpy(varsTest,"00000000000000000000");
        testVars(bdd,0, 14);
        BDD_free2(bdd);
        s[0] = '\0';
        sTest[0] = '\0';
    }
    percentage = (long double) testReducedCount / testNotReducedCount * 100.0;
    printf("%lld %lld %lf%%\n",testNotReducedCount, testReducedCount, percentage );
    end = clock();
    time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Time: %lf seconds\n", time_spent);
    begin = clock();
    testNotReducedCountTotal += testNotReducedCount;
    testReducedCountTotal += testReducedCount;
    testNotReducedCount = 0;
    testReducedCount = 0;

    for(int i = 0; i < 500; i++){
        int size = 13;
        strcpy(bf.content, testCreateRandomVector(size));
        bdd = BDD_create(bf);
        BDD_count(bdd);
        testNotReducedCount += o;
        o = 0;
        testReducedCount += BDD_reduce(bdd);
        testCount = 0;
        strcpy(sTest,bf.content);
        char c = '0';
        char sVars[20] = "";
        for(int j = 0; j < size; j++){
            strncat(sVars, &c, 1);
        }
        strcpy(varsTest,sVars);
        testVars(bdd,0, size);
        BDD_free2(bdd);
        s[0] = '\0';
        sTest[0] = '\0';
    }
    percentage = (long double) testReducedCount / testNotReducedCount * 100.0;
    printf("%lld %lld %lf%%\n",testNotReducedCount, testReducedCount, percentage);
    end = clock();
    time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Time: %lf seconds\n", time_spent);
    begin = clock();

    testNotReducedCountTotal += testNotReducedCount;
    testReducedCountTotal += testReducedCount;
    testNotReducedCount = 0;
    testReducedCount = 0;

    for(int i = 0; i < 500; i++){
        int size = 14;
        strcpy(bf.content, testCreateRandomVector(size));
        bdd = BDD_create(bf);
        BDD_count(bdd);
        testNotReducedCount += o;
        o = 0;
        testReducedCount += BDD_reduce(bdd);
        testCount = 0;
        strcpy(sTest,bf.content);
        char c = '0';
        char sVars[20] = "";
        for(int j = 0; j < size; j++){
            strncat(sVars, &c, 1);
        }
        strcpy(varsTest,sVars);
        testVars(bdd,0, size);
        BDD_free2(bdd);
        s[0] = '\0';
        sTest[0] = '\0';

    }

    percentage = (long double) testReducedCount / testNotReducedCount * 100.0;
    printf("%lld %lld %lf%%\n",testNotReducedCount, testReducedCount, percentage);
    end = clock();
    time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    printf("Time: %lf seconds\n", time_spent);

    testNotReducedCountTotal += testNotReducedCount;
    testReducedCountTotal += testReducedCount;
    testNotReducedCount = 0;
    testReducedCount = 0;


    percentage = (long double) testReducedCountTotal / testNotReducedCountTotal * 100.0;
    printf("\nTotal: %lld %lld %lf%%\n",testNotReducedCountTotal, testReducedCountTotal, percentage );

    end = clock();
    time_spent = (double)(end - beginTotal) / CLOCKS_PER_SEC;
    printf("Total time: %lf seconds\n", time_spent);

    return 0;
}