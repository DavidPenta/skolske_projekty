import random
import tkinter
import time
import math

c = tkinter.Canvas(tkinter.Tk(), width=800, height=900)
c.pack()


def print_cities():  # nakresli mesta
    u = 0
    for city in cities:
        c.create_oval((city[0]-5)*3, (city[1]-5)*3, (city[0]+5)*3, (city[1]+5)*3, fill="black", tag="p")
        c.create_text(city[0]*3, city[1]*3+30, fill="darkblue", font="Times 15 italic", text=str(u))
        u += 1


def print_way(arr, N):  # nakresli cestu
    for f in range(N-1):
        c.create_line((cities[(arr[f])][0]) * 3, (cities[(arr[f])][1]) * 3, (cities[(arr[f + 1])][0]) * 3, (cities[(arr[f + 1])][1]) * 3, width=3)
    c.create_line((cities[(arr[len(arr)-1])][0]) * 3, (cities[(arr[len(arr)-1])][1]) * 3, (cities[(arr[0])][0]) * 3, (cities[(arr[0])][1]) * 3, width=3)


def distance(p1, p2):  # zisti vzdialenost dvoch miest
    return ((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2) ** 0.5


def total_distance(points, N):  # zisti cenu celej trasy
    s = 0
    for f in range(N-1):
        s += distance(cities[points[f]], cities[points[f+1]])
    s += distance(cities[points[N-1]], cities[points[0]])
    return s


def rotate_substring(arr, N):  # otocenie podretazca
    arr_copy = list(arr)
    c1 = random.randint(2, N - 1)
    c2 = random.randint(0, N - c1)
    arr_copy[c2: (c2 + c1)] = reversed(arr_copy[c2: (c2 + c1)])
    return arr_copy


def swap(array, N):  # vymena dvoch miest
    x = random.randrange(N)
    y = random.randrange(N)
    array[x], array[y] = array[y], array[x]
    return array


def get_neighbors(arr, N):  # ziskanie okolia
    pole = []
    for f in range(20):
        # pole.append(swap(list(arr), N))
        pole.append(rotate_substring(list(arr), N))
    return pole


def check_tabu_list(tabu_list, arr):  # zistenie ci sa nachadza v tabu liste
    for ta in tabu_list:
        if arr == ta:
            return 0
    return 1


def tabu_search(tabu_size):
    start_time = time.time()
    N = len(cities)
    arr = []
    for b in range(N):
        arr.append(b)
    random.shuffle(arr)  # vytvorenie nahodneho vektora

    best = arr
    best_candidate = arr
    tabu_list = [arr]

    n = 300
    s = 0
    while s < n:
        neighborhood = get_neighbors(best_candidate, N)  # ziskanie okolia
        best_candidate = neighborhood[0]
        for neighbor in neighborhood:
            if check_tabu_list(tabu_list, neighbor) and (total_distance(neighbor, N) < total_distance(best_candidate, N)):
                best_candidate = neighbor

        tabu_list.append(best_candidate)
        if len(tabu_list) > tabu_size:
            del tabu_list[0]  # vymazanie najstarsieho

        if total_distance(best_candidate, N) < total_distance(best, N):
            best = best_candidate
            s = 0
        else:
            s += 1

    return time.time() - start_time, best


def simulated_annealing(alpha, temp, min_temp):
    start_time = time.time()
    N = len(cities)
    arr = []
    for b in range(N):
        arr.append(b)
    random.shuffle(arr)  # vytvorenie nahodneho vektora

    cost = total_distance(arr, N)
    best = arr
    best_cost = total_distance(arr, N)

    while temp > min_temp:
        arr_copy = rotate_substring(list(arr), N)
        cost2 = total_distance(arr_copy, N)

        if cost2 < cost:
            cost = cost2
            arr = list(arr_copy)
            if cost2 < best_cost:
                best_cost = cost2
                best = arr_copy
        else:
            if random.uniform(0, 1) < math.exp(-abs(cost2 - cost) / temp):
                cost = cost2
                arr = arr_copy
        temp *= alpha  # znizovanie teploty

    return time.time() - start_time, best


def print_it_on_canvas(best, N):
    print_cities()
    print_way(best, N)
    c.create_text(200, 800, fill="darkblue", font="Times 15 italic", text="Dlzka najdenej trasy je: " + str(total_distance(best, N)))


if __name__ == '__main__':
    print("Tester- 1\nManualne zadanie- 2")
    x1 = input()
    if x1 == str(2):
        x2 = int(input("pocet miest: "))
        rows, cols = (x2, 2)
        cities = []
        for i in range(rows):
            col = []
            for j in range(cols):
                col.append(random.randrange(20, 220))
            cities.append(col)
        print("tabu search 1\nsimulated annealing 2\n")
        x3 = input("Input: ")
        if x3 == str(1):
            x4 = input("velkost tabu_list: ")
            if x4 == "":
                x4 = 5
            else:
                x4 = int(x4)

            ts = tabu_search(x4)
            print("cas - " + str(ts[0]) + "s")
            print(ts[1])
            print(str(total_distance(ts[1], x2)))
            print_it_on_canvas(ts[1], x2)
        if x3 == str(2):
            x4 = input("alpha: ")
            if x4 == "":
                x4 = 0.999
            else:
                x4 = float(x4)
            x5 = input("start temperature: ")
            if x5 == "":
                x5 = 40
            else:
                x5 = float(x5)
            x6 = input("end temperature: ")
            if x6 == "":
                x6 = 0.0000001
            else:
                x6 = float(x6)
            an = simulated_annealing(x4, x5, x6)
            print("cas - " + str(an[0]) + "s")
            print(an[1])
            print(str(total_distance(an[1], x2)))
            print_it_on_canvas(an[1], x2)

    if x1 == str(1):  # tester
        for z in range(20, 41, 10):
            rows, cols = (z, 2)
            cities = []
            for i in range(rows):
                col = []
                for j in range(cols):
                    col.append(random.randrange(20, 220))
                cities.append(col)

            for i in range(10, 100, 10):
                for j in range(5):
                    ts = tabu_search(i)
                    print("tabu_list size: " + str(i))
                    print("cas - " + str(ts[0]) + "s")
                    print(ts[1])
                    print(str(total_distance(ts[1], z)))
                    print()

        for z in range(20, 41, 10):
            rows, cols = (z, 2)
            cities = []
            for i in range(rows):
                col = []
                for j in range(cols):
                    col.append(random.randrange(20, 220))
                cities.append(col)
            for i in range(10, 80, 10):
                for j in range(5):
                    an = simulated_annealing(0.99, i, 0.0000001)
                    print("alpha: 0.99, start temp: " + str(i) + ", end temp: 0.0000001")
                    print("cas - " + str(an[0]) + "s")
                    print(an[1])
                    print(str(total_distance(an[1], z)))
                    print()

                for j in range(5):
                    an = simulated_annealing(0.999, i, 0.0000001)
                    print("alpha: 0.999, start temp: " + str(i) + ", end temp: 0.0000001")
                    print("cas - " + str(an[0]) + "s")
                    print(an[1])
                    print(str(total_distance(an[1], z)))
                    print()

                for j in range(5):
                    an = simulated_annealing(0.9995, i, 0.0000001)
                    print("alpha: 0.9995, start temp: " + str(i) + ", end temp: 0.0000001")
                    print("cas - " + str(an[0]) + "s")
                    print(an[1])
                    print(str(total_distance(an[1], z)))
                    print()

                for j in range(5):
                    an = simulated_annealing(0.9999, i, 0.0000001)
                    print("alpha: 0.9999, start temp: " + str(i) + ", end temp: 0.0000001")
                    print("cas - " + str(an[0]) + "s")
                    print(an[1])
                    print(str(total_distance(an[1], z)))
                    print()

c.mainloop()
