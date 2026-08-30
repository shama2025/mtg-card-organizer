import { SearchAlert } from "lucide-react";
import Nav from "../Navbar/Nav/Nav";
import React from "react";

export default function PageNotFound() {
  return (
    <div className="bg-jeskai-dark min-h-screen">
      <Nav />

      <div className="flex items-center justify-center bg-jeskai-dark min-h-screen p-4">
        <div className="bg-jeskai-card border-2 border-jeskai-white-border rounded-lg w-65 h-95 p-2.5 shadow-xl flex flex-col justify-between select-none">
          <div className="bg-jeskai-white-surface rounded-md px-2 py-1 flex items-center justify-between text-xs font-bold border border-jeskai-white-border/40">
            <span className="truncate">404 Content Not Found</span>
            <span className="flex items-center gap-0.5 shrink-0 ml-1">
              <i className="ms ms-1 text-sm"></i>
              <i className="ms ms-u text-sm"></i>
            </span>
          </div>
          <div className="bg-jeskai-blue-dark/50 my-2 rounded-md border border-jeskai-white-border/20 flex justify-center items-center h-44 overflow-hidden">
            <SearchAlert className="w-28 h-28 text-jeskai-white-surface opacity-80 stroke-[1.5]" />
          </div>
          <div className="bg-jeskai-white-surface rounded-md px-2 py-0.5 text-[11px] font-semibold text-center border border-jeskai-white-border/40">
            Instant — HTTP Status Code
          </div>
          <div className="bg-jeskai-white-surface/90 rounded-md p-2 mt-2 text-[10px] leading-snug border border-jeskai-white-border/40 flex-1 flex flex-col justify-between">
            <p className="font-medium">Counter target request.</p>
            <p className="italic text-gray-600 mt-1.5 border-t border-gray-300/40 pt-1">
              Failed to find. Endpoint was not in the library.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
