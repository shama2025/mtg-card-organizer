import React from 'react'
import { SquarePlus } from 'lucide-react'

export default function AddBinder({setDisplayBinderCardModal}) {
  return (
     <div className="flex flex-col">
        <p className="text-jeskai-white-pure text-lg">Binders</p>
        <div className="m-1 flex flex-row">
        <p>Add a Binder</p>
        <SquarePlus 
        className="relative m-1 left-1 bottom-1 
        hover:scale-105 hover:cursor-pointer"
        onClick={() => setDisplayBinderCardModal(false)}
        />
        </div>
    </div>
)
}
