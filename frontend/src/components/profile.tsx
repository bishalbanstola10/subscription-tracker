import { useNavigate } from "react-router";
import { useAuthStore } from "../store/useAuthStore";

const Profile = () => {
  const email = useAuthStore((state) => state.email);
  const resetAuthData = useAuthStore((state) => state.resetAuthData);
  const navigate = useNavigate();

  const onLogout = () => {
    resetAuthData();
    localStorage.clear();
    navigate("/login");
  };
  return (
    <div className="max-w-sm mx-auto mt-10 p-6 bg-white rounded-2xl shadow-lg text-center">
      <img
        src="./plus1.jpg"
        alt="User Avatar"
        className="w-24 h-24 rounded-full mx-auto mb-4 object-cover border-2 border-gray-300"
      />
      <p className="text-xl font-semibold mb-2">{email}</p>
      <button
        onClick={onLogout}
        className="mt-4 px-4 py-2 bg-red-500 text-white rounded-xl hover:bg-red-600 transition"
      >
        Logout
      </button>
    </div>
  );
};

export default Profile;
