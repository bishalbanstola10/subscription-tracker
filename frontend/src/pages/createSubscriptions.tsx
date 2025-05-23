import { useForm } from "react-hook-form";
import type { AddSubscriptionFormData } from "../types/types";
import useToastStore from "../store/useToastStore";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { submitAddSubscriptionForm } from "../api.client";
import { useAuthStore } from "../store/useAuthStore";

const AddSubscription = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<AddSubscriptionFormData>();
  const showToast = useToastStore((state) => state.showToast);
  const userId = useAuthStore((state) => state.userId);
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: submitAddSubscriptionForm,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["subscriptions"] });
      showToast("Subscription successfully created.", "SUCCESS");
      reset();
    },
    onError: () => {
      showToast("Failed to create subscription.", "ERROR");
    },
  });

  const onSubmit = handleSubmit((data) => {
    console.log({ ...data, userId });
    mutation.mutate({ ...data, userId }); //userId required to submit the form
  });

  return (
    <form
      onSubmit={onSubmit}
      className="md:w-[40%] w-[90%] flex flex-col gap-5 bg-gray-200 p-8 md:p-16 mx-auto rounded-lg shadow-lg"
    >
      <p className="font-semibold text-3xl text-center">
        Add a new Subscription
      </p>

      <label className="text-xl text-left font-bold text-gray-700">
        Service Name
        <input
          type="text"
          className="border rounded w-full py-2 px-4 font-normal"
          {...register("serviceName", { required: "Service name is required" })}
        />
      </label>
      {errors.serviceName && (
        <span className="text-red-500">{errors.serviceName.message}</span>
      )}

      <label className="text-xl text-left font-bold text-gray-700">
        Price
        <input
          type="number"
          step="0.01"
          className="border rounded w-full py-2 px-4 font-normal"
          {...register("price", {
            required: "Price is required",
            min: {
              value: 0,
              message: "Price must be a positive number",
            },
          })}
        />
      </label>
      {errors.price && (
        <span className="text-red-500">{errors.price.message}</span>
      )}

      <label className="text-xl text-left font-bold text-gray-700">
        Renewal Date
        <input
          type="date"
          className="border rounded w-full py-2 px-4 font-normal"
          {...register("renewalDate", { required: "Renewal date is required" })}
        />
      </label>
      {errors.renewalDate && (
        <span className="text-red-500">{errors.renewalDate.message}</span>
      )}

      <label className="text-xl text-left font-bold text-gray-700">
        Recurrence
        <select
          className="border rounded w-full py-2 px-4 font-normal"
          {...register("recurrence", { required: "Recurrence is required" })}
        >
          <option value="">Select recurrence</option>
          <option value="DAILY">Daily</option>
          <option value="WEEKLY">Weekly</option>
          <option value="MONTHLY">Monthly</option>
          <option value="YEARLY">Yearly</option>
        </select>
      </label>
      {errors.recurrence && (
        <span className="text-red-500">{errors.recurrence.message}</span>
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
        Create Subscription
      </button>
    </form>
  );
};

export default AddSubscription;
