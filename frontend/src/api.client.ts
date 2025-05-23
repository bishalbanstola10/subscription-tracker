import type {
  AddSubscriptionFormData,
  LoginFormData,
  Subscription,
} from "./types/types";
import api from "./api/api";

export const submitLoginForm = async (formData: LoginFormData) => {
  try {
    const response = await api.post("/auth/login", formData, {
      headers: {
        Accept: "application/json",
      },
    });
    return response.data;
  } catch (error: any) {
    if (error.response && error.response.status === 401) {
      throw new Error("Invalid email or password. Please try again.");
    }
    console.error("Error submitting form", error);
    throw new Error(error.message || "An unexpected error occurred.");
  }
};

export const submitAddSubscriptionForm = async (
  formData: AddSubscriptionFormData
) => {
  try {
    const response = await api.post("/api/subscriptions", formData, {
      headers: {
        Accept: "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("error submitting form", error);
    throw new Error("Error submitting form. Please try again.");
  }
};

export const submitRegisterForm = async (formData: LoginFormData) => {
  try {
    const response = await api.post("/auth/register", formData, {
      headers: {
        Accept: "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("error submitting form", error);
    throw new Error("Error submitting form. Please try again.");
  }
};

export const getAllSubscriptions = async (userId: string | null) => {
  try {
    const response = await api.get(`/api/subscriptions/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Error getting subscriptions", error);
    throw new Error("Error Getting Subcriptions");
  }
};
export const submitEditSubscriptionForm = async (formData: Subscription) => {
  try {
    const response = await api.put(
      `/api/subscriptions/${formData.id}`,
      formData
    );
    return response.data;
  } catch (error) {
    console.error("Error editing subscriptions", error);
    throw new Error("Error Editing Subcriptions");
  }
};

export const deleteSubscription = async (id: String) => {
  try {
    const response = await api.delete(`/api/subscriptions/${id}`);
    return response.data;
  } catch (error) {
    console.log("error deleting subscription", error);
    throw new Error("Error Deleting Subscription. Please try again later.");
  }
};

export const submitGoogleCredentials = async (token: any) => {
  try {
    if (!token) throw new Error("Login Failed. Please try again.");
    const response = await api.post("/auth/oauth/google", token, {
      headers: {
        Accept: "application/json",
      },
    });
    // const response = await axios.post(
    //   "http://localhost:4005/oauth/google",
    //   token,
    //   {
    //     headers: {
    //       Accept: "application/json",
    //     },
    //   }
    // );
    return response.data;
  } catch (error) {
    console.error("Error logging in through google", error);
    throw new Error("Login Failed. Please Try again.");
  }
};
