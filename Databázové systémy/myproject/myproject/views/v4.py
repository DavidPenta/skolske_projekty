from rest_framework.response import Response
from rest_framework.decorators import api_view
import json
import os
from sqlalchemy import func
import sqlalchemy as db
from sqlalchemy.orm import sessionmaker
from myproject.views.models import Hero, MatchesPlayersDetail, Player, Match, Patch, GameObjective, AbilityUpgrade, Ability, PurchaseLog, Item


@api_view(['GET'])
def getData_V4_patches(request):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()

    patches = session.query(Patch).order_by(Patch.release_date)
    dictionary = {"patches": []}

    dates = []
    for p in patches:
        dates.append(int(round(p.release_date.timestamp())) + 3600)

    dates.append(None)
    for i in range(patches.count()):
        patch = {"patch_version": patches[i].name, "patch_start_date": dates[i], "patch_end_date": dates[i+1], "matches": []}
        dictionary["patches"].append(patch)

    matches = session.query(Match)

    for match in matches:
        for d in dictionary["patches"]:
            if d["patch_start_date"] < match.start_time < d["patch_end_date"]:
                d["matches"].append({"match_id": match.id, "duration": float(round(match.duration / 60, 2))})

    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_V4_game_exp(request, player_id):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()

    player = session.query(Player).filter_by(id=player_id).first()
    games = session.query(MatchesPlayersDetail).filter_by(player_id=player_id).order_by(MatchesPlayersDetail.match_id.asc())
    dictionary = {"id": player_id, "player_nick": player.nick, "matches": []}

    for g in games:
        m = session.query(Match).filter_by(id=g.match_id).first()

        if g.player_slot <= 4 and m.radiant_win == True:
            b = True
        elif g.player_slot >= 128 and m.radiant_win == False:
            b = True
        else:
            b = False

        hero = session.query(Hero).filter_by(id=g.hero_id).first()

        xp = int(g.xp_hero or 0) + int(g.xp_creep or 0) + int(g.xp_other or 0) + int(g.xp_roshan or 0)

        match = {"match_id": g.match_id, "hero_localized_name": hero.localized_name, "match_duration_minutes": float(round(m.duration / 60, 2)), "experiences_gained": xp, "level_gained": g.level, "winner": b}
        dictionary["matches"].append(match)

    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_V4_game_objectives(request, player_id):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()

    player = session.query(Player).filter_by(id=player_id).first()
    dictionary = {"id": player_id, "player_nick": player.nick, "matches": []}

    mpd = session.query(MatchesPlayersDetail).filter_by(player_id=player_id).order_by(MatchesPlayersDetail.match_id.asc())

    for g in mpd:
        hero = session.query(Hero).filter_by(id=g.hero_id).first()
        action = session.query(GameObjective).filter_by(match_player_detail_id_1=g.id)

        d = {}
        for a in action:
            if a.subtype in d:
                d[a.subtype] += 1
            else:
                d[a.subtype] = 1

        if d == {}:
            d = {"NO_ACTION": 1}

        match = {"match_id": g.match_id, "hero_localized_name": hero.localized_name, "actions": []}

        od = sorted(d)

        for k in od:
            a = {"hero_action": k, "count": d[k]}
            match["actions"].append(a)
        dictionary["matches"].append(match)

    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_V4_abilities(request, player_id):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()

    player = session.query(Player).filter_by(id=player_id).first()

    nick = player.nick
    if nick is None:
        nick = 'unknown'

    dictionary = {"id": player_id, "player_nick": nick, "matches": []}

    mpd = session.query(MatchesPlayersDetail).filter_by(player_id=player_id).order_by(MatchesPlayersDetail.match_id.asc())

    for g in mpd:
        hero = session.query(Hero).filter_by(id=g.hero_id).first()
        au = session.query(AbilityUpgrade).filter_by(match_player_detail_id=g.id)

        match = {"match_id": g.match_id, "hero_localized_name": hero.localized_name, "abilities": []}

        d = {}
        for a in au:
            if a.ability_id in d:
                d[a.ability_id][0] += 1
                if a.level > d[a.ability_id][1]:
                    d[a.ability_id][1] = a.level
            else:
                d[a.ability_id] = [1, a.level]

        od = sorted(d)

        for k in od:
            ab = session.query(Ability).filter_by(id=k).first()
            a = {"ability_name": ab.name, "count": d[k][0], "upgrade_level": d[k][1]}
            match["abilities"].append(a)

        dictionary["matches"].append(match)
    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_v4_top_purchases(request, match_id):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()

    dictionary = {"id": match_id, "heroes": []}
    match = session.query(Match).filter_by(id=match_id).first()
    rad_win = match.radiant_win

    mpd = session.query(MatchesPlayersDetail).filter_by(match_id=match_id)

    players = []
    for player in mpd:
        if (rad_win and player.player_slot < 5) or (rad_win == False and player.player_slot > 127):
            players.append(player)

    players = sorted(players, key=lambda x: x.hero_id, reverse=False)

    for player in players:
        hero = session.query(Hero).filter_by(id=player.hero_id).first()
        p = {"id": player.hero_id, "name": hero.localized_name, "top_purchases": []}

        items = session.query(PurchaseLog.item_id, PurchaseLog.match_player_detail_id, Item.name, func.count(PurchaseLog.item_id)).filter_by(match_player_detail_id=player.id).join(Item, PurchaseLog.item_id == Item.id).group_by(PurchaseLog.item_id, Item.name, PurchaseLog.match_player_detail_id).order_by(func.count(PurchaseLog.item_id).desc(), Item.name).limit(5)

        for i in items:
            p["top_purchases"].append({"id": i[0], "name": i[2], "count": i[3]})

        dictionary["heroes"].append(p)

    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_v4_usage(request, ability_id):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()
    dictionary = {}
    session.close()
    return Response(json.loads(json.dumps(dictionary)))


@api_view(['GET'])
def getData_v4_tower_kills(request):
    engine = db.create_engine('postgresql+psycopg2://' + os.environ.get('USER_NAME') + ':' + os.environ.get('USER_PASSWORD') + '@147.175.150.216/dota2')
    Session = sessionmaker(bind=engine)
    Session.configure(bind=engine)
    session = Session()
    dictionary = {}
    session.close()
    return Response(json.loads(json.dumps(dictionary)))
