import { Link } from "react-router-dom";

function Home() {
  return (
    <div className="home-page">
      <section className="hero-section">
        <div className="hero-content">
          <span className="hero-badge">
            Secure • Fast • Reliable
          </span>

          <h1>
            Simple shopping.
            <br />
            Secure payments.
          </h1>

          <p>
            PayCart is a modern e-commerce platform powered by
            Spring Boot microservices, JWT authentication and
            Stripe payments.
          </p>

          <div className="hero-actions">
            <Link
              to="/products"
              className="primary-button"
            >
              Shop Now
            </Link>

            <Link
              to="/register"
              className="secondary-button"
            >
              Create Account
            </Link>
          </div>
        </div>

        <div className="hero-card">
          <div className="hero-icon">🛒</div>

          <h2>Secure Checkout</h2>

          <p>
            Payments are processed securely using Stripe with
            automatic order confirmation.
          </p>

          <div className="feature-row">
            <span>✓ JWT Security</span>
            <span>✓ Stripe</span>
          </div>

          <div className="feature-row">
            <span>✓ Microservices</span>
            <span>✓ Email Alerts</span>
          </div>
        </div>
      </section>

      <section className="features-section">
        <div className="feature-card">
          <h3>🔐 Secure Authentication</h3>
          <p>
            JWT-based authentication protects user and checkout
            operations.
          </p>
        </div>

        <div className="feature-card">
          <h3>⚡ Fast Checkout</h3>
          <p>
            Add products, create orders and complete Stripe
            payments in a smooth flow.
          </p>
        </div>

        <div className="feature-card">
          <h3>📧 Instant Confirmation</h3>
          <p>
            Receive automatic payment and order confirmation
            emails after checkout.
          </p>
        </div>
      </section>
    </div>
  );
}

export default Home;