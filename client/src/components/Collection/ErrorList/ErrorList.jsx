import React from "react";

export default function ErrorList({ errors }) {
  return (
    <div>
      {" "}
      {errors.length > 0 && (
        <div className="alert alert-danger">
          <h2>The following errors occurred:</h2>
          <ul>
            {errors.map((error) => (
              <li key={error}>{error}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
