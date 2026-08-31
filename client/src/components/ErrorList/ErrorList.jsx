import React from "react";

export default function ErrorList({ errors }) {
  return (
    <div>
      {console.log('errors: ', errors)}
      {errors.length > 0 && (
        <div className="bg-jeskai-red text-jeskai-white-pure p-1 rounded-md">
          <div className="alert alert-danger">
            <h2>The following errors occurred:</h2>
            <ul>
              {errors.map((error) => (
                <li key={error}>{error}</li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}
