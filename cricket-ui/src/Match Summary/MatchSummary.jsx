import { useEffect, useState } from "react"
import { useParams } from "react-router-dom";
import BattingTable from "../Table/BattingTable";
import BowlingTable from "../Table/BowlingTable";
import { Link } from "react-router-dom";

export default function MatchSummary({scores, wickets}){
    const { id } = useParams();
    const [match, setMatch] = useState(null);
    const [teamPlayer, setTeamPlayer] = useState([]);
    const [stats, setStats] = useState([]);
    const [bowlerStats, setBowlerStats] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:8080/matches/${id}`)
        .then((res) => res.json())
        .then((data) => setMatch(data))
        .catch((err) => console.error("Error fetching match summary: ", err));

         fetch(`http://localhost:8080/team-players/match/${id}`)
        .then((res) => res.json())
        .then((data) => {
            console.log("Team player data: ", data);
            setTeamPlayer(data)
        })
        .catch((err) => console.error("Error fetching team players: ", err))

        fetch(`http://localhost:8080/matches/player-stats/${id}`)
        .then((res) => res.json())
        .then((data) => {
            console.log("Player stats data: ", data)
            setStats(data)
        })
        .catch(err => console.error("Error fetching player stats: ", err))

        fetch(`http://localhost:8080/matches/bowler-stats/${id}`)
        .then((res) => res.json())
        .then((data) => {
            console.log("Bowler stats data: ", data)
            setBowlerStats(data)
        })
        .catch(err => console.error("Error fetching bowler stats: ", err))
    }, [id])

    if (!match) {
        return <h3>Loading match summary...</h3>
    }
    
    const team1Score = scores.find(s => s.matchId === match.id && s.name === match.team1.name);
    const team2Score = scores.find(s => s.matchId === match.id && s.name === match.team2.name);

    const team1Wickets = wickets.find(w => w.matchId === match.id && w.name === match.team1.name);
    const team2Wickets = wickets.find(w => w.matchId === match.id && w.name === match.team2.name);

    const team1Players = teamPlayer.filter(p => p.team.id === match.team1.id)
    const team2Players = teamPlayer.filter(p => p.team.id === match.team2.id)

    const getPotmTeam = () => {
        if(team1Players.some(p => p.player.id === match.potm.id))   return match.team1.name;
        if(team2Players.some(p => p.player.id === match.potm.id))   return match.team2.name;
        return match.team1.name;
    }
    const potmTeam = getPotmTeam();

    const potmBatting = stats.find(stat => stat.playerName === match.potm.name);
    const potmBowling = bowlerStats.find(stat => stat.name === match.potm.name);

    const innings1Bowling = bowlerStats.filter(stat => stat.innings === 1);
    const innings2Bowling = bowlerStats.filter(stat => stat.innings === 2);

    return(
        <div className="max-w-6xl mx-auto p-4 bg-gray-50 min-h-screen">
            <div className="flex w-full justify-between bg-white rounded-lg shadow-md mb-6 overflow-hidden">
                <div key={match.id} className="w-[70%] p-6 border-r border-gray-200">
                    <div className="flex justify-between mb-6 pb-4 border-b border-gray-100">
                        <h3 className="text-xl font-bold text-gray-800">{match.team1.name}</h3>
                        <h3 className="text-xl font-bold"> {team1Score?.totalRuns ?? 0} / {team1Wickets?.totalWickets ?? 0} </h3>
                    </div>
                    <div className="flex justify-between">
                        <h3 className="text-xl font-bold">{match.team2.name}</h3>
                        <h3 className="text-xl font-bold"> {team2Score?.totalRuns ?? 0} / {team2Wickets?.totalWickets ?? 0} </h3>
                    </div>
                </div>
                <div className="w-[30%] p-4 space-y-2">
                    <h3 className="text-sm font-semibold text-gray-600 uppercase tracking-wide">Player of the Match</h3>
                    <Link to={`/match/${match.id}/player/${match.potm.id}`}>
                        <h4 className="text-lg font-bold text-gray-900">
                            {match.potm.name}, {potmTeam}{" "}
                        </h4>
                    </Link>
                    <h4 className="text-lg font-bold pt-2">
                        {potmBatting
                            ? `${potmBatting.totalRuns}(${potmBatting.ballsPlayed})`
                            : potmBowling
                            ? `${potmBowling.wickets}/${potmBowling.runs} (${potmBowling.oversBowled})`
                            : "No stats available"}
                    </h4>
                </div>
            </div>
            <div className="first-innings mb-6 bg-white rounded-lg shadow-md overflow-hidden">
                <div className="bg-blue-600 px-6 py-3">
                    <h3 className="text-xl font-bold text-white">{match.team1.name}</h3>
                </div>
                <div className="overflow-x-auto">
                    <BattingTable players={team1Players} stats={stats} teamName={match.team1.name}
                    matchId={match.id}/>
                </div>
                <div className="bowling mt-4">
                    <div className="bg-blue-50 px-6 py-3">
                        <h4 className="text-lg font-semibold text-gray-800">Bowling</h4>
                    </div>
                    <div className="overflow-x-auto">
                        <BowlingTable bowlers={innings1Bowling} teamPlayer={teamPlayer} matchId={match.id}/>
                    </div>
                </div>
            </div>

            <div className="second-innings bg-white rounded-lg shadow-md overflow-hidden">
                <div className="bg-green-600 px-6 py-3">
                    <h3 className="text-xl font-bold text-white">{match.team2.name}</h3>
                </div>
                <div className="overflow-x-auto">
                    <BattingTable players={team2Players} stats={stats} teamName={match.team2.name}
                    matchId={match.id}/>
                </div>
                <div className="bowling mt-4">
                    <div className="bg-green-50 px-6 py-3">
                        <h4 className="text-lg font-semibold text-gray-800">Bowling</h4>
                    </div>
                    <div className="overflow-x-auto">
                        <BowlingTable bowlers={innings2Bowling} teamPlayer={teamPlayer} matchId={match.id}/>
                    </div>
                </div>
            </div>
        </div>
    )
}