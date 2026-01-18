import { useNavigate } from "react-router-dom"

export default function BattingTable({players, stats, teamName, matchId}){
    const navigate = useNavigate();

    const thClass = "px-4 py-3 text-center font-bold uppercase tracking-wider"
    const tdClass = "px-4 py-4 text-center"
    
    return(
        <table className="w-full">
            <thead className="bg-gray-100">
                <tr>
                    <th className="px-6 py-3 text-left font-bold uppercase tracking-wide">Batting</th>
                    <th className={thClass}>Runs</th>
                    <th className={thClass}>Balls</th>
                    <th className={thClass}>4s</th>
                    <th className={thClass}>6s</th>
                    <th className={thClass}>SR</th>
                </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
                {players.length > 0 ? (
                    players.map((p, index) => {
                        const playerStat = stats.find(stat => stat.playerName === p.player.name)

                        return (
                            <tr key={index} className="hover:bg-gray-50 cursor-pointer" onClick={() => navigate(`/match/${matchId}/player/${p.player.id}`)}>
                                <td className="px-6 py-4 text-left">{p.player.name}</td>
                                <td className={tdClass}>{playerStat?.totalRuns ?? 0}</td>
                                <td className={tdClass}>{playerStat?.ballsPlayed ?? 0}</td>
                                <td className={tdClass}>{playerStat?.fours ?? 0}</td>
                                <td className={tdClass}>{playerStat?.sixes ?? 0}</td>
                                <td className={tdClass}>{playerStat?.strikeRate ?? 0}</td>
                            </tr>
                        )
                    })
                ) : (
                    <tr>
                        <td colSpan="6" className="px-6 py-4 text-lg text-center">No players found for {teamName}</td>
                    </tr>
                )}
            </tbody>
        </table>
    )
}