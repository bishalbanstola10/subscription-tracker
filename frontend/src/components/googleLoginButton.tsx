import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import useToastStore from "../store/useToastStore";
import { useMutation } from "@tanstack/react-query";
import { submitGoogleCredentials } from "../api.client";
interface Props {
  setIsGoogleSignInPending: (isGoogleSignInPending: boolean) => void;
}
const GoogleLoginButton = ({ setIsGoogleSignInPending }: Props) => {
  const showToast = useToastStore((state) => state.showToast);
  const setAuthData = useAuthStore((state) => state.setAuthData);
  const navigate = useNavigate();
  const mutation = useMutation({
    mutationFn: submitGoogleCredentials,
    onSuccess: (data) => {
      setIsGoogleSignInPending(false);
      setAuthData({
        token: data.token,
        userId: data.userId,
        email: data.email,
      });
      localStorage.setItem("token", data.token);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("email", data.email);
      showToast("Login Success!", "SUCCESS");
      navigate("/");
    },
    onError: (error: any) => {
      setIsGoogleSignInPending(false);
      showToast(
        error.message || "Sorry, we are having problems logging you.",
        "ERROR"
      );
    },
  });

  return (
    <div className="flex justify-center mt-4">
      <GoogleLogin
        width={300}
        onSuccess={(response) => {
          setIsGoogleSignInPending(true);
          !mutation.isPending && mutation.mutate(response);
        }}
        onError={() => {
          showToast("Google Login Failed", "ERROR");
          setIsGoogleSignInPending(false);
        }}
      />
    </div>
  );
};

export default GoogleLoginButton;
