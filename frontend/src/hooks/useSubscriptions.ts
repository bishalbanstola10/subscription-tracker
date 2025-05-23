import { useQuery } from "@tanstack/react-query";
import type { Subscription } from "../types/types";
import { getAllSubscriptions } from "../api.client";
import { useAuthStore } from "../store/useAuthStore";

export const useSubscriptions = () => {
  const userId = useAuthStore((state) => state.userId);
  return useQuery<Subscription[], Error>({
    queryKey: ["subscriptions"],
    queryFn: () => getAllSubscriptions(userId),
    staleTime: 1000 * 60 * 5,
  });
};
