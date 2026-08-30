import React from "react";
import { EllipsisVertical, Minus, Plus } from "lucide-react";

export default function Binder({ binder }) {
  return (
    <div>
      <p className="flex flex-row">
        {binder.name}
        <EllipsisVertical
          className="w-5 h-5 hover:cursor-pointer"
          onClick={() => console.log("Clicked Ellipsis")}
        />
      </p>
    </div>
  );
}
