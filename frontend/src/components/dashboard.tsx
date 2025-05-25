import { useSubscriptions } from "../hooks/useSubscriptions";
import SubscriptionCard from "./subscriptionCard";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteSubscription } from "../api.client";
import useToastStore from "../store/useToastStore";
import Spinner from "./spinner";
import { Link } from "react-router";

const Dashboard = () => {
  const { data, isLoading, isError, error } = useSubscriptions();
  const queryClient = useQueryClient();
  const showToast = useToastStore((state) => state.showToast);
  const deleteMutation = useMutation({
    mutationFn: deleteSubscription,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["subscriptions"] });
      showToast("Subscription deleted created.", "SUCCESS");
    },
    onError: () => {
      showToast("Failed to delete subscription.", "ERROR");
    },
  });

  if (isLoading) return <Spinner></Spinner>;
  if (isError) return <div>Error: {error.message}</div>;

  return (
    <div className="p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {data && data.length > 0 ? (
        data.map((sub) => (
          <SubscriptionCard
            key={sub.id}
            subscription={sub}
            onDelete={(id: string) => deleteMutation.mutate(id)}
            deleteDisabled={deleteMutation.isPending}
          />
        ))
      ) : (
        <div className="col-span-full flex flex-col items-center justify-center py-20">
          <p className="mb-4 text-gray-600">No subscriptions yet.</p>
          <Link
            to="/add"
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Add your first subscription
          </Link>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
