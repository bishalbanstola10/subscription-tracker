import { useForm } from "react-hook-form";
import type { LoginFormData } from "../types/types";
import useToastStore from "../store/useToastStore";
import { useMutation } from "@tanstack/react-query";
import { submitLoginForm } from "../api.client";
import { Link, useNavigate } from "react-router";
import { useAuthStore } from "../store/useAuthStore";
import GoogleLoginButton from "../components/googleLoginButton";
import { useState } from "react";
import Spinner from "../components/spinner";

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<LoginFormData>();
  const showToast = useToastStore((state) => state.showToast);
  const setAuthData = useAuthStore((state) => state.setAuthData);
  const navigate = useNavigate();
  const [isGoogleSignInPending, setIsGoogleSignInPending] =
    useState<boolean>(false);

  const mutation = useMutation({
    mutationFn: submitLoginForm,
    onSuccess: (data) => {
      setAuthData({
        token: data.token,
        userId: data.userId,
        email: data.email,
      });
      localStorage.setItem("token", data.token);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("email", data.email);
      showToast("Login Success!", "SUCCESS");
      reset();
      navigate("/");
    },
    onError: (error: any) => {
      showToast(
        error.message || "Sorry, we are having problems logging you.",
        "ERROR"
      );
      reset();
    },
  });

  const onSubmit = handleSubmit((data) => {
    mutation.mutate(data);
  });

  if (isGoogleSignInPending) return <Spinner />;

  return (
    <>
      <form
        className="md:w-[40%] w-[90%] flex flex-col gap-5 bg-gray-200 p-8 rounded-lg shadow-sm md:p-12 mx-auto"
        onSubmit={onSubmit}
      >
        <p className="font-semibold text-3xl text-center">Login</p>
        <label className="text-xl text-left font-bold text-gray-700 flex-1">
          Email
          <input
            type="email"
            className="border rounded w-full py-2 px-4 font-normal"
            {...register("email", { required: "Email is required" })}
          ></input>
        </label>
        {errors.email && (
          <span className="text-red-500">{errors.email.message}</span>
        )}
        <label className="text-xl text-left font-bold text-gray-700 flex-1">
          Password
          <input
            type="password"
            className="border rounded w-full py-2 px-4 font-normal"
            {...register("password", { required: "Password is required" })}
          ></input>
        </label>
        {errors.password && (
          <span className="text-red-500">{errors.password.message}</span>
        )}

        <button
          type="submit"
          disabled={mutation.isPending}
          className={`w-full py-2 font-bold rounded text-white ${
            mutation.isPending
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-500"
          }`}
        >
          Submit
        </button>
        <div className="mt-12">
          <p className="font-semibold text-2xl text-center">
            Sign In with Google
          </p>
          <GoogleLoginButton
            setIsGoogleSignInPending={setIsGoogleSignInPending}
          />
        </div>
        <div className="text-center mt-6">
          <span className="text-gray-700">Don't have an account?</span>{" "}
          <Link
            to="/register"
            className="text-blue-600 hover:underline font-semibold"
          >
            Register here
          </Link>
        </div>
      </form>
    </>
  );
};
export default Login;
