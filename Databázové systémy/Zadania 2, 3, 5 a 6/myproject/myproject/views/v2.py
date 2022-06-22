from rest_framework.response import Response
from rest_framework.decorators import api_view
import psycopg2
import json
import os


@api_view(['GET'])
def getData_V2_patches(request):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'),
                                  password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
    SELECT a.name, a.patch_start_date, a.patch_end_date, m.id, ROUND((m.duration::numeric/60), 2)
    FROM matches as m
    RIGHT JOIN
         (
             SELECT name,
                    extract(epoch from release_date)::int as patch_start_date,
                    lead(extract(epoch from release_date)::int) over (ORDER BY name) as patch_end_date
             FROM patches as p
         ) as a
    ON m.start_time >= a.patch_start_date and m.start_time <= coalesce(a.patch_end_date, extract(epoch from CURRENT_TIMESTAMP)::int)
    ORDER BY a.name, m.id;

    ''')

    pv = None
    dictionary = {"patches": []}
    patch = {"matches": []}

    row = crsr.fetchone()
    while row is not None:

        if row[0] != pv:
            if pv is not None:
                dictionary["patches"].append(patch)
            patch = {"patch_version": row[0], "patch_start_date": row[1], "patch_end_date": row[2], "matches": []}

        if row[4] is not None:
            patch["matches"].append({"match_id": row[3], "duration": float(row[4])})

        pv = row[0]

        row = crsr.fetchone()
    dictionary["patches"].append(patch)
    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_V2_game_exp(request, player_id):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'), password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
    SELECT a.id, coalesce(a.nick, 'unknown'), m.id, h.localized_name, ROUND((m.duration::numeric/60), 2), a.total_xp, a.level,
           CASE WHEN a.player_slot <= 4 and m.radiant_win = true THEN true
                WHEN a.player_slot >= 128  and m.radiant_win = false THEN true
                ELSE false
           END
    FROM heroes as h
    JOIN (
        SELECT p.id,
               p.nick,
               mpd.match_id,
               mpd.hero_id,
               mpd.player_slot,
               mpd.level,
               (coalesce(mpd.xp_hero, 0) + coalesce(mpd.xp_creep, 0) + coalesce(mpd.xp_other, 0) +
                coalesce(mpd.xp_roshan, 0)) as total_xp
        FROM matches_players_details mpd
        JOIN players p ON p.id = mpd.player_id
        WHERE player_id = %s
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    ORDER BY m.id;
    ''', [player_id])

    row = crsr.fetchone()
    player = None
    if row is not None:
        player = {"id": row[0], "player_nick": row[1], "matches": []}
    while row is not None:
        match = {"match_id": row[2], "hero_localized_name": row[3], "match_duration_minutes": float(row[4]), "experiences_gained": row[5], "level_gained": row[6], "winner": row[7]}
        player["matches"].append(match)
        row = crsr.fetchone()
    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(player)))


@api_view(['GET'])
def getData_V2_game_objectives(request, player_id):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'), password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
    SELECT a.id, coalesce(a.nick, 'unknown'), h.localized_name, m.id, coalesce(go.subtype,'NO_ACTION'),
           CASE WHEN count(go.subtype) = 0 THEN 1
                    ELSE count(go.subtype)
               END
    FROM heroes as h
    JOIN (
        SELECT p.id,
               p.nick,
               mpd.id as r,
               mpd.match_id,
               mpd.hero_id,
               mpd.player_slot,
               mpd.level
        FROM matches_players_details mpd
        JOIN players p ON p.id = mpd.player_id
        WHERE player_id = %s
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    LEFT JOIN game_objectives go on go.match_player_detail_id_1 = a.r
    GROUP BY a.id, a.nick, h.localized_name, m.id, go.subtype, m.id, go.subtype
    ORDER BY m.id;
    ''', [player_id])

    row = crsr.fetchone()

    player = None
    match = None
    if row is not None:
        player = {"id": row[0], "player_nick": row[1], "matches": []}
        match = {"match_id": row[3], "hero_localized_name": row[2], "actions": []}
        match_id = row[2]

    while row is not None:
        if row[2] == match_id:
            match["actions"].append({"hero_action": row[4], "count": row[5]})
        else:
            player["matches"].append(match)
            match = {"match_id": row[3], "hero_localized_name": row[2], "actions": []}
            match["actions"].append({"hero_action": row[4], "count": row[5]})
            match_id = row[2]
        row = crsr.fetchone()
    player["matches"].append(match)

    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(player)))


@api_view(['GET'])
def getData_V2_abilities(request, player_id):
    connection = psycopg2.connect(dbname="dota2", user=os.environ.get('USER_NAME'), password=os.environ.get('USER_PASSWORD'), host="147.175.150.216", port="5432")
    crsr = connection.cursor()

    crsr.execute('''
    SELECT a.id, coalesce(a.nick, 'unknown'), h.localized_name, m.id, abilities.name, count(abilities.name), max(au.level)
    FROM heroes as h
    JOIN (
        SELECT p.id,
               p.nick,
               mpd.id as r,
               mpd.match_id,
               mpd.hero_id,
               mpd.player_slot,
               mpd.level
        FROM matches_players_details mpd
        JOIN players p ON p.id = mpd.player_id
        WHERE player_id = %s
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    JOIN ability_upgrades au on au.match_player_detail_id = a.r
    JOIN abilities on abilities.id = au.ability_id
    GROUP BY a.id, a.nick, h.localized_name, m.id, abilities.name
    ORDER BY m.id;
    ''', [player_id])

    row = crsr.fetchone()

    player = None
    match = None
    if row is not None:
        player = {"id": row[0], "player_nick": row[1], "matches": []}
        match = {"match_id": row[3], "hero_localized_name": row[2], "abilities": []}
        match_id = row[2]

    while row is not None:
        if row[2] == match_id:
            match["abilities"].append({"ability_name": row[4], "count": row[5], "upgrade_level": row[6]})
        else:
            player["matches"].append(match)
            match = {"match_id": row[3], "hero_localized_name": row[2], "abilities": []}
            match["abilities"].append({"ability_name": row[4], "count": row[5], "upgrade_level": row[6]})
            match_id = row[2]
        row = crsr.fetchone()
    player["matches"].append(match)

    crsr.close()
    connection.close()
    return Response(json.loads(json.dumps(player)))
