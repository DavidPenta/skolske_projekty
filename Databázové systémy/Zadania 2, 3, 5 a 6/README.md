#### /v2/patches

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

#### /v2/players/{player_id}/game_exp

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
        WHERE player_id = 14944
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    ORDER BY m.id;

#### /v2/players/{player_id}/game_objectives

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
        WHERE player_id = 14944
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    LEFT JOIN game_objectives go on go.match_player_detail_id_1 = a.r
    GROUP BY a.id, a.nick, h.localized_name, m.id, go.subtype, m.id, go.subtype
    ORDER BY m.id;

#### /v2/players/{player_id}/abilities

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
        WHERE player_id = 14944
    ) as a
        ON h.id = a.hero_id
    JOIN matches m on a.match_id = m.id
    JOIN ability_upgrades au on au.match_player_detail_id = a.r
    JOIN abilities on abilities.id = au.ability_id
    GROUP BY a.id, a.nick, h.localized_name, m.id, abilities.name
    ORDER BY m.id;

#### v3/matches/{match_id}/top_purchases/

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
                    WHERE match_id = 21421
                      and (CASE
                               WHEN
                                   (SELECT radiant_win
                                    FROM matches
                                    WHERE id = 21421) is TRUE
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

#### v3/abilities/{ability_id}/usage/

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

#### v3/statistics/tower_kills/

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
