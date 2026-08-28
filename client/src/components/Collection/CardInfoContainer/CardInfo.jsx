import React from "react";

export default function CardInfo({ card }) {
  if (!card) {
    return (
      <div className="bg-jeskai-card border border-slate-700 text-slate-400 rounded-xl p-5 text-center text-sm">
        Hover over a card to view details.
      </div>
    );
  }

  return (
    <div className="bg-jeskai-card h-full text-jeskai-white-pure rounded-xl border border-slate-700 p-5 shadow-xl flex flex-col gap-4 max-h-[calc(100vh-6rem)] overflow-y-auto">
      <div className="flex justify-center bg-slate-900/50 p-3 rounded-lg border border-slate-800">
        <img
          src={card?.imgPath?.[0]?.large || card?.imgPath}
          className="h-64 object-contain rounded-md shadow-md hover:scale-105 transition-transform"
          alt={card?.name || "Card image"}
        />
      </div>

      <div className="flex flex-col gap-3 text-sm">
        <div className="flex justify-between items-center border-b border-slate-700/60 pb-2">
          <span className="text-slate-400 font-medium">Set</span>
          <span className="text-jeskai-white-surface font-semibold text-right ">
            {card?.set?.name || "N/A"}
          </span>
        </div>

        <div className="flex justify-between items-center border-b border-slate-700/60 pb-2">
          <span className="text-slate-400 font-medium">Artist</span>
          <span className="text-jeskai-blue-light font-medium text-right">
            {card?.artistName || "Unknown"}
          </span>
        </div>

        <div className="flex flex-col gap-2 pt-1">
          <span className="text-slate-400 font-medium">Legalities</span>
          <div className="flex flex-wrap gap-1.5 max-h-36 overflow-y-auto pr-1">
            {card?.legalities?.map((format, index) => (
              <span
                key={index}
                className="px-2 py-0.5 text-xs font-semibold bg-jeskai-blue-dark/40 text-jeskai-blue-light border border-jeskai-blue-light/30 rounded-full"
              >
                {format}
              </span>
            )) || (
              <span className="text-xs text-slate-500">None specified</span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
