import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router";
import Layout from "./layouts/layout";
import Home from "./pages/home";
import Toast from "./components/toast";
import Login from "./pages/login";
import Register from "./pages/register";
import AddSubscription from "./pages/createSubscriptions";
import { useEffect, useState } from "react";
import { useAuthStore } from "./store/useAuthStore";
import Spinner from "./components/spinner";
import EditSubscription from "./pages/editSubscription";
import Profile from "./components/profile";

const App = () => {
  const setAuthData = useAuthStore((state) => state.setAuthData);
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    const email = localStorage.getItem("email");

    if (token && userId && email) {
      setAuthData({ token, userId, email });
    }
    setAuthReady(true);
  }, [setAuthData]);

  if (!authReady) return <Spinner></Spinner>;

  return (
    <>
      <Router>
        <Toast />
        <Routes>
          <Route
            path="/"
            element={
              <Layout>
                <Home />
              </Layout>
            }
          />
          <Route
            path="/login"
            element={
              <Layout>
                <Login />
              </Layout>
            }
          />
          <Route
            path="/subscriptions/:id/edit"
            element={
              <Layout>
                <EditSubscription />
              </Layout>
            }
          />
          <Route
            path="/register"
            element={
              <Layout>
                <Register />
              </Layout>
            }
          />
          <Route
            path="/profile"
            element={
              <Layout>
                <Profile />
              </Layout>
            }
          />

          <Route
            path="/add"
            element={
              <Layout>
                <AddSubscription />
              </Layout>
            }
          />
        </Routes>
      </Router>
    </>
  );
};

export default App;
