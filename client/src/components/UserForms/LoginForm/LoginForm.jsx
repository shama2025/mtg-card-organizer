import { React, useState } from "react";
import { loginUser } from "../util/http";
import { useNavigate } from "react-router-dom";

export default function LoginForm({ setLoggedInUser }) {
  const [user, setUser] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState({});

  const navigator = useNavigate();

  function handleUserInput(event) {
    setUser({ ...user, [event.target.name]: event.target.value });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const response = await loginUser(user);

    if (response.user) {
      // navigate to layout
      setLoggedInUser(response.user);
      localStorage.setItem("user", JSON.stringify(response.user));
      localStorage.setItem("collection_id", response.user.collectionId);
      navigator("/");
    } else {
      // Handle error
      // set errors
      setError(response.errors);
    }
  }

  return (
    <div>
      <form onSubmit={(event) => handleSubmit(event)}>
        <h3>Login</h3>
        <div>
          <label>
            Email:{" "}
            <input
              type="text"
              value={user.email}
              name="email"
              onChange={handleUserInput}
              required
            />
          </label>
        </div>
        <div>
          <label>
            Password:{" "}
            <input
              type="password"
              value={user.password}
              name="password"
              onChange={handleUserInput}
              required
            />
          </label>
        </div>
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
}
