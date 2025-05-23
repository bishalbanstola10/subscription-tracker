import { useSubscriptions } from "../hooks/useSubscriptions";
import SubscriptionCard from "./subscriptionCard";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteSubscription } from "../api.client";
import useToastStore from "../store/useToastStore";

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

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error: {error.message}</div>;

  return (
    <div className="p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {data &&
        data.map((sub) => (
          <SubscriptionCard
            key={sub.id}
            subscription={sub}
            onDelete={(id: string) => deleteMutation.mutate(id)}
            deleteDisabled={deleteMutation.isPending}
          />
        ))}
    </div>
  );
};

export default Dashboard;
