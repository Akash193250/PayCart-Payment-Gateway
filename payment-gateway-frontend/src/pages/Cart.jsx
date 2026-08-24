import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axios";

function Cart() {
  const navigate = useNavigate();

  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get("/api/cart");
      setCart(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load cart."
      );
    } finally {
      setLoading(false);
    }
  };

  const updateQuantity = async (itemId, quantity) => {
    if (quantity < 1) {
      return;
    }

    try {
      setError("");
      setMessage("");

      await api.put(`/api/cart/update/${itemId}`, {
        quantity,
      });

      setMessage("Cart updated successfully.");
      fetchCart();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to update cart."
      );
    }
  };

  const removeItem = async (itemId) => {
    try {
      setError("");
      setMessage("");

      await api.delete(`/api/cart/remove/${itemId}`);

      setMessage("Item removed from cart.");
      fetchCart();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to remove item."
      );
    }
  };

  const clearCart = async () => {
    try {
      setError("");
      setMessage("");

      await api.delete("/api/cart/clear");

      setMessage("Cart cleared successfully.");
      fetchCart();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to clear cart."
      );
    }
  };

  if (loading) {
    return (
      <div className="page">
        <h2>Loading cart...</h2>
      </div>
    );
  }

  const items = cart?.items || [];

  return (
    <div className="cart-page">
      <div className="cart-header">
        <div>
          <h1>Your Cart</h1>
          <p>Review your items before checkout.</p>
        </div>

        {items.length > 0 && (
          <button
            className="clear-cart-button"
            onClick={clearCart}
          >
            Clear Cart
          </button>
        )}
      </div>

      {message && (
        <div className="success-message">
          {message}
        </div>
      )}

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {items.length === 0 ? (
        <div className="empty-state">
          <h2>Your cart is empty</h2>

          <button
            className="continue-button"
            onClick={() => navigate("/products")}
          >
            Continue Shopping
          </button>
        </div>
      ) : (
        <>
          <div className="cart-items">
            {items.map((item) => (
              <div
                className="cart-item"
                key={item.id}
              >
                <div className="cart-item-info">
                  <h3>{item.productName}</h3>

                  <p>
                    ₹
                    {Number(
                      item.unitPrice
                    ).toLocaleString("en-IN")}{" "}
                    each
                  </p>
                </div>

                <div className="quantity-controls">
                  <button
                    onClick={() =>
                      updateQuantity(
                        item.id,
                        item.quantity - 1
                      )
                    }
                  >
                    −
                  </button>

                  <span>{item.quantity}</span>

                  <button
                    onClick={() =>
                      updateQuantity(
                        item.id,
                        item.quantity + 1
                      )
                    }
                  >
                    +
                  </button>
                </div>

                <div className="cart-item-subtotal">
                  ₹
                  {Number(
                    item.subtotal ||
                      item.unitPrice *
                        item.quantity
                  ).toLocaleString("en-IN")}
                </div>

                <button
                  className="remove-button"
                  onClick={() =>
                    removeItem(item.id)
                  }
                >
                  Remove
                </button>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <div>
              <span>Total</span>

              <strong>
                ₹
                {Number(
                  cart.totalAmount
                ).toLocaleString("en-IN")}
              </strong>
            </div>

            <button
              className="checkout-button"
              onClick={() =>
                navigate("/checkout")
              }
            >
              Proceed to Checkout
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Cart;