import { useState } from "react";
import {
  PaymentElement,
  useElements,
  useStripe,
} from "@stripe/react-stripe-js";

function CheckoutForm({ orderId }) {
  const stripe = useStripe();
  const elements = useElements();

  const [error, setError] = useState("");
  const [processing, setProcessing] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    setProcessing(true);
    setError("");

    try {
      const { error: stripeError } =
        await stripe.confirmPayment({
          elements,

          confirmParams: {
            return_url:
              `${window.location.origin}/payment-success?orderId=${orderId}`,
          },
        });

      if (stripeError) {
        setError(
          stripeError.message ||
            "Payment failed. Please try again."
        );

        setProcessing(false);
      }
    } catch (err) {
      console.error("Stripe payment error:", err);

      setError(
        "Something went wrong while processing payment."
      );

      setProcessing(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="payment-form"
    >
      <PaymentElement />

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <button
        type="submit"
        className="pay-button"
        disabled={!stripe || processing}
      >
        {processing
          ? "Processing Payment..."
          : "Pay Securely"}
      </button>
    </form>
  );
}

export default CheckoutForm;