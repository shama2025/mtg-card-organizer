import  { React, useState } from "react";
import { createUser } from "../util/http";
import { useNavigate } from "react-router-dom";

export default function SignUpForm({ setLoggedInUser }) {
  const [user, setUser] = useState({
    userId: 0,
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
    const response = await createUser(user);

    if (response.user) {
      // navigate to layout
      setLoggedInUser(response.user);
      localStorage.setItem("user", JSON.stringify(response.user));
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
        <h3>Create an account</h3>
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
