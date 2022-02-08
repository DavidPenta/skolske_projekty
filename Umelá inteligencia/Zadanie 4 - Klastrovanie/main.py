import random
import tkinter
import ast
import numpy as np
import matplotlib.pyplot as plt
from scipy.spatial import ConvexHull
import os
import plotly.figure_factory as ff

c = tkinter.Canvas(tkinter.Tk(), width=1020, height=1020, bg="#fbfbfb")
c.pack()
colors = ['#e6194B', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#42d4f4', '#f032e6', '#bfef45', '#fabed4',
          '#469990', '#dcbeff', '#9A6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#a9a9a9',
          'tomato', '#000000']


# vypocet vzdialenosti dvoch bodov
def distance(p1, p2):
    return ((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2) ** 0.5


# nakresleni bodu do tkinter
def print_point(point, color):
    if isinstance(point, list) and len(point) == 2:
        if color == 0:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[0], outline=colors[0])
        if color == 1:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[1], outline=colors[1])
        if color == 2:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[2], outline=colors[2])
        if color == 3:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[3], outline=colors[3])
        if color == 4:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[4], outline=colors[4])
        if color == 5:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[5], outline=colors[5])
        if color == 6:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[6], outline=colors[6])
        if color == 7:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[7], outline=colors[7])
        if color == 8:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[8], outline=colors[8])
        if color == 9:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[9], outline=colors[9])
        if color == 10:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[10], outline=colors[10])
        if color == 11:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[11], outline=colors[11])
        if color == 12:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[12], outline=colors[12])
        if color == 13:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[13], outline=colors[13])
        if color == 14:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[14], outline=colors[14])
        if color == 15:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[15], outline=colors[15])
        if color == 16:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[16], outline=colors[16])
        if color == 17:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[17], outline=colors[17])
        if color == 18:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[18], outline=colors[18])
        if color == 19:
            c.create_oval(point[0] / 10 + 510 - 3, point[1] / 10 + 510 - 3, point[0] / 10 + 510 + 3,
                          point[1] / 10 + 510 + 3, fill=colors[19], outline=colors[19])


def k_means_centroid(k, points_):
    centroids = []
    l = 0

    while l < k:  # ziskanie k roznich zaciatocnich centroidov
        random_point = points_[random.randrange(0, len(points_))]
        if random_point not in centroids:
            centroids.append(random_point)
            l += 1

    clusters = []

    centroids2 = [-1]
    count = 0
    while centroids != centroids2 and count < 100:
        count += 1
        centroids2 = list(centroids)
        clusters = []
        for i in range(k):
            clusters.append([])

        for point in points_:  # priradenie bodov do zhlukov
            minimum = -1
            minimum_dis = -1
            for i in range(k):
                d = distance(point, centroids[i])
                if d < minimum_dis or minimum_dis == -1:
                    minimum = i
                    minimum_dis = d

            clusters[minimum].append(point)

        centroids = []
        for i in range(k):
            centroids.append([])

        for i in range(k):  # vypocitanie novych centroidov
            x = 0
            y = 0
            for cluster in clusters[i]:
                x += cluster[0]
                y += cluster[1]
            if len(clusters[i]) > 0:
                x /= len(clusters[i])
                y /= len(clusters[i])
            centroids[i] = [x, y]

    return clusters


def k_means_medoid(k, points_):
    clusters = []
    medoids = []

    l = 0

    while l < k: # ziskanie k roznich zaciatocnich medoidov
        random_point = points_[random.randrange(0, len(points_))]
        if random_point not in medoids:
            medoids.append(random_point)
            l += 1

    medoids2 = [-1]
    count = 0

    while medoids != medoids2 and count < 100:
        count += 1
        medoids2 = list(medoids)
        clusters = []
        for i in range(k):
            clusters.append([])

        for point in points_:  # priradenie bodov do zhlukov
            minimum = -1
            minimum_dis = -1
            for i in range(k):
                d = distance(point, medoids[i])
                if d < minimum_dis or minimum_dis == -1:
                    minimum = i
                    minimum_dis = d

            clusters[minimum].append(point)

        medoids = []
        for i in range(k):
            medoids.append([])

        for i in range(k):  # vypocitanie novych medoidov
            min_dist = -1
            min_medoid = []
            for cluster in clusters[i]:
                dist = 0
                for cluster2 in clusters[i]:
                    dist += distance(cluster, cluster2)
                if dist < min_dist or min_dist == -1:
                    min_dist = dist
                    min_medoid = list(cluster)
            medoids[i] = list(min_medoid)

    return clusters


# zistenie ci su body zhodne
def all_equal(iterator):
    iterator = iter(iterator)
    try:
        first = next(iterator)
    except StopIteration:
        return True
    return all(first == x for x in iterator)


# rozdelenie zhluku na dva podla k-means kde k=2
def divide_in_two(points_):
    centroids = []
    if len(points_) <= 2:
        return points_

    else:
        if all_equal(points_):  # ak su body v zhluku totozne
            centroids.append(points_[0])
            centroids.append(points_[1])
        else:
            l = 0
            while l < 2:  # ziskanie nahodnych centroidov
                random_point = points_[random.randrange(0, len(points_))]
                if random_point not in centroids:
                    centroids.append(random_point)
                    l += 1

    clusters = []
    centroids2 = [-1]
    count = 0

    while centroids != centroids2 and count < 30:
        count += 1
        centroids2 = list(centroids)
        clusters = []
        for j in range(2):
            clusters.append([])

        for point in points_:  # priradenie bodov do zhluku
            minimum = -1
            minimum_dis = -1
            for j in range(2):
                d = distance(point, centroids[j])
                if d < minimum_dis or minimum_dis == -1:
                    minimum = j
                    minimum_dis = d

            clusters[minimum].append(point)

        centroids = []
        for j in range(2):
            centroids.append([])

        for j in range(2):  # vypocitanie novych centroidov
            x = 0
            y = 0
            for cluster_ in clusters[j]:
                x += cluster_[0]
                y += cluster_[1]
            x /= len(clusters[j])
            y /= len(clusters[j])
            centroids[j] = [x, y]

    return clusters


# zistenie ci je mozne skupinu bodov ohranicit = tvori polygon ktory ma plochu > 0
def poly_area(arr):
    if isinstance(arr, int):
        return False
    if len(arr) == 2 and isinstance(arr[0], list):
        x = np.array([arr[0][0], arr[1][0]])
        y = np.array([arr[0][1], arr[1][1]])
        plt.fill(x, y, alpha=0.5, c="blue")
    if len(arr) < 3:
        return False
    if len(arr) > 100:
        return True
    x = [f[0] for f in arr]
    y = [f[1] for f in arr]
    if 0.5 * np.abs(np.dot(x, np.roll(y, 1))-np.dot(y, np.roll(x, 1))) > 0:  # vzorec pre vypocet plochy polygonu
        return True
    return False


# divizne zhlukovanie s pribeznym ukladanim grafov do suboru
def division_clustering_animation(k, points_, my_path):  # k -> kolko rozdeleni sa ma vykonat
    t = 0
    xcor = []
    ycor = []
    for i in points_:
        xcor.append(i[0])
        ycor.append(i[1])

    if poly_area(points_):  # ak to je mozne tak zhluk ohranic
        df2 = np.array(points_)
        ch = ConvexHull(points_)
        hull_indices = ch.vertices
        hull_pts = df2[hull_indices, :]
        plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")
    y = 0
    plt.title("Number of divisions: " + str(y))
    plt.scatter(xcor, ycor)
    plt.xlim(-5000, 5000)
    plt.ylim(-5000, 5000)
    plt.savefig(os.path.join(my_path, "graph" + str(t) + ".png"))
    plt.clf()
    t += 1

    if poly_area(points_):  # ak to je mozne tak zhluk ohranic
        df2 = np.array(points_)
        ch = ConvexHull(points_)
        hull_indices = ch.vertices
        hull_pts = df2[hull_indices, :]
        plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")
    if poly_area(points_):  # ak to je mozne tak zhluk ohranic
        df2 = np.array(points_)
        ch = ConvexHull(points_)
        hull_indices = ch.vertices
        hull_pts = df2[hull_indices, :]
        plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="red")

    points_ = divide_in_two(points_)
    plt.title("Number of divisions: " + str(y))
    plt.xlim(-5000, 5000)
    plt.ylim(-5000, 5000)
    plt.scatter(xcor, ycor)
    plt.savefig(os.path.join(my_path, "graph" + str(t) + ".png"))
    plt.clf()
    t += 1
    y += 1

    for cluster in points_:  # kazdy zhluk ohranic
        if poly_area(cluster):
            df2 = np.array(cluster)
            ch = ConvexHull(cluster)
            hull_indices = ch.vertices
            hull_pts = df2[hull_indices, :]
            plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")
    plt.title("Number of divisions: " + str(y))
    plt.xlim(-5000, 5000)
    plt.ylim(-5000, 5000)
    plt.scatter(xcor, ycor)
    plt.savefig(os.path.join(my_path, "graph" + str(t) + ".png"))
    plt.clf()
    t += 1

    while len(points_) <= k:
        max_i = 0
        max_len = 0
        for i in range(len(points_)):  # najdi zhluk ktory obsahuje najviac bodov
            if len(points_[i]) > max_len and isinstance(points_[i], list) and isinstance(points_[i][0], list):
                max_len = len(points_[i])
                max_i = i

        for cluster in points_:  # kazdy zhluk ohranic
            if poly_area(cluster):
                df2 = np.array(cluster)
                ch = ConvexHull(cluster)
                hull_indices = ch.vertices
                hull_pts = df2[hull_indices, :]
                plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")

        if poly_area(points_[max_i]):  # oznac zhluk ktory sa bude delit
            df2 = np.array(points_[max_i])
            ch = ConvexHull(points_[max_i])
            hull_indices = ch.vertices
            hull_pts = df2[hull_indices, :]
            plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="red")

        plt.title("Number of divisions: " + str(y))
        plt.scatter(xcor, ycor)
        plt.xlim(-5000, 5000)
        plt.ylim(-5000, 5000)
        plt.savefig(os.path.join(my_path, "graph" + str(t) + ".png"))
        plt.clf()
        t += 1
        y += 1

        asd = divide_in_two(points_[max_i])  # zhluk rozdeleny na dva
        del points_[max_i]
        if isinstance(asd[0], list) and len(asd[0]) == 1 and isinstance(asd[0][0], list):
            asd[0] = asd[0][0]
        if isinstance(asd[1], list) and len(asd[1]) == 1 and isinstance(asd[1][0], list):
            asd[1] = asd[1][0]
        points_.append(asd[0])
        points_.append(asd[1])

        for cluster in points_:  # kazdy zhluk ohranic
            if poly_area(cluster):
                df2 = np.array(cluster)
                ch = ConvexHull(cluster)
                hull_indices = ch.vertices
                hull_pts = df2[hull_indices, :]
                plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")

        plt.title("Number of divisions: " + str(y))
        plt.scatter(xcor, ycor)
        plt.xlim(-5000, 5000)
        plt.ylim(-5000, 5000)
        plt.savefig(os.path.join(my_path, "graph" + str(t) + ".png"))
        plt.clf()
        t += 1


# divizne zhlukovanie bez ukladania grafov do suboru
def division_clustering(k, points_):
    points_ = divide_in_two(points_)
    alone_points = []  # pole do ktore ho sa pridavaju zhluky tvorene jednym bodom

    for i in range(k-2):
        max_n = 0
        max_cluster = []
        for j in range(len(points_)):  # najdi zhluk s nanjvacsim poctom bodov
            if len(points_[j]) >= max_n:
                max_n = len(points_[j])
                max_cluster = j

        # prida dva vytvorene zhluky do spravnich poli
        if len(points_) > 0 and isinstance(points_, list) and isinstance(points_[max_cluster], list) and isinstance(points_[max_cluster][0], list):
            new = list(divide_in_two(points_[max_cluster]))
            del points_[max_cluster]

            if len(new[0]) > 1 and isinstance(new[0], list) and isinstance(new[0][0], list):
                points_.append(new[0])
            else:
                if isinstance(new[0], list) and isinstance(new[0][0], list):
                    alone_points.append(new[0][0])
                else:
                    alone_points.append(new[0])

            if len(new) > 1 and len(new[1]) > 1 and isinstance(new[1], list) and isinstance(new[1][0], list):
                points_.append(new[1])
            else:
                if isinstance(new[1], list) and isinstance(new[1][0], list):
                    alone_points.append(new[1][0])
                else:
                    alone_points.append(new[1])

        if len(points_) == 0:
            break

    for i in alone_points:
        points_.append(i)

    return points_


# ziskanie centroidov pre vypocet uspesnosti zhlukovaca
def get_centroids(clusters):
    clusters_len = len(clusters)
    centroids = []
    for i in range(clusters_len):
        centroids.append([])

    for i in range(clusters_len):
        x = 0
        y = 0
        for cluster in clusters[i]:
            if isinstance(cluster, list):
                x += cluster[0]
                y += cluster[1]
        if len(clusters[i]) > 0:
            x /= len(clusters[i])
            y /= len(clusters[i])
        centroids[i] = [x, y]
    return centroids


# vypocet uspesnosti zhlukovaca
def get_success_rate(clusters):
    len_points = 0
    for cluster in clusters:
        for point in cluster:
            len_points += 1

    clusters_len = len(clusters)
    success_rate_clusters = 0
    success_rate_points = 0
    centroids = get_centroids(clusters)

    for i in range(clusters_len):
        dist = 0
        for cluster in clusters[i]:
            if isinstance(cluster, list):
                d = distance(cluster, centroids[i])
                dist += d
                if d < 500:
                    success_rate_points += 1
        if len(clusters[i]) > 0:
            dist /= len(clusters[i])
        if dist < 500:
            success_rate_clusters += 1

    return success_rate_clusters/clusters_len * 100, success_rate_points/len_points * 100


# vykresly farebne rozlisene zhluky do tkinter
def print_graph(clusters):
    centroids = get_centroids(clusters)

    for i in range(len(centroids)):
        for cluster in clusters[i]:
            print_point(cluster, i)

    for i in range(len(centroids)):
        c.create_rectangle(centroids[i][0] / 10 + 510 - 4, centroids[i][1] / 10 + 510 - 4, centroids[i][0] / 10 + 510 + 4, centroids[i][1] / 10 + 510 + 4, fill="black", outline="white")
    c.mainloop()


# ziska body z priecinka
def get_points_from_file():
    file = open("C:\\Users\\david\\Desktop\\results", "r")
    str_points = file.readline()
    points_ = ast.literal_eval(str_points)
    file.close()
    return points_


# vytvori nahodne body
def create_random_points(o, o2):
    points_ = []
    for i in range(o):
        x1 = random.randrange(-5000, 5000)
        y1 = random.randrange(-5000, 5000)
        points_.append([x1, y1])

    for i in range(o2):
        ran = random.randrange(0, len(points_))
        col = list(points_[ran])
        col[0] += random.randrange(-100, 100)
        if col[0] < -5000:
            col[0] = -5000
        if col[0] > 5000:
            col[0] = 5000
        col[1] += random.randrange(-100, 100)
        if col[1] < -5000:
            col[1] = -5000
        if col[1] > 5000:
            col[1] = 5000
        points_.append(col)
    return points_


# vytvori dendrogram
def create_dendrogram(points):
    fig = ff.create_dendrogram(np.array(points[:100]), orientation='left', labels=points[:100])
    fig.update_layout(width=1500, height=2000)
    fig.write_image("C:\\Users\\david\\Desktop\\photo.svg")


# nakresli zhluky po n spojeniach v aglomerativnom zhlukovaci
def print_aglo(n, subor, my_path):
    file = open(subor, "r")
    str_points = file.readline()
    points_ = ast.literal_eval(str_points)

    xcor = []
    ycor = []
    for i in points_:
        xcor.append(i[0])
        ycor.append(i[1])

    str_line = ""
    for i, str_line in enumerate(file):
        if i == n:
            break

    line = ast.literal_eval(str_line)

    for cluster in line:
        if poly_area(cluster):
            df2 = np.array(cluster)
            ch = ConvexHull(cluster)
            hull_indices = ch.vertices
            hull_pts = df2[hull_indices, :]
            plt.fill(hull_pts[:, 0], hull_pts[:, 1], alpha=0.5, c="blue")

    plt.title("Number of merges: " + str(n))
    plt.scatter(xcor, ycor)
    plt.xlim(-5000, 5000)
    plt.ylim(-5000, 5000)
    plt.savefig(os.path.join(my_path, "aglo_graph" + ".png"))
    plt.clf()

    file.close()


# aglomerativne zhlukovanie
def aglo(points):
    t = 0
    f = open("results1", "w")

    clusters = list(points)
    matrix = []

    for i in range(len(points)):  # vytvori maticu vzdialenosti
        matrix.append([])
        for j in range(len(points)):
            matrix[i].append(distance(points[i], points[j]))

    f.write(str(points) + "\n")

    list_of_centroids = list(points)

    while len(list_of_centroids) > 1:
        f.write(str(clusters) + "\n")  # zapise zhluky do suboru

        t += 1
        minimum = 0
        minimum_point_1 = 0
        minimum_point_2 = 0

        for i in range(len(matrix)):  # najde centroidy medzdi ktorimi je najmensia vzdialenost
            for j in range(len(matrix[i])):
                if matrix[i][j] > 0:
                    if minimum == 0 or matrix[i][j] < minimum:
                        minimum = matrix[i][j]
                        minimum_point_1 = i
                        minimum_point_2 = j

        # spoji zhluky v poli zhlukov
        new_cluster = []

        if isinstance(clusters[minimum_point_1][0], list):
            for h in clusters[minimum_point_1]:
                new_cluster.append(h)
        else:
            new_cluster.append(clusters[minimum_point_1])

        if isinstance(clusters[minimum_point_2][0], list):
            for h in clusters[minimum_point_2]:
                new_cluster.append(h)
        else:
            new_cluster.append(clusters[minimum_point_2])

        # vymaze dva centroidy z matice
        del clusters[minimum_point_1]
        del clusters[minimum_point_2 - 1]
        clusters.append(new_cluster)

        del matrix[minimum_point_1]
        del matrix[minimum_point_2 - 1]

        del list_of_centroids[minimum_point_1]
        del list_of_centroids[minimum_point_2 - 1]

        for row in matrix:
            del row[minimum_point_1]
            del row[minimum_point_2 - 1]

        # vypocita novy centroid
        centroid_x = 0
        centroid_y = 0

        for c in clusters[-1]:
            centroid_x += c[0]
            centroid_y += c[1]

        centroid = [centroid_x / len(clusters[-1]), centroid_y / len(clusters[-1])]
        list_of_centroids.append(centroid)

        # prida novy centroid do matice
        for i in range(len(matrix)):
            matrix[i].append(distance(centroid, list_of_centroids[i]))

        new_row = []
        for i in range(len(list_of_centroids)):
            new_row.append(distance(list_of_centroids[-1], list_of_centroids[i]))
        matrix.append(new_row)
    f.close()


# testovanie k-means a divizneho zhlukovania
def testing():
    points = create_random_points(20, 5000)
    for i in range(20, 1, -1):
        print(i)
        c1_ = 0
        c2_ = 0
        c3_ = 0
        p1_ = 0
        p2_ = 0
        p3_ = 0
        for j in range(10):
            k_means_centroid_clusters = k_means_centroid(i, points)
            c1, p1 = get_success_rate(k_means_centroid_clusters)
            k_means_medoid_clusters = k_means_medoid(i, points)
            c2, p2 = get_success_rate(k_means_medoid_clusters)
            dc = division_clustering(i, points)
            c3, p3 = get_success_rate(dc)
            c1_ += c1
            c2_ += c2
            c3_ += c3
            p1_ += p1
            p2_ += p2
            p3_ += p3
        print(c1_ / 10, p1_ / 10)
        print(c2_ / 10, p2_ / 10)
        print(c3_ / 10, p3_ / 10)

    points = create_random_points(20, 10000)
    for i in range(20, 1, -1):
        print(i)
        c1_ = 0
        c2_ = 0
        c3_ = 0
        p1_ = 0
        p2_ = 0
        p3_ = 0
        for j in range(10):
            k_means_centroid_clusters = k_means_centroid(i, points)
            c1, p1 = get_success_rate(k_means_centroid_clusters)
            k_means_medoid_clusters = k_means_medoid(i, points)
            c2, p2 = get_success_rate(k_means_medoid_clusters)
            dc = division_clustering(i, points)
            c3, p3 = get_success_rate(dc)
            c1_ += c1
            c2_ += c2
            c3_ += c3
            p1_ += p1
            p2_ += p2
            p3_ += p3
        print(c1_ / 10, p1_ / 10)
        print(c2_ / 10, p2_ / 10)
        print(c3_ / 10, p3_ / 10)

    points = create_random_points(20, 15000)
    for i in range(20, 1, -1):
        print(i)
        c1_ = 0
        c2_ = 0
        c3_ = 0
        p1_ = 0
        p2_ = 0
        p3_ = 0
        for j in range(10):
            k_means_centroid_clusters = k_means_centroid(i, points)
            c1, p1 = get_success_rate(k_means_centroid_clusters)
            k_means_medoid_clusters = k_means_medoid(i, points)
            c2, p2 = get_success_rate(k_means_medoid_clusters)
            dc = division_clustering(i, points)
            c3, p3 = get_success_rate(dc)
            c1_ += c1
            c2_ += c2
            c3_ += c3
            p1_ += p1
            p2_ += p2
            p3_ += p3
        print(c1_ / 10, p1_ / 10)
        print(c2_ / 10, p2_ / 10)
        print(c3_ / 10, p3_ / 10)

    points = create_random_points(20, 20000)
    for i in range(20, 1, -1):
        print(i)
        c1_ = 0
        c2_ = 0
        c3_ = 0
        p1_ = 0
        p2_ = 0
        p3_ = 0
        for j in range(10):
            k_means_centroid_clusters = k_means_centroid(i, points)
            c1, p1 = get_success_rate(k_means_centroid_clusters)
            k_means_medoid_clusters = k_means_medoid(i, points)
            c2, p2 = get_success_rate(k_means_medoid_clusters)
            dc = division_clustering(i, points)
            c3, p3 = get_success_rate(dc)
            c1_ += c1
            c2_ += c2
            c3_ += c3
            p1_ += p1
            p2_ += p2
            p3_ += p3
        print(c1_ / 10, p1_ / 10)
        print(c2_ / 10, p2_ / 10)
        print(c3_ / 10, p3_ / 10)


def menu():
    print("0- testovanie")
    print("1- k-means centroid")
    print("2- k-means medoid")
    print("3- divizne zhlukovanie")
    print("4- aglomerativne zhlukovanie")
    x = input()

    if x == "0":
        testing()

    elif x == "1":
        print("k (20)")
        x0 = input()
        print("pocet nahodnych bodov (20)")
        x1 = input()
        print("pocet okolitych bodov (20000)")
        x2 = input()
        if x0 == "":
            x0 = 20
        else:
            x0 = int(x0)
        if x1 == "":
            x1 = 20
        else:
            x1 = int(x1)
        if x2 == "":
            x2 = 20000
        else:
            x2 = int(x2)

        points1 = create_random_points(x1, x2)
        k_means_centroid_clusters = k_means_centroid(x0, points1)
        c1, p1 = get_success_rate(k_means_centroid_clusters)
        print("Uspesnost zhlukov: ", c1, "%")
        print("Uspesnost bodov: ", p1, "%")
        print_graph(k_means_centroid_clusters)

    elif x == "2":
        print("k (20)")
        x0 = input()
        print("pocet nahodnych bodov (20)")
        x1 = input()
        print("pocet okolitych bodov (20000)")
        x2 = input()
        if x0 == "":
            x0 = 20
        else:
            x0 = int(x0)
        if x1 == "":
            x1 = 20
        else:
            x1 = int(x1)
        if x2 == "":
            x2 = 20000
        else:
            x2 = int(x2)

        points1 = create_random_points(x1, x2)
        k_means_medoid_clusters = k_means_medoid(x0, points1)
        c1, p1 = get_success_rate(k_means_medoid_clusters)
        print("Uspesnost zhlukov: ", c1, "%")
        print("Uspesnost bodov: ", p1, "%")
        print_graph(k_means_medoid_clusters)

    elif x == "3":
        print("graf pre kazde rozdelenie (0)")
        a = input()
        print("k -vykresli sa iba ak k <= 20 (vsetky)")
        x0 = input()
        print("pocet nahodnych bodov (20)")
        x1 = input()
        print("pocet okolitych bodov (20000)")
        x2 = input()
        if x1 == "":
            x1 = 20
        else:
            x1 = int(x1)
        if x2 == "":
            x2 = 20000
        else:
            x2 = int(x2)
        if x0 == "":
            x0 = x1 + x2
        else:
            x0 = int(x0)
        if a == "":
            a = "0"

        points1 = create_random_points(x1, x2)
        if a == "0":
            dc = division_clustering(x0, points1)
            if x0 <= 20:
                c1, p1 = get_success_rate(dc)
                print("Uspesnost zhlukov: ", c1, "%")
                print("Uspesnost bodov: ", p1, "%")
                print_graph(dc)

        if a == "1":
            print("kam sa maju grafy ulozit")
            path = input()
            division_clustering_animation(x0, points1, path)

    elif x == "4":
        print("1- vypisat zo suboru")
        print("2- vyrobit novy")
        x1 = input()
        if x1 == "1":
            print("pocet zluceni")
            x2 = int(input())
            print("nazov suboru so zhlukmi")
            x3 = input()
            print("nazov priecinku kam sa ma graf ulozit")
            x4 = input()
            print_aglo(x2, x3, x4)

        if x1 == "2":
            print("pocet nahodnych bodov (20)")
            x3 = input()
            print("pocet okolitych bodov (20000)")
            x4 = input()

            if x3 == "":
                x3 = 20
            else:
                x3 = int(x3)
            if x4 == "":
                x4 = 20000
            else:
                x4 = int(x4)
            points1 = create_random_points(x3, x4)
            aglo(points1)

if __name__ == '__main__':
    menu()

