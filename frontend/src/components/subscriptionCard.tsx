import { BiPencil, BiTrash } from "react-icons/bi";
import type { Subscription } from "../types/types";
import { useNavigate } from "react-router-dom";

interface Props {
  subscription: Subscription;
  onDelete: (id: string) => void;
  deleteDisabled?: boolean;
}

const SubscriptionCard = ({
  subscription,
  onDelete,
  deleteDisabled,
}: Props) => {
  const navigate = useNavigate();

  return (
    <div
      className="bg-white rounded-2xl shadow hover:shadow-lg transition-shadow duration-200 cursor-pointer p-4 border border-gray-200"
      onClick={() => navigate(`/subscriptions/${subscription.id}`)}
    >
      <div className="bg-blue-500 text-white rounded-t-xl px-4 py-3 mb-4 text-center">
        <h3 className="text-xl font-bold tracking-wide">
          {subscription.serviceName}
        </h3>
      </div>
      <div className="space-y-2 text-gray-700 text-sm">
        <p>
          <span className="font-semibold">Price:</span>{" "}
          <span className="text-blue-600 font-medium">
            ${subscription.price.toFixed(2)}
          </span>
        </p>
        <p>
          <span className="font-semibold">Cycle:</span>{" "}
          {subscription.recurrence}
        </p>
        <p>
          <span className="font-semibold">Start:</span>{" "}
          {new Date(subscription.renewalDate).toLocaleDateString()}
        </p>
      </div>

      {/* Buttons */}
      <div className="flex justify-end gap-2 mt-4">
        <button
          onClick={(e) => {
            e.stopPropagation();
            navigate(`/subscriptions/${subscription.id}/edit`, {
              state: { subscription },
            });
          }}
          className="flex items-center gap-1 px-3 py-1 text-sm bg-yellow-400 hover:bg-yellow-300 text-black font-medium rounded"
        >
          <BiPencil size={16} />
          Edit
        </button>
        <button
          disabled={deleteDisabled}
          onClick={(e) => {
            e.stopPropagation();
            onDelete?.(subscription.id);
          }}
          className="flex items-center gap-1 px-3 py-1 text-sm bg-red-500 hover:bg-red-400 text-white font-medium rounded"
        >
          <BiTrash size={16} />
          Delete
        </button>
      </div>
    </div>
  );
};

export default SubscriptionCard;
