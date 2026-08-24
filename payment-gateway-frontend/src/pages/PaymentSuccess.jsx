import { Link, useSearchParams } from "react-router-dom";

function PaymentSuccess() {
  const [searchParams] = useSearchParams();

  const orderId =
    searchParams.get("orderId");

  return (
    <div className="payment-result-page">

      <div className="payment-result-card">

        <div className="success-icon">
          ✓
        </div>

        <h1>Payment Successful!</h1>

        <p>
          Your payment was completed successfully.
        </p>

        {orderId && (
          <p>
            Order ID: <strong>#{orderId}</strong>
          </p>
        )}

        <p>
          Your order confirmation has been sent
          to your email.
        </p>

        <Link
          to="/products"
          className="result-button"
        >
          Continue Shopping
        </Link>

      </div>

    </div>
  );
}

export default PaymentSuccess;