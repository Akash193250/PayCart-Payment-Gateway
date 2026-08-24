import { Link, NavLink, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "../api/axios";

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
    if (token) {
      loadCartCount();
    } else {
      setCartCount(0);
    }
  }, [token]);

  const loadCartCount = async () => {
    try {
      const response = await api.get("/api/cart");

      const items = response.data?.items || [];

      const count = items.reduce(
        (total, item) => total + item.quantity,
        0
      );

      setCartCount(count);
    } catch {
      setCartCount(0);
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userEmail");

    setCartCount(0);
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo-link">
        PayCart
      </Link>

      <div className="nav-links">
        <NavLink
          to="/"
          className={({ isActive }) =>
            isActive ? "active-nav" : ""
          }
        >
          Home
        </NavLink>

        <NavLink
          to="/products"
          className={({ isActive }) =>
            isActive ? "active-nav" : ""
          }
        >
          Products
        </NavLink>

        {token ? (
          <>
            <NavLink
              to="/cart"
              className={({ isActive }) =>
                isActive
                  ? "active-nav cart-link"
                  : "cart-link"
              }
            >
              Cart

              {cartCount > 0 && (
                <span className="cart-badge">
                  {cartCount}
                </span>
              )}
            </NavLink>

            <button
              className="logout-button"
              onClick={logout}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <NavLink
              to="/login"
              className={({ isActive }) =>
                isActive ? "active-nav" : ""
              }
            >
              Login
            </NavLink>

            <NavLink
              to="/register"
              className={({ isActive }) =>
                isActive ? "active-nav" : ""
              }
            >
              Register
            </NavLink>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;