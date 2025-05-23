export type FormData = {
  name: string;
  phone: string;
  email: string;
  message: string;
};

export type LoginFormData = {
  email: string;
  password: string;
};

export type AddSubscriptionFormData = {
  userId: string | null;
  serviceName: string;
  price: number;
  renewalDate: string;
  recurrence: string;
};

export type Subscription = {
  id: string;
  userId: String;
  serviceName: string;
  price: number;
  renewalDate: string;
  recurrence: string;
};
