import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

export default function Player() {
  const { id } = useParams();
  const [player, setPlayer] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8080/players/${id}`)
      .then(res => res.json())
      .then(data => setPlayer(data))
      .catch(err => console.error("Error fetching players:", err));
  }, [id]);

  if (!player) return <h3 className="text-center text-lg font-semibold mt-10">Loading Player Data...</h3>;

  return (
    <div className="flex justify-center mt-25">
        <div className="bg-white shadow-lg rounded-xl p-8 w-96 border border-gray-200">
            <h1 className="text-center text-2xl font-bold mb-6 text-blue-600">{player.name}</h1>
            <div className="w-80 p-5">
                <div className="flex justify-between space-y-5 text-lg font-semibold">
                    <p className="">Age</p>
                    <p className="w-12">{player.age}</p>
                </div>
                <div className="flex justify-between text-lg font-semibold">
                    <p className="">Role</p>
                    <p className="text-center">{player.role}</p>
                </div>
            </div>
        </div>
    </div>
  );
}
