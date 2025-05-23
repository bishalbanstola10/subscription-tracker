import { Link } from "react-router";

const Hero = () => {
  return (
    <section className="text-center  py-12 px-8 bg-slate-300 lg:max-w-screen-xl mx-auto rounded-2xl shadow-md">
      <h1 className="text-3xl sm:text-6xl   font-bold mb-4">
        Track and Manage Your Subscriptions Easily
      </h1>
      <p className="text-lg lg:text-2xl text-gray-600">
        Stay on top of recurring payments. Never forget to cancel again.
      </p>
      <Link
        to={"/register"}
        className="block w-fit mx-auto my-4  text-lg px-8 py-4 bg-blue-300 rounded-md"
      >
        Join Now
      </Link>
    </section>
  );
};
export default Hero;
