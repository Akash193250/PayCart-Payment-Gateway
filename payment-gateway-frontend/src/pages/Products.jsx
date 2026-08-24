import { useEffect, useState } from "react";
import api from "../api/axios";

function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [addingId, setAddingId] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get("/api/products");
      setProducts(response.data);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to load products."
      );
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (productId) => {
    try {
      setAddingId(productId);
      setMessage("");
      setError("");

      await api.post("/api/cart/add", {
        productId,
        quantity: 1,
      });

      setMessage("Product added to cart successfully.");
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Unable to add product to cart."
      );
    } finally {
      setAddingId(null);
    }
  };

  if (loading) {
    return (
      <div className="products-page">
        <div className="loading-state">
          <h2>Loading products...</h2>
        </div>
      </div>
    );
  }

  return (
    <div className="products-page">
      <div className="products-header">
        <div>
          <span className="section-badge">
            Featured Collection
          </span>

          <h1>Explore Products</h1>

          <p>
            Browse our products and enjoy secure checkout
            with PayCart.
          </p>
        </div>
      </div>

      {message && (
        <div className="success-message products-message">
          {message}
        </div>
      )}

      {error && (
        <div className="error-message products-message">
          {error}
        </div>
      )}

      {products.length === 0 ? (
        <div className="empty-state">
          <h2>No products available</h2>
          <p>Please check again later.</p>
        </div>
      ) : (
        <div className="products-grid">
          {products.map((product) => (
            <div
              className="product-card"
              key={product.id}
            >
              <div className="product-image-area">
                <div className="product-image-icon">
                  {product.name
                    ?.toLowerCase()
                    .includes("laptop")
                    ? "💻"
                    : product.name
                        ?.toLowerCase()
                        .includes("headphone")
                    ? "🎧"
                    : product.name
                        ?.toLowerCase()
                        .includes("phone")
                    ? "📱"
                    : product.name
                        ?.toLowerCase()
                        .includes("watch")
                    ? "⌚"
                    : "🛍️"}
                </div>

                <span className="product-tag">
                  Available
                </span>
              </div>

              <div className="product-card-content">
                <h2>{product.name}</h2>

                <p className="product-description">
                  {product.description ||
                    "Quality product available through PayCart."}
                </p>

                <div className="product-footer">
                  <div>
                    <span className="price-label">
                      Price
                    </span>

                    <div className="product-price">
                      ₹
                      {Number(
                        product.price
                      ).toLocaleString("en-IN")}
                    </div>
                  </div>

                  <button
                    className="add-cart-button"
                    onClick={() =>
                      addToCart(product.id)
                    }
                    disabled={
                      addingId === product.id
                    }
                  >
                    {addingId === product.id
                      ? "Adding..."
                      : "Add to Cart"}
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Products;