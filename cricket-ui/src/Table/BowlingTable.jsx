import { useNavigate } from "react-router-dom";

export default function BowlingTable({bowlers, teamPlayer, matchId}){
    const navigate = useNavigate();

    const thClass = "px-4 py-3 text-center font-bold uppercase tracking-wider"
    const tdClass = "px-6 py-4 text-center"

    return(
        <table className="w-full">
            <thead className="bg-gray-100">
                <tr>
                    <th className="px-6 py-3 text-left font-bold uppercase tracking-wide">Bowling</th>
                    <th className={thClass}>Overs</th>
                    <th className={thClass}>Runs</th>
                    <th className={thClass}>Wickets</th>
                    <th className={thClass}>Economy</th>
                </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
                {bowlers.length > 0 ? (
                    bowlers.map((b, index) => {
                    const bowler = teamPlayer.find(p => p.player.name === b.name);

                    return (
                        <tr key={index}className="hover:bg-gray-50 cursor-pointer"
                        onClick={() => bowler && navigate(`/match/${matchId}/player/${bowler.player.id}`)}
                        >
                            <td className="px-6 py-4 text-left">{b.name}</td>
                            <td className={tdClass}>{b.oversBowled}</td>
                            <td className={tdClass}>{b.runs}</td>
                            <td className={tdClass}>{b.wickets}</td>
                            <td className={tdClass}>{b.economy}</td>
                        </tr>
                    );
                    })
                ) : (
                    <tr>
                        <td colSpan="5" className="px-6 py-4 text-lg text-center">No bowling data available</td>
                    </tr>
                )}
            </tbody>
        </table>
    )
}