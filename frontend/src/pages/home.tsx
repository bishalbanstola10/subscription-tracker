import Hero from "../components/hero";
import { useAuthStore } from "../store/useAuthStore";
import InstructionCards from "../components/instructionCards";
import Dashboard from "../components/dashboard";

const Home = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  return (
    <main className="px-4 py-4 lg:max-w-screen-xl mx-auto">
      {isAuthenticated ? (
        <Dashboard />
      ) : (
        <>
          <Hero />
          <h2 className="text-center font-bold text-4xl mt-16">How it Works</h2>
          <InstructionCards />
        </>
      )}
    </main>
  );
};
export default Home;
