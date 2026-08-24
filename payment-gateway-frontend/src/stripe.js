import { loadStripe } from "@stripe/stripe-js";

const stripeKey =
  import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY;

console.log(
  "Stripe key available:",
  !!stripeKey
);

const stripePromise = loadStripe(stripeKey);

export default stripePromise;