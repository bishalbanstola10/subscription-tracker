import { NavLink } from "react-router-dom"; // Make sure you're importing from 'react-router-dom'
import { useAuthStore } from "../store/useAuthStore";

const Header = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="text-3xl font-sans font-bold">Subscription Tracker</h1>
      <div className="flex justify-between my-5 p-2 space-x-10 md:space-x-16 font-mono text-2xl  px-10">
        <NavLink
          to="/"
          className={({ isActive }) =>
            isActive
              ? "font-bold text-blue-500 border-b-2 border-blue-500"
              : "text-black-400 bg-white"
          }
        >
          Home
        </NavLink>
        {!isAuthenticated ? (
          <NavLink
            to="/add"
            className={({ isActive }) =>
              isActive
                ? "font-bold text-blue-500 border-b-2 border-blue-500"
                : "text-black-400 bg-white"
            }
          >
            Add
          </NavLink>
        ) : (
          <NavLink
            to="/login"
            className={({ isActive }) =>
              isActive
                ? "font-bold text-blue-500 border-b-2 border-blue-500"
                : "text-black-400 bg-white"
            }
          >
            Login
          </NavLink>
        )}
        {!isAuthenticated ? (
          <NavLink
            to="/register"
            className={({ isActive }) =>
              isActive
                ? "font-bold text-blue-500 border-b-2 border-blue-500"
                : "text-black-400 bg-white"
            }
          >
            Register
          </NavLink>
        ) : (
          <NavLink
            to="/profile"
            className={({ isActive }) =>
              isActive
                ? "font-bold text-blue-500 border-b-2 border-blue-500"
                : "text-black-400 bg-white"
            }
          >
            Profile
          </NavLink>
        )}
      </div>
    </div>
  );
};
export default Header;
