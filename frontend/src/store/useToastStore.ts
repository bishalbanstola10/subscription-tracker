import { create } from "zustand";
type ToastType = "SUCCESS" | "ERROR";
interface ToastState {
  message: string;
  type: ToastType;
  visible: boolean;
  showToast: (message: string, type: ToastType) => void;
  hideToast: () => void;
}
const useToastStore = create<ToastState>((set) => ({
  message: "",
  type: "SUCCESS",
  visible: false,
  showToast: (message, ToastType) =>
    set({ message: message, type: ToastType, visible: true }),
  hideToast: () => set({ message: "", visible: false }),
}));
export default useToastStore;
