import { Link } from "react-router-dom";

export default function MatchCard({ matches, scores, wickets }) {
    if (!matches) return <h3>Unable to fetch matches.</h3>;
    if (matches.length === 0) return <h3>No matches available.</h3>;

    if (!Array.isArray(scores) || !Array.isArray(wickets)) {
        return <h3>Loading scores and wickets...</h3>;
    }

    return (
        <div className="min-h-screen pt-25 bg-[#f4f6f8]">
            <div className="grid grid-cols-4 gap-10 justify-items-center
                max-lg:grid-cols-2 max-sm:grid-cols-1">
                
                {matches.map((match) => (
                    <Link key={match.id} to={`/match/${match.id}`}>
                        <div className="bg-white rounded-xl w-[280px] p-9 shadow-md hover:shadow-lg transition">
                            <div className="flex justify-between">
                                <h3 className="font-semibold text-lg">{match.team1.name}</h3>
                                <h3 className="font-semibold text-lg">
                                    {scores.find(s => s.matchId === match.id && s.name === match.team1.name)?.totalRuns ?? 0}
                                    {" / "}
                                    {wickets.find(w => w.matchId === match.id && w.name === match.team1.name)?.totalWickets ?? 0}
                                </h3>
                            </div>

                            <div className="flex justify-between mt-4">
                                <h3 className="font-semibold text-lg">{match.team2.name}</h3>
                                <h3 className="font-semibold text-lg">
                                    {scores.find(s => s.matchId === match.id && s.name === match.team2.name)?.totalRuns ?? 0}
                                    {" / "}
                                    {wickets.find(w => w.matchId === match.id && w.name === match.team2.name)?.totalWickets ?? 0}
                                </h3>
                            </div>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
}