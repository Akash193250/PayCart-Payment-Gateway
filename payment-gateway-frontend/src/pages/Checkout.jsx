import { useEffect, useState } from "react";
import { Elements } from "@stripe/react-stripe-js";

import api from "../api/axios";
import stripePromise from "../stripe";
import CheckoutForm from "../components/CheckoutForm";

function Checkout() {
  const [cart, setCart] = useState(null);
  const [orderId, setOrderId] = useState(null);
  const [clientSecret, setClientSecret] = useState("");

  const [loading, setLoading] = useState(true);
  const [creatingPayment, setCreatingPayment] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      setError("");

      const response = await api.get("/api/cart");

      console.log("CHECKOUT CART RESPONSE:", response.data);

      setCart(response.data);
    } catch (err) {
      console.error(err);

      setError(
        err.response?.data?.message ||
          "Unable to load cart."
      );
    } finally {
      setLoading(false);
    }
  };

  const startCheckout = async () => {
    try {
      setCreatingPayment(true);
      setError("");

      // 1. Create order
      const orderResponse =
        await api.post("/api/orders");

      const newOrderId =
        orderResponse.data.orderId;

      console.log(
        "NEW ORDER ID:",
        newOrderId
      );

      setOrderId(newOrderId);

      // 2. Create Stripe PaymentIntent
      const paymentResponse =
        await api.post("/api/payments", {
          orderId: newOrderId,
        });

      console.log(
        "PAYMENT RESPONSE:",
        paymentResponse.data
      );

      setClientSecret(
        paymentResponse.data.clientSecret
      );
    } catch (err) {
      console.error(
        "CHECKOUT ERROR:",
        err
      );

      setError(
        err.response?.data?.message ||
          "Unable to start checkout."
      );
    } finally {
      setCreatingPayment(false);
    }
  };

  if (loading) {
    return (
      <div className="page">
        <h2>Loading checkout...</h2>
      </div>
    );
  }

  const items = cart?.items || [];

  if (items.length === 0) {
    return (
      <div className="checkout-page">
        <div className="empty-state">
          <h2>Your cart is empty</h2>
          <p>
            Add a product before checkout.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-page">
      <div className="checkout-header">
        <h1>Secure Checkout</h1>
        <p>
          Review your order and complete payment.
        </p>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="checkout-layout">
        <div className="checkout-card">
          <h2>Order Summary</h2>

          {items.map((item) => (
            <div
              className="checkout-item"
              key={item.id}
            >
              <div>
                <strong>
                  {item.productName}
                </strong>

                <p>
                  Qty: {item.quantity}
                </p>
              </div>

              <strong>
                ₹
                {Number(
                  item.subtotal ??
                    item.unitPrice *
                      item.quantity
                ).toLocaleString("en-IN")}
              </strong>
            </div>
          ))}

          <div className="checkout-total">
            <span>Total</span>

            <strong>
              ₹
              {Number(
                cart.totalAmount
              ).toLocaleString("en-IN")}
            </strong>
          </div>
        </div>

        <div className="checkout-card">
          <h2>Payment</h2>

          {!clientSecret ? (
            <button
              className="checkout-payment-button"
              onClick={startCheckout}
              disabled={creatingPayment}
            >
              {creatingPayment
                ? "Preparing Payment..."
                : "Continue to Payment"}
            </button>
          ) : (
            <Elements
              stripe={stripePromise}
              options={{
                clientSecret,
                appearance: {
                  theme: "stripe",
                },
              }}
            >
              <CheckoutForm
                orderId={orderId}
              />
            </Elements>
          )}
        </div>
      </div>
    </div>
  );
}

export default Checkout;