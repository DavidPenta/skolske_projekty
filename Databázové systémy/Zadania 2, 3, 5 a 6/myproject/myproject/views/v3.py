from rest_framework.response import Response
from rest_framework.decorators import api_view
import psycopg2
import json
import os


@api_view(['GET'])
def getData_v3_top_purchases(request, match_id):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'), password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
        SELECT x.hero_id, x.localized_name, x.c, x.item_id, x.name
        FROM (
          SELECT
            ROW_NUMBER() OVER (PARTITION BY hero_id ORDER BY c DESC, name) AS r,
            t.*
          FROM
                (
                SELECT item_id, hero_id, localized_name, i.name, COUNT(item_id) as c
                from purchase_logs as p
                         JOIN (
                    SELECT n, hero_id, localized_name
                    from heroes as h
                             JOIN (
                        SELECT hero_id, id as n
                        FROM matches_players_details
                        WHERE match_id = %s
                          and (CASE
                                   WHEN
                                       (SELECT radiant_win
                                        FROM matches
                                        WHERE id = %s) is TRUE
                                       THEN (player_slot < 5)
                                   ELSE (player_slot > 127)
                            END)
                    ) as pl on hero_id = h.id
                ) as he on p.match_player_detail_id = he.n
                JOIN items i on i.id = p.item_id
                GROUP BY item_id, hero_id, localized_name, i.name
                ) as t
            ) x
        WHERE x.r <= 5
        ORDER BY x.hero_id, x.c DESC, x.name;

    ''', [match_id, match_id])

    dictionary = {"id": match_id, "heroes": []}

    row = crsr.fetchone()
    for i in range(5):
        hero = {"id": row[0], "name": row[1], "top_purchases": []}
        for j in range(5):
            purchase = {"id": row[3], "name": row[4], "count": row[2]}
            hero["top_purchases"].append(purchase)
            row = crsr.fetchone()

        dictionary["heroes"].append(hero)


    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_v3_usage(request, ability_id):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'),
                                  password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
        
    SELECT ability_id, name, hero_id, localized_name, c, TimeRange, Win
    FROM (
             SELECT *, ROW_NUMBER() OVER (PARTITION BY hero_id, Win ORDER BY c DESC) as rn
             FROM (
                      SELECT x.ability_id, x.name, x.hero_id, x.localized_name, count(*) as c, x.TimeRange, x.Win
                      FROM (
                               SELECT a.id    as ability_id,
                                      a.name,
                                      h.id    as hero_id,
                                      h.localized_name,
                                      case
                                          when au.time >= m.duration * 0 and au.time < m.duration * 0.10 then '0-9'
                                          when au.time >= m.duration * 0.10 and au.time < m.duration * 0.20 then '10-19'
                                          when au.time >= m.duration * 0.20 and au.time < m.duration * 0.30 then '20-29'
                                          when au.time >= m.duration * 0.30 and au.time < m.duration * 0.40 then '30-39'
                                          when au.time >= m.duration * 0.40 and au.time < m.duration * 0.50 then '40-49'
                                          when au.time >= m.duration * 0.50 and au.time < m.duration * 0.60 then '50-59'
                                          when au.time >= m.duration * 0.60 and au.time < m.duration * 0.70 then '60-69'
                                          when au.time >= m.duration * 0.70 and au.time < m.duration * 0.80 then '70-79'
                                          when au.time >= m.duration * 0.80 and au.time < m.duration * 0.90 then '80-89'
                                          when au.time >= m.duration * 0.90 and au.time < m.duration then '90-99'
                                          when au.time >= m.duration and au.time < m.duration * 1.1 then '100-109'
                                          when au.time >= m.duration * 1.1 then '110+'
                                          end as TimeRange,
                                      case
                                          when m.radiant_win = true and mpd.player_slot < 5 then 'Won'
                                          when m.radiant_win = true and mpd.player_slot > 127 then 'Lost'
                                          when m.radiant_win = false and mpd.player_slot > 127 then 'Won'
                                          when m.radiant_win = false and mpd.player_slot < 5 then 'Lost'
                                          end as Win
                               FROM matches_players_details as mpd
                                        JOIN ability_upgrades au on mpd.id = au.match_player_detail_id
                                        JOIN abilities a on au.ability_id = a.id
                                        JOIN matches m on mpd.match_id = m.id
                                        JOIN heroes h on mpd.hero_id = h.id
                               WHERE a.id = %s
                           ) as x
                      group by x.TimeRange, x.Win, x.ability_id, x.name, x.hero_id, x.localized_name
                      ORDER BY hero_id, Win, count(*) DESC
                  ) as xx
         ) as xxx
    where rn = 1;
    ''', [ability_id])

    row = crsr.fetchone()
    dictionary = {"id": ability_id, "name": row[1], "heroes": []}

    hero_id = row[2]
    hero = {"id": row[2], "name": row[3]}
    while row is not None:
        if row[2] != hero_id:
            dictionary["heroes"].append(hero)
            hero = {"id": row[2], "name": row[3]}

        if row[6] == "Won":
            hero["usage_winners"] = {"bucket": row[5], "count": row[4]}

        if row[6] == "Lost":
            hero["usage_loosers"] = {"bucket": row[5], "count": row[4]}

        row = crsr.fetchone()
    dictionary["heroes"].append(hero)

    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_v3_tower_kills(request):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'),
                                  password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
    SELECT hero_id,
           localized_name,
           Max(xxx.count)
    FROM   (SELECT Count(*),
                   hero_id,
                   localized_name
            FROM   (SELECT Sum(starts)
                             OVER (
                               ORDER BY match_id, time) AS island_id,
                           hero_id,
                           localized_name,
                           mpd_id,
                           match_id,
                           time
                    FROM   (SELECT CASE
                                     WHEN Lag(mpd.id)
                                            OVER (
                                              ORDER BY match_id, time) = mpd.id THEN
                                     0
                                     ELSE 1
                                   END    AS starts,
                                   hero_id,
                                   localized_name,
                                   mpd.id AS mpd_id,
                                   match_id,
                                   time
                            FROM   (SELECT go.match_player_detail_id_1,
                                           go.match_player_detail_id_2,
                                           go.time
                                    FROM   game_objectives AS go
                                    WHERE  go.subtype = 'CHAT_MESSAGE_TOWER_KILL')
                                   AS go
                                   INNER JOIN matches_players_details AS mpd
                                           ON mpd.id = go.match_player_detail_id_1
                                   INNER JOIN heroes AS h
                                           ON h.id = hero_id) AS bb) AS xx
            GROUP  BY island_id,
                      hero_id,
                      localized_name,
                      match_id) AS xxx
    GROUP  BY hero_id,
              localized_name
    ORDER  BY Max(xxx.count) DESC,
              localized_name;

    ''')

    dictionary = {"heroes": []}

    row = crsr.fetchone()
    while row is not None:
        dictionary["heroes"].append({"id": row[0], "name": row[1], "tower_kills": row[2]})

        row = crsr.fetchone()

    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(dictionary)))
