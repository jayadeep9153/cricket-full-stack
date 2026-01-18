import { useState, useEffect } from 'react'
import './App.css'
import MatchCard from './matches/MatchCard.jsx'
import Navbar from './Navbar/Navbar.jsx';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import MatchSummary from './Match Summary/MatchSummary.jsx';
import Player from './Players/Player.jsx';

export default function App() {
  const [matches, setMatches] = useState([]);
  const [scores, setScores] = useState([]);
  const [wickets, setWickets] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/matches")
    .then(res => res.json())
    .then(data => setMatches(data))
    .catch(err => console.error("Error fetching matches: ", err));

    fetch(`http://localhost:8080/matches/scores`)
    .then(res => res.json())
    .then(data => {
        console.log("Score data: ", data)
        setScores(data)
    })
    .catch(err => console.error("Error displaying score: ", err));

    fetch(`http://localhost:8080/matches/wickets`)
    .then((res) => res.json())
    .then((data) => {
        console.log("Team wickets data: ", data)
        setWickets(data)
    })
    .catch(err => console.error("Error fetching wickets data: ", err));
  }, [])

  const router = createBrowserRouter([
    {
      path: '/',
      element: (
        <>
          <Navbar />
          <MatchCard matches={matches} scores={scores} wickets={wickets}/>
        </>
      )
    },
    {
      path: '/match/:id',
      element:
      <>
          <Navbar />
          <MatchSummary scores={scores} wickets={wickets}/>
        </>
    },
    {
      path: '/match/:matchId/player/:id',
      element:
      <>
          <Navbar />
          <Player />
        </>
    }
  ])

  return(
    <div>
      <RouterProvider router={router} /> 
      {/* <TestTailwind /> */}
    </div>
  )
}
