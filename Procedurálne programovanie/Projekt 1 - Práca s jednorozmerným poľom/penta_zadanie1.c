/*
* penta_zadanie1.c --
* Zadanie_1 Praca s jednozozmernym polom
* autor: David Penta
* cviciaca: Anna Považanová
* datum: 12.11.2020
* INFO
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

FILE *vypis(void){
    char c, *znak = "";
    int i, poc_riadkov = 0, j=0, den, mesiac;
    long long rod_c;
    double cislo, delitelne1;
// otvorenie suboru pacienti.txt
    FILE *fr;
    fr = fopen("pacienti.txt", "r");
    if (fr == NULL)
        printf("Neotvoreny subor");

//zistenie poctu riadkov
    for (c = getc(fr); c != EOF; c = getc(fr))
        if (c == '\n')
            poc_riadkov++;
    fseek(fr, 0, SEEK_SET);
// kontrola a vypis riadkov zo subora s ich nazvom
    for (i = 0; i <= poc_riadkov; i++) {
        // ziskanie riadku zo subora
        fgets(znak, 52, fr);
        if (j == 0)
            printf("meno priezvisko: %s",znak);
        if (j == 1){
            rod_c = strtoll(znak,NULL,10);
            if (rod_c % 11 == 0)
                printf("rodne cislo: %s",znak);
            else
                printf("zle rodne cislo\n");
        }
        if (j == 2) {
            if (strlen(znak) == 4 && ('A'<= znak[0] && znak[0] <= 'Z') && ('0' <= znak[1] && znak[1] <= '9') && ('0' <= znak[2] && znak[2] <= '9'))
                printf("diagnoza: %s", znak);
            else
                printf("zla diagnoza\n");
        }
        if (j == 3)
            printf("vysetrenie: %s",znak);
        if (j == 4){
            cislo = strtod(znak,NULL);
            // zistenie ci ma vysledok max 4 desatine miesta, a ci je z intervalu <0,1000>
            delitelne1 = cislo * 10000;
            if (cislo+1 >= 1 && cislo <= 1000 && ( fmod(delitelne1,1) == 0))
                printf("vysledok: %s",znak);
            else
                printf("zly vysledok\n");
        }
        if (j == 5){
            // overenie ci bol zadany datum mozny
            strtok(znak, "\n");
            if (strlen(znak) == 8){
                den = (int) znak[7] - '0';
                den += (int) (znak[6] - '0')*10;
                if (den <= 31 || den >0) {
                    mesiac = (int) znak[5] - '0';
                    mesiac += (int) (znak[4] - '0') * 10;
                    if (mesiac <= 12 || mesiac>0) {
                        printf("datum: %s", znak);
                        printf("\n");
                    } else
                        printf("zly datum-mesiac\n");
                }
                else
                    printf("zly datum-den\n");
            }
            else
                printf("zly datum\n");
        }
        if (j == 6)
            printf("\n");
        j++;
        if(j == 7)
            j = 0;

    }
    printf("\n");
    // vratenie otvoreneho suporu, aby ho mohli pouzit aj ine funkcie
    return fr;
}

void datum(FILE *fr){
    char c, *znak = "", znak2[4], **diagnoza1, dnes[9], z, *prvok = "";
    int i, poc_riadkov = 0, j=0, den, mesiac, den1, mesiac1, rok, rok1, a=0,k, p = 0, b=0, pocet,  maxpocet = 0, e,u;

    // zistenie postu riadkov v subore
    fseek(fr, 0, SEEK_SET);
    for (c = getc(fr); c != EOF; c = getc(fr))
        if (c == '\n')
            poc_riadkov++;
    fseek(fr, 0, SEEK_SET);
// nacitanie vstupu, ked som to nacital rovno ako string tak to neslo
    scanf("%d",&u);
    itoa(u, dnes, 10);
    if (strlen(dnes) != 8){
        printf("neplatny vstup\n");
        exit(0);}
// vypocitanie dna, mesiaca a roku
    den1 = (int) dnes[7] - '0';
    den1 += (int) (dnes[6] - '0') * 10;

    mesiac1 = (int) dnes[5] - '0';
    mesiac1 += (int) (dnes[4] - '0') * 10;

    rok1 = (int) dnes[3] - '0';
    rok1 += (int) (dnes[2] - '0') * 10;
    rok1 += (int) (dnes[1] - '0') * 100;
    rok1 += (int) (dnes[0] - '0') * 1000;
// kontrola platnosti vstupu
    if(den1 > 31 || den1 < 1 || mesiac1 > 12 || mesiac1 < 1){
        printf("neplatny vstup\n");
        exit(0);}


// zistenie ake velke pole sa ma alokovat
    for (i = 0; i <= poc_riadkov; i++) {
        fgets(znak, 50, fr);
        strtok(znak, "\n");

        if (j == 5){
            if (strlen(znak) == 8){

                den = (int) znak[7] - '0';
                den += (int) (znak[6] - '0')*10;

                mesiac = (int) znak[5] - '0';
                mesiac += (int) (znak[4] - '0') * 10;

                rok = (int) znak[3] - '0';
                rok += (int) (znak[2] - '0') * 10;
                rok += (int) (znak[1] - '0') * 100;
                rok += (int) (znak[0] - '0') * 1000;

                if (rok1 > rok)
                    b++;

                if (rok1 == rok) {
                    if (mesiac1 > mesiac)
                        b++;

                    if (mesiac1 == mesiac){
                        if (den1 > den) {
                            b++;
                        }
                    }
                }
            }
        }
        j++;
        if(j == 7)
            j = 0;

    }
    // alokovanie pola danej velkosti
    if (b > 0)
        diagnoza1 = (char *)  malloc(b * 4);
    j = 0;
    // zapisanie diagnoz vo vyhovujucom datume do pola diagnoza1
    fseek(fr, 0, SEEK_SET);
    for (i = 0; i <= poc_riadkov; i++) {
        fgets(znak, 50, fr);
        strtok(znak, "\n");

        if (j == 2) {
            if (strlen(znak) == 3 && ('A'<= znak[0] && znak[0] <= 'Z') && ('0' <= znak[1] && znak[1] <= '9') && ('0' <= znak[2] && znak[2] <= '9'))
                strcpy(znak2, znak);
            else
                printf("zla diagnoza\n");
        }
        if (j == 5){
            if (strlen(znak) == 8){
                den = (int) znak[7] - '0';
                den += (int) (znak[6] - '0')*10;

                mesiac = (int) znak[5] - '0';
                mesiac += (int) (znak[4] - '0') * 10;

                rok = (int) znak[3] - '0';
                rok += (int) (znak[2] - '0') * 10;
                rok += (int) (znak[1] - '0') * 100;
                rok += (int) (znak[0] - '0') * 1000;

                if (rok1 > rok){
                    diagnoza1[a] = (char *)  malloc(3);
                    strcpy(diagnoza1[a], znak2);
                    a++;
                }
                if (rok1 == rok) {
                    if (mesiac1 > mesiac) {
                        diagnoza1[a] = (char *)  malloc(3);
                        strcpy(diagnoza1[a], znak2);
                        a++;
                    }
                    if (mesiac1 == mesiac){
                        if (den1 > den) {
                            diagnoza1[a] = (char *)  malloc(3);
                            strcpy(diagnoza1[a], znak2);
                            a++;
                        }
                    }
                }
            }
        }
        j++;
        if(j == 7)
            j = 0;
    }
    j = 0;
// zistenie najpocetnejsej diagnozi v poli
    for (i = 0; i < a; i++){
        pocet = 0;
        for (j = i ; j < a; j++) {
            if (strcmp(diagnoza1[i], diagnoza1[j]) == 0)
                pocet++;
        }
        if (pocet > maxpocet)
        {
            maxpocet = pocet;
            prvok = diagnoza1[i];
        }
    }
    if (a > 0)
        printf("Najcastejsie vysetrovana diagnoza do %s je %s.\n",dnes,prvok);
// uvolnenie alokovanej pamate
    free(diagnoza1);
    diagnoza1 = NULL;

}

void polia(FILE *fr, char ***p1, char** p2, char ***p3, char ***p4, double **p5, char ***p6, int* pocet){
    char c, znak[52],**meno, **diagnoza, **vysetrenie, **datum, **rod_cislo;
    int poc_riadkov = 1, i, j = 0 ,a=0, poc;
    double *vysledok;

    // zistenie poctu ridkov v subore
    fseek(fr, 0, SEEK_SET);
    for (c = getc(fr); c != EOF; c = getc(fr))
        if (c == '\n')
            poc_riadkov++;
    fseek(fr, 0, SEEK_SET);

    // vypocet kolko ludi je v subore
    poc =  (poc_riadkov + 1)/7;
    *pocet = &poc;

    // alokacia dynamickych poli
    meno = (char *)  malloc(poc * 52);
    rod_cislo = (char *)  malloc(poc * 12);
    diagnoza = (char *)  malloc(poc * 5);
    vysetrenie = (char *)  malloc(poc * 52);
    vysledok = (double*)  malloc(poc * sizeof(double));
    datum = (char *)  malloc(poc * 12);
    // overenie ci sa podarilo alokovat polia
    if (meno == NULL || rod_cislo == NULL ||diagnoza ==  NULL || vysetrenie ==  NULL || vysledok ==  NULL || datum ==  NULL) {
        printf("Polia sa nepodarilo vytvorit");
        exit(0);
    }
    // nacitanie informacii do prislusnich dynamickych poli
    for (i = 0; i <= poc_riadkov; i++) {
        fgets(znak,52,fr);
        strtok(znak, "\n");
        if (j == 0){
            meno[a] = (char *)  malloc(52);
            strcpy(meno[a], znak);
        }
        if (j == 1){
            rod_cislo[a] = (char *)  malloc(12);
            strcpy(rod_cislo[a], znak);
        }
        if (j == 2){
            diagnoza[a] = (char *)  malloc(5);
            strcpy(diagnoza[a], znak);
        }
        if (j == 3){
            vysetrenie[a] = (char *)  malloc(52);
            strcpy(vysetrenie[a], znak);
        }
        if (j == 4){
            vysledok[a] = strtod(znak,NULL);
        }
        if (j == 5){
            datum[a] = (char *)  malloc(10);
            strcpy(datum[a], znak);
        }
        j++;
        if(j == 7){
            j = 0;
            a++;
        }
    }
    // nastavenie pointerov na dynamicke polia
    *p1 = (char **) meno;
    *p2 = (char**) rod_cislo;
    *p3 = (char **) diagnoza;
    *p4 = (char **) vysetrenie;
    *p5 = (double *) vysledok;
    *p6 = (char **) datum;
}

void cislo(char** p2, char** p4, double *p5, int pocet) {
    int i;
    long long rodne_cislo;
// nacitanie rodneho cisla
    scanf("%lld",&rodne_cislo);
    // overenie ci je platne
    if (rodne_cislo % 11 != 0){
        printf("neplatny vstup\n");
        exit(0);}

    printf("Vysledky vysetreni, ktore boli vykonane pacientovi s rodnym cislo %lld su nasledovne:\n",rodne_cislo);
// vypis vysetreni z pola, ktore maju zhodne rodne cislo
    for (i = 0; i < pocet; i++){
        if (rodne_cislo == atoll(*(p2+i)))
            printf("%s: %.9g\n",*(p4+i), *(p5+i));
    }
}

void vek(char** p2, char** p3, int pocet) {
    int i,j, a = 0, b = 0, a1 = 0, b1 = 0, g;
    char c, pis[5];
    int r_cislo, rok, mesiac, den;
    int* pole_rod_cislo_muzi;
    int* pole_rod_cislo_zeny;
// zistenie dnesneho datumu
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
// nacitanie vstupu
    scanf("\n");
    fgets(pis, 5, stdin);
    strtok(pis, "\n");
// zistenie kolko muzov a kolko zien maju zhodnu diagnozu
    for (i = 0; i < pocet; i++)
        if (strcmp(*(p3 + i),pis) == 0){
            r_cislo = (int)(( atoll(*(p2+i)) / 10000000) % 10);
            if (r_cislo == 0 || r_cislo == 1)
                a++;
            else
                b++;
        }
    // alokovanie pola pre muzov a zeny zistenej velkosti
    pole_rod_cislo_muzi = (int *)  malloc(a * sizeof(int));
    pole_rod_cislo_zeny = (int *)  malloc(b * sizeof(int));

    for (i = 0; i < pocet; i++)
        if (strcmp(*(p3 + i),pis) == 0){
            // zistenie pohlavia
            r_cislo = (int)((atoll(*(p2+i)) / 10000000) % 10);
            // zistenie dna, mesiaca a roku z rodneho cisla
            rok = (int)((atoll(*(p2+i)) / 100000000) % 100);
            mesiac = (int)((atoll(*(p2+i)) / 1000000) % 100);
            den = (int)(atoll(*(p2+i)) % 100);
            // vypocitanie veku k dnesnemu dnu
            if (rok > 25) // ludia narodeni pred 1953 maju 9 miestne rod. cislo, nie 10
                rok = (int)tm.tm_year - rok;
            else
                rok = (int)tm.tm_year-100 - rok;
            if (mesiac > (int)tm.tm_mon)
                rok--;
            if (mesiac == (int)tm.tm_mon){
                if (den > (int)tm.tm_mday)
                    rok--;
            }
            // zapisanie do pola
            if (r_cislo == 0 || r_cislo == 1){
                pole_rod_cislo_muzi[a1] = rok;
                a1++;
            }
            else{
                pole_rod_cislo_zeny[b1] = rok;
                b1++;
            }
        }
    j = 0;
        // usporiadanie pola
    for (i = 0; i < a; i++) {
        for (j = i; j+1 < a; j++) {

            if (pole_rod_cislo_muzi[j+1] < pole_rod_cislo_muzi[i])
            {
                g = pole_rod_cislo_muzi[j+1];
                pole_rod_cislo_muzi[j+1] = pole_rod_cislo_muzi[i];
                pole_rod_cislo_muzi[i] = g;
                j = i;
            }
        }
    }
    // usporiadanie pola
    for (i = 0; i < b; i++) {
        for (j = i; j+1 < b; j++) {

            if (pole_rod_cislo_zeny[j+1] < pole_rod_cislo_zeny[i])
            {
                g = pole_rod_cislo_zeny[j+1];
                pole_rod_cislo_zeny[j+1] = pole_rod_cislo_zeny[i];
                pole_rod_cislo_zeny[i] = g;
                j = i;
            }
        }
    }
    // vypis histogramu
    if(a > 0)
        printf("Muzi\n");

    for (i = 0; i < a; i++) {
        g = 1;
        for (j = i; j+1 < a; j++){
            if (pole_rod_cislo_muzi[i] == pole_rod_cislo_muzi[j + 1]) {
                if (pole_rod_cislo_muzi[i] > 0){
                    g++;
                    pole_rod_cislo_muzi[j + 1] = 0;
                }
            }
        }
        if (pole_rod_cislo_muzi[i] > 0)
            printf("%d: %d\n",pole_rod_cislo_muzi[i],g);
    }
    if (b > 0)
        printf("Zeny\n");

    for (i = 0; i < b; i++) {
        g = 1;
        for (j = i; j+1 < b; j++){
            if (pole_rod_cislo_zeny[i] == pole_rod_cislo_zeny[j + 1]) {
                if (pole_rod_cislo_zeny[i] > 0){
                    g++;
                    pole_rod_cislo_zeny[j + 1] = 0;
                }
            }
        }
        if (pole_rod_cislo_zeny[i] > 0)
            printf("%d: %d\n",pole_rod_cislo_zeny[i],g);
    }
    // dealokovanie pamate
    free(pole_rod_cislo_muzi);
    pole_rod_cislo_muzi = NULL;
    free(pole_rod_cislo_zeny);
    pole_rod_cislo_zeny = NULL;
}

FILE *zmena(FILE *fr,char** p1, char** p2, char** p3, char** p4, double *p5, char** p6, int pocet) {
    int i, datum, count = 0, c = 0, den,mesiac;
    char vysetrenie[52], dat[10], buffer[52];
    long long r_cislo;
    double e;
    FILE *kopia;
    // vytvorenie docasneho suboru, ktore nahradi prvy subor
    kopia = fopen("replace.tmp", "w");
    //nacitanie
    scanf("%lld", &r_cislo);
    if (r_cislo% 11 != 0){
        printf("neplatny vstup- rodne cislo\n");
        exit(0);
    }
    scanf("\n");
    fgets(vysetrenie, 52, stdin);
    strtok(vysetrenie, "\n");
    scanf("%d", &datum);
    scanf("%lf", &e);
    itoa(datum, dat, 10);

    // vypocitanie dna, mesiaca a roku datumu zo vstupu
    den = (int) dat[7] - '0';
    den += (int) (dat[6] - '0') * 10;

    mesiac = (int) dat[5] - '0';
    mesiac += (int) (dat[4] - '0') * 10;

    // kontrola platnosti vstupu
    if(den > 31 || den < 1 || mesiac > 12 || mesiac < 1){
        printf("neplatny vstup- datum\n");
        exit(0);}

    // hladanie prislusneho pacienta v poliach
    for (i = 0; i < pocet; i++) {
        if (atoll(*(p2+i)) == r_cislo && strcmp(*(p4 + i), vysetrenie) == 0 && strcmp(*(p6 + i), dat) == 0) {
            printf("Pacientovy s rodnym cislom %lld bol zmeneny vysledok vysetrwnia %s z povodnej hodnoty %.9g na novu hodnotu %.9g.\n",r_cislo,vysetrenie, *(p5 + i),e);
           // uprava
            *(p5 + i) = e;
            c = 1;
            //skopirovanie celeho suboru aj so zmenou
            for (i = 0; i < pocet; i++) {
                fprintf(kopia,"%s\n", *(p1 + i));
                fprintf(kopia,"%s\n", *(p2 + i));
                fprintf(kopia,"%s\n", *(p3 + i));
                fprintf(kopia,"%s\n", *(p4 + i));
                fprintf(kopia,"%.9g\n", *(p5 + i));
                fprintf(kopia,"%s\n", *(p6 + i));
                if (i+1 < pocet)
                    fprintf(kopia,"\n");
            }
        }
    }
    // yatvorenie kopie
    fclose(kopia);

    if (c == 1){
        // ak bol pacient najdeny a subor prepisani do kopie
        // prvy subor sa zatvori
        fclose(fr);
        // prvy subor sa odstrani
        remove("pacienti.txt");
        // kopia sa premenuje na prvy subor
        rename("replace.tmp", "pacienti.txt");
        // subor sa znovu otvori aby sa s nim dalo v dalsich funkciach pracovat
        fr = fopen("pacienti.txt", "r");
        if (fr == NULL)
            printf("Neotvoreny subor");
    }
    return fr;
}

void dealoc(char **p1,  char**p2, char **p3, char **p4, double *p5, char **p6, int pocet){
    int i;
    // uvolnenie vsetkych poli na informacie o pacientovi
    free(p5);
    p5 = NULL;
    // uvolnenie dvojtych poli
    for (i = 0; i < pocet; i++){
        free(p1[i]);
        p1[i] = NULL;
        free(p2[i]);
        p2[i] = NULL;
        free(p3[i]);
        p3[i] = NULL;
        free(p4[i]);
        p4[i] = NULL;
        free(p6[i]);
        p6[i] = NULL;
    }
    free(p1);
    p1 = NULL;
    free(p2);
    p2 = NULL;
    free(p3);
    p3 = NULL;
    free(p4);
    p4 = NULL;
    free(p6);
    p6 = NULL;

}
void z(char** p1, char** p2, char** p3, char** p4, double *p5, char** p6, int pocet){
    int i, datum1, datum2,den, den1, den2,mes, mes1, mes2, mes3, rok, rok1, rok2, datum,g =0, c1 =0, c2 = 0,a = 0,e = 0,j;
    char vysetrenie[52], vysetrenie2[52] ;
    double *pole, g24;
    char **pole2;
    // nacitanie vstupu
    scanf("%d\n",&datum1);
    scanf("%d\n",&datum2);
    scanf("\n");
    fgets(vysetrenie, 52, stdin);
    // odstranenie "/n" zo vstupneho retazca
    strtok(vysetrenie, "\n");

    // zistenie dna, mesiaca a roka pre oba datumy
    den1 = datum1 % 100;
    mes1 = (datum1 / 100) % 100;
    rok1 = (datum1 / 10000) % 10000;

    den2 = datum2 % 100;
    mes2 = (datum2 / 100) % 100;
    rok2 = (datum2 / 10000) % 10000;
    //kontrola datumov zo vstupu
    if ( den1 > 31 || den2 > 31 || mes1 > 12 || mes2 > 12 || den1 < 1 || den2 < 1 || mes2 < 1 || mes2 < 1){
        printf("neplatny vstup- datum\n");
        exit(0);}

    // zistenie ci sa koncovy datum nenachadza pred pociatocnim
    if (rok2 > rok1)
        g = 1;
    if (rok2 == rok1){
        if (mes2 > mes1){
            g = 1;
            if (den2 >= den1)
                g = 1;
        }
    }
    // ak su datumy vyhovujuce
    if (g == 1) {
        for (i = 0; i < pocet; i++) {
            c1 = 0;
            c2 = 0;
            // zistenie ci sa vysetrenie zhoduje
            if (strcmp(*(p4 + i), vysetrenie) == 0) {
                // zistenie dna mesiaca a roku vysetrenia
                datum = atoi(*(p6 + i));
                den = datum % 100;
                mes = (datum / 100) % 100;
                rok = (datum / 10000) % 10000;
                // zistenie ci je datum vysetrenie neskorsi ako prvy datum
                if (rok > rok1)
                    c1 = 1;
                if (rok == rok1) {
                    if (mes > mes1)
                        c1 = 1;
                    if (mes == mes1){
                        if (den >= den1)
                            c1 = 1;
                    }
                }
                // zistenie ci je datum vysetrenie skorsi ako druhy datum
                if (c1 == 1) {
                    if (rok2 > rok)
                        c2 = 1;
                    if (rok2 == rok) {
                        if (mes2 > mes)
                            c2 = 1;
                        if (mes2 == mes){
                            if (den2 >= den)
                                c2 = 1;
                        }
                    }
                }
                if (c2 == 1)
                    a++;
            }
        }
        //alokovania pola na hodnoty a pola na prisluchajuce mena
        pole = (double *)malloc(sizeof(double)*a);
        pole2 = (char **) malloc(a);

        for (i = 0; i < pocet; i++) {
            c1 = 0;
            c2 = 0;
            // zistenie ci sa vysetrenie zhoduje
            if (strcmp(*(p4 + i), vysetrenie) == 0) {
                datum = atoi(*(p6 + i));
                // zistenie dna mesiaca a roku vysetrenia
                den = datum % 100;
                mes = (datum / 100) % 100;
                rok = (datum / 10000) % 10000;
                c1 = 0;
                // zistenie ci je datum vysetrenie skorsi ako prvy datum
                if (rok > rok1)
                    c1 = 1;
                if (rok == rok1) {
                    if (mes > mes1)
                        c1 = 1;
                    if (mes == mes1) {
                        if (den >= den1)
                            c1 = 1;
                    }
                }
                // zistenie ci je datum vysetrenie skorsi ako druhy datum
                if (c1 == 1) {
                    if (rok2 > rok){
                        // ak je tak sa meno a hodnota zapisu do poli
                        pole[e] = *(p5 + i);
                        pole2[e] = *(p1 + i);
                        e++;
                    }
                    if (rok2 == rok) {
                        if (mes2 > mes) {
                            pole[e] = *(p5 + i);
                            pole2[e] = *(p1 + i);
                            e++;
                        }
                        if(mes2 == mes) {
                            if (den2 >= den){
                                pole[e] = *(p5 + i);
                                pole2[e] = *(p1 + i);
                                e++;
                            }
                        }
                    }
                }
            }
        }
        // zoradenie ploli hodnot, pole mien sa prisluchjuco tiez zoraduje
        for (i = 0; i < a; i++) {
            for (j = i; j+1 < a; j++) {
                if (pole[j+1] > pole[i])
                {
                    strcpy( vysetrenie2, pole2[j+1]);
                    g24 = pole[j+1];
                    strcpy( pole2[j+1], pole2[i]);
                    pole[j+1] = pole[i];
                    pole[i] = g24;
                    strcpy(pole2[i] , vysetrenie2);
                    j = i;
                }
            }
        }
        // vypisi
        if (a >= 3)
            printf("Traja pacienti s najvyssimi hodnotami %s za obdobie %d - %d su (namerana hodnota v zatvorke):\n%s (%.9g)\n%s (%.9g)\n%s (%.9g)\n",vysetrenie ,datum1, datum2, pole2[0], pole[0],pole2[1],pole[1],pole2[2],pole[2]);

        if(a == 2)
            printf("Dvaja pacienti s hodnotami %s za obdobie %d - %d su (namerana hodnota v zatvorke):\n%s (%.9g)\n%s (%.9g)\n",vysetrenie ,datum1, datum2,pole2[0], pole[0],pole2[1],pole[1]);

        if(a == 1)
            printf("Jediny pacient s hodnotami %s za obdobie %d - %d je (namerana hodnota v zatvorke):\n%s (%.9g)\n",vysetrenie ,datum1, datum2,pole2[0], pole[0]);

        // ulovnenie pola hodnot
        free(pole);
        pole = NULL;
    }
    // ak sa koncovy datum sa nachadza pred pociatocnim datumom
    else
        printf("Koncovy datum sa nachadza pred pociatocnim datumom.\n");
}
int main(void) {
    FILE *f;
    int a=0, b = 0, i, as = 0;
    char n = 'a';
    int *pocet;
    char** p1;
    char** p2;
    char** p3;
    char** p4;
    double* p5;
    char** p6;

    // pokial nepride na vstupe 'k', tak sa opakuje while cyklus
    while (n != 'k'){
        scanf("%c",&n);
        if (n == 'v'){
            f = vypis();
            a = 1;
        }
        if (n == 'o' && a == 1)
            datum(f);

        if (n == 'n' && a == 1) {
            if (b == 1)
                dealoc(p1, p2, p3, p4, p5, p6, as);

            polia(f, &p1, &p2, &p3, &p4, &p5, &p6, &pocet);
            b = 1;
            as = *pocet;
        }
        if (n == 's'){
            if (b == 1)
                cislo(p2, p4, p5, as);
            else
                printf("Polia nie su vytvorene\n");
        }
        if (n == 'h'){
            if (b == 1)
                vek(p2,p3,as);
            else
                printf("Polia nie su vytvorene\n");
        }
        if (n == 'p'){
            if (b == 1)
                f = zmena(f,p1,p2,p3,p4,p5,p6,as);
            else
                printf("Polia nie su vytvorene\n");
        }
        if (n == 'z'){
            if (b == 1)
                z(p1,p2,p3,p4,p5,p6,as);
            else
                printf("Polia nie su vytvorene\n");
        }
    }
    // ak je na vstupe 'k'
    if (b == 1)
        // zavola sa funkcia na ddealokovanie poli, a subor sa zatvori
        dealoc(p1, p2, p3, p4, p5, p6, as);
    fclose(f);
    return 0;
}