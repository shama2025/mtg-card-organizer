import { React, useState } from "react";
import { loginUser } from "../util/http";
import { useNavigate } from "react-router-dom";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { useCollectionId } from "../../../contexts/CollectionId";

export default function LoginForm({ setLoggedInUser }) {
  const { setCollectionId } = useCollectionId();

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
      setLoggedInUser(response.user);
      localStorage.setItem("user", JSON.stringify(response.user));
      localStorage.setItem("collection_id", response.user.collectionId);
      setCollectionId(response.user.collectionId);
      navigator("/collection");
    } else {
      setError(response.errors);
    }
  }

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-jeskai-white-surface border-jeskai-white-border border rounded-2xl bg-jeskai-dark p-6 w-full max-w-md shadow-lg">
        <h3 className="text-2xl font-bold mb-6 text-center">Login</h3>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium">Email</label>
            <input
              type="email"
              placeholder="email@example.com"
              value={user.email}
              name="email"
              onChange={handleUserInput}
              required
              className="border border-jeskai-white-border bg-transparent rounded-2xl p-3 focus:outline-none focus:ring-2 focus:ring-jeskai-white-surface"
            />
          </div>
          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium">Password</label>
            <input
              type="password"
              placeholder="••••••••"
              value={user.password}
              name="password"
              onChange={handleUserInput}
              required
              className="border border-jeskai-white-border bg-transparent rounded-2xl p-3 focus:outline-none focus:ring-2 focus:ring-jeskai-white-surface"
            />
          </div>
          <button
            type="submit"
            className="w-full mt-4 bg-jeskai-white-surface text-jeskai-dark font-semibold py-3 rounded-2xl hover:scale-105 hover:cursor-pointer transition-transform"
          >
            Log In
          </button>
          <button
            className="w-full mt-4 bg-jeskai-white-surface text-jeskai-dark font-semibold py-3 rounded-2xl hover:scale-105 hover:cursor-pointer transition-transform"
            onClick={() => navigator("/users/signup")}
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}
