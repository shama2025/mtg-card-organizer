import React, { use, useRef, useEffect } from "react";
import { Bars } from "react-loader-spinner";

export default function Chat({ modelChat, userChat }) {
  const endRef = useRef(null);
  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [modelChat]);

  return (
    <div className="p-2">
      <div className="flex flex-col">
        <div className="flex justify-end items-end p-1">
          <div className="border w-fit p-1 rounded-md">
            <p>{userChat}</p>
          </div>
        </div>
        <div className="flex justify-start items-start p-1">
          <div className="border w-fit p-1 rounded-md" ref={endRef}>
            {modelChat ? (
              <p className="wrap-break-word">{modelChat}</p>
            ) : (
              <Bars height="30" width="30" />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
