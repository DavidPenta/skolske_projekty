/*
* David_Penta_Projekt_2.c --
* projekt_2: Praca so spojitim zoznamom
* autor: David Penta
* cviciaca: Anna Považanová
* datum: 1.12.2020
* INFO
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
// alokacia pamate pre jednotlive polozky struktury
typedef struct zviera {
    char meno[52];
    char druh[32];
    int vyska;
    double vaha;
    int narodenie;
    int krmenie;
    char osetrovatel[52];
    struct zviera *dalsi; // ukazovatel na dalsiu strukturu
} ZVIERA;

void pridaj(ZVIERA** p_p_zaciatok, char *p1, char *p2, int p3, double p4, int p5, int p6, char *p7) {
    ZVIERA *novy_prvok = (ZVIERA*) malloc(sizeof(ZVIERA));
    ZVIERA *posledny_prvok = *p_p_zaciatok;
    // pridanie premenich daneho zvierata do spajaneho zoznamu
    strcpy(novy_prvok->meno, p1);
    strcpy(novy_prvok->druh, p2);
    novy_prvok->vyska = p3;
    novy_prvok->vaha = p4;
    novy_prvok->narodenie = p5;
    novy_prvok->krmenie = p6;
    strcpy(novy_prvok->osetrovatel, p7);
    novy_prvok->dalsi = NULL;
    if (*p_p_zaciatok == NULL) {
        *p_p_zaciatok = novy_prvok;
        return;
    }
    while (posledny_prvok->dalsi != NULL)
        posledny_prvok = posledny_prvok->dalsi;
    posledny_prvok->dalsi = novy_prvok;
}

ZVIERA *n(void) {
    char c, string[52], p1[52],p2[32],p7[52];
    int i, j=0, poc_riadkov = 0,p3,p5,p6,n = 0;
    double p4;
    FILE *fr;
    ZVIERA *p_zaciatok = NULL;
    // otvorenie suboru zvierata.txt
    fr = fopen("zvierata.txt", "r");
    if (fr == NULL){
        printf("Zaznamy neboli nacitane\n");
        exit(0);
    }
    // zistenie poctu riadkov vsubore zvierata.txt
    for (c = getc(fr); c != EOF; c = getc(fr))
        if (c == '\n')
            poc_riadkov++;
    fseek(fr, 0, SEEK_SET);

    // ulozenie udajov zo suboru po jednom zvierati do premenich
    for (i = 0; i <= poc_riadkov; i++) {
        fgets(string, 52, fr);
        strtok(string, "\n");

        if (j == 1)
            strcpy(p1,string);
        if (j == 2)
            strcpy(p2,string);
        if (j == 3)
            p3 = atoi(string);
        if (j == 4)
            p4 = strtod(string,NULL);
        if (j == 5)
            p5 = atoi(string);
        if (j == 6)
            p6 = atoi(string);
        if (j == 7)
            strcpy(p7,string);
        j++;
        if (j == 8){
            j = 0;
            // zavolanie funkcie ktora prida zviera do spajaneho suboru
            pridaj(&p_zaciatok, p1, p2, p3, p4, p5, p6, p7);
            n++;
        }
    }
    printf("Nacitalo sa %d zaznamov\n",n);
    return p_zaciatok;
}

void v(ZVIERA *n){
    int i = 1;
    while (n != NULL) {
        printf("%d.\n",i);
        i++;
        printf("Meno: %s\n", n->meno);
        printf("Druh: %s\n", n->druh);
        printf("Vyska: %d\n", n->vyska);
        printf("Vaha: %.15g\n", n->vaha);
        printf("Datum narodenia: %d\n", n->narodenie);
        printf("Datum krmenia: %d\n", n->krmenie);
        printf("Meno osetrovatela: %s\n", n->osetrovatel);
        n = n->dalsi;
    }
}

void p(ZVIERA** p_p_zaciatok){
    char p1[52],p2[32],p7[52], str[52];
    int i,c,p3,p5,p6,g=1;
    double p4;
    ZVIERA *p = NULL;
    ZVIERA (*kopia) = (*p_p_zaciatok);
    ZVIERA * novy_prvok = (ZVIERA*) malloc(sizeof(ZVIERA));
    scanf("%d",&c);
    // nacitanie udajov noveho zvierata
    scanf("\n");
    fgets(p1, 52, stdin);
    strtok(p1, "\n");
    scanf("\n");
    fgets(p2, 32, stdin);
    strtok(p2, "\n");
    scanf("\n");
    fgets(str, 52, stdin);
    p3 = atoi(str);
    scanf("\n");
    fgets(str, 52, stdin);
    p4 = strtod(str,NULL);
    scanf("\n");
    fgets(str, 52, stdin);
    p5 = atoi(str);
    scanf("\n");
    fgets(str, 52, stdin);
    p6= atoi(str);
    scanf("\n");
    fgets(p7, 52, stdin);
    strtok(p7, "\n");
    // pridanie premenich daneho zvierata do noveho prvku
    strcpy(novy_prvok->meno, p1);
    strcpy(novy_prvok->druh, p2);
    novy_prvok->vyska = p3;
    novy_prvok->vaha = p4;
    novy_prvok->narodenie = p5;
    novy_prvok->krmenie = p6;
    strcpy(novy_prvok->osetrovatel, p7);

    // pridanie noveho prvku na spavne miesto v spajanom zozname
    for (i = 1; i < c ; i++){
        p = (*p_p_zaciatok);
        if ((*p_p_zaciatok)->dalsi != NULL){
            (*p_p_zaciatok) = (*p_p_zaciatok)->dalsi;
            g++;
        }
    }

    if (g + 1 <= c)
        novy_prvok->dalsi = NULL;
    else
        novy_prvok->dalsi = (*p_p_zaciatok);

    if (p != NULL){
        p->dalsi = novy_prvok;
        (*p_p_zaciatok) = kopia;
    }
    else
        (*p_p_zaciatok) = novy_prvok;
}

void z(ZVIERA** p_p_zaciatok){
    int i = 0;
    char meno1[52], meno2[52], c;
    ZVIERA (*p) = NULL;
    ZVIERA (*k) = (*p_p_zaciatok);
    ZVIERA (*f) = NULL;

    getchar();
    while ((c=getchar()) != '\n' && i < 9)
        meno1[i++] = c;
    meno1[i] = '\0';

    for (i = 0; meno1[i];i++) {
        meno1[i] = tolower(meno1[i]); // zmeni vsteky pismena na male
    }

    while (k != NULL){
        for (i = 0; k->meno[i] ;i++)
            meno2[i] = tolower(k->meno[i]); // zmeni vsteky pismena na male
        meno2[i] = '\0';

        if (strcmp(meno2,meno1) == 0){
            if (p == NULL){
                p = k;
                (*p_p_zaciatok) = k->dalsi;
                k = k ->dalsi;
                free(p);
                p = NULL;
            }
            else{
                p->dalsi = k->dalsi;
                f = p;
                p = k;
                k = k ->dalsi;
                free(p);
                p = f;

            }
            printf("Zviera s menom %s bolo vymazane.\n",meno1);

        }
        else{
            p = k;
            k = k ->dalsi;
        }
    }

}

void dealokuj(ZVIERA *p_zaciatok)
{
    // dealokovanie pamate
    ZVIERA *p_akt;
    while (p_zaciatok != NULL)
    {
        p_akt = p_zaciatok;
        p_zaciatok = p_zaciatok->dalsi;
        free(p_akt);
    }
}

void a(ZVIERA** p_p_zaciatok){
    char vstup1[52], vstup2[10] , meno[52];
    int i, datum, g = 0;
    ZVIERA (*k) = (*p_p_zaciatok);

    scanf("\n");
    fgets(vstup1, 52, stdin);
    strtok(vstup1, "\n");
    scanf("\n");
    fgets(vstup2, 10, stdin);
    datum = atoi(vstup2);

    for (i = 0; vstup1[i];i++) {
        vstup1[i] = tolower(vstup1[i]); // zmeni vsteky pismena na male
    }

    while (k != NULL){
        for (i = 0; k->meno[i] ;i++)
            meno[i] = tolower(k->meno[i]); // zmeni vsteky pismena na male
        meno[i] = '\0';

        if (strcmp(vstup1,meno) == 0){
            g = 1;
            k->krmenie = datum; // zmena udajov
        }
        k = k->dalsi;
    }
    if (g == 1)
        printf("Zviera s menom %s bolo naposledy nakrmene dna %d\n",vstup1,datum);

}

void h(ZVIERA *k){
    char vstup[10] ;
    int i = 1, datum, datum_z_k, den1 ,den2, mes1, mes2, rok1, rok2, g = 0;
    scanf("\n");
    fgets(vstup, 10, stdin);
    datum = atoi(vstup);

    while (k != NULL){
        // zistenia dna, mesiaca a roku
        den1 = datum % 100;
        mes1 = (datum / 100) % 100;
        rok1 = (datum / 10000) % 10000;

        datum_z_k = k->krmenie;

        den2 = datum_z_k % 100;
        mes2 = (datum_z_k / 100) % 100;
        rok2 = (datum_z_k / 10000) % 10000;
        // zistenie ci je datum krmenia mensi
        if (rok2 < rok1)
            g = 1;
        if (rok2 == rok1){
            if (mes2 < mes1)
                g = 1;
            if (mes2 == mes1)
                if (den2 < den1)
                    g = 1;

        }
        if (g == 1) {
            printf("%d.\n",i);
            i++;
            printf("Meno: %s\n", k->meno);
            printf("Druh: %s\n", k->druh);
            printf("Vyska: %d\n", k->vyska);
            printf("Vaha: %lf\n", k->vaha);
            printf("Datum narodenia: %d\n", k->narodenie);
            printf("Datum krmenia: %d\n", k->krmenie);
            printf("Meno osetrovatela: %s\n", k->osetrovatel);
        }
        g = 0;
        k = k->dalsi;
    }

}
int main(void) {
    ZVIERA *p_zaciatok = NULL;
    char s = 'q';
    int aa = 0;
    while (s != 'k') {
        scanf("%c", &s);
        if (s == 'n') {
            if (aa == 0)
                aa = 1;
            else
                dealokuj(p_zaciatok);
            p_zaciatok = n();
        }
        if (aa == 1){  // ak uz bola zavolana funkcia n()
            if (s == 'v')
                v(p_zaciatok);
            if (s == 'p')
                p(&p_zaciatok);
            if (s == 'z')
                z(&p_zaciatok);
            if (s == 'a')
                a(&p_zaciatok);
            if (s == 'h')
                h(p_zaciatok);
        }
    }
    if (aa == 1)
        dealokuj(p_zaciatok);
    return 0;
}
