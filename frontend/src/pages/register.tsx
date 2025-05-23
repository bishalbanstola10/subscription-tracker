import { useForm } from "react-hook-form";
import type { LoginFormData } from "../types/types";
import useToastStore from "../store/useToastStore";
import { useMutation } from "@tanstack/react-query";
import { submitRegisterForm } from "../api.client";
import { Link, useNavigate } from "react-router";
import GoogleLoginButton from "../components/googleLoginButton";
import Spinner from "../components/spinner";
import { useState } from "react";

const Register = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<LoginFormData>();
  const showToast = useToastStore((state) => state.showToast);
  const navigate = useNavigate();
  const [isGoogleSignInPending, setIsGoogleSignInPending] =
    useState<boolean>(false);

  const mutation = useMutation({
    mutationFn: submitRegisterForm,
    onSuccess: () => {
      showToast("Account created. You can now log in.", "SUCCESS");
      reset();
      navigate("/login");
    },
    onError: () => {
      showToast("Sorry, we are having problems registering you.", "ERROR");
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
        <p className="font-semibold text-3xl text-center">Register</p>
        <label className="text-xl text-left font-bold text-gray-700 flex-1">
          Email
          <input
            type="email"
            className="border rounded w-full py-2 px-4 font-normal"
            {...register("email", {
              required: "Email is required",
              pattern: {
                value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                message: "Please enter a valid email address",
              },
            })}
          ></input>
        </label>
        {errors.email && (
          <span className="text-red-500">{errors.email.message}</span>
        )}
        <label className="text-xl text-left font-bold text-gray-700 flex-1">
          Password
          <input
            type="text"
            className="border rounded w-full py-2 px-4 font-normal"
            {...register("password", {
              required: "Password is required",
              minLength: {
                value: 6,
                message: "Password must be greater than 6 characters.",
              },
            })}
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
          Register
        </button>
        <div className="mt-12">
          <p className="font-semibold text-3xl text-center">
            Sign In with Google
          </p>
          <GoogleLoginButton
            setIsGoogleSignInPending={setIsGoogleSignInPending}
          />
        </div>
        <div className="text-center mt-6">
          <span className="text-gray-700">Already Have an Account?</span>{" "}
          <Link
            to="/login"
            className="text-blue-600 hover:underline font-semibold"
          >
            Login here
          </Link>
        </div>
      </form>
    </>
  );
};
export default Register;
