import { Link } from 'react-router-dom'
import './Navbar.css'

export default function Navbar(){
    return(
        <div className="navbar">
            <Link to="/"><h2>Cricket Tracker</h2></Link>
        </div>
    )
}