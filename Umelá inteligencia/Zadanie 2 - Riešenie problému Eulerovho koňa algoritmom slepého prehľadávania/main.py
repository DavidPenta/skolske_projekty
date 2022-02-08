import random
import time

solution = 0
start_time = 0


def setTime():
    global start_time
    start_time = time.time()


def setSolution():
    global solution
    solution += 1


def resetSolution():
    global solution
    solution = 0


def printBoard(arr):
    print("Riesenie cislo: " + str(solution + 1))
    print("Cas: " + str(round(time.time() - start_time, 6)) + "s")
    print('\n'.join([''.join(['{:4}'.format(item) for item in row]) for row in arr]))
    print()


def solve(arr, move_count, x, y, s, t):
    x_operator = [1, 1, 2, 2, -1, -1, -2, -2]  # mozny pohyb kona po sachovnici
    y_operator = [2, -2, 1, -1, 2, -2, 1, -1]
    if solution < s and time.time() - start_time < t:
        if move_count > len(arr) ** 2:  # sachovnica je plna, tym padom riesenie je zistene
            printBoard(arr)
            setSolution()
        else:
            for i in range(len(x_operator)):  # prehladavanie vsetkych moznosti
                next_x = x + x_operator[i]
                next_y = y + y_operator[i]

                if 0 <= next_x < len(arr) and 0 <= next_y < len(arr):  # kontrola ci je v sachovnici
                    if arr[next_x][next_y] == 0:  # kontrola ci je policko volne
                        arr[next_x][next_y] = move_count
                        solve(arr, move_count + 1, next_x, next_y, s, t)  # rekurzivne volanie
                        arr[next_x][next_y] = 0


def program(g, y, x, t, s):
    arr = []  # vytvorenie sachovnice
    for i in range(g):
        col = []
        for j in range(g):
            col.append(0)
        arr.append(col)

    print("Pociatocna pozicia: " + str(y), str(x))

    arr[x][y] = 1
    setTime()
    solve(arr, 2, x, y, s, t)
    if time.time() - start_time > t:
        print("V casovom limite sa nepodarilo najst riesenie")
    elif solution < s:
        print("Riesenie neexistuje")
    resetSolution()


print("Tester: 1")
print("Vlastne: 2")
x = input("Zvolte akciu: ")

if x == str(1):
    program(5, 0, 4, 15, 5)
    program(5, random.randrange(5), random.randrange(5), 15, 5)
    program(5, random.randrange(5), random.randrange(5), 15, 5)
    program(5, random.randrange(5), random.randrange(5), 15, 5)
    program(5, random.randrange(5), random.randrange(5), 15, 5)
    program(5, random.randrange(5), random.randrange(5), 15, 5)

    program(6, 0, 5, 15, 5)
    program(6, random.randrange(6), random.randrange(6), 1500, 5)
    program(6, random.randrange(6), random.randrange(6), 1500, 5)
    program(6, random.randrange(6), random.randrange(6), 1500, 5)
    program(6, random.randrange(6), random.randrange(6), 1500, 5)
    program(6, random.randrange(6), random.randrange(6), 1500, 5)

if x == str(2):
    g = input("Velkost sachovnice: ")
    x = input("Pociatocny stlpec: ")
    y = input("Pociatocny riadok: ")
    t = input("Maximalny cas (s): ")
    s = input("Pocet rieseni: ")
    program(int(g), int(x), int(y), int(t), int(s))
