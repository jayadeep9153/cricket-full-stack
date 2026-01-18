package com.repository

import com.cricketinfo.entity.Score
import com.dto.ScoreDTO.AllTeamScoresDTO
import com.dto.ScoreDTO.AllTeamsWicketsDTO
import com.dto.ScoreDTO.BowlerStatsDTO
import com.dto.ScoreDTO.PlayerStatsDTO
import com.dto.ScoreDTO.TeamScoreDTO
import com.dto.ScoreDTO.TeamWicketsDTO
import io.micronaut.data.annotation.Join
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.MYSQL)
interface ScoreRepository extends CrudRepository<Score, Long>{
    @Join("match")
    @Join("match.team1")
    @Join("match.team2")
    @Join("match.tossWinner")
    @Join("match.matchWinner")
    @Join("match.potm")
    @Join("battingTeam")
    @Join("bowlingTeam")
    @Join("striker")
    @Join("nonStriker")
    @Join("bowler")
    @Join(value = "fielder", type = Join.Type.LEFT_FETCH)
    List<Score> findAll()

    static final FIND_ALL_TEAM_SCORES = """
        select 
            s.match_id, 
            t.id,
            t.name, 
            sum(s.runs_scored + s.extra_runs) as total_runs
        from score s
        join teams t on s.batting_team_id = t.id
        group by t.id, t.name, s.match_id;
    """

    static final FIND_TEAM_SCORES_QUERY = """
        select
            t.id, 
            t.name, 
            sum(s.runs_scored + s.extra_runs) as total_runs
        from score s
        join teams t on s.batting_team_id = t.id 
        where s.match_id = :matchId
        group by t.id, t.name;
    """

    static final FIND_ALL_TEAMS_WICKETS = """
        select
            s.match_id, 
            t.id,
            t.name, sum(s.is_wicket) as total_wickets
        from score s
        join teams t on s.bowling_team_id = t.id
        group by s.match_id, t.id, t.name;
    """

    static final FIND_TEAM_WICKETS_QUERY = """
        select 
            t.id, 
            t.name, 
            sum(s.is_wicket) as total_wickets
        from score s
        join teams t on s.bowling_team_id = t.id 
        where s.match_id = :matchId
        group by t.id, t.name;
    """

    static final PLAYER_MATCH_STATS = """
        SELECT
            p.id,
            p.name AS player_name,
            SUM(s.runs_scored) AS total_runs,
            COUNT(*) AS balls_played,
            SUM(CASE WHEN s.runs_scored = 4 AND s.extra_type NOT IN ('WIDE','BYE','LEGBYE') THEN 1 ELSE 0 END) AS fours,
            SUM(CASE WHEN s.runs_scored = 6 AND s.extra_type NOT IN ('WIDE','BYE','LEGBYE') THEN 1 ELSE 0 END) AS sixes,
            ROUND((SUM(s.runs_scored) * 100.0) / NULLIF(COUNT(*),0), 2) AS strike_rate
        FROM score s
        JOIN players p ON s.striker_id = p.id
        WHERE s.match_id = :matchId
        GROUP BY p.id, p.name
        ORDER BY p.id;
    """

    static final BOWLER_MATCH_STATS = """
        SELECT 
        p.id, 
        p.name,
        s.innings,
        CONCAT(
            FLOOR(SUM(CASE WHEN s.extra_type NOT IN ('WIDE','NOBALL') THEN 1 ELSE 0 END) / 6),
            '.',
            MOD(SUM(CASE WHEN s.extra_type NOT IN ('WIDE','NOBALL') THEN 1 ELSE 0 END), 6)
        ) AS overs_bowled,
        SUM(s.runs_scored + 
            case when s.extra_type in ('WIDE', 'NOBALL') then s.extra_runs else 0 end
        ) AS runs,
        SUM(
            CASE 
                WHEN s.is_wicket = 1 
                AND s.wicket_type IN ('BOWLED','CAUGHT_OUT','LBW','STUMPED','HIT_WICKET')
                THEN 1 ELSE 0
            END
        ) AS wickets,
        ROUND(
            sum(s.runs_scored + 
                case when s.extra_type in ('WIDE', 'NOBALL') then s.extra_runs else 0 end
            ) * 6.0 /
            NULLIF(sum(case when s.extra_type not in ('WIDE', 'NOBALL') then 1 else 0 end), 0)
            , 2) as economy
        FROM score s 
        JOIN players p ON s.bowler_id = p.id
        WHERE s.match_id = :matchId
        GROUP BY p.id, p.name, s.innings
        ORDER by s.innings, p.id;
    """

    @Query(value = FIND_ALL_TEAM_SCORES, nativeQuery = true)
    List<AllTeamScoresDTO> findAllTeamsScores()

    @Query(value = FIND_TEAM_SCORES_QUERY, nativeQuery = true)
    List<TeamScoreDTO> findTeamScoreByMatchId(int matchId)

    @Query(value = FIND_ALL_TEAMS_WICKETS, nativeQuery = true)
    List<AllTeamsWicketsDTO> findAllTeamsWickets()

    @Query(value = FIND_TEAM_WICKETS_QUERY, nativeQuery = true)
    List<TeamWicketsDTO> findTeamWicketsByMatch(int matchId)

    @Query(value = PLAYER_MATCH_STATS, nativeQuery = true)
    List<PlayerStatsDTO> findPlayerStatsByMatch(int matchId)

    @Query(value = BOWLER_MATCH_STATS, nativeQuery = true)
    List<BowlerStatsDTO> findBowlerStatsByMatch(int matchId)
}